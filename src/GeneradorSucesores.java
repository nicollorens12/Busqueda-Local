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

        int count1 = 0;
        int count2 = 0;
        int count3 = 0;

        for(int i = 0 ; i < estado.TrayectosSize(); ++i) { //cotxes
            for (int j = 1; j < estado.GetTrayecto(i).size() - 1; ++j) { //primera persona
                for (int k = j + 1; k < estado.GetTrayecto(i).size() - 1; ++k) {
                    if(!Objects.equals(estado.GetTrayecto(i).get(j), estado.GetTrayecto(i).get(k))){
                        Estado tmpEstado = new Estado(estado);
                        tmpEstado.OperadorSwap(i,j,k);
                        StringBuffer s = new StringBuffer();
                        ++count1;
                        s.append("OperadorSwap: coche "+i+";  usuari "+j+";  a pos "+k); //+"\n"+ state.toString();
                        retVal.add(new Successor(s.toString(),tmpEstado));
                        //    System.out.println(s.toString());
                        //  System.out.println(count);
                    }
                }
            }
        }

        for(int i = 0; i < estado.TrayectosSize(); ++i){ //cotxe 1
            for(int j = 0; j < estado.TrayectosSize(); ++j) { //cotxe 2
                if (i != j) {
                    HashSet<Integer> setaux = new HashSet<Integer>();
                    for (int m = 1; m < estado.GetTrayecto(i).size() - 1; ++m) {
                        if (!setaux.contains(estado.GetTrayecto(i).get(m))) {
                            setaux.add(estado.GetTrayecto(i).get(m));
                            for (int k = 1; k <= (estado.GetTrayecto(j).size() - 1); ++k) { //on el deixem recollida

                                Estado tmpEstado = new Estado(estado);
                                tmpEstado.OperadorMover(i, j, estado.GetTrayecto(i).get(m), k);
                                StringBuffer s = new StringBuffer();
                                ++count2;

                                //System.out.println("Move:  Car " + i + " to Car " + j + " changing user " + m + " from " + k + " to " + l);
                                s.append("OperadorMove: coche " + i + ";   a coche " + j + ";   usuari " + m + ";   a " + k); //+"\n"+ state.toString();
                                retVal.add(new Successor(s.toString(), tmpEstado));
                                // System.out.println(s.toString());
                                //System.out.println(count);

                            }
                        }
                    }
                }
            }
        }

//l ultim de la llista no es pot borrar amb cap
        for(int i = 0; i < estado.TrayectosSize(); ++i) { //cotxe 1
            if (estado.GetTrayecto(i).size() == 2) {
                for (int j = 0; j < estado.TrayectosSize(); ++j) { //cotxe 2
                    if (j != i) {
                        for (int k = 1; k <= (estado.GetTrayecto(j).size() - 1); ++k) { //on el deixem recollida
                            Estado tmpEstado = new Estado(estado);
                            tmpEstado.OperadorEliminar(i, j, k); ++count3;
                            StringBuffer s = new StringBuffer();
                            s.append("OperadorEliminar: eliminar coche " + i + ";    afegir a " + j + ";  usuari " + k);
                            retVal.add(new Successor(s.toString(), tmpEstado));
                        }

                    }
                }
            }
        }
        System.out.println("Swap inside nodes created: "+ count1+ "\n Move nodes created: "+ count2+ "\n Delete Cars nodes created: "+ count3 );
        return retVal;
    }
}
