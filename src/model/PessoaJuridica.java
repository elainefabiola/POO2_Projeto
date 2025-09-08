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
        // RN7: Caso o cliente pessoa jurídica tenha ficado com o carro mais que 3 diárias terá direito a 10% de desconto.
        if (dias > 3) {
            return 0.10; // 10% de desconto
        }
        return 0.0; // Sem desconto
    }
}
