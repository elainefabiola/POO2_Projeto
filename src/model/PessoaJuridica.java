package model;

public class PessoaJuridica extends Cliente {
    private String cnpj;

    public PessoaJuridica(String cnpj, String nome) {
        super.setDocumento(cnpj);
        super.setNome(nome);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    @Override
    public double calcularDesconto(int dias) {
        if (dias > 3) {
            return 0.10; // 10% de desconto
        }
        return 0.0; // Sem desconto
    }
}
