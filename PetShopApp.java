import java.sql.*;
import java.util.Scanner;

class PetShop {
    protected Connection connection;

    public PetShop() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:petshop.db");
            initializeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, endereco TEXT, telefone TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS estoque (id INTEGER PRIMARY KEY AUTOINCREMENT, servico TEXT, quantidade INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrarCliente(String nome, String endereco, String telefone) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clientes (nome, endereco, telefone) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, endereco);
            preparedStatement.setString(3, telefone);
            preparedStatement.executeUpdate();
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exibirClientes() {
        if (connection == null) {
            System.out.println("Erro: Conexão com o banco de dados não foi inicializada.");
            return;
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes")) {

            System.out.println("Lista de Clientes:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Nome: " + resultSet.getString("nome") +
                        ", Endereço: " + resultSet.getString("endereco") +
                        ", Telefone: " + resultSet.getString("telefone"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agendarServico(String clienteNome, String servico, int quantidade) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO estoque (servico, quantidade) VALUES (?, ?)")) {
            preparedStatement.setString(1, servico);
            preparedStatement.setInt(2, quantidade);
            preparedStatement.executeUpdate();
            System.out.println("Serviço agendado para " + clienteNome + ": " + quantidade + " unidades de " + servico);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exibirEstoque() {
        if (connection == null) {
            System.out.println("Erro: Conexão com o banco de dados não foi inicializada.");
            return;
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM estoque")) {

            System.out.println("Estoque:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Serviço: " + resultSet.getString("servico") +
                        ", Quantidade: " + resultSet.getInt("quantidade"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adicionarItemEstoque(String servico, int quantidade) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO estoque (servico, quantidade) VALUES (?, ?)")) {
            preparedStatement.setString(1, servico);
            preparedStatement.setInt(2, quantidade);
            preparedStatement.executeUpdate();
            System.out.println("Estoque atualizado para " + servico + ": " + quantidade + " unidades");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class PetShopApp {
    public static void main(String[] args) {
        PetShop petShop = new PetShop();
        Scanner scanner = new Scanner(System.in);

        int opcao;
        do {
            System.out.println("\nPetShop Menu:");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Exibir Clientes");
            System.out.println("3 - Agendar Serviço");
            System.out.println("4 - Exibir Estoque");
            System.out.println("5 - Adicionar Item ao Estoque");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Nome do Cliente: ");
                    String nome = scanner.next();
                    System.out.print("Endereço: ");
                    String endereco = scanner.next();
                    System.out.print("Telefone: ");
                    String telefone = scanner.next();
                    petShop.cadastrarCliente(nome, endereco, telefone);
                    break;
                case 2:
                    petShop.exibirClientes();
                    break;
                case 3:
                    System.out.print("Nome do Cliente: ");
                    String clienteNome = scanner.next();
                    System.out.print("Serviço: ");
                    String servico = scanner.next();
                    System.out.print("Quantidade: ");
                    int quantidade = scanner.nextInt();
                    petShop.agendarServico(clienteNome, servico, quantidade);
                    break;
                case 4:
                    petShop.exibirEstoque();
                    break;
                case 5:
                    System.out.print("Serviço: ");
                    String novoServico = scanner.next();
                    System.out.print("Quantidade: ");
                    int novaQuantidade = scanner.nextInt();
                    petShop.adicionarItemEstoque(novoServico, novaQuantidade);
                    break;
                case 0:
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
        try {
            // Fechar a conexão com o banco de dados ao sair
            if (petShop.connection != null) {
                petShop.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
