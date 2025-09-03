package repositories;

import database.Aluguel;
import java.util.List;
import java.util.stream.Collectors;

public class AluguelRepository extends RepositorioMemoria<Aluguel, Integer> {

    @Override
    public Integer getIdentificador(Aluguel aluguel) {
        return aluguel.getId();
    }

    public void salvarComId(Aluguel aluguel) {
        if (aluguel.getId() == 0) {
            aluguel = new Aluguel(gerarId(), aluguel.getCliente(), aluguel.getVeiculo(), 
                                 aluguel.getDataInicio(), aluguel.getDataFim());
        }
        dados.put(aluguel.getId(), aluguel);
    }

    public List<Aluguel> buscarPorCliente(String documentoCliente) {
        return listarTodos().stream()
                .filter(a -> a.getCliente().getDocumento().equals(documentoCliente))
                .collect(Collectors.toList());
    }

    public List<Aluguel> buscarPorVeiculo(String placaVeiculo) {
        return listarTodos().stream()
                .filter(a -> a.getVeiculo().getPlaca().equals(placaVeiculo))
                .collect(Collectors.toList());
    }

    public List<Aluguel> buscarAtivos() {
        return listarTodos().stream()
                .filter(a -> !a.isFinalizado())
                .collect(Collectors.toList());
    }
}
