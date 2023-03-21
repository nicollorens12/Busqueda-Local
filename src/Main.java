package BusquedaLocal.src;

import IA.Comparticion.Usuario;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        int nUs, nCond, seed;
        //leer variables
        nUs = 10;
        nCond = 3;
        seed = 0;
        Estado e = new Estado(nUs,nCond,seed);
        for(int i = 0; i < e.TrayectoSize(); ++i){
            ArrayList<Integer> coche = e.GetTrayectos(i);
            int distancia = e.DistanciaTrayecto(i);
            double tiempo = e.TiempoDemora(i);
            System.out.println("coche : " + i + " Distancia: " + distancia + " Tiempo: " + tiempo);
            for(int j = 0; j < coche.size(); ++j){
                Integer usuarioId = coche.get(j);
                Usuario u = e.GetUsuario(usuarioId);
                String tipoPasajero = u.isConductor() ? "Conductor" : "Pasajero";
                String coodenadas = j >= coche.size()/2 ? "     Cordenadas destino X: " + u.getCoordDestinoX() + " Y: " + u.getCoordDestinoY() : "     Cordenadas Origen X: " + u.getCoordOrigenX() + " Y: " + u.getCoordOrigenY();
                System.out.println("    " + tipoPasajero + " numero " + usuarioId);
                System.out.println(coodenadas);
            }
        }
        //TSPHillClimbingSearch(e);
        //TSPSimulatedAnnealingSearch(TSPB);
    }
    private static void TSPHillClimbingSearch(Estado estate) {
        System.out.println("\nTSP HillClimbing  -->");
        try {
            Problem problem =  new Problem(estate,new GeneradorSucesores(), new EstadoFinal(),new Heuristica());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static void TSPSimulatedAnnealingSearch(Estado estate) {
        System.out.println("\nTSP Simulated Annealing  -->");
        try {
            Problem problem =  new Problem(estate,new GeneradorSucesores(), new EstadoFinal(),new Heuristica());
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(2000,100,5,0.001);
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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