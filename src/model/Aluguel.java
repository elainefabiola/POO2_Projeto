package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

public class Aluguel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDateTime dataHoraRetirada;
    private LocalDateTime dataHoraDevolucao;
    private String localRetirada;
    private String localDevolucao;
    private BigDecimal valorTotal;
    private boolean ativo;

    public Aluguel(String id, Cliente cliente, Veiculo veiculo, LocalDateTime dataHoraRetirada, String localRetirada) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataHoraRetirada = dataHoraRetirada;
        this.localRetirada = localRetirada;
        this.ativo = true;
        this.valorTotal = BigDecimal.ZERO;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getDataHoraRetirada() {
        return dataHoraRetirada;
    }

    public LocalDateTime getDataHoraDevolucao() {
        return dataHoraDevolucao;
    }

    public String getLocalRetirada() {
        return localRetirada;
    }

    public String getLocalDevolucao() {
        return localDevolucao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void finalizar(LocalDateTime devolucao, String localDevolucao) {
        this.dataHoraDevolucao = devolucao;
        this.localDevolucao = localDevolucao;
        this.ativo = false;
        this.valorTotal = calcularValorTotal();
    }

    private BigDecimal calcularValorTotal() {
        int dias = calcularDias();
        double valorBruto = veiculo.getTipo().calcularAluguel(dias);
        double desconto = cliente.calcularDesconto(dias);
        double valorComDesconto = valorBruto * (1 - desconto);
        return BigDecimal.valueOf(valorComDesconto);
    }

    private int calcularDias() {
        if (dataHoraDevolucao == null) {
            return 0;
        }
        Duration duracao = Duration.between(dataHoraRetirada, dataHoraDevolucao);
        long horas = duracao.toHours();
        return (int) Math.ceil(horas / 24.0);
    }
}
