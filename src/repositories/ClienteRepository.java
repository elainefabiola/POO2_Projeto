package repositories;

import database.Cliente;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteRepository {


    public String getIdentificador(Cliente cliente) {
        return cliente.getDocumento();
    }

    private List<Cliente> clienteList;

    public ClienteRepository(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    public void salvar(Cliente cliente){
        clienteList.add(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteList;
    }

    public List<Cliente> buscarPorNomeParcial(String termo) {
        if (termo == null) termo = "";
        final String t = termo.toLowerCase();
        return listarTodos().stream()
                .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }
}
