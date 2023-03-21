package BusquedaLocal.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.lang.Math;
import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
public class Estado {

    public Usuarios users;
    public ArrayList<ArrayList<Integer>> trayectos;
    public ArrayList<Integer> distancias = new ArrayList<>(); // nose si dejarlo o calcularlo al momento, asi no gasta tanta memoria
    public ArrayList<Integer> pasajerosSimulatenos = new ArrayList<>(); // nose si dejarlo o calcularlo al momento, asi no gasta tanta memoria
    private int Velocidad = 30; //Km/h

    public Estado(int nUs, int nCond, int seed ){
        users = new Usuarios(nUs,nCond,seed);
        trayectos = Inicial3(); // Inicial1(nUs,nCond);
    }

    private ArrayList<ArrayList<Integer>> Inicial2 (){
        // creamos tantos trayectos como conducotres, llenamos cada coche con numero max personas
        // cogemos un pasajero, lo dejamos en y luego cogemos a otro
        ArrayList<ArrayList<Integer>> trayectos = new ArrayList<>();
        for(int i = 0; i < users.size(); ++i){
            ArrayList<Integer> coche = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                //Se añade dos veces dado que tiene que ser el orden que entra y sale
                coche.add(i);
                coche.add(i);
                trayectos.add(coche);
            }
        }
        for(int i = 0; i < users.size(); ++i){
            Usuario u = users.get(i);
            if(!u.isConductor()){
                int j = 0;
                boolean insertado = false;
                while (j < trayectos.size() && !insertado) {
                    if(trayectos.get(j).size() <= 6) {
                        insertado = true;
                        trayectos.get(j).add(1,i);
                        trayectos.get(j).add(1 ,i);
                    }
                    ++j;
                }
            }
        }
        distancias.clear();
        for(ArrayList<Integer> trayecto : trayectos){
            distancias.add(calcular_distancia(trayecto));
        }
        return trayectos;
    }

    private ArrayList<ArrayList<Integer>> Inicial3 (){
        // creamos tantos trayectos como conducotres, llenamos cada coche con numero max personas
        // Primero cogemos a los pasajeros y luego los dejamos a todos
        ArrayList<ArrayList<Integer>> trayectos = new ArrayList<>();
        for(int i = 0; i < users.size(); ++i){
            ArrayList<Integer> coche = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                //Se añade dos veces dado que tiene que ser el orden que entra y sale
                coche.add(i);
                coche.add(i);
                trayectos.add(coche);
            }
        }
        for(int i = 0; i < users.size(); ++i){
            Usuario u = users.get(i);
            if(!u.isConductor()){
                int j = 0;
                boolean insertado = false;
                while (j < trayectos.size() && !insertado) {
                    if(trayectos.get(j).size() < 6) {
                        insertado = true;
                        trayectos.get(j).add(1,i);
                        trayectos.get(j).add(trayectos.get(j).size() -1 ,i);
                    }
                    ++j;
                }
            }
        }
        distancias.clear();
        for(ArrayList<Integer> trayecto : trayectos){
            distancias.add(calcular_distancia(trayecto));
        }
        return trayectos;
    }


    public int calcular_distancia(List<Integer> trayecto){
        int dist = 0;
        if (trayecto.size() > 2){
            int xOrig = users.get(trayecto.get(0)).getCoordOrigenX();
            int yOrig = users.get(trayecto.get(0)).getCoordOrigenY();
            int xDest = users.get(trayecto.get(1)).getCoordOrigenX();
            int yDest = users.get(trayecto.get(1)).getCoordOrigenY();
            dist += Math.abs(xDest-xOrig)+Math.abs(yDest-yOrig);
        }
        for (int i=1; i<trayecto.size()-1; i+=2){
            int xOrig = users.get(trayecto.get(i)).getCoordOrigenX();
            int yOrig = users.get(trayecto.get(i)).getCoordOrigenY();
            int xDest = users.get(trayecto.get(i)).getCoordDestinoX();
            int yDest = users.get(trayecto.get(i)).getCoordDestinoY();
            dist += Math.abs(xDest-xOrig)+Math.abs(yDest-yOrig);
        }
        if (trayecto.size() > 2){
            int xOrig = users.get(trayecto.get(trayecto.size()-2)).getCoordDestinoX();
            int yOrig = users.get(trayecto.get(trayecto.size()-2)).getCoordDestinoY();
            int xDest = users.get(trayecto.get(trayecto.size()-1)).getCoordDestinoX();
            int yDest = users.get(trayecto.get(trayecto.size()-1)).getCoordDestinoY();
            dist += Math.abs(xDest-xOrig)+Math.abs(yDest-yOrig);
        }
        if (trayecto.size() == 2){
            int xOrig = users.get(trayecto.get(0)).getCoordOrigenX();
            int yOrig = users.get(trayecto.get(0)).getCoordOrigenY();
            int xDest = users.get(trayecto.get(1)).getCoordDestinoX();
            int yDest = users.get(trayecto.get(1)).getCoordDestinoY();
            dist += Math.abs(xDest-xOrig)+Math.abs(yDest-yOrig);
        }
        return dist;
    }
    public int calcular_pasajerosSimulataneos(List<Integer> trayecto){
        int maxPasajeros=0;
        //Esto da un null y no lo entiendo del todo pq, antes habia esto
        // Set aux = new Set<Integer>()
        Set aux = null;
        for (int i=1; i<trayecto.size()-1; ++i){
            if (aux.contains(trayecto.get(i))) aux.remove(trayecto.get(i));
            else {
                aux.add(trayecto.get(i));
                if (maxPasajeros<aux.size()) maxPasajeros = aux.size();
            }
        }
        return maxPasajeros;
    }

    public void OperadorMover(int coche1, int coche2, int userId, int userPos){

        int losDos = 0;
        for (int i=0; i<trayectos.get(coche1).size() && losDos != 2; ++i){
            if(trayectos.get(coche1).get(i).equals(userId)){
                ++losDos;
                trayectos.get(coche1).remove(i);
                --i;
            }
        }

        if (userPos == 0  || userPos>=trayectos.get(coche2).size()-1) userPos = 1;
        trayectos.get(coche2).add(userPos,userId);
        trayectos.get(coche2).add(userPos,userId);

        if (trayectos.get(coche1).size() != 0){
            distancias.set(coche1, calcular_distancia(trayectos.get(coche1)));
            pasajerosSimulatenos.set(coche1, pasajerosSimulatenos(trayectos.get(coche1)));
        }
        distancias.set(coche2, calcular_distancia(trayectos.get(coche2)));
        pasajerosSimulatenos.set(coche2, pasajerosSimulatenos(trayectos.get(coche2)));
    }

    private Integer pasajerosSimulatenos(ArrayList<Integer> integers) {
        return 1;
    }

    public void OperadorSwap(int coche, int i, int j){
        if (i != 0 && j != 0 && i != trayectos.get(coche).size()-1 && j != trayectos.get(coche).size()-1) {
            int pos;
            Collections.swap(trayectos.get(coche), i, j);
            distancias.set(coche, calcular_distancia(trayectos.get(coche)));
            pasajerosSimulatenos.set(coche, calcular_pasajerosSimulataneos(trayectos.get(coche)));
        }
    }
    public void OperadorEliminar(int coche1, int coche2,int userPos){
        if (trayectos.get(coche1).size() == 2){
            OperadorMover(coche1,coche2,trayectos.get(coche1).get(0),userPos);
            trayectos.remove(coche1);
            distancias.remove(coche1);
        }
    }

    public double Heuristica(){
        return Heuristica3(); // 1, 2 o 3
    }
    public double Heuristica1(){ // penaliza si dist>30km, nºpasajeros a la vez >2
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 1*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        return penalizacion;
    }
    public double Heuristica2(){ // penaliza más cuanto más distancia, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            penalizacion += 1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 1*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 1*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }
    public double Heuristica3(){ // penaliza más cuanto más distancia, muchisimo más cuando distancia>30km, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 100*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
            else penalizacion += 1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 1*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 1*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }

    public Usuarios GetUsuarios(){
        return this.users;
    }

    public Usuario GetUsuario(int index){
        return this.users.get(index);
    }

    public ArrayList<Integer> GetTrayectos(int index){
        return this.trayectos.get(index);
    }

    public int TrayectoSize(){
        return this.trayectos.size();
    }

    public int DistanciaTrayecto(int coche){
        return this.distancias.get(coche);
    }

    public double TiempoDemora(int coche){
        //v=d/t   t=d/v
        double distancia = this.distancias.get(coche);
        return distancia/ this.Velocidad;
    }
}