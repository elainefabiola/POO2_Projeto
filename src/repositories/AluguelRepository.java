package repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.Aluguel;
import utils.ArquivoUtil;

public class AluguelRepository {
    private List<Aluguel> aluguelList;
    private static final String ARQUIVO_ALUGUEIS = "alugueis.dat";

    public AluguelRepository(List<Aluguel> aluguelList) {
        this.aluguelList = aluguelList;
    }

    public void salvarEmArquivo() {
        ArquivoUtil.salvarLista(ARQUIVO_ALUGUEIS, aluguelList);
    }

    public void carregarDeArquivo() {
        List<Aluguel> carregados = ArquivoUtil.lerLista(ARQUIVO_ALUGUEIS);
        if (carregados != null) {
            aluguelList.clear();
            aluguelList.addAll(carregados);
        }
    }

    public void salvar(Aluguel aluguel) {
        boolean existe = aluguelList.stream().anyMatch(a -> a.getId().equals(aluguel.getId()));

        if (!existe) {
            aluguelList.add(aluguel);
        } else {
            aluguelList.removeIf(a -> a.getId().equals(aluguel.getId()));
            aluguelList.add(aluguel);
        }
    }

    public Optional<Aluguel> buscarPorId(String id) {
        return aluguelList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    /**
     * Correção do bug: "Aluguel não encontrado" ao gerar recibos.
     * - Adicionado método buscarPorPrefixo(String prefixo)
     * - Permite encontrar aluguéis mesmo com ID truncado no menu
     */

    public Optional<Aluguel> buscarPorPrefixo(String prefixo) {
        if (prefixo == null || prefixo.isBlank()) {
            return Optional.empty();
        }

        return aluguelList.stream()
                .filter(a -> a.getId() != null && a.getId().startsWith(prefixo))
                .findFirst();
    }

    public List<Aluguel> listarTodos() {
        return aluguelList;
    }

    /**
     * Lista aluguéis com paginação usando Stream.skip() e limit().
     */
    public List<Aluguel> listarComPaginacao(int pagina, int tamanhoPagina) {
        return aluguelList.stream()
                .sorted(Comparator.comparing(Aluguel::getDataHoraRetirada).reversed())
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    /**
     * Busca aluguéis com filtro personalizado usando Predicate.
     */
    public List<Aluguel> buscarComFiltro(Predicate<Aluguel> filtro) {
        return aluguelList.stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    /**
     * Busca aluguéis com filtro e paginação.
     */
    public List<Aluguel> buscarComFiltroPaginado(Predicate<Aluguel> filtro, int pagina, int tamanhoPagina) {
        return aluguelList.stream()
                .filter(filtro)
                .sorted(Comparator.comparing(Aluguel::getDataHoraRetirada).reversed())
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    public List<Aluguel> buscarAtivos() {
        return aluguelList.stream()
                .filter(Aluguel::isAtivo)
                .collect(Collectors.toList());
    }

    public List<Aluguel> buscarPorCliente(String documento) {
        return aluguelList.stream()
                .filter(a -> a.getCliente().getDocumento().equals(documento))
                .collect(Collectors.toList());
    }

    public List<Aluguel> buscarPorVeiculo(String placa) {
        return aluguelList.stream()
                .filter(a -> a.getVeiculo().getPlaca().equals(placa))
                .collect(Collectors.toList());
    }

    /**
     * Busca aluguéis finalizados (não ativos).
     */
    public List<Aluguel> buscarFinalizados() {
        return aluguelList.stream()
                .filter(a -> !a.isAtivo())
                .collect(Collectors.toList());
    }

    /**
     * Busca aluguéis finalizados com paginação.
     */
    public List<Aluguel> buscarFinalizadosComPaginacao(int pagina, int tamanhoPagina) {
        return aluguelList.stream()
                .filter(a -> !a.isAtivo())
                .sorted(Comparator.comparing(Aluguel::getDataHoraDevolucao).reversed())
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    /**
     * Busca aluguéis por período (entre duas datas).
     */
    public List<Aluguel> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return aluguelList.stream()
                .filter(a -> !a.isAtivo())
                .filter(a -> a.getDataHoraDevolucao() != null)
                .filter(a -> !a.getDataHoraDevolucao().isBefore(inicio))
                .filter(a -> !a.getDataHoraDevolucao().isAfter(fim))
                .collect(Collectors.toList());
    }

    /**
     * Calcula faturamento total de aluguéis finalizados.
     */
    public BigDecimal calcularFaturamentoTotal() {
        return aluguelList.stream()
                .filter(a -> !a.isAtivo())
                .map(Aluguel::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula faturamento por período.
     */
    public BigDecimal calcularFaturamentoPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return buscarPorPeriodo(inicio, fim).stream()
                .map(Aluguel::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Conta o total de aluguéis que atendem a um filtro.
     */
    public long contarComFiltro(Predicate<Aluguel> filtro) {
        return aluguelList.stream()
                .filter(filtro)
                .count();
    }
}
