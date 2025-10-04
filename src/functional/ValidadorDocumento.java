package functional;

@FunctionalInterface
public interface ValidadorDocumento {
    boolean validar(String documento);

    static ValidadorDocumento cpf() {
        return documento -> documento != null && documento.matches("\\d{11}");
    }

    static ValidadorDocumento cnpj() {
        return documento -> documento != null && documento.matches("\\d{14}");
    }

    default ValidadorDocumento and(ValidadorDocumento outro) {
        return documento -> this.validar(documento) && outro.validar(documento);
    }

    default ValidadorDocumento or(ValidadorDocumento outro) {
        return documento -> this.validar(documento) || outro.validar(documento);
    }
}
