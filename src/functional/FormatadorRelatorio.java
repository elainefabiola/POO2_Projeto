package functional;

@FunctionalInterface
public interface FormatadorRelatorio<T> {
    String formatar(T objeto);

    default FormatadorRelatorio<T> comPrefixo(String prefixo) {
        return objeto -> prefixo + this.formatar(objeto);
    }

    default FormatadorRelatorio<T> comSufixo(String sufixo) {
        return objeto -> this.formatar(objeto) + sufixo;
    }
}
