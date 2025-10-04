package functional;

@FunctionalInterface
public interface CalculadoraDesconto {
    double calcular(int dias);

    default CalculadoraDesconto maiorDesconto(CalculadoraDesconto outra) {
        return dias -> Math.max(this.calcular(dias), outra.calcular(dias));
    }
}
