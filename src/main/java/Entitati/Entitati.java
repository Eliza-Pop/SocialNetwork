package Entitati;

public abstract class Entitati<ID>
{
    private ID  id;

    protected Entitati(ID id)
    {
        this.id=id;
    }

    public ID get_id()
    {
        return id;
    }

    public void set_id(ID id_nou)
    {
        this.id = id_nou;
    }
}
