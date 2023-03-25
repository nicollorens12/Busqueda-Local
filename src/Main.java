package BusquedaLocal.src;

import IA.Comparticion.Usuario;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        //for(int i = 0; i < 700; ++i) {
            int nUs, nCond, seed;
            //leer variables
            nUs = 200;
            nCond = 100;
            seed = 1234;
            Estado e = new Estado(nUs, nCond, seed);
            //TSPHillClimbingSearch(e);
            TSPSimulatedAnnealingSearch(e);
        //}
    }
    private static void TSPHillClimbingSearch(Estado estate) {
        System.out.println("\nTSP HillClimbing  -->");
        try {
            long start_time = System.nanoTime();
            Problem problem = new Problem(estate, new GeneradorSucesores(), new EstadoFinal(), new Heuristica());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            long end_time = System.nanoTime();
            double difference = (end_time - start_time) / 1e6;


            //Distancia total de todos los conductores y numero de conductores
            //Numero de conductores es
            Estado solucion = (Estado) search.getGoalState();
            EstadoFinal comprobar = new EstadoFinal();
            boolean esCorrecto = comprobar.isGoalState(solucion);
            if (solucion == null) System.out.println("Joder no hay solucion");
            else System.out.println("La solucion es valida? " + esCorrecto);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            if (esCorrecto) {
                printSolucion(solucion);
                System.out.println("\n\nTime elapsed: " + difference + "milliseconds, which are " + (difference / 1e3) + " seconds.");
                int total_distance = solucion.CalcularDistanciaTotal();
                System.out.println("\n\nWith " + solucion.TrayectosSize() + " drivers and a total distance traveled of " + total_distance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void TSPSimulatedAnnealingSearch(Estado estate) {
        System.out.println("\nTSP Simulated Annealing  -->");
        try {
            long start_time = System.nanoTime();
            Problem problem =  new Problem(estate,new GeneradorSucesoresSA(), new EstadoFinal(),new Heuristica());
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(2000,100,5,0.001);
            search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);

            long end_time = System.nanoTime();
            double difference = (end_time-start_time) / 1e6;
            System.out.println("\n\nTime elapsed: " + difference + "milliseconds, which are " + (difference/1e3) + " seconds.");

            Estado solucion = (Estado) search.getGoalState();
            EstadoFinal comprobar = new EstadoFinal();
            boolean esCorrecto = comprobar.isGoalState(solucion);
            if (solucion == null) System.out.println("Joder no hay solucion");
            else System.out.println("La solucion es valida? " + esCorrecto);

            System.out.println();
            //List act = agent.getActions();
            //printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            if(esCorrecto) {
                printSolucion(solucion);
                System.out.println("\n\nTime elapsed: " + difference + "milliseconds, which are " + (difference / 1e3) + " seconds.");
                int total_distance = solucion.CalcularDistanciaTotal();
                System.out.println("\n\nWith " + solucion.TrayectosSize() + " drivers and a total distance traveled of " + total_distance);
            }
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

    private static void printSolucion(Estado estado){
        for(int i = 0; i < estado.TrayectosSize(); ++i){
            System.out.println("Coche #" + i);
            for(Integer usuarioId: estado.GetTrayecto(i)){
                Usuario usuario = estado.GetUsuario(usuarioId);
                String strPritn = usuario.isConductor() ? "Conductor " : "Pasajero" ;
                System.out.print(strPritn + " #" + usuarioId + ", ");
            }
            System.out.println(" ");
        }
    }
}