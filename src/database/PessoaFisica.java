package database;

public class PessoaFisica extends Cliente {
    private String cpf;

    public PessoaFisica(String cpf, String nome) {
        super(cpf, nome);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public double calcularDesconto(int dias) {
        // RN6: Caso o cliente pessoa física tenha ficado com o carro mais que 5 diárias terá direito a 5% de desconto.
        if (dias > 5) {
            return 0.05; // 5% de desconto
        }
        return 0.0; // Sem desconto
    }
}
