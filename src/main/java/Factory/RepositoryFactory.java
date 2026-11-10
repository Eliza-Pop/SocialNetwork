package Factory;

import Entitati.Prietenie;
import Entitati.Tuple;
import Entitati.Utilizatori;
import Entitati.Validator.Validator;
import Excepti.RepositoryException;
import Repository.Db.PrietenieDbRepository;
import Repository.Db.UtilizatorDbRepository;
import Repository.Memorie.Fisier.PrietenieFileRepository;
import Repository.Memorie.Fisier.UtilizatoriFileRepository;
import Repository.Memorie.InMemoryRepository;
import Repository.Repository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class RepositoryFactory {

    private final RepositoryConfig config;

    public RepositoryFactory(RepositoryConfig config) {
        this.config = config;
    }

    private <R> R creeaza(Map<RepositoryType,Supplier<R>> creatori)
    {
        return Optional.ofNullable(creatori.get(config.getType())).orElseThrow((()->new RepositoryException("Tip de persistenta nesuportat!"))).get();
    }

    public Repository<String, Utilizatori> createUtilizatoriRepository(Validator<Utilizatori> validator)
    {
        Map<RepositoryType, Supplier<Repository<String, Utilizatori>>> creatori = new EnumMap<RepositoryType, Supplier<Repository<String, Utilizatori>>>(RepositoryType.class);
        creatori.put(RepositoryType.MEMORY,()->new InMemoryRepository<String, Utilizatori>(validator));
        creatori.put(RepositoryType.DATABASE,()->new UtilizatorDbRepository(validator, config.getDbUrl(), config.getDbUser(), config.getDbPassword()));
        creatori.put(RepositoryType.FILE,()->new UtilizatoriFileRepository(validator,config.getUtilizatoriFile()));
        return creeaza(creatori);
    }

    public Repository<Tuple<String,String>, Prietenie> createPrietenieRepository(Validator<Prietenie> validator)
    {
        Map<RepositoryType, Supplier<Repository<Tuple<String,String>, Prietenie>>> creatori = new EnumMap<RepositoryType, Supplier<Repository<Tuple<String,String>, Prietenie>>>(RepositoryType.class);
        creatori.put(RepositoryType.MEMORY,()->new InMemoryRepository<Tuple<String, String>, Prietenie>(validator));
        creatori.put(RepositoryType.DATABASE,()->new PrietenieDbRepository(validator,config.getDbUrl(),config.getDbUser(),config.getDbPassword()));
        creatori.put(RepositoryType.FILE,()->new PrietenieFileRepository(validator,config.getPrieteniiFile()));
        return creeaza(creatori);
    }



}
