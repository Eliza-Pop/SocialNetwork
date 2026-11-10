package Repository.Db;

import Entitati.Utilizatori;
import Entitati.Validator.Validator;
import Repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import java.util.Optional;

public class UtilizatorDbRepository implements Repository<String, Utilizatori> {

    private static final String COLLUMS = "id, username, first_name, last_name";

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

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

    private static Utilizatori map(ResultSet rs) throws SQLException{
        return new Utilizatori(rs.getLong("id"),rs.getString("username"),rs.getString("first_name"),rs.getString("last_name"));
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
