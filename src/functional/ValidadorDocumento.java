package functional;

/**
 * Interface funcional para validação de documentos (CPF/CNPJ).
 * Utiliza Predicate semanticamente para validar regras de negócio.
 */
@FunctionalInterface
public interface ValidadorDocumento {
    /**
     * Valida um documento (CPF ou CNPJ).
     *
     * @param documento documento a ser validado
     * @return true se válido, false caso contrário
     */
    boolean validar(String documento);

    /**
     * Validador de CPF: deve conter exatamente 11 dígitos numéricos.
     */
    static ValidadorDocumento cpf() {
        return documento -> documento != null && documento.matches("\\d{11}");
    }

    /**
     * Validador de CNPJ: deve conter exatamente 14 dígitos numéricos.
     */
    static ValidadorDocumento cnpj() {
        return documento -> documento != null && documento.matches("\\d{14}");
    }

    /**
     * Composição: combina dois validadores com operador AND.
     */
    default ValidadorDocumento and(ValidadorDocumento outro) {
        return documento -> this.validar(documento) && outro.validar(documento);
    }

    /**
     * Composição: combina dois validadores com operador OR.
     */
    default ValidadorDocumento or(ValidadorDocumento outro) {
        return documento -> this.validar(documento) || outro.validar(documento);
    }
}
