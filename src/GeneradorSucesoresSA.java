package BusquedaLocal.src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class GeneradorSucesoresSA implements SuccessorFunction {

    public static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt(max-min)+min;
        return randomNum;
    }

    public List getSuccessors(Object aState) {
        Estado estado = (Estado) aState;
        ArrayList<Successor> retval = new ArrayList<>();

        // Operador de eliminar coche
        if (estado.TrayectosSize() > 1) {
            int carToDelete = randInt(0, estado.TrayectosSize() - 1);
            while(estado.GetTrayecto(carToDelete).size() > 2){
                carToDelete = randInt(0, estado.TrayectosSize() - 1);
            }
            int destCar = randInt(0, estado.TrayectosSize() - 1);
            while (carToDelete == destCar) {
                destCar = randInt(0, estado.TrayectosSize() - 1);
            }
            int userIdx = 0;
            int userId = estado.GetTrayecto(carToDelete).get(userIdx);

            Estado tempState = new Estado(estado);
            tempState.OperadorEliminar(carToDelete, destCar, userId);
            StringBuffer s = new StringBuffer();
            s.append("OperadorEliminar: eliminar coche " + carToDelete + ";    afegir a " + destCar + ";  usuari " + userId);
            retval.add(new Successor(s.toString(), tempState));
        }

        // Operador de mover usuario entre coches
        if (estado.TrayectosSize() > 1) {
            int fromCar = randInt(0, estado.TrayectosSize() - 1);
            int toCar = randInt(0, estado.TrayectosSize() - 1);
            while (fromCar == toCar) {
                toCar = randInt(0, estado.TrayectosSize() - 1);
            }

            int fromCarSize = estado.GetTrayecto(fromCar).size();
            int userIdx = randInt(1, fromCarSize);
            int userId = estado.GetTrayecto(fromCar).get(userIdx);

            int toCarSize = estado.GetTrayecto(toCar).size();
            if (toCarSize < 4 && fromCarSize > 2) {
                int possAdd = 2;
                if(toCarSize > 2) possAdd = randInt(1,toCarSize);
                Estado tempState = new Estado(estado);
                tempState.OperadorMover(fromCar, toCar, userId,possAdd);
                StringBuffer s = new StringBuffer();
                s.append("OperadorMove: coche " + fromCar + ";   a coche " + toCar + ";   usuari " + userId + ";   a " + possAdd);
                retval.add(new Successor(s.toString(), tempState));
            }
        }

        // Operador de mover usuario dentro del mismo coche
        int car = randInt(0, estado.TrayectosSize() - 1);
        int carSize = estado.GetTrayecto(car).size();
        if (carSize > 2) {
            int fromIdx = randInt(1, carSize - 2);
            int toIdx = randInt(fromIdx + 1, carSize - 1);
            if (fromIdx != toIdx) {
                Estado tempState = new Estado(estado);
                //public void OperadorSwap(int coche, int i, int j){
                tempState.OperadorSwap(car,fromIdx,toIdx);
                StringBuffer s = new StringBuffer();
                s.append("OperadorSwap: coche "+car+";  usuari "+fromIdx+";  a pos "+fromIdx);
                retval.add(new Successor(s.toString(), tempState));
            }
        }
        return retval;
    }
}
