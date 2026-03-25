package br.com.krono.teste;

import br.com.krono.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Scanner;

public class CrudUsuario {

    static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exercicios_pratica-jpa"); //pega config do persistence.xml
        EntityManager em = emf.createEntityManager();

        Scanner sc = new Scanner(System.in);

        Usuario usuario;

        int opcao;

        do {
            System.out.println("\n=== CRUD USUÁRIO ===");
            System.out.println("1 - Inserir");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Remover");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {

                case 1: // INSERIR
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    try {
                    usuario = new Usuario(nome, email);

                    em.getTransaction().begin();
                    em.persist(usuario);
                    em.getTransaction().commit();

                    }catch (Exception e){
                        System.out.println("Erro ao inserir usuário: " + e.getMessage());
                    }


                    break;

                case 2:
                    TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
                    query.getResultList()
                            .forEach(u -> System.out.println(u.getId() + ": " + u.getNome() + " - " + u.getEmail()));

                    if (query.getResultList().isEmpty()){
                        System.out.println("Nenhum usuário encontrado!");
                    }
                    break;

                case 3:
                    System.out.print("Digite o ID: ");
                    Long idBusca = sc.nextLong();

                    usuario = em.find(Usuario.class, idBusca);

                    if (usuario != null){
                        System.out.println(usuario.getId() + ": " + usuario.getNome() + " - " + usuario.getEmail());
                    }else{
                        System.out.println("Usuário não encontrado!");
                    }

                    break;

                case 4:
                    System.out.print("ID do usuário: ");
                    Long idUpdate = sc.nextLong();
                    sc.nextLine();

                    usuario = em.find(Usuario.class, idUpdate);
                    if (usuario != null){
                        System.out.println("Nome atual: " + usuario.getNome());
                        System.out.print("Novo nome: ");
                        String novoNome = sc.nextLine();
                        System.out.println("Email atual: " + usuario.getEmail());
                        System.out.print("Novo Email: ");
                        String novoEmail = sc.nextLine();
                        usuario.setNome(novoNome);
                        usuario.setEmail(novoEmail);
                        System.out.println("Atualizando...");
                        em.getTransaction().begin();
                        em.merge(usuario); //atualiza o objeto no banco
                        em.getTransaction().commit();


                    }


                    break;

                case 5:
                    System.out.print("ID do usuário: ");
                    Long idDelete = sc.nextLong();

                    usuario = em.find(Usuario.class, idDelete);
                    if (usuario != null){
                        System.out.println("Removendo...");
                        em.getTransaction().begin();
                        em.remove(usuario); //remove o objeto do banco
                        em.getTransaction().commit();
                    }else{
                        System.out.println("Usuário não encontrado!");
                    }


                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}
