package entidades.clientes;

import aed3.Arquivo;

import java.io.File;
import java.util.Scanner;

public class InteracoesClientes {
    private static Scanner console = new Scanner(System.in);
    private Arquivo<Cliente> arqClientes;
  
    public InteracoesClientes() {
      try {
        File d;
        d = new File("dados");
        if (!d.exists())
          d.mkdir();
        d = new File("dados/clientes");
        if (!d.exists())
          d.mkdir();
        arqClientes = new Arquivo<>("dados/clientes", Cliente.class.getConstructor());
      } catch (Exception e) {
        System.out.println("Arquivo não pode ser aberto ou criado.");
        e.printStackTrace();
      }
    }
  
    public Cliente leCliente() throws Exception {
      System.out.print("\nNome: ");
      String nome = console.nextLine();
      System.out.print("\nEmail: ");
      String email = console.nextLine();
      Cliente l = new Cliente(nome, email);
      return l;
    }
  
    public void mostraCliente(Cliente l) {
      System.out.println(
            "\nNome: " + l.getNome() +
            "\nEmail: " + l.getEmail());
    }
  
    public void menuClientes() throws Exception {
      int opcao;
      do {
        System.out.println("\nMENU DE Clientes");
        System.out.println("\n1) Incluir Cliente");
        System.out.println("2) Buscar Cliente");
        System.out.println("3) Alterar Cliente");
        System.out.println("4) Excluir Cliente");
        System.out.println("\n0) Retornar ao menu anterior");
  
        System.out.print("\nOpcao: ");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }
  
        switch (opcao) {
          case 1:
            incluirCliente();
            break;
          case 2:
            buscarCliente();
            break;
          case 3:
            alterarCliente();
            break;
          case 4:
            excluirCliente();
            break;
          case 0:
            break;
          default:
            System.out.println("Opcao inválida");
        }
      } while (opcao != 0);
    }
  
    public void incluirCliente() {
      Cliente novoCliente;
      try {
        novoCliente = leCliente();
      } catch (Exception e) {
        System.out.println("Dados inválidos");
        return;
      }
  
      int id;
      try {
        id = arqClientes.create(novoCliente);
      } catch (Exception e) {
        System.out.println("Cliente não pode ser criado");
        e.printStackTrace();
        return;
      }
  
      System.out.println("\nCliente criado com o ID " + id);
  
    }
  
    public void buscarCliente() {
      int id;
      System.out.print("\nID do Cliente: ");
      try {
        id = Integer.valueOf(console.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("ID inválido.");
        return;
      }
  
      try {
        Cliente l = arqClientes.read(id);
        mostraCliente(l);
      } catch (Exception e) {
        System.out.println("Erro no acesso ao arquivo");
        e.printStackTrace();
      }
  
    }
  
    public void alterarCliente() throws Exception{
      int id;
      System.out.print("\nID do Cliente: ");
      try {
        id = Integer.valueOf(console.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("ID inválido.");
        return;
      }
      int opcao;
      Cliente l = arqClientes.read(id);
      do {
        System.out.println("\nMENU PARA ALTERAR CLIENTES");
        System.out.println("\n1) Alterar Nome");
        System.out.println("2) Buscar Cliente");
        System.out.println("\n0) Retornar ao menu anterior");
  
        System.out.print("\nOpção: ");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }
        
        switch (opcao) {
          case 1:
          System.out.print("\nNome: ");
          String nome = console.nextLine();
          l.setNome(nome);
            break;
          case 2:
          System.out.print("\nEmail: ");
          String email = console.nextLine();
          l.setEmail(email);
            break;
          case 0:
            break;
          default:
            System.out.println("Opcao inválida");
        }
      } while (opcao != 0);

      arqClientes.update(l);
    }
  
    public void excluirCliente() throws Exception{
      int id;
      System.out.print("\nID do Cliente: ");
      try {
        id = Integer.valueOf(console.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("ID inválido.");
        return;
      }
  
      try {
        if(arqClientes.delete(id)){
          System.out.println("\nCliente excluído");
        }else{
          System.out.println("\nCliente já foi excluído");
        }
        
      } catch (Exception e) {
        System.out.println("Erro no acesso ao arquivo");
        e.printStackTrace();
      }
  
    }
}
