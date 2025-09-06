package repositories;

import database.Veiculo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VeiculoRepository {
    private List<Veiculo> veiculoList;

    public VeiculoRepository(List<Veiculo> veiculoList) {
        this.veiculoList = veiculoList;
    }

    public void salvar(Veiculo veiculo) {
        if (buscarPorPlaca(veiculo.getPlaca()).isEmpty()) {
            veiculoList.add(veiculo);
        } else {
            // Atualizar veículo existente
            veiculoList.removeIf(v -> v.getPlaca().equals(veiculo.getPlaca()));
            veiculoList.add(veiculo);
        }
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
        return listarTodos().stream()
                .filter(v -> v.getNome() != null && v.getNome().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Veiculo> buscarDisponiveis() {
        return listarTodos().stream()
                .filter(Veiculo::isDisponivel)
                .collect(Collectors.toList());
    }
}
