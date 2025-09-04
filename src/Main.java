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


        ClienteRepository cr = new ClienteRepository(clientes); ///salvar na base, deletar, listar
        ClienteService cs = new ClienteService(cr); //regras de negocio, validacoes, possuipendencia

        cs.cadastrarCliente(new PessoaFisica("78363786", "Ana"));
        cs.cadastrarCliente(new PessoaFisica("12345", "joao"));


        new MenuPrincipal(cs).start();

//
//
//        List<Veiculo> veiculos = new ArrayList<>();
//        List<Aluguel> alugueis = new ArrayList<>();
//
//        // REPOSITÓRIOS
//        ClienteRepository cr = new ClienteRepository();
//        VeiculoRepository vr = new VeiculoRepository();
//        AluguelRepository ar = new AluguelRepository();
//
//        // SERVICES (regras de negócio)
//        ClienteService cs = new ClienteService(cr);
//        VeiculoService vs = new VeiculoService(vr);
//        AluguelService as = new AluguelService(ar, vr, cr);
//
//        // VIEW (interface do usuário)
//        MenuPrincipal mp = new MenuPrincipal(cs, vs, as);
//        mp.start(); // inicia o menu de interação
    }
}
