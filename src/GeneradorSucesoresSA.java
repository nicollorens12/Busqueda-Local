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
        int o = randInt(0,3);
        // if 1 swap inside
        if (o == 0) {
            int i = randInt(0,estado.TrayectosSize()-1);
            if(estado.GetTrayecto(i).size() > 2) {
                int j = randInt(1, estado.GetTrayecto(i).size() - 2);
                int k = randInt(j + 1, estado.GetTrayecto(i).size() - 1);
                if (!Objects.equals(estado.GetTrayecto(i).get(j), estado.GetTrayecto(i).get(k))) {
                    Estado temp_state = new Estado(estado);
                    temp_state.OperadorSwap(i, j, k);
                    StringBuffer s = new StringBuffer();
                    s.append("swap inside: Car " + i + " changing user " + j + " to position " + k); //+"\n"+ state.toString();
                    retval.add(new Successor(s.toString(), temp_state));
                    //    System.out.println(s.toString());
                    //  System.out.println(count);
                }
            }
        }
        // else swap outside
        else if(o == 1) {
            int i = randInt(0,estado.TrayectosSize()); //cotxe 1
            int j = randInt(0,estado.TrayectosSize());  //cotxe 2
            if (i != j) {
                if (estado.GetTrayecto(i).size() > 2) {
                    int m = randInt(1, estado.GetTrayecto(i).size() - 1);
                    int k = 1;
                    int l = 2;
                    if (estado.GetTrayecto(j).size() > 2) {
                        k = randInt(1, estado.GetTrayecto(j).size() - 2); //on el deixem recollida
                        l = randInt(k + 1, estado.GetTrayecto(j).size() - 1); //on el deixem deixada
                    }
                    Estado temp_state = new Estado(estado);
                    temp_state.OperadorMover(i, j, k, l);
                    StringBuffer s = new StringBuffer();
                    s.append("swap outside:  Car " + i + " to Car " + j + " changing user " + m + " from " + k + " to " + l);
                    retval.add(new Successor(s.toString(), temp_state));
                }
            }
        }

        else {
            int i = randInt(0,estado.TrayectosSize()); //cotxe 1
            if (estado.GetTrayecto(i).size() == 2) {
                int j = randInt(0, estado.TrayectosSize()); //cotxe 2
                if (i != j) {
                    int k = randInt(0,estado.GetTrayecto(i).size());
                    int usuarioId = estado.GetTrayecto(i).get(k);
                    Estado temp_state = new Estado(estado);
                    temp_state.OperadorEliminar(i, j, usuarioId);
                    StringBuffer s = new StringBuffer();
                    s.append("delete : Deleting car " + i + " and adding to Car " + j + " afegir a " + j + ";  usuari " + k); //+"\n"+ state.toString();
                    retval.add(new Successor(s.toString(), temp_state));
                }
            }

        }
        return retval;
    }
}
