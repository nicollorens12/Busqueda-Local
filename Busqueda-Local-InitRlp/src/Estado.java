package BusquedaLocal.src;

import java.util.*;
import java.lang.Math;
import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import com.sun.jdi.ArrayReference;

public class Estado {

    private static Usuarios users;
    private ArrayList<ArrayList<Integer>> trayectos;
    private ArrayList<Integer> distancias = new ArrayList<>(); // nose si dejarlo o calcularlo al momento, asi no gasta tanta memoria
    private ArrayList<Integer> pasajerosSimulatenos = new ArrayList<>(); // nose si dejarlo o calcularlo al momento, asi no gasta tanta memoria
    private static int Velocidad = 30; //Km/h

    //region Constructores

    public Estado(int nUs, int nCond, int seed ){
        users = new Usuarios(nUs,nCond,seed);

        int tipoGeneracion = 3;
        if (tipoGeneracion == 0) trayectos = InicialA();
        else if (tipoGeneracion == 1) trayectos = InicialB();
        else if (tipoGeneracion == 2) trayectos = InicialC();
        else {
            int maxOcup = nUs/nCond;
            trayectos = InicialValido(users, maxOcup);
        }
    }

    public Estado(Estado estado){
        this.users = estado.users;
        this.trayectos = new ArrayList(estado.trayectos.size());
        for (int i = 0; i < estado.trayectos.size(); ++i){
            this.trayectos.add(new ArrayList<Integer>(estado.trayectos.get(i)));

        }
        this.distancias = new ArrayList<>(estado.distancias);
        this.pasajerosSimulatenos = new ArrayList<>(estado.pasajerosSimulatenos);
    }

    //endregion

    //region EstadosIniciales

    /* creamos num coche max, colocamos 2 pasageros en forma 0ABAB0
    / creamos tantos trayectos como conducotres, llenamos cada coche con numero max personas
    / Primero cogemos a los pasajeros y luego los dejamos a todos*/
    private ArrayList<ArrayList<Integer>> InicialA (){
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
            distancias.add(CalcularDistancia(trayecto));
            pasajerosSimulatenos.add(calcularPasajerosSimulataneos(trayecto));
        }
        return trayectos;
    }

    /*creamos num coche max, colocamos 2 pasageros en forma 0AABB0
     creamos tantos trayectos como conducotres, llenamos cada coche con numero max personas*/
    private ArrayList<ArrayList<Integer>> InicialB (){
        ArrayList<ArrayList<Integer>> trayectos = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i<users.size(); ++i){
            ArrayList<Integer> coche = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                coche.add(i);
                coche.add(i);
                trayectos.add(coche);
            }
        }
        for(int i = 0; i<users.size(); ++i){
            Usuario u = users.get(i);
            if(!u.isConductor()){
                int j = 0; boolean insertado = false;
                while(j<trayectos.size() && !insertado){
                    if (trayectos.get(j).size() < 6){
                        insertado = true;
                        trayectos.get(j).add(1,i);
                        trayectos.get(j).add(1,i);
                    }
                    j++;
                }
            }
        }
        for(ArrayList<Integer> trayecto : trayectos){
            distancias.add(CalcularDistancia(trayecto));
            pasajerosSimulatenos.add(calcularPasajerosSimulataneos(trayecto));
        }
        return trayectos;
    }

    /*creamos num coche max, colocamos 1 pasager
      creamos tantos trayectos como conducotres, llenamos cada coche con numero max personas*/
    private ArrayList<ArrayList<Integer>> InicialC (){
        ArrayList<ArrayList<Integer>> trayectos = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i<users.size(); ++i){
            ArrayList<Integer> coche = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                coche.add(i);
                coche.add(i);
                trayectos.add(coche);
            }
        }
        for(int i = 0; i<users.size(); ++i){
            Usuario u = users.get(i);
            if(!u.isConductor()){
                int j = 0; boolean insertado = false;
                while(j<trayectos.size() && !insertado){
                    if (trayectos.get(j).size() < 4){
                        insertado = true;
                        trayectos.get(j).add(1,i);
                        trayectos.get(j).add(1,i);
                    }
                    j++;
                }
            }
        }
        for(ArrayList<Integer> trayecto : trayectos){
            distancias.add(CalcularDistancia(trayecto));
            pasajerosSimulatenos.add(calcularPasajerosSimulataneos(trayecto));
        }
        return trayectos;
    }

    /*En este caso hacemos la media de todos los usuarios y adjuntamos los usuarios mas cercanos
    * */
    private ArrayList<ArrayList<Integer>> InicialValido(Usuarios users, int maxOcup){
        if(maxOcup > 3){
            new Exception("No hay suficientes coches");
        }
        users.sort(new Distance());
        int size = users.size();
        ArrayList<Boolean> assigned = new ArrayList<>(Collections.nCopies(users.size(), false));
        ArrayList<ArrayList<Integer>> trayectos = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < size; ++i){

            if(users.get(i).isConductor()) {
                ArrayList<Integer> coche = new ArrayList<>();
                coche.add(i);
                coche.add(i);
                int left = iterate_left(users, i - 1, assigned);
                int right = iterate_right(users, i + 1, assigned);

                if (left == -1 && right != -1) {
                    coche.add(1,right);
                    coche.add(1,right);
                    assigned.set(i, true);
                    assigned.set(right, true);
                } else if (left != -1 && right == -1) {
                    coche.add(1,left);
                    coche.add(1,left);
                    assigned.set(i, true);
                    assigned.set(left, true);
                } else if ((i - left) <= (right - i)) {
                    coche.add(1,left);
                    coche.add(1,left);
                    assigned.set(i, true);
                    assigned.set(left, true);
                } else if ((i - left) > (right - i)) {
                    coche.add(1,right);
                    coche.add(1,right);
                    assigned.set(i, true);
                    assigned.set(right, true);
                } else { //Caso en que hay conductor pero no mas usuarios

                    assigned.set(i, true);
                }
                trayectos.add(coche);
            }
        }
        for(ArrayList<Integer> trayecto : trayectos){
            distancias.add(CalcularDistancia(trayecto));
            pasajerosSimulatenos.add(calcularPasajerosSimulataneos(trayecto));
        }
        return trayectos;
    }

    private int iterate_left(Usuarios users,int i, ArrayList<Boolean> assigned){
        if(i < 0) return -1;
        Usuario u = users.get(i);
        while(u.isConductor() || assigned.get(i)){
            --i;
            if(i < 0) return -1;
            u = users.get(i);
        }
        return i;
    }
    private int iterate_right(Usuarios users,int i , ArrayList<Boolean> assigned){
        if(i >= users.size()) return -1;
        Usuario u = users.get(i);
        while(u.isConductor() || assigned.get(i)){
            ++i;
            if(i >= users.size()) return -1;
            u = users.get(i);
        }
        return i;
    }

    //endregion

    //region Operadores y Heuristica

    public void OperadorMover(int coche1, int coche2, int userId, int userPos){

        int losDos = 0;
        for (int i=0; i<trayectos.get(coche1).size() && losDos != 2; ++i){
            if(trayectos.get(coche1).get(i).equals(userId)){
                trayectos.get(coche1).remove(i);
                ++losDos;
                --i;
            }
        }

        if (userPos == 0  || userPos>=trayectos.get(coche2).size()-1) userPos = 1;
        trayectos.get(coche2).add(userPos,userId);
        trayectos.get(coche2).add(userPos,userId);

        distancias.set(coche2, CalcularDistancia(trayectos.get(coche2)));
        pasajerosSimulatenos.set(coche2, calcularPasajerosSimulataneos(trayectos.get(coche2)));

        if (trayectos.get(coche1).size() != 0){
            distancias.set(coche1, CalcularDistancia(trayectos.get(coche1)));
            pasajerosSimulatenos.set(coche1, calcularPasajerosSimulataneos(trayectos.get(coche1)));
        } else {
            trayectos.remove(coche1);
            distancias.remove(coche1);
        }
    }

    public void OperadorSwap(int coche, int i, int j){
        if (i != 0 && j != 0 && i != trayectos.get(coche).size()-1 && j != trayectos.get(coche).size()-1) {
            Collections.swap(trayectos.get(coche), i, j);
            distancias.set(coche, CalcularDistancia(trayectos.get(coche)));
            pasajerosSimulatenos.set(coche, calcularPasajerosSimulataneos(trayectos.get(coche)));
        }
    }
    public void OperadorEliminar(int coche1, int coche2,int userPos){
        if (trayectos.get(coche1).size() == 2){
            OperadorMover(coche1,coche2,trayectos.get(coche1).get(0),userPos);
        }
    }
    public double Heuristica(){
        return Heuristica3();
        //return Borrar(); // 1, 2 o 3
    }

    //endregion

    //region ClasesPrivadas

    public int CalcularDistancia(List<Integer> trayecto){
        int dist = 0;//
        if(trayecto.size() > 0) {
            ArrayList<Integer> UsuariosRecogidos = new ArrayList<>();
            int xOrigT = users.get(trayecto.get(0)).getCoordOrigenX();
            int yOrigT = users.get(trayecto.get(0)).getCoordOrigenY();
            UsuariosRecogidos.add(trayecto.get(0));
            for (int i = 1; i < trayecto.size(); ++i) {
                int xDest = users.get(trayecto.get(i)).getCoordOrigenX();
                int yDest = users.get(trayecto.get(i)).getCoordOrigenY();
                if (EstaPasajero(UsuariosRecogidos, trayecto.get(i))) {
                    xDest = users.get(trayecto.get(i)).getCoordDestinoX();
                    yDest = users.get(trayecto.get(i)).getCoordDestinoY();
                } else {
                    UsuariosRecogidos.add(trayecto.get(i));
                }
                dist += Math.abs(xOrigT - xDest) + Math.abs(yOrigT - yDest);
                xOrigT = xDest;
                yOrigT = yDest;
            }
        }
        return dist;
    }

    public double CalcularDistanciaTotal(){ // km totals recorreguts (usuari 100m -> dividim per 10 -> 1km)
        int total_traveled = 0;
        for(int i = 0; i < distancias.size(); ++i) total_traveled += distancias.get(i);
        return total_traveled/10.0;
    }

    private boolean EstaPasajero(ArrayList<Integer> pasajeros, Integer pasajero){
        for(int i = 0; i < pasajeros.size();++i){
            if(pasajero == pasajeros.get(i)) return true;
        }
        return  false;
    }

    private int calcularPasajerosSimulataneos(List<Integer> trayecto){
        int maxPasajeros=0;
        HashSet aux = new HashSet<Integer>();
        for (int i=1; i<trayecto.size()-1; ++i){
            if (aux.contains(trayecto.get(i))) aux.remove(trayecto.get(i));
            else {
                aux.add(trayecto.get(i));
                if (maxPasajeros<aux.size()) maxPasajeros = aux.size();
            }
        }
        return maxPasajeros;
    }

    private double Heuristica1(){ // penaliza si dist>30km, nºpasajeros a la vez >2
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 1*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        return penalizacion;
    }

    private double Heuristica2(){ // penaliza más cuanto más distancia, nºpasajeros a la vez >2, nº de coches
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

    private double Heuristica3(){ // penaliza más cuanto más distancia, muchisimo más cuando distancia>30km, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 100*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
            else penalizacion += 1.1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 100*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 49*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }

    private double bbb(){ // penaliza más cuanto más distancia, muchisimo más cuando distancia>30km, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 100*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
            else penalizacion += 1.1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 100*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 50*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }

    private double aaa(){ // penaliza más cuanto más distancia, muchisimo más cuando distancia>30km, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 100*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
            else penalizacion += 1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 100*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 50*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }

    private double copiadoModificado(){ // penaliza más cuanto más distancia, muchisimo más cuando distancia>30km, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 2*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
            else penalizacion += 1*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 100*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 50*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }

    public double copiado(){ // penaliza más cuanto más distancia, muchisimo más cuando distancia>30km, nºpasajeros a la vez >2, nº de coches
        int penalizacion = 0;

        for(int i=0; i<distancias.size(); ++i){
            if (distancias.get(i) > 300) penalizacion += 5*distancias.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        for(int i=0; i<pasajerosSimulatenos.size(); ++i){
            if (pasajerosSimulatenos.get(i) > 2) penalizacion += 140*pasajerosSimulatenos.get(i); // podemos multiplicarlo por constante para darle más/menos peso
        }

        penalizacion += 80*trayectos.size(); // podemos multiplicarlo por constante para darle más/menos peso

        return penalizacion;
    }

    //endregion

    //region MetodosConsulta

    public ArrayList<ArrayList<Integer>> GetTrayectos(){
        return this.trayectos;
    }

    public Usuarios GetUsuarios(){
        return this.users;
    }

    public Usuario GetUsuario(int index){
        return this.users.get(index);
    }

    public ArrayList<Integer> GetTrayecto(int index){
        return this.trayectos.get(index);
    }

    public int TrayectosSize(){ return this.trayectos.size();  }
    public int DistanciaTrayecto(int coche){
        return this.distancias.get(coche);
    }

    public double TiempoDemora(int coche){
        double distancia = this.distancias.get(coche);
        return distancia/ this.Velocidad;
    }

    public int DistanciasSize(){ return this.distancias.size(); }

    public int PasajerosSimultaneosSize(){ return this.pasajerosSimulatenos.size();}

    public ArrayList<Integer> PasajerosSimultaneos(){ return this.pasajerosSimulatenos;}

    class Distance implements Comparator<Usuario> {
        @Override
        public int compare(Usuario user1, Usuario user2) {
            int dist1 = (Math.abs((user1.getCoordOrigenX() + user1.getCoordDestinoX()) - 0) + Math.abs((user1.getCoordOrigenY() + user1.getCoordDestinoY()) - 0));
            int dist2 = (Math.abs((user2.getCoordOrigenX() + user2.getCoordDestinoX()) - 0) + Math.abs((user2.getCoordOrigenY() + user2.getCoordDestinoY()) - 0));

            return dist1 < dist2 ? -1 : dist1 == dist2 ? 0 : 1;
        }
    }
    //endregion

    public boolean TodosUsuarios(int nUs){
        ArrayList<Integer> comprobacion = new ArrayList<Integer>();
        for (int i=0; i<nUs; i++){
            comprobacion.add(0);
        }

        for (int i=0; i<trayectos.size(); i++) {
            for (int j=0;j<trayectos.get(i).size(); j++) {
                int idx = trayectos.get(i).get(j);
                if (idx >= comprobacion.size()) System.out.println("Error.  idx " + idx + ".  size comprobacion " + comprobacion.size());
                comprobacion.set(idx, comprobacion.get(idx) + 1);
            }
        }

        for (int i=0; i<comprobacion.size(); i++){
            if (comprobacion.get(i) != 2) return false;
        }
        return true;
    }
}