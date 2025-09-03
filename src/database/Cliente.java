package database;

public abstract class Cliente {
    private String documento;
    private String nome;

    public Cliente(String documento, String nome) {
        this.documento = documento;
        this.nome = nome;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public abstract double calcularDesconto(int dias);
}
