import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.*;
import repositories.*;
import services.*;
import views.*;

public class Main {
    public static void main(String[] args) {
        // BASE DE DADOS
        List<Cliente> clientes = new ArrayList<>();
        List<Veiculo> veiculos = new ArrayList<>();
        List<Aluguel> alugueis = new ArrayList<>();

        // REPOSITÓRIOS
        ClienteRepository clienteRepo = new ClienteRepository(clientes);
        VeiculoRepository veiculoRepo = new VeiculoRepository(veiculos);
        AluguelRepository aluguelRepo = new AluguelRepository(alugueis);

        // Carregar dados persistidos
        clienteRepo.carregarDeArquivo();
        veiculoRepo.carregarDeArquivo();
        aluguelRepo.carregarDeArquivo();

        // SERVICES (regras de negócio)
        ClienteService clienteService = new ClienteService(clienteRepo);
        VeiculoService veiculoService = new VeiculoService(veiculoRepo);
        AluguelService aluguelService = new AluguelService(aluguelRepo, clienteRepo, veiculoRepo);

        // DADOS INICIAIS PARA TESTE - apenas se não existirem dados
        try {
            // Verificar se já existem dados carregados
            boolean temDados = !clienteService.listarTodos().isEmpty() && !veiculoService.listarTodos().isEmpty();
            
            if (!temDados) {
                System.out.println("Inicializando dados de exemplo...");
                
                // Clientes de exemplo
                clienteService.cadastrarCliente(new PessoaFisica("12345678901", "Ana Silva"));
                clienteService.cadastrarCliente(new PessoaFisica("98765432100", "João Santos"));
                clienteService.cadastrarCliente(new PessoaJuridica("12345678000100", "Tech Solutions Ltda"));
                clienteService.cadastrarCliente(new PessoaFisica("11122233344", "Maria Oliveira"));
                clienteService.cadastrarCliente(new PessoaJuridica("98765432000111", "Empresa ABC Ltda"));

                // Veículos de exemplo
                veiculoService.cadastrarVeiculo(new Veiculo("ABC-1456", "Gol 1.0", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("DEF-5678", "Civic 2.0", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("GHI-9012", "Hilux 2.8", TipoVeiculo.SUV));
                veiculoService.cadastrarVeiculo(new Veiculo("JKL-3456", "Onix 1.4", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("MNO-7890", "Corolla 2.0", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("PQR-1234", "Amarok 2.0", TipoVeiculo.SUV));
                
                // Criar alguns aluguéis de exemplo
                LocalDateTime agora = LocalDateTime.now();
                
                // Aluguel ativo - Ana Silva com Gol
                Aluguel aluguel1 = aluguelService.alugar("12345678901", "ABC-1456", 
                    agora.minusDays(2), "Filial Centro");
                System.out.println("✅ Aluguel ativo criado: " + aluguel1.getId() + 
                    " (Ana Silva - Gol)");
                
                // Aluguel ativo - Tech Solutions com Hilux  
                Aluguel aluguel2 = aluguelService.alugar("12345678000100", "GHI-9012",
                    agora.minusDays(1), "Filial Shopping");
                System.out.println("✅ Aluguel ativo criado: " + aluguel2.getId() + 
                    " (Tech Solutions - Hilux)");
                
                // Aluguel finalizado - João Santos com Civic (já devolvido)
                Aluguel aluguel3 = aluguelService.alugar("98765432100", "DEF-5678",
                    agora.minusDays(5), "Filial Norte");
                aluguelService.devolver(aluguel3.getId(), agora.minusDays(3), "Filial Centro");
                System.out.println("✅ Aluguel finalizado criado: " + aluguel3.getId() + 
                    " (João Santos - Civic - R$ " + aluguel3.getValorTotal() + ")");
                
                System.out.println("\nDados de exemplo criados com sucesso!");
                System.out.println("- " + clienteService.listarTodos().size() + " clientes cadastrados");
                System.out.println("- " + veiculoService.listarTodos().size() + " veículos cadastrados");
                System.out.println("- " + aluguelService.listarAtivos().size() + " aluguéis ativos");
                System.out.println("- " + aluguelService.listarTodos().size() + " aluguéis totais");
            } else {
                System.out.println("Dados já existem - carregados da persistência");
                System.out.println("- " + clienteService.listarTodos().size() + " clientes");
                System.out.println("- " + veiculoService.listarTodos().size() + " veículos");
                System.out.println("- " + aluguelService.listarAtivos().size() + " aluguéis ativos");
                System.out.println("- " + aluguelService.listarTodos().size() + " aluguéis totais");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados iniciais: " + e.getMessage());
        }

        // VIEW (interface do usuário)
        MenuPrincipal menuPrincipal = new MenuPrincipal(clienteService, veiculoService, aluguelService);
        menuPrincipal.start();
    }
}
