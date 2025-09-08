package services;

import java.util.List;
import model.Cliente;
import repositories.ClienteRepository;

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

        String doc = cliente.getDocumento();
        // Validação básica de CPF (11 dígitos numéricos)
        if (cliente instanceof model.PessoaFisica) {
            if (!doc.matches("\\d{11}")) {
                throw new IllegalArgumentException("CPF deve conter apenas 11 numeros, e nada de letras");
            }
        }
        // Validação básica de CNPJ (14 dígitos numéricos)
        if (cliente instanceof model.PessoaJuridica) {
            if (!doc.matches("\\d{14}")) {
                throw new IllegalArgumentException("CNPJ deve conter apenas 14 numeros, e nada de letras");
            }
        }
            // Verifica se já existe cliente com o mesmo documento (CPF/CNPJ)
            if (repository.buscarPorDocumento(cliente.getDocumento()).isPresent()) {
                throw new IllegalArgumentException("Já existe um cliente com este CPF/CNPJ cadastrado");
            }
        
        repository.salvar(cliente);
        repository.salvarEmArquivo();
    }


    //public Cliente buscarPorDocumento(String documento) {
//        return repository.buscarPorId(documento);
//    }

    public List<Cliente> buscarPorNome(String nome) {
        return repository.buscarPorNomeParcial(nome);
    }

    public List<Cliente> listarTodos() {
        return repository.listarTodos();
    }

//    public void removerCliente(String documento) {
//        repository.remover(documento);
//    }
//
//    public boolean clienteExiste(String documento) {
//        return repository.existe(documento);
//    }
}
