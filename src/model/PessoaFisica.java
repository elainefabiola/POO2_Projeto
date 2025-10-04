package model;

public class PessoaFisica extends Cliente {
    private String cpf;

    public PessoaFisica(String cpf, String nome) {

        super.setDocumento(cpf);
        super.setNome(nome);
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public double calcularDesconto(int dias) {
        if (dias > 5) {
            return 0.05; // 5% de desconto
        }
        return 0.0; // Sem desconto
    }
}
