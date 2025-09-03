package database;

/**
 * Enum enriquecido com valor de diária e helper de cálculo.
 * Mantém a regra de preço acoplada à categoria (limpo e expressivo).
 */
public enum TipoVeiculo {
    PEQUENO(100.0),
    MEDIO(150.0),
    SUV(200.0);

    private final double valorDiaria;

    TipoVeiculo(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    // Valor de diária base da categoria.
    public double getValorDiaria() {
        return valorDiaria;
    }

    // Atalho útil para serviços: total = diária * dias (sem multas/descontos).
    public double calcularAluguel(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Dias deve ser > 0.");
        return valorDiaria * dias;
    }
}
