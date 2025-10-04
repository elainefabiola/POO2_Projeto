package services;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import functional.ValidadorDocumento;
import model.Cliente;
import model.PessoaFisica;
import model.PessoaJuridica;
import repositories.ClienteRepository;

public class ClienteService {
    private final ClienteRepository repository;

    private static final Predicate<Cliente> CLIENTE_VALIDO = cliente ->
            cliente != null &&
            cliente.getNome() != null && !cliente.getNome().trim().isEmpty() &&
            cliente.getDocumento() != null && !cliente.getDocumento().trim().isEmpty();

    private static final Predicate<String> NOME_VALIDO = nome ->
            nome != null && !nome.trim().isEmpty();

    private static final ValidadorDocumento VALIDADOR_CPF = ValidadorDocumento.cpf();
    private static final ValidadorDocumento VALIDADOR_CNPJ = ValidadorDocumento.cnpj();

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public void cadastrarCliente(Cliente cliente) {
        if (!CLIENTE_VALIDO.test(cliente)) {
            throw new IllegalArgumentException("Dados do cliente são obrigatórios");
        }

        String doc = cliente.getDocumento();

        if (cliente instanceof PessoaFisica) {
            if (!VALIDADOR_CPF.validar(doc)) {
                throw new IllegalArgumentException("CPF deve conter apenas 11 numeros, e nada de letras");
            }
        }

        if (cliente instanceof PessoaJuridica) {
            if (!VALIDADOR_CNPJ.validar(doc)) {
                throw new IllegalArgumentException("CNPJ deve conter apenas 14 numeros, e nada de letras");
            }
        }

        if (repository.buscarPorDocumento(cliente.getDocumento()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente com este CPF/CNPJ cadastrado");
        }

        repository.salvar(cliente);
        repository.salvarEmArquivo();
    }

    public List<Cliente> buscarPorNome(String nome) {
        return repository.buscarPorNomeParcial(nome);
    }

    public List<Cliente> listarTodos() {
        return repository.listarTodos();
    }

    public List<Cliente> listarComPaginacao(int pagina, int tamanhoPagina) {
        return repository.listarComPaginacao(pagina, tamanhoPagina);
    }

    public List<Cliente> buscarComFiltro(Predicate<Cliente> filtro) {
        return repository.buscarComFiltro(filtro);
    }

    public List<Cliente> listarPessoasFisicas() {
        Predicate<Cliente> ehPessoaFisica = c -> c instanceof PessoaFisica;
        return repository.buscarComFiltro(ehPessoaFisica);
    }

    public List<Cliente> listarPessoasJuridicas() {
        Predicate<Cliente> ehPessoaJuridica = c -> c instanceof PessoaJuridica;
        return repository.buscarComFiltro(ehPessoaJuridica);
    }

    public void forEach(Consumer<Cliente> acao) {
        repository.listarTodos().forEach(acao);
    }

    public void imprimirClientes() {
        Consumer<Cliente> impressora = cliente -> {
            String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
            System.out.printf("[%s] %s - Doc: %s%n",
                    tipo, cliente.getNome(), cliente.getDocumento());
        };
        forEach(impressora);
    }

    public List<Cliente> listarOrdenadosPorNome() {
        return repository.listarTodos().stream()
                .sorted(Comparator.comparing(Cliente::getNome))
                .toList();
    }
}
