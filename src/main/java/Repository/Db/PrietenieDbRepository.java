package Repository.Db;

import Entitati.Prietenie;
import Entitati.Tuple;
import Entitati.Validator.Validator;
import Repository.Repository;

import java.util.Collection;
import java.util.Optional;

public class PrietenieDbRepository implements Repository<Tuple<String,String>, Prietenie> {

    private final Validator<Prietenie> validator;
    private final String url;
    private final String user;
    private final String password;

    public PrietenieDbRepository(Validator<Prietenie> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }


    @Override
    public Optional<Prietenie> findOne(Tuple<String, String> stringStringTuple) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Collection<Prietenie> findAll() {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Prietenie save(Prietenie entitate) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Optional<Prietenie> delete(Tuple<String, String> stringStringTuple) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }

    @Override
    public Prietenie update(Prietenie entitate) {
        throw new UnsupportedOperationException("UtilizatorDbRepository se implementeaza la laboratorul 4");
    }
}
