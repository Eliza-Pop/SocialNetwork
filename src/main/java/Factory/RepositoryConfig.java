package Factory;

import Excepti.RepositoryException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

public class RepositoryConfig {

    private final RepositoryType type;

    private final Path utilizatoriFile;
    private final Path prieteniiFile;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final int pageSize;

    public RepositoryConfig(RepositoryType type, Path utilizatoriFile, Path prieteniiFile, String dbUrl, String dbUser, String dbPassword , int pageSize) {
        this.type = type;
        this.utilizatoriFile = utilizatoriFile;
        this.prieteniiFile = prieteniiFile;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.pageSize = pageSize;
    }

    public String getDbUrl()
    {
        return dbUrl;
    }

    public String getDbUser()
    {
        return dbUser;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public Path getUtilizatoriFile()
    {
        return utilizatoriFile;
    }

    public Path getPrieteniiFile()
    {
        return prieteniiFile;
    }

    public RepositoryType getType()
    {
        return type;
    }

    private static RepositoryType citesteTip(Properties p)
    {
        String valoare = cere(p,"repository.type");
        try {

            return RepositoryType.valueOf(valoare.toUpperCase(Locale.ROOT));

        }catch (IllegalArgumentException e)
        {
            throw new RepositoryException("Tip de persistenta necunoscut "+valoare+" in repository.type"+" valori posibile:MEMORY,FILE,DATABASE",e);
        }
    }

    private static int citestePageSize(Properties p)
    {
        String valoare = cere(p,"page.size");
        try{
           int marime = Integer.parseInt(valoare);
           if(marime<=0)
               throw new RepositoryException("Marimea paginii trebuie sa fie mai mare decat zero !");
           return marime;

        }catch (NumberFormatException e)
        {
            throw new RepositoryException("Page.size trebuie sa fie un numar intreg ! "+valoare,e);
        }
    }

    public static RepositoryConfig load(Path cale) {
        Properties p = new Properties();
        try (BufferedReader r = Files.newBufferedReader(cale, StandardCharsets.UTF_8)) {
            p.load(r);
        } catch (IOException e) {
            throw new RepositoryException("Nu se poate citi fisierul de configurare " + cale, e);
        }
        RepositoryType tip =citesteTip(p) ;
        switch (tip)
        {
            case MEMORY:
                return new RepositoryConfig(tip,null,null,null,null,null,0);
            case DATABASE:
                return new RepositoryConfig(tip,null,null,cere(p,"db.url"),cere(p,"db.user"),cere(p,"db.password"),citestePageSize(p));
            case FILE:
                return new RepositoryConfig(tip, Paths.get(cere(p,"repository.file.Utilizatori")),Paths.get(cere(p,"repository.file.Prietenii")),null,null,null,0);
            default:
                throw new RepositoryException("Tip de persistenta nesuportat "+tip);
        }

    }

    private static String cere(Properties p,String cheie)
    {
        String valoare = p.getProperty(cheie);
        if(valoare==null || valoare.trim().isEmpty())
        {
            throw new RepositoryException("Lipseste proprietatea "+cheie+" din fisierul de configurare!");
        }
        return valoare.trim();
    }

}
