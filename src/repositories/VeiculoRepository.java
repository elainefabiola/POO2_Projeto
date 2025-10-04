package repositories;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.TipoVeiculo;
import model.Veiculo;
import utils.ArquivoUtil;

public class VeiculoRepository {
    private List<Veiculo> veiculoList;
    private static final String ARQUIVO_VEICULOS = "veiculos.dat";

    public VeiculoRepository(List<Veiculo> veiculoList) {
        this.veiculoList = veiculoList;
    }

    public void salvarEmArquivo() {
        ArquivoUtil.salvarLista(ARQUIVO_VEICULOS, veiculoList);
    }

    public void carregarDeArquivo() {
        List<Veiculo> carregados = ArquivoUtil.lerLista(ARQUIVO_VEICULOS);
        if (carregados != null) {
            veiculoList.clear();
            veiculoList.addAll(carregados);
        }
    }

    public void salvar(Veiculo veiculo) {
        if (buscarPorPlaca(veiculo.getPlaca()).isEmpty()) {
            veiculoList.add(veiculo);
        } else {
            veiculoList.removeIf(v -> v.getPlaca().equals(veiculo.getPlaca()));
            veiculoList.add(veiculo);
        }
        salvarEmArquivo();
    }

    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return veiculoList.stream()
                .filter(v -> v.getPlaca().equals(placa))
                .findFirst();
    }

    public List<Veiculo> listarTodos() {
        return veiculoList;
    }

    /**
     * Lista veículos com paginação usando Stream.skip() e limit().
     *
     * @param pagina número da página (começa em 0)
     * @param tamanhoPagina quantidade de itens por página
     * @return lista de veículos da página solicitada
     */
    public List<Veiculo> listarComPaginacao(int pagina, int tamanhoPagina) {
        return veiculoList.stream()
                .sorted(Comparator.comparing(Veiculo::getNome))
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    /**
     * Busca veículos com filtro personalizado usando Predicate.
     */
    public List<Veiculo> buscarComFiltro(Predicate<Veiculo> filtro) {
        return veiculoList.stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    /**
     * Busca veículos com filtro e paginação.
     */
    public List<Veiculo> buscarComFiltroPaginado(Predicate<Veiculo> filtro, int pagina, int tamanhoPagina) {
        return veiculoList.stream()
                .filter(filtro)
                .sorted(Comparator.comparing(Veiculo::getNome))
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    public List<Veiculo> buscarPorNomeParcial(String termo) {
        if (termo == null) termo = "";
        final String t = termo.toLowerCase();
        return veiculoList.stream()
                .filter(v -> v.getNome() != null && v.getNome().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Veiculo> buscarDisponiveis() {
        return veiculoList.stream()
                .filter(Veiculo::isDisponivel)
                .collect(Collectors.toList());
    }

    /**
     * Busca veículos disponíveis com paginação.
     */
    public List<Veiculo> buscarDisponiveisComPaginacao(int pagina, int tamanhoPagina) {
        return veiculoList.stream()
                .filter(Veiculo::isDisponivel)
                .sorted(Comparator.comparing(Veiculo::getNome))
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    /**
     * Busca veículos por tipo.
     */
    public List<Veiculo> buscarPorTipo(TipoVeiculo tipo) {
        return veiculoList.stream()
                .filter(v -> v.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Conta o total de veículos que atendem a um filtro.
     */
    public long contarComFiltro(Predicate<Veiculo> filtro) {
        return veiculoList.stream()
                .filter(filtro)
                .count();
    }
}
