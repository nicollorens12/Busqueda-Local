import java.util.ArrayList;
import java.util.Comparator;

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

public class Estado {

    //cambios estados
    public static String INTERCAMBIO = "Intercambio";
    public static String ELIMINAR = "Eliminar";

    private static Usuarios users;
    private ArrayList<ArrayList<Usuario>> orden;
    
    public Estado(int nUs, int nCond, int seed ){
        seed = 0;
        users = new Usuarios(nUs,nCond,seed);
        int maxOcup = nUs/nCond;
        this.orden = BFS_Inicial(users, maxOcup); 
    }

    public Estado(ArrayList<ArrayList<Usuario>> newOrden){
        this.orden = newOrden;
    }

    private ArrayList<ArrayList<Usuario>> BFS_Inicial (Usuarios users, int maxOcup){
        /*//Usamos numero maximo conductores y recoje a los usuarios mas cercanos con maximo de ocupacion de un coche de N/M
        En este caso daremos un error si el maxOcup es mas grande que 3
        */
        if(maxOcup > 3){
            new Exception("No hay suficientes coches");
        }
        users.sort(new Distance());
        ArrayList<ArrayList<Usuario>> userRetur = new ArrayList<ArrayList<Usuario>>(); 
        for(Usuario u:users.stream().filter(u -> u.isConductor()).toArray(Usuario[]::new))
        {
            ArrayList<Usuario> earlyUsers = new ArrayList<Usuario>();
            earlyUsers.add(u);
            userRetur.add(earlyUsers);
        }
        for(Usuario u:users.stream().filter(u -> !u.isConductor()).toArray(Usuario[]::new)){
            for(ArrayList<Usuario> usersAdded: userRetur){
                if(userRetur.size() < maxOcup){
                    usersAdded.add(u);
                }
            }
        }
        return userRetur;        
    }

    public ArrayList<ArrayList<Usuario>> getOrder(){
        return this.orden;
    }
}

class Distance implements Comparator<Usuario> {
    @Override
    public int compare(Usuario user1, Usuario user2) {
        int dist1 = (Math.abs(user1.getCoordOrigenX() - 0) + Math.abs(user1.getCoordOrigenY() - 0));
        int dist2 = (Math.abs(user2.getCoordOrigenX() - 0) + Math.abs(user2.getCoordOrigenY() - 0));

        return dist1 < dist2 ? -1 : dist1 == dist2 ? 0 : 1;
    }
}