package UI;


import Entitati.Utilizatori;
import Excepti.*;
import Service.ServiceRetea;
import Service.ServiceUtilizatori;


import java.util.Scanner;

import static java.lang.System.exit;

public class UI {

    private final ServiceRetea serviceRetea;

    private final ServiceUtilizatori serviceUtilizatori;

    private final Scanner in = new Scanner(System.in);

    public UI(ServiceRetea serviceRetea, ServiceUtilizatori serviceUtilizatori) {
        this.serviceRetea = serviceRetea;
        this.serviceUtilizatori = serviceUtilizatori;
    }

    //Functia afiseaza meniul cu optiuni de actiuni
    //O(1)
    public void run()
    {
        boolean ruleaza=true;
        while(ruleaza)
        {
            System.out.println("\nMeniu:\n");
            System.out.println("\nIntroduceti numarul din fata actiunii dorite\n");
            System.out.println("0.Iesire");
            System.out.println("1.Adauga utilizator");
            System.out.println("2.Sterge utilizator");
            System.out.println("3.Adauga prietenie");
            System.out.println("4.Sterge prietenie");
            System.out.println("5.Afiseaza numarul de comunitati");
            System.out.println("6.Afiseaza cea mai sociabila comunitate");
            System.out.println("7.Afiseaza toate prieteniile existente");
            System.out.println("Comanda dorita: ");
            String optiune = in.nextLine().trim();
            try {
                executa(optiune);
            }catch (RepositoryException | UnsupportedOperationException | ExceptieDuplicat | ExceptieFisier | ExceptieInexistenta | ExceptiePrietenie | ExceptieValidare e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    //Functia apeleaza in functie de numarul dat de la tastatura functile
    //O(1)
    private void executa(String optiune)
    {
        switch (optiune)
        {
            case "0" :
                System.out.println("La revedere!");
                exit(0);
            case "1":
                serviceUtilizatori.AddUtilizator(citeste("username"),citeste("prenume"),citeste("nume"));
                System.out.println("Utilizatorul a fost adaugat");
                break;
            case "2":
                serviceRetea.StergeUtilizator(citeste("username"));
                System.out.println("Utilizatorul a fost sters");
                break;
            case "3":
                serviceRetea.AddPrietenie(citeste("username"),citeste("username"));
                System.out.println("Prietenia a fost adaugata");
                break;
            case "4":
                serviceRetea.StergePrietenie(citeste("username"),citeste("username"));
                System.out.println("Prietenia a fost stearsa");
                break;
            case "5":
                System.out.println("Numarul de comunitati este:");
                System.out.println(serviceRetea.numarComunitati());
                break;
            case "6":
                System.out.println("Cea mai sociabila comunitate este:");
                serviceRetea.ceaMaiLungaComunitate().stream().map(Utilizatori::get_Username).forEach(System.out::println);
                break;
        }
    }

    private String citeste(String optiune)
    {
        System.out.print(optiune+":");
        return in.nextLine().trim();
    }
}
