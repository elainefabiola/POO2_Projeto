package views;

import database.*;
import services.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;
    private final AluguelService aluguelService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public MenuPrincipal(ClienteService clienteService, VeiculoService veiculoService, AluguelService aluguelService) {
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.aluguelService = aluguelService;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void start() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== SISTEMA DE LOCAÇÃO DE VEÍCULOS ===");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Veículos");
            System.out.println("3. Gerenciar Aluguéis");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuVeiculos();
                    break;
                case 3:
                    menuAlugueis();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        
        scanner.close();
    }

    private void menuClientes() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== GERENCIAMENTO DE CLIENTES ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Buscar Cliente");
            System.out.println("3. Listar Todos os Clientes");
            System.out.println("4. Remover Cliente");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    buscarCliente();
                    break;
                case 3:
                    listarClientes();
                    break;
                case 4:
                    removerCliente();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        System.out.print("Tipo de cliente (1-Pessoa Física, 2-Pessoa Jurídica): ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Documento (CPF/CNPJ): ");
        String documento = scanner.nextLine();
        
        try {
            Cliente cliente;
            if (tipo == 1) {
                cliente = new PessoaFisica(documento, nome);
            } else if (tipo == 2) {
                cliente = new PessoaJuridica(documento, nome);
            } else {
                System.out.println("Tipo inválido!");
                return;
            }
            
            clienteService.cadastrarCliente(cliente);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        System.out.println("\n=== BUSCAR CLIENTE ===");
        System.out.print("Digite o documento ou nome: ");
        String termo = scanner.nextLine();
        
        try {
            // Tentar buscar por documento primeiro
            Cliente cliente = clienteService.buscarPorDocumento(termo);
            if (cliente != null) {
                exibirCliente(cliente);
            } else {
                // Se não encontrar, buscar por nome
                List<Cliente> clientes = clienteService.buscarPorNome(termo);
                if (!clientes.isEmpty()) {
                    System.out.println("Clientes encontrados:");
                    for (Cliente c : clientes) {
                        exibirCliente(c);
                    }
                } else {
                    System.out.println("Nenhum cliente encontrado.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("\n=== LISTAR TODOS OS CLIENTES ===");
        List<Cliente> clientes = clienteService.listarTodos();
        
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (Cliente cliente : clientes) {
                exibirCliente(cliente);
            }
        }
    }

    private void removerCliente() {
        System.out.println("\n=== REMOVER CLIENTE ===");
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.nextLine();
        
        try {
            clienteService.removerCliente(documento);
            System.out.println("Cliente removido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void exibirCliente(Cliente cliente) {
        System.out.println("Documento: " + cliente.getDocumento());
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("Tipo: " + (cliente instanceof PessoaFisica ? "Pessoa Física" : "Pessoa Jurídica"));
        System.out.println("---");
    }

    private void menuVeiculos() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== GERENCIAMENTO DE VEÍCULOS ===");
            System.out.println("1. Cadastrar Veículo");
            System.out.println("2. Buscar Veículo");
            System.out.println("3. Listar Todos os Veículos");
            System.out.println("4. Listar Veículos Disponíveis");
            System.out.println("5. Remover Veículo");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            switch (opcao) {
                case 1:
                    cadastrarVeiculo();
                    break;
                case 2:
                    buscarVeiculo();
                    break;
                case 3:
                    listarVeiculos();
                    break;
                case 4:
                    listarVeiculosDisponiveis();
                    break;
                case 5:
                    removerVeiculo();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarVeiculo() {
        System.out.println("\n=== CADASTRAR VEÍCULO ===");
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        
        System.out.print("Nome/Modelo: ");
        String nome = scanner.nextLine();
        
        System.out.println("Tipo de veículo:");
        System.out.println("1. PEQUENO (R$ 100/dia)");
        System.out.println("2. MÉDIO (R$ 150/dia)");
        System.out.println("3. SUV (R$ 200/dia)");
        System.out.print("Escolha o tipo: ");
        int tipoOpcao = scanner.nextInt();
        
        TipoVeiculo tipo;
        switch (tipoOpcao) {
            case 1:
                tipo = TipoVeiculo.PEQUENO;
                break;
            case 2:
                tipo = TipoVeiculo.MEDIO;
                break;
            case 3:
                tipo = TipoVeiculo.SUV;
                break;
            default:
                System.out.println("Tipo inválido!");
                return;
        }
        
        try {
            Veiculo veiculo = new Veiculo(placa, nome, tipo);
            veiculoService.cadastrarVeiculo(veiculo);
            System.out.println("Veículo cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarVeiculo() {
        System.out.println("\n=== BUSCAR VEÍCULO ===");
        System.out.print("Digite a placa ou nome: ");
        String termo = scanner.nextLine();
        
        try {
            // Tentar buscar por placa primeiro
            Veiculo veiculo = veiculoService.buscarPorPlaca(termo);
            if (veiculo != null) {
                exibirVeiculo(veiculo);
            } else {
                // Se não encontrar, buscar por nome
                List<Veiculo> veiculos = veiculoService.buscarPorNome(termo);
                if (!veiculos.isEmpty()) {
                    System.out.println("Veículos encontrados:");
                    for (Veiculo v : veiculos) {
                        exibirVeiculo(v);
                    }
                } else {
                    System.out.println("Nenhum veículo encontrado.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
    }

    private void listarVeiculos() {
        System.out.println("\n=== LISTAR TODOS OS VEÍCULOS ===");
        List<Veiculo> veiculos = veiculoService.listarTodos();
        
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
        } else {
            for (Veiculo veiculo : veiculos) {
                exibirVeiculo(veiculo);
            }
        }
    }

    private void listarVeiculosDisponiveis() {
        System.out.println("\n=== VEÍCULOS DISPONÍVEIS ===");
        List<Veiculo> veiculos = veiculoService.buscarDisponiveis();
        
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo disponível.");
        } else {
            for (Veiculo veiculo : veiculos) {
                exibirVeiculo(veiculo);
            }
        }
    }

    private void removerVeiculo() {
        System.out.println("\n=== REMOVER VEÍCULO ===");
        System.out.print("Digite a placa do veículo: ");
        String placa = scanner.nextLine();
        
        try {
            veiculoService.removerVeiculo(placa);
            System.out.println("Veículo removido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void exibirVeiculo(Veiculo veiculo) {
        System.out.println("Placa: " + veiculo.getPlaca());
        System.out.println("Nome: " + veiculo.getNome());
        System.out.println("Tipo: " + veiculo.getTipo());
        System.out.println("Disponível: " + (veiculo.isDisponivel() ? "Sim" : "Não"));
        System.out.println("---");
    }

    private void menuAlugueis() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== GERENCIAMENTO DE ALUGUÉIS ===");
            System.out.println("1. Realizar Aluguel");
            System.out.println("2. Finalizar Aluguel");
            System.out.println("3. Buscar Aluguéis por Cliente");
            System.out.println("4. Buscar Aluguéis por Veículo");
            System.out.println("5. Listar Aluguéis Ativos");
            System.out.println("6. Listar Todos os Aluguéis");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            switch (opcao) {
                case 1:
                    realizarAluguel();
                    break;
                case 2:
                    finalizarAluguel();
                    break;
                case 3:
                    buscarAlugueisPorCliente();
                    break;
                case 4:
                    buscarAlugueisPorVeiculo();
                    break;
                case 5:
                    listarAlugueisAtivos();
                    break;
                case 6:
                    listarTodosAlugueis();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void realizarAluguel() {
        System.out.println("\n=== REALIZAR ALUGUEL ===");
        System.out.print("Documento do cliente: ");
        String documentoCliente = scanner.nextLine();
        
        System.out.print("Placa do veículo: ");
        String placaVeiculo = scanner.nextLine();
        
        System.out.print("Data de início (dd/MM/yyyy): ");
        String dataInicioStr = scanner.nextLine();
        
        System.out.print("Data de fim (dd/MM/yyyy): ");
        String dataFimStr = scanner.nextLine();
        
        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr, dateFormatter);
            LocalDate dataFim = LocalDate.parse(dataFimStr, dateFormatter);
            
            Aluguel aluguel = aluguelService.realizarAluguel(documentoCliente, placaVeiculo, dataInicio, dataFim);
            System.out.println("Aluguel realizado com sucesso!");
            System.out.println("ID do aluguel: " + aluguel.getId());
            System.out.println("Valor total: R$ " + String.format("%.2f", aluguel.getValorTotal()));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void finalizarAluguel() {
        System.out.println("\n=== FINALIZAR ALUGUEL ===");
        System.out.print("ID do aluguel: ");
        int idAluguel = scanner.nextInt();
        
        try {
            aluguelService.finalizarAluguel(idAluguel);
            System.out.println("Aluguel finalizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarAlugueisPorCliente() {
        System.out.println("\n=== BUSCAR ALUGUÉIS POR CLIENTE ===");
        System.out.print("Documento do cliente: ");
        String documento = scanner.nextLine();
        
        try {
            List<Aluguel> alugueis = aluguelService.buscarPorCliente(documento);
            if (alugueis.isEmpty()) {
                System.out.println("Nenhum aluguel encontrado para este cliente.");
            } else {
                for (Aluguel aluguel : alugueis) {
                    exibirAluguel(aluguel);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarAlugueisPorVeiculo() {
        System.out.println("\n=== BUSCAR ALUGUÉIS POR VEÍCULO ===");
        System.out.print("Placa do veículo: ");
        String placa = scanner.nextLine();
        
        try {
            List<Aluguel> alugueis = aluguelService.buscarPorVeiculo(placa);
            if (alugueis.isEmpty()) {
                System.out.println("Nenhum aluguel encontrado para este veículo.");
            } else {
                for (Aluguel aluguel : alugueis) {
                    exibirAluguel(aluguel);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarAlugueisAtivos() {
        System.out.println("\n=== ALUGUÉIS ATIVOS ===");
        List<Aluguel> alugueis = aluguelService.buscarAtivos();
        
        if (alugueis.isEmpty()) {
            System.out.println("Nenhum aluguel ativo.");
        } else {
            for (Aluguel aluguel : alugueis) {
                exibirAluguel(aluguel);
            }
        }
    }

    private void listarTodosAlugueis() {
        System.out.println("\n=== TODOS OS ALUGUÉIS ===");
        List<Aluguel> alugueis = aluguelService.listarTodos();
        
        if (alugueis.isEmpty()) {
            System.out.println("Nenhum aluguel cadastrado.");
        } else {
            for (Aluguel aluguel : alugueis) {
                exibirAluguel(aluguel);
            }
        }
    }

    private void exibirAluguel(Aluguel aluguel) {
        System.out.println("ID: " + aluguel.getId());
        System.out.println("Cliente: " + aluguel.getCliente().getNome());
        System.out.println("Veículo: " + aluguel.getVeiculo().getNome() + " (" + aluguel.getVeiculo().getPlaca() + ")");
        System.out.println("Data Início: " + aluguel.getDataInicio().format(dateFormatter));
        System.out.println("Data Fim: " + aluguel.getDataFim().format(dateFormatter));
        System.out.println("Valor Total: R$ " + String.format("%.2f", aluguel.getValorTotal()));
        System.out.println("Finalizado: " + (aluguel.isFinalizado() ? "Sim" : "Não"));
        System.out.println("---");
    }
}
