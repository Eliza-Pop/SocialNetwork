package Entitati.Validator;

import java.util.Objects;

@FunctionalInterface
public interface Validator <T>{
    void validare(T entitate);

    default Validator<T> andThen(Validator<T> other)
    {
        Objects.requireNonNull(other,"Validatorul nu poate fi null.");
        return entitate -> {
            validare(entitate);
            other.validare(entitate);
        };
    }
}
