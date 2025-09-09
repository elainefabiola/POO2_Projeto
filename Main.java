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
                
                // Clientes de exemplo - Pessoa Física
                clienteService.cadastrarCliente(new PessoaFisica("12345678901", "Ana Silva"));
                clienteService.cadastrarCliente(new PessoaFisica("98765432100", "João Santos"));
                clienteService.cadastrarCliente(new PessoaFisica("11122233344", "Maria Oliveira"));
                clienteService.cadastrarCliente(new PessoaFisica("55544433322", "Carlos Ferreira"));
                clienteService.cadastrarCliente(new PessoaFisica("77788899911", "Fernanda Costa"));
                clienteService.cadastrarCliente(new PessoaFisica("66699988877", "Roberto Lima"));
                
                // Clientes de exemplo - Pessoa Jurídica
                clienteService.cadastrarCliente(new PessoaJuridica("12345678000100", "Tech Solutions Ltda"));
                clienteService.cadastrarCliente(new PessoaJuridica("98765432000111", "Empresa ABC Ltda"));
                clienteService.cadastrarCliente(new PessoaJuridica("11223344000155", "Inovação Digital Ltda"));
                clienteService.cadastrarCliente(new PessoaJuridica("55667788000199", "Logística Express S.A."));
                clienteService.cadastrarCliente(new PessoaJuridica("99887766000133", "Consultoria Prime Ltda"));
                clienteService.cadastrarCliente(new PessoaJuridica("44556677000122", "Desenvolvimento Web Inc"));

                // Veículos de exemplo - Categoria PEQUENO
                veiculoService.cadastrarVeiculo(new Veiculo("ABC-1456", "Gol 1.0", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("JKL-3456", "Onix 1.4", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("STU-1111", "Palio 1.0", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("VWX-2222", "Ka 1.0", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("YZA-3333", "Up 1.0", TipoVeiculo.PEQUENO));
                veiculoService.cadastrarVeiculo(new Veiculo("BCD-4444", "Kwid 1.0", TipoVeiculo.PEQUENO));
                
                // Veículos de exemplo - Categoria MEDIO
                veiculoService.cadastrarVeiculo(new Veiculo("DEF-5678", "Civic 2.0", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("MNO-7890", "Corolla 2.0", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("EFG-5555", "Jetta 2.0", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("HIJ-6666", "Sentra 2.0", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("KLM-7777", "Cruze 1.4", TipoVeiculo.MEDIO));
                veiculoService.cadastrarVeiculo(new Veiculo("NOP-8888", "Focus 2.0", TipoVeiculo.MEDIO));
                
                // Veículos de exemplo - Categoria SUV
                veiculoService.cadastrarVeiculo(new Veiculo("GHI-9012", "Hilux 2.8", TipoVeiculo.SUV));
                veiculoService.cadastrarVeiculo(new Veiculo("PQR-1234", "Amarok 2.0", TipoVeiculo.SUV));
                veiculoService.cadastrarVeiculo(new Veiculo("QRS-9999", "Ranger 3.2", TipoVeiculo.SUV));
                veiculoService.cadastrarVeiculo(new Veiculo("TUV-0000", "S10 2.8", TipoVeiculo.SUV));
                veiculoService.cadastrarVeiculo(new Veiculo("WXY-1212", "Frontier 2.5", TipoVeiculo.SUV));
                veiculoService.cadastrarVeiculo(new Veiculo("ZAB-3434", "Trailblazer 2.8", TipoVeiculo.SUV));
                
                // Criar alguns aluguéis de exemplo
                LocalDateTime agora = LocalDateTime.now();
                
                // ALUGUÉIS ATIVOS (veículos alugados)
                
                // Ana Silva com Gol (categoria PEQUENO)
                Aluguel aluguel1 = aluguelService.alugar("12345678901", "ABC-1456", 
                    agora.minusDays(2), "Filial Centro");
                System.out.println("✅ Aluguel ativo: " + aluguel1.getId() + 
                    " (Ana Silva - Gol PEQUENO)");
                
                // Tech Solutions com Hilux (categoria SUV)
                Aluguel aluguel2 = aluguelService.alugar("12345678000100", "GHI-9012",
                    agora.minusDays(1), "Filial Shopping");
                System.out.println("✅ Aluguel ativo: " + aluguel2.getId() + 
                    " (Tech Solutions - Hilux SUV)");
                
                // Carlos Ferreira com Civic (categoria MEDIO)
                Aluguel aluguel3 = aluguelService.alugar("55544433322", "DEF-5678",
                    agora.minusDays(3), "Filial Norte");
                System.out.println("✅ Aluguel ativo: " + aluguel3.getId() + 
                    " (Carlos Ferreira - Civic MEDIO)");
                
                // Logística Express com Amarok (categoria SUV)
                Aluguel aluguel4 = aluguelService.alugar("55667788000199", "PQR-1234",
                    agora.minusDays(4), "Filial Sul");
                System.out.println("✅ Aluguel ativo: " + aluguel4.getId() + 
                    " (Logística Express - Amarok SUV)");
                
                // Fernanda Costa com Onix (categoria PEQUENO)
                Aluguel aluguel5 = aluguelService.alugar("77788899911", "JKL-3456",
                    agora.minusDays(1), "Filial Leste");
                System.out.println("✅ Aluguel ativo: " + aluguel5.getId() + 
                    " (Fernanda Costa - Onix PEQUENO)");
                
                // Consultoria Prime com Corolla (categoria MEDIO)
                Aluguel aluguel6 = aluguelService.alugar("99887766000133", "MNO-7890",
                    agora.minusDays(2), "Filial Oeste");
                System.out.println("✅ Aluguel ativo: " + aluguel6.getId() + 
                    " (Consultoria Prime - Corolla MEDIO)");
                
                // ALUGUÉIS FINALIZADOS (devolvidos)
                
                // João Santos com Jetta (já devolvido) - categoria MEDIO
                Aluguel aluguel7 = aluguelService.alugar("98765432100", "EFG-5555",
                    agora.minusDays(8), "Filial Norte");
                aluguelService.devolver(aluguel7.getId(), agora.minusDays(6), "Filial Centro");
                System.out.println("✅ Aluguel finalizado: " + aluguel7.getId() + 
                    " (João Santos - Jetta MEDIO - R$ " + aluguel7.getValorTotal() + ")");
                
                // Maria Oliveira com Palio (já devolvido) - categoria PEQUENO
                Aluguel aluguel8 = aluguelService.alugar("11122233344", "STU-1111",
                    agora.minusDays(10), "Filial Sul");
                aluguelService.devolver(aluguel8.getId(), agora.minusDays(7), "Filial Norte");
                System.out.println("✅ Aluguel finalizado: " + aluguel8.getId() + 
                    " (Maria Oliveira - Palio PEQUENO - R$ " + aluguel8.getValorTotal() + ")");
                
                // Empresa ABC com Ranger (já devolvido) - categoria SUV  
                Aluguel aluguel9 = aluguelService.alugar("98765432000111", "QRS-9999",
                    agora.minusDays(12), "Filial Leste");
                aluguelService.devolver(aluguel9.getId(), agora.minusDays(9), "Filial Sul");
                System.out.println("✅ Aluguel finalizado: " + aluguel9.getId() + 
                    " (Empresa ABC - Ranger SUV - R$ " + aluguel9.getValorTotal() + ")");
                
                System.out.println("\n=== RESUMO DOS DADOS CRIADOS ===");
                System.out.println("📋 CLIENTES:");
                System.out.println("   • Total: " + clienteService.listarTodos().size() + " clientes");
                System.out.println("   • Pessoa Física: 6 clientes");
                System.out.println("   • Pessoa Jurídica: 6 clientes");
                
                System.out.println("🚗 VEÍCULOS:");
                System.out.println("   • Total: " + veiculoService.listarTodos().size() + " veículos");
                System.out.println("   • Categoria PEQUENO: 6 veículos");
                System.out.println("   • Categoria MEDIO: 6 veículos");
                System.out.println("   • Categoria SUV: 6 veículos");
                
                System.out.println("📄 ALUGUÉIS:");
                System.out.println("   • Aluguéis ativos: " + aluguelService.listarAtivos().size());
                System.out.println("   • Aluguéis totais: " + aluguelService.listarTodos().size());
                System.out.println("   • Veículos disponíveis: " + veiculoService.listarDisponiveis().size());
                System.out.println("\n✅ Sistema pronto para uso!");
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
