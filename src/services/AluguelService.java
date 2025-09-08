package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import model.Aluguel;
import model.Cliente;
import model.Veiculo;
import repositories.AluguelRepository;
import repositories.ClienteRepository;
import repositories.VeiculoRepository;

public class AluguelService {
    private final AluguelRepository aluguelRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    public AluguelService(AluguelRepository aluguelRepository, 
                         ClienteRepository clienteRepository,
                         VeiculoRepository veiculoRepository) {
        this.aluguelRepository = aluguelRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public Aluguel alugar(String documento, String placa, LocalDateTime retirada, String localRetirada) {
        // Buscar cliente
        Optional<Cliente> clienteOpt = clienteRepository.listarTodos().stream()
                .filter(c -> c.getDocumento().equals(documento))
                .findFirst();
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        // Buscar veículo
        Optional<Veiculo> veiculoOpt = veiculoRepository.buscarPorPlaca(placa);
        if (veiculoOpt.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }

        Veiculo veiculo = veiculoOpt.get();
        if (!veiculo.isDisponivel()) {
            throw new IllegalArgumentException("Veículo não está disponível");
        }

        // Criar aluguel
        String id = UUID.randomUUID().toString();
        Aluguel aluguel = new Aluguel(id, clienteOpt.get(), veiculo, retirada, localRetirada);
        
        // Marcar veículo como indisponível
        veiculo.setDisponivel(false);
        veiculoRepository.salvar(veiculo);
        
        // Salvar aluguel
        aluguelRepository.salvar(aluguel);
        
        return aluguel;
    }

    public void devolver(String aluguelId, LocalDateTime devolucao, String localDevolucao) {
        Optional<Aluguel> aluguelOpt = aluguelRepository.buscarPorId(aluguelId);
        if (aluguelOpt.isEmpty()) {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }

        Aluguel aluguel = aluguelOpt.get();
        if (!aluguel.isAtivo()) {
            throw new IllegalArgumentException("Aluguel já finalizado");
        }

        // Finalizar aluguel
        aluguel.finalizar(devolucao, localDevolucao);
        
        // Marcar veículo como disponível
        Veiculo veiculo = aluguel.getVeiculo();
        veiculo.setDisponivel(true);
        veiculoRepository.salvar(veiculo);
        
        // Atualizar aluguel
        aluguelRepository.salvar(aluguel);
    }

    public List<Aluguel> listarAtivos() {
        return aluguelRepository.buscarAtivos();
    }

    public List<Aluguel> listarPorCliente(String documento) {
        return aluguelRepository.buscarPorCliente(documento);
    }

    public List<Aluguel> listarPorVeiculo(String placa) {
        return aluguelRepository.buscarPorVeiculo(placa);
    }

    public List<Aluguel> listarTodos() {
        return aluguelRepository.listarTodos();
    }
}

