package Repository.Memorie.Fisier;

import Entitati.Prietenie;
import Entitati.Tuple;
import Entitati.Validator.Validator;
import Excepti.ExceptieDuplicat;
import Excepti.ExceptieInexistenta;
import Excepti.ExceptieValidare;


import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PrietenieFileRepository extends AbstractFileRepository<Tuple<String,String>, Prietenie>  {

    public PrietenieFileRepository(Validator<Prietenie> validator, Path cale) {
        super(validator, cale);
    }

    /**
     * Primeste linia si o transforma in element Prietenie
     *
     * @param linie o linie nevida din fisier
     *@return entitatea Prietenie formata din elementele liniei
     *@throws IllegalArgumentException daca linia nu contine toate elementele
     */
    @Override
    protected Prietenie extractEntity(String linie) {
        String[] p =linie.split(";",-1);
        if(p.length!=3)
        {
            throw new IllegalArgumentException("Se asteptau trei campuri dar am primit doar "+p.length+" !");
        }
        return new Prietenie(p[0].trim(),p[1].trim(), LocalDateTime.parse(p[2].trim()));
    }

    /**
     * Primeste o entitate Prietenie si o returneaza ca string
     *
     * @param Entitate o prietenie valida
     *@return prietenia sub forma de string
     *
     */
    @Override
    protected String createEntityAsString(Prietenie Entitate) {
        return Entitate.Utilizator1()+";"+Entitate.Utilizator2()+";"+Entitate.Date();
    }

}
