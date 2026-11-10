package Entitati.Validator;

import Entitati.Utilizatori;
import Excepti.ExceptieValidare;

public class ValidatorUtilizator implements Validator<Utilizatori> {

    //Functie de validare se asigura ca la scrierea de la tastatura sau citirea din fisier
    //string username, string name si string prenume nu sunt null
    //O(1)
    public void validare(Utilizatori entitate)
    {
        if(entitate==null)
        {
            throw new ExceptieValidare("Utilizatorul nu poate fi NULL!");
        }
        else
        {
            if(entitate.get_id()==null)
            {
                throw new ExceptieValidare("Username invalid!");
            }
            else
            {
                if(entitate.get_Nume()==null)
                {
                    throw new ExceptieValidare("Nume invalid!");
                }
                else
                {
                    if(entitate.get_Prenume()==null)
                    {
                        throw new ExceptieValidare("Prenume invalid!");
                    }
                }
            }
        }
    }
}

