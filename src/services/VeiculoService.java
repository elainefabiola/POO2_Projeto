package services;

import java.util.List;
import java.util.Optional;
import model.Veiculo;
import repositories.VeiculoRepository;

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
        // Validação de placa: padrão brasileiro antigo (ABC-1234) ou Mercosul (ABC1D23)
        String placa = veiculo.getPlaca();
        if (!placa.matches("[A-Z]{3}-\\d{4}") && !placa.matches("[A-Z]{3}\\d[A-Z]\\d{2}")) {
            throw new IllegalArgumentException("Placa inválida. Use o formato ABC-1234 ou ABC1D23.");
        }
        if (veiculo.getNome() == null || veiculo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do veículo é obrigatório");
        }
        if (veiculo.getTipo() == null) {
            throw new IllegalArgumentException("Tipo do veículo é obrigatório");
        }
        
        Optional<Veiculo> existente = repository.buscarPorPlaca(veiculo.getPlaca());
        if (existente.isPresent()) {
                throw new IllegalArgumentException("Já existe um veículo com esta placa cadastrado.");
        }
        
        repository.salvar(veiculo);
        repository.salvarEmArquivo();
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
        repository.salvarEmArquivo();
    }
}
