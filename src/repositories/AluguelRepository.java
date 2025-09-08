package repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.Aluguel;

public class AluguelRepository {
    private List<Aluguel> aluguelList;

    public AluguelRepository(List<Aluguel> aluguelList) {
        this.aluguelList = aluguelList;
    }

    public void salvar(Aluguel aluguel) {
        if (buscarPorId(aluguel.getId()).isEmpty()) {
            aluguelList.add(aluguel);
        } else {
            // Atualizar aluguel existente
            aluguelList.removeIf(a -> a.getId().equals(aluguel.getId()));
            aluguelList.add(aluguel);
        }
    }

    public Optional<Aluguel> buscarPorId(String id) {
        return aluguelList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Aluguel> listarTodos() {
        return aluguelList;
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
}
