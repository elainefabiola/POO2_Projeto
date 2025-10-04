package services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.TipoVeiculo;
import model.Veiculo;
import repositories.VeiculoRepository;

public class VeiculoService {
    private final VeiculoRepository repository;

    private static final Predicate<String> PLACA_VALIDA = placa ->
            placa != null &&
            (placa.matches("[A-Z]{3}-\\d{4}") || placa.matches("[A-Z]{3}\\d[A-Z]\\d{2}"));

    private static final Predicate<Veiculo> VEICULO_VALIDO = veiculo ->
            veiculo != null &&
            veiculo.getPlaca() != null && !veiculo.getPlaca().trim().isEmpty() &&
            veiculo.getNome() != null && !veiculo.getNome().trim().isEmpty() &&
            veiculo.getTipo() != null;

    private static final Function<Veiculo, String> VEICULO_PARA_DESCRICAO = veiculo ->
            String.format("%s - %s (%s) - %s",
                    veiculo.getPlaca(),
                    veiculo.getNome(),
                    veiculo.getTipo(),
                    veiculo.isDisponivel() ? "Disponível" : "Alugado");

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public void cadastrarVeiculo(Veiculo veiculo) {
        if (!VEICULO_VALIDO.test(veiculo)) {
            throw new IllegalArgumentException("Dados do veículo são obrigatórios");
        }

        if (!PLACA_VALIDA.test(veiculo.getPlaca())) {
            throw new IllegalArgumentException("Placa inválida. Use o formato ABC-1234 ou ABC1D23.");
        }

        Optional<Veiculo> existente = repository.buscarPorPlaca(veiculo.getPlaca());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Já existe um veículo com esta placa cadastrado.");
        }

        repository.salvar(veiculo);
        repository.salvarEmArquivo();
    }

    public Optional<Veiculo> buscarPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa é obrigatória");
        }
        return repository.buscarPorPlaca(placa);
    }

    public List<Veiculo> listarTodos() {
        return repository.listarTodos();
    }

    /**
     * Lista veículos com paginação.
     */
    public List<Veiculo> listarComPaginacao(int pagina, int tamanhoPagina) {
        return repository.listarComPaginacao(pagina, tamanhoPagina);
    }

    public List<Veiculo> listarDisponiveis() {
        return repository.buscarDisponiveis();
    }

    /**
     * Lista veículos disponíveis com paginação.
     */
    public List<Veiculo> listarDisponiveisComPaginacao(int pagina, int tamanhoPagina) {
        return repository.buscarDisponiveisComPaginacao(pagina, tamanhoPagina);
    }

    public List<Veiculo> buscarPorNome(String nome) {
        return repository.buscarPorNomeParcial(nome);
    }

    /**
     * Busca veículos usando Predicate personalizado.
     */
    public List<Veiculo> buscarComFiltro(Predicate<Veiculo> filtro) {
        return repository.buscarComFiltro(filtro);
    }

    /**
     * Lista veículos por tipo.
     */
    public List<Veiculo> listarPorTipo(TipoVeiculo tipo) {
        return repository.buscarPorTipo(tipo);
    }

    /**
     * Executa uma ação em cada veículo usando Consumer.
     */
    public void forEach(Consumer<Veiculo> acao) {
        repository.listarTodos().forEach(acao);
    }

    /**
     * Imprime todos os veículos de forma formatada usando Consumer e Function.
     */
    public void imprimirVeiculos() {
        Consumer<Veiculo> impressora = veiculo ->
                System.out.println(VEICULO_PARA_DESCRICAO.apply(veiculo));
        forEach(impressora);
    }

    /**
     * Transforma lista de veículos em Map agrupado por tipo usando Function.
     */
    public Map<TipoVeiculo, List<Veiculo>> agruparPorTipo() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(Veiculo::getTipo));
    }

    /**
     * Conta veículos disponíveis por tipo.
     */
    public Map<TipoVeiculo, Long> contarDisponiveisPorTipo() {
        return repository.buscarDisponiveis().stream()
                .collect(Collectors.groupingBy(Veiculo::getTipo, Collectors.counting()));
    }

    /**
     * Ordena veículos por nome usando Comparator.
     */
    public List<Veiculo> listarOrdenadosPorNome() {
        return repository.listarTodos().stream()
                .sorted(Comparator.comparing(Veiculo::getNome))
                .toList();
    }

    public void alterarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        Optional<Veiculo> existente = repository.buscarPorPlaca(veiculo.getPlaca());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }

        repository.salvar(veiculo);
        repository.salvarEmArquivo();
    }
}
