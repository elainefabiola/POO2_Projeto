package model;

public class PessoaFisica extends Cliente {

    public PessoaFisica(String cpf, String nome) {
        if (nome != null && nome.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Nome de pessoa física não pode conter números");
        }
        super.setDocumento(cpf);
        super.setNome(nome);
    }

    public String getCpf() {
        return getDocumento();
    }

    @Override
    public double calcularDesconto(int dias) {
        if (dias > 5) {
            return 0.05; // 5% de desconto
        }
        return 0.0; // Sem desconto
    }
}
