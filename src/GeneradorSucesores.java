package BusquedaLocal.src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class GeneradorSucesores implements SuccessorFunction {
    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        Estado estado =(Estado) aState;

        int cSwap = 0;
        int cMover = 0;
        int cEliminar = 0;

        for(int i = 0 ; i < estado.TrayectosSize(); ++i) { // cotches
            for (int j = 1; j < estado.GetTrayecto(i).size() - 1; ++j) { //conductor no
                for (int k = j + 1; k < estado.GetTrayecto(i).size() - 1; ++k) {
                    //if(!Objects.equals(estado.GetTrayecto(i).get(j), estado.GetTrayecto(i).get(k))){

                    Estado tmpEstado = new Estado(estado);
                    tmpEstado.OperadorSwap(i,j,k);
                    StringBuffer s = new StringBuffer();
                    s.append("OperadorSwap: coche " + i + ";  de pos " + j + ";  a pos " + k + ". " + tmpEstado.toString());
                    retVal.add(new Successor(s.toString(),tmpEstado));
                    ++cSwap;
                    //}
                }
            }
        }

        for(int i = 0; i < estado.TrayectosSize(); ++i){ // coche 1
            for(int j = 0; j < estado.TrayectosSize(); ++j) { // coche 2
                if (i != j) {
                    HashSet<Integer> setaux = new HashSet<Integer>();
                    for (int m = 1; m < estado.GetTrayecto(i).size() - 1; ++m) {
                        if (!setaux.contains(estado.GetTrayecto(i).get(m))) {
                            setaux.add(estado.GetTrayecto(i).get(m));
                            for (int k = 1; k <= (estado.GetTrayecto(j).size() - 1); ++k) { //on el deixem recollida

                                Estado tmpEstado = new Estado(estado);
                                tmpEstado.OperadorMover(i, j, estado.GetTrayecto(i).get(m), k);
                                StringBuffer s = new StringBuffer();
                                s.append("OperadorMove: coche " + i + ";   a coche " + j + ";   usuari " + m + ";   a " + k + ". " + tmpEstado.toString());
                                retVal.add(new Successor(s.toString(), tmpEstado));
                                ++cMover;

                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < estado.TrayectosSize(); ++i) { // coche 1
            if (estado.GetTrayecto(i).size() == 2) {
                for (int j = 0; j < estado.TrayectosSize(); ++j) { // coche 2
                    if (j != i) {
                        for (int k = 1; k <= (estado.GetTrayecto(j).size() - 1); ++k) { // on el deixem recollida

                            Estado tmpEstado = new Estado(estado);
                            tmpEstado.OperadorEliminar(i, j, k);
                            StringBuffer s = new StringBuffer();
                            s.append("OperadorEliminar: eliminar coche " + i + ";    afegir a coche " + j + ";  usuari " + k + ". " + tmpEstado.toString());
                            retVal.add(new Successor(s.toString(), tmpEstado));
                            ++cEliminar;
                        }

                    }
                }
            }
        }
        System.out.println("Nodos operador Swap creados: "+ cSwap+ "\n Nodos operador Mover creados : "+ cMover+ "\n Nodos operador Eliminar creados: "+ cEliminar );
        return retVal;
    }
}
