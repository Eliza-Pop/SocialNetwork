package Repository;

import Entitati.Entitati;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Repository <ID,E extends Entitati<ID>> {

    Optional<E> findOne(ID id);

    Collection<E> findAll();

    default List<E> findAll(Predicate<E> filter) {
        return findAll().stream().filter(filter).collect(Collectors.toList());
    }

    E save(E entitate);

    Optional<E> delete(ID id);

    E update(E entitate);
}
