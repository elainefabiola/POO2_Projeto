package services;

import database.Cliente;
import repositories.ClienteRepository;
import java.util.List;

public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public void cadastrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("Documento do cliente é obrigatório");
        }
        
        repository.salvar(cliente);
    }

    public Cliente buscarPorDocumento(String documento) {
        return repository.buscarPorId(documento);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return repository.buscarPorNomeParcial(nome);
    }

    public List<Cliente> listarTodos() {
        return repository.listarTodos();
    }

    public void removerCliente(String documento) {
        repository.remover(documento);
    }

    public boolean clienteExiste(String documento) {
        return repository.existe(documento);
    }
}
