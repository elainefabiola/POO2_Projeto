package database;

import java.time.LocalDate;

/**
 * Entidade que representa um aluguel de veículo.
 * Contém informações sobre o cliente, veículo, datas e valores.
 */
public class Aluguel {
    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double valorTotal;
    private boolean finalizado;

    public Aluguel(int id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.finalizado = false;
        calcularValorTotal();
    }

    private void calcularValorTotal() {
        int dias = (int) (dataFim.toEpochDay() - dataInicio.toEpochDay());
        double valorBase = veiculo.getTipo().calcularAluguel(dias);
        double desconto = cliente.calcularDesconto(dias);
        this.valorTotal = valorBase * (1 - desconto);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    @Override
    public String toString() {
        return String.format("Aluguel{id=%d, cliente='%s', veiculo='%s', valor=%.2f, finalizado=%s}",
                id, cliente.getNome(), veiculo.getNome(), valorTotal, finalizado ? "sim" : "não");
    }
}
