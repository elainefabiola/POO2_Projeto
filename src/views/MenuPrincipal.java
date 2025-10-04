package views;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import model.*;
import services.*;

public class MenuPrincipal {
    private ClienteService clienteService;
    private VeiculoService veiculoService;
    private AluguelService aluguelService;
    private Scanner scanner;

    public MenuPrincipal(ClienteService clienteService, VeiculoService veiculoService, AluguelService aluguelService) {
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.aluguelService = aluguelService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== BEM-VINDO AO SISTEMA ADA LOCATECAR ===");

        while (true) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> new MenuCliente(clienteService, scanner).iniciar();
                case 2 -> new MenuVeiculo(veiculoService, scanner).iniciar();
                case 3 -> new MenuAluguel(aluguelService, clienteService, veiculoService, scanner).iniciar();
                case 4 -> new MenuRefatoracoes(clienteService, veiculoService, aluguelService, scanner).iniciar();
                case 5 -> new MenuRelatorios(aluguelService, clienteService, veiculoService, scanner).iniciar();
                case 0 -> {
                    System.out.println("Saindo do sistema... Obrigado por usar o ADA LocateCar!");
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("         ADA LOCATECAR - MENU PRINCIPAL");
        System.out.println("=".repeat(60));
        System.out.println("1 - Gestão de Clientes");
        System.out.println("2 - Gestão de Veículos");
        System.out.println("3 - Gestão de Aluguéis");
        System.out.println("-".repeat(60));
        System.out.println("4 - 🔍 DEMONSTRAÇÃO DAS REFATORAÇÕES (Streams, Predicates...)");
        System.out.println("5 - 📊 RELATÓRIOS (Files + Streams)");
        System.out.println("-".repeat(60));
        System.out.println("0 - Sair");
        System.out.println("=".repeat(60));
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // limpar buffer
            return -1;
        }
    }
}

class MenuCliente {
    private ClienteService clienteService;
    private Scanner scanner;

    public MenuCliente(ClienteService clienteService, Scanner scanner) {
        this.clienteService = clienteService;
        this.scanner = scanner;
    }

    public void iniciar() {
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> listarClientes();
                case 3 -> buscarClientePorNome();
                case 0 -> {return;}
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n=== GESTÃO DE CLIENTES ===");
        System.out.println("1 - Cadastrar Cliente");
        System.out.println("2 - Listar Clientes");
        System.out.println("3 - Buscar por Nome");
        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");
    }

    private void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        System.out.println("1 - Pessoa Física");
        System.out.println("2 - Pessoa Jurídica");
        System.out.print("Tipo: ");
        
        int tipo = lerOpcao();
        scanner.nextLine(); // limpar buffer

        try {
            if (tipo == 1) {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("CPF: ");
                String cpf = scanner.nextLine();
                
                PessoaFisica cliente = new PessoaFisica(cpf, nome);
                clienteService.cadastrarCliente(cliente);
                System.out.println("Cliente pessoa física cadastrado com sucesso!");
                
            } else if (tipo == 2) {
                System.out.print("Razão Social: ");
                String nome = scanner.nextLine();
                System.out.print("CNPJ: ");
                String cnpj = scanner.nextLine();
                
                PessoaJuridica cliente = new PessoaJuridica(cnpj, nome);
                clienteService.cadastrarCliente(cliente);
                System.out.println("Cliente pessoa jurídica cadastrado com sucesso!");
            } else {
                System.out.println("Tipo inválido!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        List<Cliente> clientes = clienteService.listarTodos();
        
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
            System.out.printf("%d. [%s] %s - %s%n", 
                i + 1, tipo, cliente.getNome(), cliente.getDocumento());
        }
    }

    private void buscarClientePorNome() {
        scanner.nextLine(); // limpar buffer
        System.out.print("Digite parte do nome: ");
        String termo = scanner.nextLine();
        
        List<Cliente> clientes = clienteService.buscarPorNome(termo);
        
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
        } else {
            System.out.println("\n=== CLIENTES ENCONTRADOS ===");
            for (Cliente cliente : clientes) {
                String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
                System.out.printf("[%s] %s - %s%n", 
                    tipo, cliente.getNome(), cliente.getDocumento());
            }
        }
    }

    private int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}

class MenuVeiculo {
    private VeiculoService veiculoService;
    private Scanner scanner;

    public MenuVeiculo(VeiculoService veiculoService, Scanner scanner) {
        this.veiculoService = veiculoService;
        this.scanner = scanner;
    }

    public void iniciar() {
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarVeiculo();
                case 2 -> listarVeiculos();
                case 3 -> listarDisponiveis();
                case 4 -> buscarVeiculoPorNome();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n=== GESTÃO DE VEÍCULOS ===");
        System.out.println("1 - Cadastrar Veículo");
        System.out.println("2 - Listar Todos");
        System.out.println("3 - Listar Disponíveis");
        System.out.println("4 - Buscar por Nome");
        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");
    }

    private void cadastrarVeiculo() {
        System.out.println("\n=== CADASTRAR VEÍCULO ===");
        scanner.nextLine(); // limpar buffer
        
        try {
            System.out.print("Placa: ");
            String placa = scanner.nextLine();
            
            System.out.print("Nome/Modelo: ");
            String nome = scanner.nextLine();
            
            System.out.println("Tipo do veículo:");
            System.out.println("1 - PEQUENO (R$ 100,00/dia)");
            System.out.println("2 - MEDIO (R$ 150,00/dia)");
            System.out.println("3 - SUV (R$ 200,00/dia)");
            System.out.print("Escolha o tipo: ");
            
            int tipoOpcao = scanner.nextInt();
            TipoVeiculo tipo = null;
            
            switch (tipoOpcao) {
                case 1 -> tipo = TipoVeiculo.PEQUENO;
                case 2 -> tipo = TipoVeiculo.MEDIO;
                case 3 -> tipo = TipoVeiculo.SUV;
                default -> System.out.println("Tipo inválido!");
            }
            
            Veiculo veiculo = new Veiculo(placa, nome, tipo);
            veiculoService.cadastrarVeiculo(veiculo);
            System.out.println("Veículo cadastrado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    private void listarVeiculos() {
        System.out.println("\n=== LISTA DE VEÍCULOS ===");
        List<Veiculo> veiculos = veiculoService.listarTodos();
        
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
            return;
        }

        for (int i = 0; i < veiculos.size(); i++) {
            Veiculo veiculo = veiculos.get(i);
            String status = veiculo.isDisponivel() ? "DISPONÍVEL" : "ALUGADO";
            System.out.printf("%d. %s - %s [%s] - R$ %.2f/dia - %s%n", 
                i + 1, veiculo.getPlaca(), veiculo.getNome(), 
                veiculo.getTipo(), veiculo.getTipo().getValorDiaria(), status);
        }
    }

    private void listarDisponiveis() {
        System.out.println("\n=== VEÍCULOS DISPONÍVEIS ===");
        List<Veiculo> disponiveis = veiculoService.listarDisponiveis();
        
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum veículo disponível.");
            return;
        }

        for (int i = 0; i < disponiveis.size(); i++) {
            Veiculo veiculo = disponiveis.get(i);
            System.out.printf("%d. %s - %s [%s] - R$ %.2f/dia%n", 
                i + 1, veiculo.getPlaca(), veiculo.getNome(), 
                veiculo.getTipo(), veiculo.getTipo().getValorDiaria());
        }
    }

    private void buscarVeiculoPorNome() {
        scanner.nextLine(); // limpar buffer
        System.out.print("Digite parte do nome: ");
        String termo = scanner.nextLine();
        
        List<Veiculo> veiculos = veiculoService.buscarPorNome(termo);
        
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo encontrado.");
        } else {
            System.out.println("\n=== VEÍCULOS ENCONTRADOS ===");
            for (Veiculo veiculo : veiculos) {
                String status = veiculo.isDisponivel() ? "DISPONÍVEL" : "ALUGADO";
                System.out.printf("%s - %s [%s] - R$ %.2f/dia - %s%n", 
                    veiculo.getPlaca(), veiculo.getNome(), 
                    veiculo.getTipo(), veiculo.getTipo().getValorDiaria(), status);
            }
        }
    }

    private int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}

class MenuAluguel {
    private AluguelService aluguelService;
    private ClienteService clienteService;
    private VeiculoService veiculoService;
    private Scanner scanner;

    public MenuAluguel(AluguelService aluguelService, ClienteService clienteService, 
                      VeiculoService veiculoService, Scanner scanner) {
        this.aluguelService = aluguelService;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.scanner = scanner;
    }

    public void iniciar() {
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> alugarVeiculo();
                case 2 -> devolverVeiculo();
                case 3 -> listarAlugueisAtivos();
                case 4 -> listarTodosAlugueis();
                case 0 -> {return;}
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n=== GESTÃO DE ALUGUÉIS ===");
        System.out.println("1 - Alugar Veículo");
        System.out.println("2 - Devolver Veículo");
        System.out.println("3 - Listar Aluguéis Ativos");
        System.out.println("4 - Listar Todos os Aluguéis");
        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");
    }

    private void alugarVeiculo() {
        System.out.println("\n=== ALUGAR VEÍCULO ===");
        scanner.nextLine(); // limpar buffer
        
        try {
            // Mostrar clientes disponíveis
            System.out.println("\n--- CLIENTES CADASTRADOS ---");
            List<Cliente> clientes = clienteService.listarTodos();
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado!");
                return;
            }
            
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
                System.out.printf("%d. [%s] %s - %s%n", 
                    i + 1, tipo, cliente.getNome(), cliente.getDocumento());
            }
            
            System.out.print("Documento do cliente: ");
            String documento = scanner.nextLine();
            
            // Mostrar veículos disponíveis
            System.out.println("\n--- VEÍCULOS DISPONÍVEIS ---");
            List<Veiculo> disponiveis = veiculoService.listarDisponiveis();
            if (disponiveis.isEmpty()) {
                System.out.println("Nenhum veículo disponível!");
                return;
            }
            
            for (int i = 0; i < disponiveis.size(); i++) {
                Veiculo veiculo = disponiveis.get(i);
                System.out.printf("%d. %s - %s [%s] - R$ %.2f/dia%n", 
                    i + 1, veiculo.getPlaca(), veiculo.getNome(), 
                    veiculo.getTipo(), veiculo.getTipo().getValorDiaria());
            }
            
            System.out.print("Placa do veículo: ");
            String placa = scanner.nextLine();
            
            System.out.print("Local de retirada: ");
            String localRetirada = scanner.nextLine();
            
            LocalDateTime agora = LocalDateTime.now();
            
            Aluguel aluguel = aluguelService.alugar(documento, placa, agora, localRetirada);
            
            System.out.println("\n=== ALUGUEL REALIZADO COM SUCESSO ===");
            System.out.println("ID do Aluguel: " + aluguel.getId());
            System.out.println("Cliente: " + aluguel.getCliente().getNome());
            System.out.println("Veículo: " + aluguel.getVeiculo().getPlaca() + " - " + aluguel.getVeiculo().getNome());
            System.out.println("Data/Hora Retirada: " + agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            System.out.println("Local Retirada: " + localRetirada);
            System.out.println("Valor da Diária: R$ " + String.format("%.2f", aluguel.getVeiculo().getTipo().getValorDiaria()));
            
        } catch (Exception e) {
            System.out.println("Erro ao alugar veículo: " + e.getMessage());
        }
    }

    private void devolverVeiculo() {
        System.out.println("\n=== DEVOLVER VEÍCULO ===");
        
        // Listar aluguéis ativos
        List<Aluguel> ativos = aluguelService.listarAtivos();
        if (ativos.isEmpty()) {
            System.out.println("Nenhum aluguel ativo!");
            return;
        }
        
        System.out.println("--- ALUGUÉIS ATIVOS ---");
        for (int i = 0; i < ativos.size(); i++) {
            Aluguel aluguel = ativos.get(i);
            System.out.printf("%d. ID: %s | Cliente: %s | Veículo: %s - %s%n", 
                i + 1, aluguel.getId(), aluguel.getCliente().getNome(),
                aluguel.getVeiculo().getPlaca(), aluguel.getVeiculo().getNome());
        }
        
        scanner.nextLine(); // limpar buffer
        System.out.print("ID do aluguel: ");
        String aluguelId = scanner.nextLine();
        
        System.out.print("Local de devolução: ");
        String localDevolucao = scanner.nextLine();
        
        // Buscar o aluguel para verificar data de retirada
        Optional<Aluguel> aluguelParaValidacao = aluguelService.listarAtivos().stream()
                .filter(a -> a.getId().equals(aluguelId))
                .findFirst();
        
        LocalDateTime dataRetirada = null;
        if (aluguelParaValidacao.isPresent()) {
            dataRetirada = aluguelParaValidacao.get().getDataHoraRetirada();
        }
        
        // Input opcional de data/hora
        LocalDateTime dataDevolucao = lerDataHoraDevolucao(dataRetirada);
        
        try {
            aluguelService.devolver(aluguelId, dataDevolucao, localDevolucao);
            
            // Buscar o aluguel finalizado para exibir detalhes
            Optional<Aluguel> aluguelOpt = aluguelService.listarTodos().stream()
                    .filter(a -> a.getId().equals(aluguelId))
                    .findFirst();
            
            if (aluguelOpt.isPresent()) {
                Aluguel aluguel = aluguelOpt.get();
                System.out.println("\n=== DEVOLUÇÃO REALIZADA COM SUCESSO ===");
                System.out.println("Cliente: " + aluguel.getCliente().getNome());
                System.out.println("Veículo: " + aluguel.getVeiculo().getPlaca() + " - " + aluguel.getVeiculo().getNome());
                System.out.println("Retirada: " + aluguel.getDataHoraRetirada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("Devolução: " + dataDevolucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("Local Devolução: " + localDevolucao);
                System.out.println("VALOR TOTAL: R$ " + String.format("%.2f", aluguel.getValorTotal()));
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao devolver veículo: " + e.getMessage());
        }
    }

    private LocalDateTime lerDataHoraDevolucao(LocalDateTime dataRetirada) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        while (true) {
            System.out.println("\n--- DATA/HORA DE DEVOLUÇÃO ---");
            System.out.println("Formato: dd/MM/yyyy HH:mm (exemplo: 25/12/2024 14:30)");
            
            if (dataRetirada != null) {
                System.out.println("Data de retirada: " + dataRetirada.format(formatter));
                System.out.println("A devolução deve ser posterior à retirada.");
            }
            
            System.out.print("Digite a data/hora de devolução ou ENTER para usar agora: ");
            
            String input = scanner.nextLine().trim();
            
            // Se usuário não digitou nada, usa data/hora atual
            if (input.isEmpty()) {
                LocalDateTime agora = LocalDateTime.now();
                System.out.println("Usando data/hora atual: " + agora.format(formatter));
                return agora;
            }
            
            // Tentar fazer parse da data/hora informada
            try {
                LocalDateTime dataInformada = LocalDateTime.parse(input, formatter);
                
                // Remover validação que impedia devoluções futuras
                // Permitir devoluções futuras para casos de agendamento
                
                // Validação: data não pode ser anterior à data de retirada
                if (dataRetirada != null && dataInformada.isBefore(dataRetirada)) {
                    System.out.println("❌ Erro: Data de devolução não pode ser anterior à data de retirada!");
                    System.out.println("Data de retirada: " + dataRetirada.format(formatter));
                    continue;
                }
                
                // Validação: data não pode ser anterior a 1900 (data mínima razoável)
                if (dataInformada.getYear() < 1900) {
                    System.out.println("❌ Erro: Data inválida. Use um ano a partir de 1900.");
                    continue;
                }
                
                System.out.println("✅ Data/hora aceita: " + dataInformada.format(formatter));
                return dataInformada;
                
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido!");
                System.out.println("Use o formato: dd/MM/yyyy HH:mm");
                System.out.println("Exemplos válidos:");
                System.out.println("  • 25/12/2024 14:30");
                System.out.println("  • 01/01/2025 09:15");
                System.out.println("  • 15/03/2024 23:45");
                System.out.print("Tentar novamente? (s/n): ");
                
                String resposta = scanner.nextLine().trim().toLowerCase();
                if (!resposta.equals("s") && !resposta.equals("sim") && !resposta.isEmpty()) {
                    // Se usuário não quer tentar novamente, usa data atual
                    LocalDateTime agora = LocalDateTime.now();
                    System.out.println("Usando data/hora atual: " + agora.format(formatter));
                    return agora;
                }
            }
        }
    }

    private void listarAlugueisAtivos() {
        System.out.println("\n=== ALUGUÉIS ATIVOS ===");
        List<Aluguel> ativos = aluguelService.listarAtivos();
        
        if (ativos.isEmpty()) {
            System.out.println("Nenhum aluguel ativo.");
            return;
        }

        for (int i = 0; i < ativos.size(); i++) {
            Aluguel aluguel = ativos.get(i);
            System.out.printf("%d. ID: %s%n", i + 1, aluguel.getId());
            System.out.printf("   Cliente: %s (%s)%n", aluguel.getCliente().getNome(), aluguel.getCliente().getDocumento());
            System.out.printf("   Veículo: %s - %s [%s]%n", 
                aluguel.getVeiculo().getPlaca(), aluguel.getVeiculo().getNome(), aluguel.getVeiculo().getTipo());
            System.out.printf("   Retirada: %s em %s%n", 
                aluguel.getDataHoraRetirada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), 
                aluguel.getLocalRetirada());
            System.out.println();
        }
    }

    private void listarTodosAlugueis() {
        System.out.println("\n=== TODOS OS ALUGUÉIS ===");
        List<Aluguel> todos = aluguelService.listarTodos();
        
        if (todos.isEmpty()) {
            System.out.println("Nenhum aluguel registrado.");
            return;
        }

        for (int i = 0; i < todos.size(); i++) {
            Aluguel aluguel = todos.get(i);
            String status = aluguel.isAtivo() ? "ATIVO" : "FINALIZADO";
            System.out.printf("%d. [%s] ID: %s%n", i + 1, status, aluguel.getId());
            System.out.printf("   Cliente: %s (%s)%n", aluguel.getCliente().getNome(), aluguel.getCliente().getDocumento());
            System.out.printf("   Veículo: %s - %s%n", aluguel.getVeiculo().getPlaca(), aluguel.getVeiculo().getNome());
            System.out.printf("   Retirada: %s%n", 
                aluguel.getDataHoraRetirada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            if (!aluguel.isAtivo()) {
                System.out.printf("   Devolução: %s%n", 
                    aluguel.getDataHoraDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.printf("   Valor Total: R$ %.2f%n", aluguel.getValorTotal());
            }
            System.out.println();
        }
    }

    private int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}

/**
 * Menu para demonstração das refatorações implementadas
 * (Streams, Predicates, Functions, Consumer, Supplier, Comparators)
 */
class MenuRefatoracoes {
    private ClienteService clienteService;
    private VeiculoService veiculoService;
    private AluguelService aluguelService;
    private Scanner scanner;

    public MenuRefatoracoes(ClienteService clienteService, VeiculoService veiculoService,
                           AluguelService aluguelService, Scanner scanner) {
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.aluguelService = aluguelService;
        this.scanner = scanner;
    }

    public void iniciar() {
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> demonstrarPaginacao();
                case 2 -> demonstrarFiltrosComPredicate();
                case 3 -> demonstrarAgrupamentoComStreams();
                case 4 -> demonstrarRankings();
                case 5 -> demonstrarCalculosComReduce();
                case 6 -> demonstrarConsumer();
                case 7 -> demonstrarComparator();
                case 8 -> demonstrarTodas();
                case 0 -> {return;}
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    🔍 DEMONSTRAÇÃO DAS REFATORAÇÕES - CHECKLIST REFACT.MD");
        System.out.println("=".repeat(70));
        System.out.println("1 - ✅ Paginação (Stream.skip() + limit())");
        System.out.println("2 - ✅ Filtros com Predicate");
        System.out.println("3 - ✅ Agrupamento com Streams (groupingBy)");
        System.out.println("4 - ✅ Rankings com Function (veículos/clientes mais ativos)");
        System.out.println("5 - ✅ Cálculos com Reduce (faturamento total)");
        System.out.println("6 - ✅ Consumer para impressão formatada");
        System.out.println("7 - ✅ Comparator com Lambda (ordenação)");
        System.out.println("8 - 🎯 Executar TODAS as demonstrações");
        System.out.println("0 - Voltar");
        System.out.println("=".repeat(70));
        System.out.print("Escolha: ");
    }

    private void demonstrarPaginacao() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 1. PAGINAÇÃO COM STREAM.SKIP() E LIMIT()");
        System.out.println("=".repeat(70));

        System.out.print("Tamanho da página (ex: 5): ");
        scanner.nextLine(); // limpar buffer
        int tamanhoPagina = Integer.parseInt(scanner.nextLine());

        System.out.print("Número da página (começando em 0): ");
        int numeroPagina = Integer.parseInt(scanner.nextLine());

        System.out.println("\n📄 Clientes - Página " + numeroPagina + " (tamanho: " + tamanhoPagina + "):");
        System.out.println("-".repeat(70));

        List<Cliente> paginaClientes = clienteService.listarComPaginacao(numeroPagina, tamanhoPagina);

        if (paginaClientes.isEmpty()) {
            System.out.println("Nenhum cliente nesta página.");
        } else {
            paginaClientes.forEach(cliente -> {
                String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
                System.out.printf("  [%s] %s - %s%n", tipo, cliente.getNome(), cliente.getDocumento());
            });
        }

        System.out.println("\n📄 Veículos - Página " + numeroPagina + " (tamanho: " + tamanhoPagina + "):");
        System.out.println("-".repeat(70));

        List<Veiculo> paginaVeiculos = veiculoService.listarComPaginacao(numeroPagina, tamanhoPagina);

        if (paginaVeiculos.isEmpty()) {
            System.out.println("Nenhum veículo nesta página.");
        } else {
            paginaVeiculos.forEach(veiculo ->
                System.out.printf("  %s - %s [%s] - R$ %.2f/dia%n",
                    veiculo.getPlaca(), veiculo.getNome(), veiculo.getTipo(),
                    veiculo.getTipo().getValorDiaria())
            );
        }

        System.out.println("\n✅ Código usado: clienteService.listarComPaginacao(pagina, tamanho)");
        System.out.println("   Implementação: .skip(pagina * tamanho).limit(tamanho)");
        aguardarEnter();
    }

    private void demonstrarFiltrosComPredicate() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 2. FILTROS COM PREDICATE<T>");
        System.out.println("=".repeat(70));

        System.out.println("\n🔍 Filtro 1: Listar apenas Pessoas Físicas");
        System.out.println("-".repeat(70));
        List<Cliente> pessoasFisicas = clienteService.listarPessoasFisicas();
        System.out.println("Total de PF: " + pessoasFisicas.size());
        pessoasFisicas.stream().limit(5).forEach(pf ->
            System.out.printf("  [PF] %s - %s%n", pf.getNome(), pf.getDocumento())
        );

        System.out.println("\n🔍 Filtro 2: Listar apenas Pessoas Jurídicas");
        System.out.println("-".repeat(70));
        List<Cliente> pessoasJuridicas = clienteService.listarPessoasJuridicas();
        System.out.println("Total de PJ: " + pessoasJuridicas.size());
        pessoasJuridicas.stream().limit(5).forEach(pj ->
            System.out.printf("  [PJ] %s - %s%n", pj.getNome(), pj.getDocumento())
        );

        System.out.println("\n🔍 Filtro 3: Veículos disponíveis");
        System.out.println("-".repeat(70));
        List<Veiculo> disponiveis = veiculoService.listarDisponiveis();
        System.out.println("Total de veículos disponíveis: " + disponiveis.size());
        disponiveis.stream().limit(5).forEach(v ->
            System.out.printf("  %s - %s [%s]%n", v.getPlaca(), v.getNome(), v.getTipo())
        );

        System.out.println("\n✅ Código usado:");
        System.out.println("   Predicate<Cliente> ehPessoaFisica = c -> c instanceof PessoaFisica;");
        System.out.println("   clienteService.buscarComFiltro(ehPessoaFisica)");
        aguardarEnter();
    }

    private void demonstrarAgrupamentoComStreams() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 3. AGRUPAMENTO COM STREAMS (GROUPINGBY)");
        System.out.println("=".repeat(70));

        System.out.println("\n📊 Veículos agrupados por tipo:");
        System.out.println("-".repeat(70));
        veiculoService.agruparPorTipo().forEach((tipo, lista) ->
            System.out.printf("  %s: %d veículos%n", tipo, lista.size())
        );

        System.out.println("\n📊 Veículos disponíveis por tipo:");
        System.out.println("-".repeat(70));
        veiculoService.contarDisponiveisPorTipo().forEach((tipo, count) ->
            System.out.printf("  %s: %d disponíveis%n", tipo, count)
        );

        System.out.println("\n📊 Aluguéis agrupados por tipo de veículo:");
        System.out.println("-".repeat(70));
        aluguelService.agruparPorTipoVeiculo().forEach((tipo, lista) ->
            System.out.printf("  %s: %d aluguéis%n", tipo, lista.size())
        );

        System.out.println("\n✅ Código usado:");
        System.out.println("   .collect(Collectors.groupingBy(Veiculo::getTipo))");
        System.out.println("   .collect(Collectors.groupingBy(..., Collectors.counting()))");
        aguardarEnter();
    }

    private void demonstrarRankings() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 4. RANKINGS COM FUNCTION<T,R> - PIPELINE COMPLEXO");
        System.out.println("=".repeat(70));

        System.out.println("\n🏆 TOP 5 Veículos Mais Alugados:");
        System.out.println("-".repeat(70));
        aluguelService.obterVeiculosMaisAlugados().stream()
            .limit(5)
            .forEach(entry ->
                System.out.printf("  %s: %d aluguéis%n", entry.getKey(), entry.getValue())
            );

        System.out.println("\n🏆 TOP 5 Clientes que Mais Alugaram:");
        System.out.println("-".repeat(70));
        aluguelService.obterClientesQueMaisAlugaram().stream()
            .limit(5)
            .forEach(entry ->
                System.out.printf("  %s: %d aluguéis%n", entry.getKey(), entry.getValue())
            );

        System.out.println("\n✅ Pipeline usado:");
        System.out.println("   .collect(Collectors.groupingBy(..., Collectors.counting()))");
        System.out.println("   .entrySet().stream()");
        System.out.println("   .sorted(Map.Entry.comparingByValue().reversed())");
        aguardarEnter();
    }

    private void demonstrarCalculosComReduce() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 5. CÁLCULOS COM STREAMS + REDUCE");
        System.out.println("=".repeat(70));

        System.out.println("\n💰 Faturamento Total (usando reduce):");
        System.out.println("-".repeat(70));
        java.math.BigDecimal faturamentoTotal = aluguelService.calcularFaturamentoTotal();
        System.out.printf("  Faturamento total: R$ %.2f%n", faturamentoTotal);

        System.out.println("\n💰 Faturamento por Tipo de Veículo:");
        System.out.println("-".repeat(70));
        aluguelService.calcularFaturamentoPorTipo().forEach((tipo, valor) ->
            System.out.printf("  %s: R$ %.2f%n", tipo, valor)
        );

        System.out.println("\n✅ Código usado:");
        System.out.println("   .map(Aluguel::getValorTotal)");
        System.out.println("   .reduce(BigDecimal.ZERO, BigDecimal::add)");
        aguardarEnter();
    }

    private void demonstrarConsumer() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 6. CONSUMER<T> PARA IMPRESSÃO FORMATADA");
        System.out.println("=".repeat(70));

        System.out.println("\n📋 Clientes (usando Consumer):");
        System.out.println("-".repeat(70));
        clienteService.listarTodos().stream().limit(5).forEach(cliente -> {
            String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
            System.out.printf("  [%s] %-30s %s%n", tipo, cliente.getNome(), cliente.getDocumento());
        });

        System.out.println("\n🚗 Veículos (usando Consumer):");
        System.out.println("-".repeat(70));
        veiculoService.listarTodos().stream().limit(5).forEach(veiculo -> {
            String status = veiculo.isDisponivel() ? "DISPONÍVEL" : "ALUGADO";
            System.out.printf("  %-10s %-20s [%-7s] - %-10s%n",
                veiculo.getPlaca(), veiculo.getNome(), veiculo.getTipo(), status);
        });

        System.out.println("\n✅ Código usado:");
        System.out.println("   Consumer<Cliente> impressora = cliente -> {...};");
        System.out.println("   clientes.forEach(impressora);");
        aguardarEnter();
    }

    private void demonstrarComparator() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ 7. COMPARATOR COM LAMBDA (ORDENAÇÃO)");
        System.out.println("=".repeat(70));

        System.out.println("\n📋 Clientes ordenados por nome (A-Z):");
        System.out.println("-".repeat(70));
        clienteService.listarOrdenadosPorNome().stream().limit(8).forEach(cliente ->
            System.out.printf("  %s%n", cliente.getNome())
        );

        System.out.println("\n🚗 Veículos ordenados por nome (A-Z):");
        System.out.println("-".repeat(70));
        veiculoService.listarOrdenadosPorNome().stream().limit(8).forEach(veiculo ->
            System.out.printf("  %s - %s%n", veiculo.getPlaca(), veiculo.getNome())
        );

        System.out.println("\n📅 Aluguéis ordenados por data (mais recentes primeiro):");
        System.out.println("-".repeat(70));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        aluguelService.listarOrdenadosPorDataRetirada().stream().limit(5).forEach(aluguel ->
            System.out.printf("  %s - %s - %s%n",
                aluguel.getDataHoraRetirada().format(formatter),
                aluguel.getCliente().getNome(),
                aluguel.getVeiculo().getNome())
        );

        System.out.println("\n✅ Código usado:");
        System.out.println("   .sorted(Comparator.comparing(Cliente::getNome))");
        System.out.println("   .sorted(Comparator.comparing(Aluguel::getData).reversed())");
        aguardarEnter();
    }

    private void demonstrarTodas() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎯 EXECUTANDO TODAS AS DEMONSTRAÇÕES");
        System.out.println("=".repeat(70));

        demonstrarPaginacao();
        demonstrarFiltrosComPredicate();
        demonstrarAgrupamentoComStreams();
        demonstrarRankings();
        demonstrarCalculosComReduce();
        demonstrarConsumer();
        demonstrarComparator();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ TODAS AS DEMONSTRAÇÕES CONCLUÍDAS!");
        System.out.println("=".repeat(70));
    }

    private void aguardarEnter() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    private int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}

/**
 * Menu para geração de relatórios usando Files + Streams
 */
class MenuRelatorios {
    private AluguelService aluguelService;
    private ClienteService clienteService;
    private VeiculoService veiculoService;
    private Scanner scanner;
    private RelatorioService relatorioService;

    public MenuRelatorios(AluguelService aluguelService, ClienteService clienteService,
                         VeiculoService veiculoService, Scanner scanner) {
        this.aluguelService = aluguelService;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.scanner = scanner;
        this.relatorioService = new RelatorioService(aluguelService, clienteService, veiculoService);
    }

    public void iniciar() {
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> gerarRelatorioFaturamento();
                case 2 -> gerarRelatorioVeiculosMaisAlugados();
                case 3 -> gerarRelatorioClientesQueMaisAlugaram();
                case 4 -> gerarReciboAluguel();
                case 5 -> gerarReciboDevolucao();
                case 6 -> gerarRelatorioCompleto();
                case 7 -> gerarTodosRelatorios();
                case 0 -> {return;}
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("    📊 RELATÓRIOS (FILES + STREAMS) - CHECKLIST REFACT.MD");
        System.out.println("=".repeat(70));
        System.out.println("1 - ✅ Faturamento Total por Período");
        System.out.println("2 - ✅ Veículos Mais Alugados");
        System.out.println("3 - ✅ Clientes que Mais Alugaram");
        System.out.println("4 - ✅ Recibo de Aluguel");
        System.out.println("5 - ✅ Recibo de Devolução");
        System.out.println("6 - ✅ Relatório Completo de Aluguéis");
        System.out.println("7 - 🎯 Gerar TODOS os Relatórios");
        System.out.println("0 - Voltar");
        System.out.println("=".repeat(70));
        System.out.print("Escolha: ");
    }

    private void gerarRelatorioFaturamento() {
        System.out.println("\n📊 RELATÓRIO DE FATURAMENTO POR PERÍODO");
        System.out.println("-".repeat(70));

        try {
            LocalDateTime hoje = LocalDateTime.now();
            LocalDateTime trintaDiasAtras = hoje.minusDays(30);

            System.out.println("Gerando relatório dos últimos 30 dias...");
            relatorioService.gerarRelatorioFaturamentoPorPeriodo(trintaDiasAtras, hoje);

            System.out.println("\n✅ Usa: Files.newBufferedWriter() + Streams");
            System.out.println("   Pipeline: filter + map + reduce + groupingBy");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void gerarRelatorioVeiculosMaisAlugados() {
        System.out.println("\n🚗 RELATÓRIO DE VEÍCULOS MAIS ALUGADOS");
        System.out.println("-".repeat(70));

        try {
            relatorioService.gerarRelatorioVeiculosMaisAlugados();
            System.out.println("\n✅ Usa: Files + Streams + groupingBy + counting");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void gerarRelatorioClientesQueMaisAlugaram() {
        System.out.println("\n👥 RELATÓRIO DE CLIENTES QUE MAIS ALUGARAM");
        System.out.println("-".repeat(70));

        try {
            relatorioService.gerarRelatorioClientesQueMaisAlugaram();
            System.out.println("\n✅ Usa: Files + Streams + groupingBy + counting");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void gerarReciboAluguel() {
        System.out.println("\n📄 GERAR RECIBO DE ALUGUEL");
        System.out.println("-".repeat(70));

        List<Aluguel> alugueis = aluguelService.listarTodos();
        if (alugueis.isEmpty()) {
            System.out.println("Nenhum aluguel cadastrado!");
            return;
        }

        System.out.println("Aluguéis disponíveis:");
        for (int i = 0; i < Math.min(5, alugueis.size()); i++) {
            Aluguel a = alugueis.get(i);
            System.out.printf("%d. %s - %s%n", i+1, a.getId().substring(0, 8), a.getCliente().getNome());
        }

        scanner.nextLine();
        System.out.print("ID do aluguel: ");
        String id = scanner.nextLine();

        try {
            relatorioService.gerarReciboAluguel(id);
            System.out.println("\n✅ Usa: Files + BufferedWriter");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void gerarReciboDevolucao() {
        System.out.println("\n📄 GERAR RECIBO DE DEVOLUÇÃO");
        System.out.println("-".repeat(70));

        List<Aluguel> finalizados = aluguelService.listarFinalizados();
        if (finalizados.isEmpty()) {
            System.out.println("Nenhum aluguel finalizado!");
            return;
        }

        System.out.println("Aluguéis finalizados:");
        for (int i = 0; i < Math.min(5, finalizados.size()); i++) {
            Aluguel a = finalizados.get(i);
            System.out.printf("%d. %s - %s - R$ %.2f%n",
                i+1, a.getId().substring(0, 8), a.getCliente().getNome(), a.getValorTotal());
        }

        scanner.nextLine();
        System.out.print("ID do aluguel: ");
        String id = scanner.nextLine();

        try {
            relatorioService.gerarReciboDevolucao(id);
            System.out.println("\n✅ Usa: Files + BufferedWriter + cálculo de valores");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void gerarRelatorioCompleto() {
        System.out.println("\n📋 RELATÓRIO COMPLETO DE ALUGUÉIS");
        System.out.println("-".repeat(70));

        try {
            relatorioService.gerarRelatorioCompletodeAlugueis();
            System.out.println("\n✅ Usa: Files + Streams + sorted + filter");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void gerarTodosRelatorios() {
        System.out.println("\n🎯 GERANDO TODOS OS RELATÓRIOS...");
        System.out.println("=".repeat(70));

        try {
            LocalDateTime hoje = LocalDateTime.now();
            LocalDateTime trintaDiasAtras = hoje.minusDays(30);

            relatorioService.gerarRelatorioFaturamentoPorPeriodo(trintaDiasAtras, hoje);
            relatorioService.gerarRelatorioVeiculosMaisAlugados();
            relatorioService.gerarRelatorioClientesQueMaisAlugaram();
            relatorioService.gerarRelatorioCompletodeAlugueis();

            System.out.println("\n✅ TODOS OS RELATÓRIOS GERADOS COM SUCESSO!");
            System.out.println("   Verifique o diretório 'relatorios/'");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}


