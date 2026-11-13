package org.example;

import Entitati.Prietenie;
import Entitati.Tuple;
import Entitati.Utilizatori;
import Entitati.Validator.Validator;
import Entitati.Validator.ValidatorPrietenie;
import Entitati.Validator.ValidatorUtilizator;
import Excepti.ExceptieValidare;
import Excepti.RepositoryException;
import Factory.RepositoryConfig;
import Factory.RepositoryFactory;


import Factory.RepositoryType;
import Repository.Repository;
import Service.ServiceRetea;
import Service.ServiceUtilizatori;
import UI.UI;

import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args)
    {
        try {
            RepositoryConfig config = RepositoryConfig.load(Paths.get("src/main/java/resources/config.properties"));
            RepositoryFactory factory = new RepositoryFactory(config);
            Validator<Utilizatori> validatorUtilizator = new ValidatorUtilizator().andThen(u -> {
                if (u.get_Username().trim().isEmpty() || u.get_Nume().trim().isEmpty() || u.get_Prenume().trim().isEmpty())
                    throw new ExceptieValidare("Nici un camp al utilizatorului nu poate fi gol!");
            }).andThen(u -> {
                if (config.getType() == RepositoryType.FILE && (u.get_Username().contains(";") || u.get_Nume().contains(";") || u.get_Prenume().contains(";")))
                    throw new ExceptieValidare("Utilizatorul nu poate contine caracterul ';' !");
            });
            Repository<String, Utilizatori> repoU = factory.createUtilizatoriRepository(validatorUtilizator);
            Repository<Tuple<String, String>, Prietenie> repoP = factory.createPrietenieRepository(new ValidatorPrietenie(repoU));
            new UI(new ServiceRetea(repoU, repoP), new ServiceUtilizatori(repoU)).run();
        }catch (RepositoryException e)
        {
            System.out.println("Aplicatia nu a putut porni !"+e.getMessage());
        }

    }
}