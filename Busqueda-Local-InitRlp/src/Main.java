package BusquedaLocal.src;

import IA.Comparticion.Usuario;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        //for(int i = 0; i < 700; ++i) {
            int nUs, nCond, seed;
            nUs = 200;
            nCond = 100;
            seed = 1234;
            Estado e = new Estado(nUs, nCond, seed);

            boolean hc = true;
            if (hc) HillClimbingSearch(e);
            else {
                int it = 2000;
                int pit = 100;
                int k = 5;
                double lbd = 0.001;
                SimulatedAnnealingSearch(e,it,pit,k,lbd);
            }
        //}
    }
    private static void HillClimbingSearch(Estado estate) {
        System.out.println("\n| HillClimbing Seaarch |");
        try {
            Problem problem = new Problem(estate, new GeneradorSucesoresHC(), new EstadoFinal(), new Heuristica());
            Search search = new HillClimbingSearch();

            Date d1,d2;
            Calendar a,b;
            d1=new Date();
            try {
                SearchAgent agent = new SearchAgent(problem, search);
                System.out.println();
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
            } catch (Exception e) {
                e.printStackTrace();
            }
            d2=new Date();
            a= Calendar.getInstance();
            b= Calendar.getInstance();
            a.setTime(d1);
            b.setTime(d2);

            long m=b.getTimeInMillis()-a.getTimeInMillis();

            Estado solucion = (Estado) search.getGoalState();
            EstadoFinal comprobar = new EstadoFinal();
            //boolean esCorrecto = comprobar.isGoalState(solucion);
            if (solucion == null) System.out.println("Error: no hay solucion");
            //else if (!esCorrecto) System.out.println("La solucion NO es valida");
            //else {
                printSolucion(solucion);
                System.out.println("\nTiempo de ejecucion: " + m + " ms.   Número de coches utilizados: " + solucion.TrayectosSize() + "   Distancia total recorrida: " + solucion.CalcularDistanciaTotal() + " km. Forman parte todos los usuarios? " + solucion.TodosUsuarios(200));
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SimulatedAnnealingSearch(Estado estate, int it, int pit, int k, double lbd) {
        System.out.println("\n| Simulated Annealing Search |");
        try {
            Problem problem = new Problem(estate, new GeneradorSucesoresSA(), new EstadoFinal(), new Heuristica());
            Search search = new SimulatedAnnealingSearch(it,pit,k,lbd);

            Date d1,d2;
            Calendar a,b;
            d1=new Date();
            try {
                SearchAgent agent = new SearchAgent(problem, search);
                System.out.println();
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
            } catch (Exception e) {
                e.printStackTrace();
            }
            d2=new Date();
            a= Calendar.getInstance();
            b= Calendar.getInstance();
            a.setTime(d1);
            b.setTime(d2);

            long m=b.getTimeInMillis()-a.getTimeInMillis();

            System.out.println("T = "+m+" ms");

            Estado solucion = (Estado) search.getGoalState();
            //EstadoFinal comprobar = new EstadoFinal();
            //boolean esCorrecto = comprobar.isGoalState(solucion);
            if (solucion == null) System.out.println("Error: no hay solucion");
            //else if (!esCorrecto) System.out.println("La solucion NO es valida");
            //else {
                printSolucion(solucion);
                System.out.println("\n\nTiempo de ejecucion: " + m + " ms.   Número de coches utilizados: " + solucion.TrayectosSize() + "   Distancia total recorrida: " + solucion.CalcularDistanciaTotal() + " km. Forman parte todos los usuarios? " + solucion.TodosUsuarios(200));
            //}
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