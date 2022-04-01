package aed3;

import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {

  private RandomAccessFile arquivo;
  private HashExtensivel<ParIDEndereco> indiceDireto;
  private Constructor<T> construtor;
  private int TAMANHO_CABECALHO = 4;

  public Arquivo(String nomePasta, Constructor<T> c) throws Exception {
    arquivo = new RandomAccessFile(nomePasta + "/dados.db", "rw");
    indiceDireto = new HashExtensivel<>(
        ParIDEndereco.class.getConstructor(),
        4,
        nomePasta + "/indiceID.1.db",
        nomePasta + "/indiceID.2.db");
    construtor = c;
    if (arquivo.length() < TAMANHO_CABECALHO) {
      arquivo.seek(0);
      arquivo.writeInt(0);
    }
  }

  public int create(T entidade) throws Exception {
    // mover o ponteiro para início do arquivo (cabeçalho)
    arquivo.seek(0);
    // ler últimoID
    int ultimoID = arquivo.readInt();
    // objeto.ID <= últimoID + 1
    int novoID = ultimoID + 1;

    entidade.setID(novoID);
    // mover o ponteiro para início do arquivo
    arquivo.seek(0);
    // escrever objeto.ID
    arquivo.writeInt(novoID);

    // Movimenta o ponteiro do arquivo para o ponto de inserção do novo registro
    arquivo.seek(arquivo.length());
    long endereco = arquivo.getFilePointer();

    // Cria o registro no arquivo
    byte[] ba = entidade.toByteArray();
    arquivo.writeByte('#'); // # -> registro válido; * -> registro excluído
    arquivo.writeShort(ba.length);
    arquivo.write(ba);
    // inserir o par (objeto.ID, pos) no índice
    indiceDireto.create(new ParIDEndereco(novoID, endereco));
    return novoID;
  };

  public T read(int id) throws Exception {
    // pos <= buscar o ID no índice
    ParIDEndereco p = indiceDireto.read(id);
    // se pos ≠ -1
    if (p == null)
      return null;

    // então mover o ponteiro para pos
    arquivo.seek(p.getEndereco());

    // ler registro
    byte lapide = arquivo.readByte();
    int tamanho = arquivo.readShort();
    byte[] ba = new byte[tamanho];

    if (lapide == '#') {
      // extrair objeto do registro
      arquivo.read(ba);
      T entidade = construtor.newInstance();
      entidade.fromByteArray(ba);

      // confirmação
      if (entidade.getID() == id)
        return entidade;
    }

    System.out.println("Arquivo corrompido.");
    return null;
  };

  public boolean update(T novaEntidade) throws Exception {
    // pos <= buscar o ID no índice
    ParIDEndereco p = indiceDireto.read(novaEntidade.getID());
    if (p == null)
      // mover o ponteiro para pos
      arquivo.seek(p.getEndereco());
    // ler registro
    int tamanho = arquivo.readShort();
    byte[] ba = new byte[tamanho];
    byte lapide = arquivo.readByte();

    if (lapide != '*') {
      // extrair objeto do registro
      arquivo.read(ba);
      // cria novo registro
      ba = novaEntidade.toByteArray();
      arquivo.writeByte('#');

      if (tamanho <= ba.length) {
        // mover o ponteiro para pos
        arquivo.seek(p.getEndereco());
        // escrever novo registro mantendo ind.tamanho
      } else {
        // mover para pos
        arquivo.writeByte('*');
        // move pro fim do arquivo
        arquivo.writeShort(ba.length);
        // posição do ponteiro

        // escrever novo registro
        arquivo.write(ba);
        // atualizar o endereço para o id no indice

      }

    }

    return false;
  };

  public boolean delete(int id) throws Exception {
    // pos <= buscar o ID no índice
    ParIDEndereco p = indiceDireto.read(id);

    if (p == null) {
      return false;
    }

    // mover o ponteiro para pos
    arquivo.seek(p.getEndereco());

    // ler registro
    byte lapide = arquivo.readByte();

    if (lapide != '*') {
      // mover para pos
      arquivo.seek(p.getEndereco());
      // lápide excuído
      arquivo.writeByte('*');
      // remover indice
      indiceDireto.delete(id);

      return true;
    }

    return false;
  };

}
