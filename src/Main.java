import database.*;
import repositories.*;
import services.*;
import views.*;
import java.util.ArrayList;
import java.util.List;

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

        // SERVICES (regras de negócio)
        ClienteService clienteService = new ClienteService(clienteRepo);
        VeiculoService veiculoService = new VeiculoService(veiculoRepo);
        AluguelService aluguelService = new AluguelService(aluguelRepo, clienteRepo, veiculoRepo);

        // DADOS INICIAIS PARA TESTE
        try {
            clienteService.cadastrarCliente(new PessoaFisica("12345678900", "Ana Silva"));
            clienteService.cadastrarCliente(new PessoaFisica("98765432100", "João Santos"));
            clienteService.cadastrarCliente(new PessoaJuridica("12345678000100", "Tech Solutions Ltda"));

            veiculoService.cadastrarVeiculo(new Veiculo("ABC-1234", "Gol 1.0", TipoVeiculo.PEQUENO));
            veiculoService.cadastrarVeiculo(new Veiculo("DEF-5678", "Civic 2.0", TipoVeiculo.MEDIO));
            veiculoService.cadastrarVeiculo(new Veiculo("GHI-9012", "Hilux 2.8", TipoVeiculo.SUV));
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados iniciais: " + e.getMessage());
        }

        // VIEW (interface do usuário)
        MenuPrincipal menuPrincipal = new MenuPrincipal(clienteService, veiculoService, aluguelService);
        menuPrincipal.start();
    }
}
