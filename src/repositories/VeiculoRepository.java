package repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
}
