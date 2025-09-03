package repositories;

import database.Cliente;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteRepository extends RepositorioMemoria<Cliente, String> {

    @Override
    public String getIdentificador(Cliente cliente) {
        return cliente.getDocumento();
    }

    public List<Cliente> buscarPorNomeParcial(String termo) {
        if (termo == null) termo = "";
        final String t = termo.toLowerCase();
        return listarTodos().stream()
                .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }
}
