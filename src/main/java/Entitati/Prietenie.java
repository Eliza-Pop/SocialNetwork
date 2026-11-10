package Entitati;

import Excepti.ExceptieValidare;

import java.time.LocalDateTime;

public class Prietenie extends Entitati<Tuple<String,String>>{
    private final LocalDateTime Date;

    public Prietenie(String utilizator1, String utilizator2) {
        this(utilizator1,utilizator2,LocalDateTime.now());

    }

    public Prietenie(String utilizator1, String utilizator2, LocalDateTime date) {
        super(idPentru(utilizator1,utilizator2));
        this.Date=date;
    }

    public static Tuple<String,String> idPentru(String a,String b)
    {
        if(a==null || b==null)
        {
            throw new ExceptieValidare("Username-ul nu poate fi null!");
        }
        if(a.compareTo(b)<=0)
        {
            return new Tuple<>(a,b);
        }
        return new Tuple<>(b,a);
    }

    public String Utilizator1() {
        return get_id().getStanga();
    }

    public String Utilizator2() {
        return get_id().getDreapta();
    }

    public LocalDateTime Date(){ return Date;}

    public boolean contine(String Username)
    {
        if(Utilizator1().equals(Username) || Utilizator2().equals(Username))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return Utilizator1() + "--" +Utilizator2() + " din: "+Date;
    }

}

