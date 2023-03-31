package BusquedaLocal.src;

import aima.search.framework.GoalTest;

public class EstadoFinal implements GoalTest {

    public boolean isGoalState(Object aState) {
        /*boolean esFinal = true;
        Estado estado = (Estado) aState;
        int i=0;
        while(esFinal && i< estado.DistanciasSize()) {
            if (estado.DistanciaTrayecto(i) >300) esFinal = false;
            i++;
        }
        i=0;
        while(esFinal && i< estado.PasajerosSimultaneosSize()) {
            if (estado.PasajerosSimultaneos().get(i)>2) esFinal = false;
            i++;
        }

        //System.out.println("Número de coches utilizados: " + estado.TrayectosSize() + "   Distancia total recorrida: " + estado.CalcularDistanciaTotal() + " km.");
        return esFinal;*/
        return false;
    }
}
