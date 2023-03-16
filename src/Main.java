import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

public class Main {
    public static void main(String[] args) {
        int nUs, nCond, seed;
        //leer variables
        nUs = 200;
        nCond = 100;
        Estado e = new Estado(nUs,nCond,0);
    }

    private static void TSPHillClimbingSearch(Estado city) {
        System.out.println("\nTSP HillClimbing  -->");
        try {
            Problem problem =  new Problem(city,new GeneradorSucesores(), new EstadoFinal(),new Heuristica());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            
            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
        
    }
    
    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}