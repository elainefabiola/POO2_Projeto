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
        this.veiculoList = ArquivoUtil.lerLista(ARQUIVO_VEICULOS);
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
        carregarDeArquivo();
        return veiculoList.stream()
                .filter(v -> v.getPlaca().equals(placa))
                .findFirst();
    }

    public List<Veiculo> listarTodos() {
        carregarDeArquivo();
        return veiculoList;
    }

    public List<Veiculo> buscarPorNomeParcial(String termo) {
        carregarDeArquivo();
        if (termo == null) termo = "";
        final String t = termo.toLowerCase();
        return listarTodos().stream()
                .filter(v -> v.getNome() != null && v.getNome().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Veiculo> buscarDisponiveis() {
        carregarDeArquivo();
        return listarTodos().stream()
                .filter(Veiculo::isDisponivel)
                .collect(Collectors.toList());
    }
}
