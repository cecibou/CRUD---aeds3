package entidades.clientes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import aed3.Registro;

public class Cliente implements Registro {

  private int ID;
  private String nome;
  private String email;

  public Cliente() {
    this(-1, "", "");
  }

  public Cliente(String n, String e) {
    this(-1, n, e);
  }

  public Cliente(int i, String n, String e) {
    this.ID = i;
    this.nome = n;
    this.email = e;
  }

  public int getID() {
    return this.ID;
  }

  public void setID(int i) {
    this.ID = i;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String s) {
    this.nome = s;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String s) {
    this.email = s;
  }

  public String toString() {
    return "\nID: " + this.ID +
        "\nNome: " + this.nome +
        "\nEmail: " + this.email;
  }

  public byte[] toByteArray() throws Exception {
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(ba);
    dos.writeInt(this.ID);
    dos.writeUTF(this.nome);
    dos.writeUTF(this.email);
    return ba.toByteArray();
  }

  public void fromByteArray(byte[] vb) throws Exception {
    ByteArrayInputStream ba = new ByteArrayInputStream(vb);
    DataInputStream dis = new DataInputStream(ba);
    this.ID = dis.readInt();
    this.nome = dis.readUTF();
    this.email = dis.readUTF();
  }

}
