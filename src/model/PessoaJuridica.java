package model;

public class PessoaJuridica extends Cliente {

    public PessoaJuridica(String cnpj, String nome) {
        super.setDocumento(cnpj);
        super.setNome(nome);
    }

    public String getCnpj() {
        return getDocumento();
    }

    @Override
    public double calcularDesconto(int dias) {
        if (dias > 3) {
            return 0.10; // 10% de desconto
        }
        return 0.0; // Sem desconto
    }
}
