package Repository.Memorie.Fisier;

import Entitati.Utilizatori;
import Entitati.Validator.Validator;


import java.nio.file.Path;

public class UtilizatoriFileRepository extends AbstractFileRepository<String, Utilizatori> {

    public UtilizatoriFileRepository(Validator<Utilizatori> validator, Path cale) {
        super(validator, cale);
    }

    /**
     * Primeste linia si o transforma in element Utilizator
     *
     * @param linie o linie nevida din fisier
     *@return entitatea Utilizator formata din elementele liniei
     *@throws IllegalArgumentException daca linia nu contine toate elementele
     */
    @Override
    protected Utilizatori extractEntity(String linie) {
        String[] p=linie.split(";",-1);
        if(p.length!=3)
        {
            throw new IllegalArgumentException("Se asteptau trei campuri dar am primit doar "+p.length+" !");
        }
        return new Utilizatori(p[0].trim(),p[1].trim(),p[2].trim());
    }

    /**
     * Primeste o entitate Utilizator si o returneaza ca string
     *
     * @param Entitate un utilizator valida
     *@return utilizatorul sub forma de string
     *
     */
    @Override
    protected String createEntityAsString(Utilizatori Entitate) {
        return Entitate.get_Username()+";"+Entitate.get_Prenume()+";"+Entitate.get_Nume();
    }
}
