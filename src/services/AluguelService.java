package services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.Aluguel;
import model.Cliente;
import model.TipoVeiculo;
import model.Veiculo;
import repositories.AluguelRepository;
import repositories.ClienteRepository;
import repositories.VeiculoRepository;

public class AluguelService {
    private final AluguelRepository aluguelRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    private static final Predicate<Aluguel> ALUGUEL_ATIVO = Aluguel::isAtivo;
    private static final Predicate<Aluguel> ALUGUEL_FINALIZADO = aluguel -> !aluguel.isAtivo();

    private static final Function<Aluguel, String> ALUGUEL_PARA_DESCRICAO = aluguel ->
            String.format("ID: %s | Cliente: %s | Veículo: %s | Status: %s",
                    aluguel.getId().substring(0, 8),
                    aluguel.getCliente().getNome(),
                    aluguel.getVeiculo().getNome(),
                    aluguel.isAtivo() ? "Ativo" : "Finalizado");

    public AluguelService(AluguelRepository aluguelRepository,
                         ClienteRepository clienteRepository,
                         VeiculoRepository veiculoRepository) {
        this.aluguelRepository = aluguelRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public Aluguel alugar(String documento, String placa, LocalDateTime retirada, String localRetirada) {
        Optional<Cliente> clienteOpt = clienteRepository.listarTodos().stream()
                .filter(c -> c.getDocumento().equals(documento))
                .findFirst();
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        // Buscar veículo
        Optional<Veiculo> veiculoOpt = veiculoRepository.buscarPorPlaca(placa);
        if (veiculoOpt.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }

        Veiculo veiculo = veiculoOpt.get();
        if (!veiculo.isDisponivel()) {
            throw new IllegalArgumentException("Veículo não está disponível");
        }

        String id = UUID.randomUUID().toString();
        Aluguel aluguel = new Aluguel(id, clienteOpt.get(), veiculo, retirada, localRetirada);

        veiculo.setDisponivel(false);
        veiculoRepository.salvar(veiculo);

        // Salvar aluguel
        aluguelRepository.salvar(aluguel);
        aluguelRepository.salvarEmArquivo();

        return aluguel;
    }

    public void devolver(String aluguelId, LocalDateTime devolucao, String localDevolucao) {
        Optional<Aluguel> aluguelOpt = aluguelRepository.buscarPorId(aluguelId);
        if (aluguelOpt.isEmpty()) {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }

        Aluguel aluguel = aluguelOpt.get();
        if (!aluguel.isAtivo()) {
            throw new IllegalArgumentException("Aluguel já finalizado");
        }

        aluguel.finalizar(devolucao, localDevolucao);

        // Buscar o veículo atual na lista em memória para manter referência
        String placaVeiculo = aluguel.getVeiculo().getPlaca();
        Optional<Veiculo> veiculoAtualOpt = veiculoRepository.buscarPorPlaca(placaVeiculo);
        if (veiculoAtualOpt.isPresent()) {
            Veiculo veiculoAtual = veiculoAtualOpt.get();
            veiculoAtual.setDisponivel(true);
            veiculoRepository.salvar(veiculoAtual);
        }

        // Atualizar aluguel
        aluguelRepository.salvar(aluguel);
        aluguelRepository.salvarEmArquivo();
    }

    public List<Aluguel> listarAtivos() {
        return aluguelRepository.buscarAtivos();
    }

    /**
     * Lista aluguéis com paginação.
     */
    public List<Aluguel> listarComPaginacao(int pagina, int tamanhoPagina) {
        return aluguelRepository.listarComPaginacao(pagina, tamanhoPagina);
    }

    /**
     * Busca aluguéis usando Predicate personalizado.
     */
    public List<Aluguel> buscarComFiltro(Predicate<Aluguel> filtro) {
        return aluguelRepository.buscarComFiltro(filtro);
    }

    public List<Aluguel> listarPorCliente(String documento) {
        return aluguelRepository.buscarPorCliente(documento);
    }

    public List<Aluguel> listarPorVeiculo(String placa) {
        return aluguelRepository.buscarPorVeiculo(placa);
    }

    public List<Aluguel> listarTodos() {
        return aluguelRepository.listarTodos();
    }

    /**
     * Lista aluguéis finalizados.
     */
    public List<Aluguel> listarFinalizados() {
        return aluguelRepository.buscarFinalizados();
    }

    /**
     * Busca aluguéis por período.
     */
    public List<Aluguel> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return aluguelRepository.buscarPorPeriodo(inicio, fim);
    }

    /**
     * Executa uma ação em cada aluguel usando Consumer.
     */
    public void forEach(Consumer<Aluguel> acao) {
        aluguelRepository.listarTodos().forEach(acao);
    }

    /**
     * Imprime todos os aluguéis de forma formatada usando Consumer e Function.
     */
    public void imprimirAlugueis() {
        Consumer<Aluguel> impressora = aluguel ->
                System.out.println(ALUGUEL_PARA_DESCRICAO.apply(aluguel));
        forEach(impressora);
    }

    /**
     * Calcula faturamento total.
     */
    public BigDecimal calcularFaturamentoTotal() {
        return aluguelRepository.calcularFaturamentoTotal();
    }

    /**
     * Calcula faturamento por período.
     */
    public BigDecimal calcularFaturamentoPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return aluguelRepository.calcularFaturamentoPorPeriodo(inicio, fim);
    }

    /**
     * Agrupa aluguéis por tipo de veículo.
     */
    public Map<TipoVeiculo, List<Aluguel>> agruparPorTipoVeiculo() {
        return aluguelRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(a -> a.getVeiculo().getTipo()));
    }

    /**
     * Encontra os veículos mais alugados (retorna lista ordenada).
     */
    public List<Map.Entry<String, Long>> obterVeiculosMaisAlugados() {
        return aluguelRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        a -> a.getVeiculo().getPlaca() + " - " + a.getVeiculo().getNome(),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    /**
     * Encontra os clientes que mais alugaram (retorna lista ordenada).
     */
    public List<Map.Entry<String, Long>> obterClientesQueMaisAlugaram() {
        return aluguelRepository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCliente().getDocumento() + " - " + a.getCliente().getNome(),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    /**
     * Calcula faturamento total por tipo de veículo.
     */
    public Map<TipoVeiculo, BigDecimal> calcularFaturamentoPorTipo() {
        return aluguelRepository.buscarFinalizados().stream()
                .collect(Collectors.groupingBy(
                        a -> a.getVeiculo().getTipo(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Aluguel::getValorTotal,
                                BigDecimal::add)));
    }

    /**
     * Ordena aluguéis por data de retirada (mais recentes primeiro).
     */
    public List<Aluguel> listarOrdenadosPorDataRetirada() {
        return aluguelRepository.listarTodos().stream()
                .sorted(Comparator.comparing(Aluguel::getDataHoraRetirada).reversed())
                .toList();
    }
    /**
     * Busca um aluguel pelo prefixo do ID (primeiros caracteres do UUID).
     * Permite que o usuário digite apenas parte do ID exibido no menu.
     */
    public Optional<Aluguel> buscarPorPrefixo(String prefixo) {
        return aluguelRepository.buscarPorPrefixo(prefixo);
    }
}

