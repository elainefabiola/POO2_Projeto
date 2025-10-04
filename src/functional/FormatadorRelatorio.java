package functional;

/**
 * Interface funcional para formatação de objetos em relatórios.
 * Converte objetos em representações String formatadas.
 */
@FunctionalInterface
public interface FormatadorRelatorio<T> {
    /**
     * Formata um objeto para exibição em relatório.
     *
     * @param objeto objeto a ser formatado
     * @return representação em String do objeto
     */
    String formatar(T objeto);

    /**
     * Composição: adiciona um prefixo à formatação.
     */
    default FormatadorRelatorio<T> comPrefixo(String prefixo) {
        return objeto -> prefixo + this.formatar(objeto);
    }

    /**
     * Composição: adiciona um sufixo à formatação.
     */
    default FormatadorRelatorio<T> comSufixo(String sufixo) {
        return objeto -> this.formatar(objeto) + sufixo;
    }
}
