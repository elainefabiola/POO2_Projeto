package model;

/**
 * Entidade de domínio baseada no UML do grupo.
 * Atributos:
 *  - placa (identificador único natural)
 *  - nome  (modelo/descrição exibida ao usuário)
 *  - tipo  (enum TipoVeiculo)
 *  - disponivel (estado para aluguel/devolução)
 *
 * Observações:
 *  - Encapsulamento total (campos privados + getters/setters previstos no UML).
 *  - equals/hashCode por Placa (chave natural) para uso seguro em coleções.
 *  - toString para logs/console.
 */
public class Veiculo {

    private String placa;
    private String nome;            // no código do professor era "modelo"; o UML usa "nome"
    private TipoVeiculo tipo;
    private boolean disponivel = true; // por padrão, recém-cadastrado está disponível

    /**
     * Constrói um veículo válido.
     * @param placa identificador natural (ex.: "ABC-1234"). Não pode ser nula/vazia.
     * @param nome  descrição/modelo (ex.: "Gol 1.0"). Não pode ser nula/vazia.
     * @param tipo  categoria (PEQUENO, MEDIO, SUV). Não pode ser nula.
     */
    public Veiculo(String placa, String nome, TipoVeiculo tipo) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("Placa não pode ser nula/vazia.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo/vazio.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo do veículo não pode ser nulo.");
        }
        this.placa = placa;
        this.nome = nome;
        this.tipo = tipo;
    }

    // ==== Getters conforme o UML ====

    public String getPlaca() {
        return placa;
    }

    public String getNome() {
        return nome;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    // ==== Setters previstos no UML ====

    /**
     * Conforme o UML, o "setter" exposto é setTipoVeiculo().
     * Mantenho a assinatura simples (void), retornando o próprio enum via getTipo().
     */
    public void setTipoVeiculo(TipoVeiculo tipo) {
        if (tipo == null) throw new IllegalArgumentException("Tipo não pode ser nulo.");
        this.tipo = tipo;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    // ==== Utilidades ====

    /** Chave natural pensada para repositórios genéricos. */
    public String getIdentificador() {
        return placa;
    }

    @Override
    public String toString() {
        return String.format("Veiculo{placa='%s', nome='%s', tipo=%s, disponivel=%s}",
                placa, nome, tipo, disponivel ? "sim" : "não");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo v)) return false;
        return placa.equals(v.placa);
    }

    @Override
    public int hashCode() {
        return placa.hashCode();
    }
}
