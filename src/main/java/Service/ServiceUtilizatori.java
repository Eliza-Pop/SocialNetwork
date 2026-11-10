package Service;

import Entitati.Utilizatori;

import Repository.Repository;

import java.util.Collection;
import java.util.Optional;

public class ServiceUtilizatori {
    private final Repository<String, Utilizatori> repoU;

    public ServiceUtilizatori(Repository<String, Utilizatori> repoU) {
        this.repoU = repoU;
    }

    /**
     *It receives username,surname and name , it takes them and creates an object Utilizator
     * and adds this object to the existing list of objects
     *
     *
     * @return the deleted user
     */
    public Utilizatori AddUtilizator(String username, String prenume,String nume)
    {
        return repoU.save(new Utilizatori(username,prenume,nume));
    }

    /**
     *It searches for the user with the specified username
     *
     * @return the user it was looking for
     */
    public Optional<Utilizatori> CautaUtilizator(String username)
    {
        return repoU.findOne(username);
    }

    /**
     * Shows all existing users
     *
     * @return all existing user objects
     */
    public Collection<Utilizatori> AfiseazaTotiUtilizatori()
    {
        return repoU.findAll();
    }

    /**
     * Searches for the user with the specified username
     * @param username
     * @return
     */
    public Optional<Utilizatori> cautaUtilizator(String username)
    {
        return repoU.findOne(username);
    }
}
