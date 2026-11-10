package Repository.Memorie;

import Entitati.Entitati;
import Entitati.Validator.Validator;
import Excepti.ExceptieDuplicat;
import Excepti.ExceptieInexistenta;
import Repository.Repository;

import java.util.*;

public class InMemoryRepository <ID,E extends Entitati<ID>> implements Repository<ID,E> {

    private final Map<ID,E> date = new LinkedHashMap<>();
    private final Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id==null)
            throw new IllegalArgumentException("ID-ul nu poate fi null !");
        return Optional.ofNullable(date.get(id));
    }

    @Override
    public Collection<E> findAll() {
        return Collections.unmodifiableCollection(date.values());
    }

    @Override
    public E save(E entitate) {
        if(entitate==null)
            throw new IllegalArgumentException("Entitatea nu poate sa fie null !");
        validator.validare(entitate);
        if(date.containsKey(entitate.get_id()))
            throw new ExceptieDuplicat("Exista deja o entitate cu acest ID !");
        date.put(entitate.get_id(),entitate);
        return entitate;
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id==null)
            throw new IllegalArgumentException("ID-ul nu poate fi null!");
        return Optional.ofNullable(date.remove(id));
    }

    @Override
    public E update(E entitate) {
        if(entitate==null)
            throw new IllegalArgumentException("Entitatea nu poate fi null !");
        validator.validare(entitate);
        if(!date.containsKey(entitate.get_id()))
            throw new ExceptieInexistenta("Nu exista entitate cu acest ID !");
        return date.put(entitate.get_id(),entitate);
    }
}
