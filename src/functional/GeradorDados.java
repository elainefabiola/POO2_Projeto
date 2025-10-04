package functional;

import java.util.function.Supplier;

/**
 * Interface funcional para geração de dados de teste.
 * Especialização do Supplier para criação de objetos de domínio.
 */
@FunctionalInterface
public interface GeradorDados<T> extends Supplier<T> {
    /**
     * Gera um dado de teste.
     *
     * @return objeto gerado
     */
    @Override
    T get();
}
