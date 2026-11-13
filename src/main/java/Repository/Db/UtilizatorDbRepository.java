package Repository.Db;

import Entitati.Entitati;
import Entitati.Utilizatori;
import Entitati.Validator.Validator;
import Excepti.ExceptieDuplicat;
import Repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.Optional;

public class UtilizatorDbRepository implements Repository<String, Utilizatori> {

    private static final String COLUMNS = "id, username, first_name, last_name";

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
    public Optional<Utilizatori> findOne(String username) {
        if(username == null)
        {
            throw new IllegalArgumentException("Id-il nu poate fi null");
        }
        /**
         * SELECT COLUMS FROM Users  WHERE username = ?
         */
        String sql = "SELECT " + COLUMNS + " FROM Users WHERE username = ?";
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1,username);
            try(ResultSet rs = ps.executeQuery())
            {
                return rs.next() ? Optional.of(map(rs)) : Optional.<Utilizatori> empty();
            }
        }
        catch (SQLException e)
        {
            throw DbErrors.translate(e," searching a user !");
        }
    }

    @Override
    public Collection<Utilizatori> findAll() {
        String sql = "SELECT " + COLUMNS + " FROM Users ORDER BY id";
        List<Utilizatori> rezultat = new ArrayList<>();
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery())
        {
            while(rs.next())
            {
                rezultat.add(map(rs));
            }
            return rezultat;
        }
        catch (SQLException e)
        {
            throw DbErrors.translate(e," listing the users !");
        }
    }

    @Override
    public Utilizatori save(Utilizatori entitate) {
        /**
         * INSERT INTO Users(username, first_name, last_name) values(?,?,?) RETURNING id
         */
        validator.validare(entitate);
        if(entitate == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null !");
        }
        String sql = "INSERT INTO Users(username, first_name, last_name) values(?,?,?) RETURNING id ";
        try(Connection c = connect();PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1,entitate.get_Username());
            ps.setString(2,entitate.get_Prenume());
            ps.setString(3,entitate.get_Nume());
            try(ResultSet rs = ps.executeQuery())
            {
                rs.next();
                entitate.set_IdBD(rs.getLong(1));
            }
            return entitate;
        }
        catch (SQLException e)
        {
            if(DbErrors.has(e,DbErrors.UNIQUE_VIOLATION))
            {
                throw new ExceptieDuplicat("Exista deja un Utilizator cu acest username !");
            }
            throw DbErrors.translate(e," saving the user !");
        }
    }

    @Override
    public Optional<Utilizatori> delete(String username) {
        if(username==null)
            throw new IllegalArgumentException("Id-ul nu poate fi null !");
        String sql = "DELETE FROM Users WHERE username = ? RETURNING "+COLUMNS;
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1,username);
            try(ResultSet rs = ps.executeQuery())
            {
                if(rs.next())
                    return Optional.of(map(rs));
                else
                    return Optional.<Utilizatori> empty();
            }
        }
        catch (SQLException e)
        {
            throw DbErrors.translate(e," deleting a user !");
        }
    }

    @Override
    public Utilizatori update(Utilizatori entitate) {
        if(entitate==null)
            throw new IllegalArgumentException("Id-ul nu poate fi null !");
        String sql = "UPDATE Users SET first_name = ?, last_name = ? WHERE username = ?";
        validator.validare(entitate);
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1,entitate.get_Prenume());
            ps.setString(2,entitate.get_Nume());
            ps.setString(3,entitate.get_Username());
            if(ps.executeUpdate()==0)
            {
                return entitate;
            }
            else
                return null;
        }
        catch (SQLException e)
        {
            throw DbErrors.translate(e," updating a user !");
        }
    }
}
