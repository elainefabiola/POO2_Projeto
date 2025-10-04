package functional;

/**
 * Interface funcional para calcular desconto com base no número de diárias.
 * Encapsula a lógica de cálculo de desconto como uma operação funcional.
 */
@FunctionalInterface
public interface CalculadoraDesconto {
    /**
     * Calcula o desconto a ser aplicado.
     *
     * @param dias número de diárias do aluguel
     * @return percentual de desconto (0.0 a 1.0)
     */
    double calcular(int dias);

    /**
     * Composição de calculadoras: aplica o maior desconto entre duas calculadoras.
     */
    default CalculadoraDesconto maiorDesconto(CalculadoraDesconto outra) {
        return dias -> Math.max(this.calcular(dias), outra.calcular(dias));
    }
}
