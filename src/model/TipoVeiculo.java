package model;

public enum TipoVeiculo {
    PEQUENO(100.0),
    MEDIO(150.0),
    SUV(200.0);

    private final double valorDiaria;

    TipoVeiculo(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public double calcularAluguel(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Dias deve ser > 0.");
        return valorDiaria * dias;
    }
}
