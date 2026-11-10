package Service;

import Entitati.Prietenie;
import Entitati.Tuple;
import Entitati.Utilizatori;
import Excepti.ExceptieInexistenta;
import Excepti.ExceptiePrietenie;
import Repository.Repository;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceRetea {

    private final Repository<String,Utilizatori> repoU;
    private final Repository<Tuple<String,String>,Prietenie> repoP;

    public ServiceRetea(Repository<String,Utilizatori> repoU,Repository<Tuple<String,String>,Prietenie> repoP)
    {
        this.repoU =repoU;
        this.repoP=repoP;
    }

    /**
     * It checks if a user exists returning them if they do or an exception if they don't
     *
     * @return the user with the specified username
     * @throws ExceptieInexistenta if the user doesn't exist
     */
    public Utilizatori ExistaU(String username) {

        return repoU.findOne(username).orElseThrow(()->new ExceptieInexistenta("Utilizatorul nu exista !"));
    }

    /**
     *It creates and adds to the existing list of friendships the friendship
     * between the two user with the specified usernames
     *
     * @throws ExceptiePrietenie when it is asked to create a friendship between the same user
     * @return the friendship
     */
    public Prietenie AddPrietenie(String username1, String username2)
    {
        if(username1.equals(username2))
        {
            throw new ExceptiePrietenie("Utilizatorii trebuie sa fie diferiti!");
        }
        else
        {
            ExistaU(username1);
            ExistaU(username2);
            return repoP.save(new Prietenie(username1, username2, LocalDateTime.now()));
        }
    }

    /**
     *It deletes the friendship with the specified usernames
     *
     * @throws ExceptiePrietenie when it is asked to delet friendship between a user and themself
     * @return the deleted friendship
     */
    public Prietenie StergePrietenie(String username1,String username2)
    {
        if(username1.equals(username2))
        {
            throw new ExceptiePrietenie("Utilizatorii trebuie sa fie diferiti!");
        }
        else {
            ExistaU(username1);
            ExistaU(username2);
            Prietenie p = repoP.delete(Prietenie.idPentru(username1,username2)).orElseThrow(()->new ExceptieInexistenta("Prietenia nu exista!"));
            return p;
        }
    }

    /**
     * It delets all the friemdships of the user with the specified username
     *
     * @param username
     */
    public void StergePrieteniileUtilizatorului(String username)
    {
        List<Tuple<String,String>> deSters = repoP.findAll(p->p.contine(username)).stream().map(Prietenie::get_id).collect(Collectors.toList());
        deSters.forEach(repoP::delete);
    }


    /**
     * It serches for the friendship that contains the specified usernames
     *
     * @param username1
     * @param username2
     * @return the friendship it looked for
     */
    public Optional<Prietenie> CautaPrietenie(String username1,String username2)
    {
        return repoP.findOne(Prietenie.idPentru(username1,username2));
    }

    /**
     *It deletes the user with the specified username
     *
     * @throws ExceptieInexistenta when it is asked to delet a user that doesn't exist
     * @return the deleted user
     */
    public Utilizatori StergeUtilizator(String username)
    {
        Utilizatori u = repoU.delete(username).orElseThrow(()->new ExceptieInexistenta("Nu exista acest utilizator!"));
        StergePrieteniileUtilizatorului(username);
        return u;
    }


    /**
     * It shows all existing friendships
     *
     * @return all frienships
     */
    public Collection<Prietenie> AfiseazaToatePrieteniile()
    {
        return repoP.findAll();
    }


    /**
     * It counts all the conected component of a specifed graph
     * @param g
     * @return the number of components
     */
    private List<List<String>> componente(Map<String,List<String>> g)
    {
        Set<String> v = new HashSet<>();
        List<List<String>> rezultat= new ArrayList<>();
        g.keySet().forEach(u->{
            if(!v.contains(u))
            {
                List<String> comp = new ArrayList<>();
                DFS(u,g,v,comp);
                rezultat.add(comp);
            }
        });
        return rezultat;
    }

    /**
     * It shows the number of comunities made up of existing frienships
     * a comunity is formed by a list of users who are all tied togheted by shared frienships
     * like a graph with no related components in it
     * @return number of comunities
     */
    public int numarComunitati()
    {
        return componente(graph()).size();
    }

    /**
     * Finds the comunity made uo of the most members
     * @return a list of all the users in the larges comunity
     */
    public List<Utilizatori> ceaMaiLungaComunitate()
    {
        Map<String, List<String>> g = graph();
        return componente(g).stream()
                .max(Comparator.comparingInt(comp->drum(comp,g)))
                .orElse(Collections.emptyList())
                .stream()
                .map(repoU::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Creates a graph of all existing frienships
     * @return the graph
     */
    private Map<String, List<String>> graph()
    {
        Map<String,List<String>> g=repoU.findAll().stream().collect(Collectors.toMap(Utilizatori::get_Username,u->new ArrayList<String>(),(a,b)->a,LinkedHashMap::new));
        repoP.findAll().stream().filter(p->g.containsKey(p.Utilizator1())&& g.containsKey(p.Utilizator2()))
                .forEach(p-> {
                    g.get(p.Utilizator1()).add(p.Utilizator2());
                    g.get(p.Utilizator2()).add(p.Utilizator1());
                });
        return g;

    }

    /**
     * Cautare in adancime DFS
     * @param x
     * @param g
     * @param v
     * @param comp
     */
    private void DFS(String x, Map<String ,List<String>> g, Set<String> v,List<String> comp)
    {

        v.add(x);
        comp.add(x);
        g.get(x).forEach(i->{
            if(!v.contains(i))
                DFS(i,g,v,comp);
        });

    }

    /**
     * Cautare in latime BFS
     * @param x
     * @param g
     * @return
     */
    private Map<String ,Integer> BFS(String x, Map<String ,List<String>> g)
    {
        Map<String,Integer> d = new HashMap<>();
        Queue<String> q = new LinkedList<>();
        d.put(x,0);
        q.add(x);
        while (!q.isEmpty())
        {
            String s = q.poll();
            g.get(s).stream().filter(i->! d.containsKey(i)).forEach(i->{
                d.put(i,d.get(s)+1);
                q.add(i);
            });
        }
        return d;
    }

    /**
     * Parcurge graful in cautarea unui drum
     *
     * @param comp
     * @param g
     * @return
     */
    private int drum(List<String> comp,Map<String,List<String>> g)
    {
        return comp.stream().mapToInt(s->{
            Map<String,Integer> d = BFS(s,g);
            return comp.stream().mapToInt(x->d.getOrDefault(x,0)).max().orElse(0);
        }).max().orElse(0);
    }


}
