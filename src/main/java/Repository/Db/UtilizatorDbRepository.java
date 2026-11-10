package Repository.Db;

import Entitati.Utilizatori;
import Entitati.Validator.Validator;
import Repository.Repository;

import java.util.Collection;

import java.util.Optional;

public class UtilizatorDbRepository implements Repository<String, Utilizatori> {

    private final Validator<Utilizatori> validator;
    private final String url;
    private final String user;
    private final String password;

    public UtilizatorDbRepository(Validator<Utilizatori> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<Utilizatori> findOne(String s) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Collection<Utilizatori> findAll() {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Utilizatori save(Utilizatori entitate) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Optional<Utilizatori> delete(String s) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Utilizatori update(Utilizatori entitate) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }
}
