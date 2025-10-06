package services;

import functional.TipoClienteStrategy;
import functional.ValidadorDocumento;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import model.Cliente;
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

        // Refatorado: usando Predicate composto para validação
        Predicate<Cliente> validacaoPF = TipoClienteStrategy.EH_PESSOA_FISICA
                .and(c -> VALIDADOR_CPF.validar(c.getDocumento()));
        Predicate<Cliente> validacaoPJ = TipoClienteStrategy.EH_PESSOA_JURIDICA
                .and(c -> VALIDADOR_CNPJ.validar(c.getDocumento()));

        if (TipoClienteStrategy.EH_PESSOA_FISICA.test(cliente) && !validacaoPF.test(cliente)) {
            throw new IllegalArgumentException("CPF deve conter apenas 11 numeros, e nada de letras");
        }

        if (TipoClienteStrategy.EH_PESSOA_JURIDICA.test(cliente) && !validacaoPJ.test(cliente)) {
            throw new IllegalArgumentException("CNPJ deve conter apenas 14 numeros, e nada de letras");
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
        return repository.buscarComFiltro(TipoClienteStrategy.EH_PESSOA_FISICA);
    }

    public List<Cliente> listarPessoasJuridicas() {
        return repository.buscarComFiltro(TipoClienteStrategy.EH_PESSOA_JURIDICA);
    }

    public void forEach(Consumer<Cliente> acao) {
        repository.listarTodos().forEach(acao);
    }

    public void imprimirClientes() {
        // Refatorado: usando TipoClienteStrategy para determinar tipo
        Consumer<Cliente> impressora = cliente -> {
            String tipo = TipoClienteStrategy.TIPO_ABREVIADO.apply(cliente);
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
