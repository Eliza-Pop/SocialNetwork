package Entitati;

import java.util.Objects;

public class Tuple<E1,E2>{
    private final E1 stanga;
    private final E2 dreapta;

    public Tuple(E1 stanga, E2 dreapta) {
        this.stanga = stanga;
        this.dreapta = dreapta;
    }

    public E1 getStanga()
    {
        return stanga;
    }

    public E2 getDreapta()
    {
        return dreapta;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this==o)
            return true;
        if(o==null || getClass()!=o.getClass())
        {
            return false;
        }
        Tuple<?,?> t = (Tuple<?, ?>) o;
        return Objects.equals(stanga,t.stanga) && Objects.equals(dreapta,t.dreapta);
    }

    @Override
    public int hashCode()
    {
        return  Objects.hash(stanga,dreapta);
    }

    @Override
    public String toString()
    {
        return "("+stanga+", "+dreapta+")";
    }
}
