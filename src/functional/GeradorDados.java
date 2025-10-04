package functional;

import java.util.function.Supplier;

@FunctionalInterface
public interface GeradorDados<T> extends Supplier<T> {
    @Override
    T get();
}
