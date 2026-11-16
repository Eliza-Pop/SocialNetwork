package Repository.Db;

import Entitati.Prietenie;
import Entitati.Tuple;
import Entitati.Utilizatori;
import Entitati.Validator.Validator;
import Excepti.ExceptieDuplicat;
import Excepti.ExceptieInexistenta;
import Repository.Repository;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PrietenieDbRepository implements Repository<Tuple<String,String>, Prietenie> {

    private static final String SELECT_ALL = "SELECT u1.username AS username1, u2.username AS username2, f.friends_from "
                    + "FROM friendships f "
                    + "JOIN users u1 ON u1.id = f.id1 "
                    + "JOIN users u2 ON u2.id = f.id2 ";

    private static final String SAME_PAIR = "a.username = ? AND b.username = ? "
                    + "AND f.id1 = LEAST(a.id, b.id) AND f.id2 = GREATEST(a.id, b.id)";

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

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    /**
     * Creates a map of all existing friendships in the database
     * @param rs
     * @return the newly created friendship
     * @throws SQLException
     */
    private static Prietenie map(ResultSet rs) throws SQLException{
        return new Prietenie(rs.getString("username1"),rs.getString("username2"),rs.getTimestamp("friends_from").toLocalDateTime());

    }

    /**
     * Searches for the friendship with the specified ID made up of a pair of usernames
     * @param ID
     * @return
     */
    @Override
    public Optional<Prietenie> findOne(Tuple<String, String> ID) {
            if(ID.getDreapta()==null || ID.getStanga()==null)
            {
                throw new IllegalArgumentException("Id-il nu poate fi null");
            }
            String sql = SELECT_ALL + "WHERE u1.username = ? AND u2.username = ? OR u1.username = ? AND u2.username = ?";
            try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql))
            {
               ps.setString(1,ID.getStanga());
               ps.setString(2,ID.getDreapta());
               ps.setString(3,ID.getDreapta());
               ps.setString(4,ID.getStanga());
               try(ResultSet rs = ps.executeQuery())
               {
                   return rs.next() ? Optional.of(map(rs)) : Optional.<Prietenie> empty();
               }
            }
            catch (SQLException e)
            {
                throw DbErrors.translate(e," searching a friendship !");
            }
    }

    /**
     * Finds and return all existing friendships
     * @return
     */
    @Override
    public Collection<Prietenie> findAll()
    {
        String sql = SELECT_ALL + "ORDER BY f.id1, f.id2";
        List<Prietenie> rezultat = new ArrayList<>();
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                rezultat.add(map(rs));
            }
            return rezultat;
        }
        catch (SQLException e)
        {
            throw DbErrors.translate(e," listing the friendships !");
        }
    }

    /**
     * Saves a specified friendships in the database and returns the added friendship
     * @param entitate
     * @return
     */
    @Override
    public Prietenie save(Prietenie entitate) {
        validator.validare(entitate);
        if(entitate == null)
        {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null !");
        }
        String sql = "INSERT INTO friendships (id1, id2, friends_from) "
                + "SELECT LEAST(a.id, b.id), GREATEST(a.id, b.id), CAST(? AS TIMESTAMP) "
                + "FROM users a, users b WHERE a.username = ? AND b.username = ?";
        try(Connection c = connect();PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setTimestamp(1,Timestamp.valueOf(entitate.Date()));
            ps.setString(2,entitate.Utilizator1());
            ps.setString(3,entitate.Utilizator2());
            if(ps.executeUpdate()==0)
            {
                throw new ExceptieInexistenta("Unul dintre utilizatori nu exista!");
            }
            return entitate;
        }
        catch (SQLException e)
        {
            if(DbErrors.has(e,DbErrors.UNIQUE_VIOLATION))
            {
                throw new ExceptieDuplicat("Exista deja acesta Prietenie !");
            }
            throw DbErrors.translate(e," saving the friendship !");
        }
    }

    /**
     * Delets a friendship with the specified ID made up of a pair of usernames from the database
     * @param ID
     * @return
     */
    @Override
    public Optional<Prietenie> delete(Tuple<String, String> ID) {
        if(ID.getDreapta()==null || ID.getStanga()==null)
        {
            throw new IllegalArgumentException("Id-il nu poate fi null");
        }
        String sql = "DELETE FROM friendships f USING users a, users b WHERE " + SAME_PAIR
                + " RETURNING f.friends_from";
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setString(1, ID.getStanga());
            ps.setString(2, ID.getDreapta());
            try(ResultSet rs = ps.executeQuery())
            {
                if(rs.next())
                    return Optional.of(new Prietenie(ID.getStanga(),ID.getDreapta(),rs.getTimestamp(1).toLocalDateTime()));
                else
                    return Optional.<Prietenie> empty();
            }
        }
        catch (SQLException e)
        {
            throw DbErrors.translate(e," deleting a user !");
        }
    }

    /**
     * Updates a specified frienship after it is modified
     * @param entitate
     * @return
     */
    @Override
    public Prietenie update(Prietenie entitate) {
        if(entitate==null)
            throw new IllegalArgumentException("Id-ul nu poate fi null !");
        String sql = "UPDATE Friendships f SET friends_from=CAST(?) AS TIMESTAMP FROM Users a , Users b WHERE"+SAME_PAIR;
        validator.validare(entitate);
        try(Connection c = connect();PreparedStatement ps= c.prepareStatement(sql))
        {
            ps.setTimestamp(1,Timestamp.valueOf(entitate.Date()));
            ps.setString(2,entitate.Utilizator1());
            ps.setString(3,entitate.Utilizator2());
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
