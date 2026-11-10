package Repository.Memorie.Fisier;

import Entitati.Entitati;
import Entitati.Validator.Validator;
import Excepti.*;
import Repository.Memorie.InMemoryRepository;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractFileRepository <ID,E extends Entitati<ID>> extends InMemoryRepository<ID,E>
{
    private final Path cale;

    public AbstractFileRepository(Validator<E> validator, Path cale) {
        super(validator);
        this.cale = cale;
        incarca();
    }

    /**
     * Convertește o linie din fișier într-o entitate de tip an.
     *
     *@param linie o linie nevidă din fișier
     *@return entitatea descrisă de linie
     *@throws IllegalArgumentException dacă nu sunt prezente câmpurile necesare
     *sau dacă un câmp nu poate fi analizat (parsate)
     *@throws DateTimeParseException dacă un câmp de tip dată nu este în format ISO
     */
    protected abstract E extractEntity(String linie);

    protected abstract String createEntityAsString(E Entitate);

    @Override
    public E save(E entitate)
    {
        E rezultat = super.save(entitate);
        scrieTot();
        return rezultat;
    }

    @Override
    public Optional<E> delete(ID id)
    {
        Optional<E> rezultat = super.delete(id);
        if(rezultat.isPresent())
            scrieTot();
        return rezultat;
    }

    @Override
    public E update(E entitate)
    {
        E rezultat = super.update(entitate);
        scrieTot();
        return rezultat;
    }

    /**
     * Rescrie intreg fisierul scriind ce se afla in memorie
     *
     * @throws RepositoryException daca nu poate deschide fisierul* */
    protected void scrieTot()
    {
        try {
            Path parinte = cale.getParent();
            if(parinte!=null)
            {
                Files.createDirectories(parinte);
            }
            List<String> linii=findAll().stream().map(this::createEntityAsString).collect(Collectors.toList());
            Files.write(cale,linii,StandardCharsets.UTF_8);
        }
        catch (IOException e){
            throw new RepositoryException("Nu a putut fi deschis fisierul : "+cale,e);
        }

    }

    private boolean incarcaLinia(int numar,String linie)
    {
        try
        {
            super.save(extractEntity(linie));
            return false;
        }
        catch (IllegalArgumentException | DateTimeParseException | ExceptieValidare | ExceptieDuplicat e)
        {
            System.err.println("Linia "+numar+" din fisier "+cale+" a fost ignorata pentru ca: "+e.getMessage());
            return false;
        }
        catch (ExceptieInexistenta e)
        {
            System.err.println("Nu exista utilizator pentru a creea prieteniile !");
            return true;
        }

    }

    /**
     * Citeste fisierul si adauga elementele Utilizator si Prietenii in memorie
     *
     *@throws IllegalArgumentException,DateTimeParseException,ExceptieValidare,ExceptieDuplicat in
     * functie de caz daca elementul citit din fisier este invalid
     *@throws ExceptieInexistenta dacă incearca sa creeze o prietenie cu un utilizator care nu exista
     *
     */
    private void incarca()
    {
        if(!Files.exists(cale))
        {
            return;
        }
        List<String> linii;
        try(Stream<String> randuri=Files.lines(cale,StandardCharsets.UTF_8))
        {
            linii=randuri.collect(Collectors.toList());
        }
        catch (IOException | UncheckedIOException e)
        {
            throw new ExceptieFisier("Nu a putut fi deschis fisierul "+cale,e);
        }
        boolean rescris= IntStream
                .range(0,linii.size())
                .filter(i->!linii
                        .get(i).
                        trim().
                        isEmpty()).
                mapToObj(i->incarcaLinia(i+1, linii.get(i)))
                .reduce(false,Boolean::logicalOr);
        if(rescris)
            scrieTot();
    }


    protected Optional<E> StergeFaraScriere(ID id)
    {
        return super.delete(id);
    }
}
