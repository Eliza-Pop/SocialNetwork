package Entitati.Validator;

import Entitati.Prietenie;
import Entitati.Utilizatori;
import Excepti.ExceptieInexistenta;
import Excepti.ExceptieValidare;
import Repository.Repository;

public class ValidatorPrietenie implements Validator<Prietenie>{

    private final Repository<String, Utilizatori> repou;

    public ValidatorPrietenie(Repository<String, Utilizatori> repou) {
        this.repou = repou;
    }

    @Override
    public void validare(Prietenie entitate)
    {
        if(entitate==null)
        {
            throw new ExceptieValidare("Prietenia nu poate fi NULL!");
        }
        if(entitate.Utilizator1().equals(entitate.Utilizator2()))
        {
            throw new ExceptieValidare("Utilizatorii trebuie sa fie diferiti!");
        }
        if(entitate.Utilizator1()==null)
        {
            throw new ExceptieValidare("Primul utilizator este invalid !");
        }
        if(entitate.Utilizator2()==null)
        {
            throw new ExceptieValidare("Al doilea utilizator este invalid !");
        }
        if(entitate.Date()==null)
        {
            throw new ExceptieValidare("Data la care s-a creat prietenia este invalida !");
        }
        verificaExista(entitate.Utilizator1());
        verificaExista(entitate.Utilizator2());
    }

    private void verificaExista(String username)
    {
        if(repou.findOne(username).isEmpty())
            throw new ExceptieInexistenta("Utilizatorul "+username+" nu exista !");
    }
}
