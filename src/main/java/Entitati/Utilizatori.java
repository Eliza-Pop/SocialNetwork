package Entitati;

import java.util.Objects;

public class Utilizatori extends Entitati <String>{

    private String Nume;
    private String Prenume;
    private Long IdBD;

    public Utilizatori(String Username, String Prenume, String Nume) {

        this(Username, Prenume, Nume, null);

    }

    public Utilizatori(String Username, String Prenume, String Nume, Long IdBD) {

        super(Username);
        this.Nume=Nume;
        this.Prenume=Prenume;
        this.IdBD=IdBD;
    }

    public Long get_IdBD()
    {
        return IdBD;
    }

    public String get_Nume()
    {
        return Nume;
    }

    public String get_Prenume()
    {
        return Prenume;
    }

    public String get_Username(){return get_id();}

    public void set_Nume(String Nume)
    {
        this.Nume=Nume;
    }

    public void set_Prenume(String Prenume)
    {
        this.Prenume=Prenume;
    }

    public void set_IdBD(Long IdBD) {
       this.IdBD = IdBD;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this==o) {
            return true;
        }
        else
        {
            if(!(o instanceof Utilizatori))
            {
                return false;
            }
            else
            {
                return Objects.equals(get_id(),((Utilizatori) o).get_id());
            }
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(get_id());
    }

}
