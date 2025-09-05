package services;

import database.Veiculo;
import repositories.VeiculoRepository;
import java.util.List;
import java.util.Optional;

public class VeiculoService {
    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public void cadastrarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa do veículo é obrigatória");
        }
        if (veiculo.getNome() == null || veiculo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do veículo é obrigatório");
        }
        if (veiculo.getTipo() == null) {
            throw new IllegalArgumentException("Tipo do veículo é obrigatório");
        }
        
        Optional<Veiculo> existente = repository.buscarPorPlaca(veiculo.getPlaca());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Placa já cadastrada");
        }
        
        repository.salvar(veiculo);
    }

    public Optional<Veiculo> buscarPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa é obrigatória");
        }
        return repository.buscarPorPlaca(placa);
    }

    public List<Veiculo> listarTodos() {
        return repository.listarTodos();
    }

    public List<Veiculo> listarDisponiveis() {
        return repository.buscarDisponiveis();
    }

    public List<Veiculo> buscarPorNome(String nome) {
        return repository.buscarPorNomeParcial(nome);
    }

    public void alterarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }
        
        Optional<Veiculo> existente = repository.buscarPorPlaca(veiculo.getPlaca());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }
        
        repository.salvar(veiculo);
    }
}
