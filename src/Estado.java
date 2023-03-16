import java.util.ArrayList;
import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
public class Estado {

    public static Usuarios users;
    public ArrayList<ArrayList<Usuario>> Orden;
    
    public Estado(int nUs, int nCond, int seed ){
        seed = 0;
        users = new Usuarios(nUs,nCond,seed);
        Orden = Inicial(nUs,nCond);
    }

    private ArrayList<ArrayList<Usuario>> Inicial (int nUs, int nCond){
        //Usamos numero maximo conductores y recoje a los usuarios mas  con maximo de ocupacion de un coche de N/M
        int car_max_capacity = nUs/nCond;
        char[][] Ciudad;
        ArrayList<ArrayList<Usuario>> Orden_Inicial;
        Ciudad = new char[100][100];
        for(int i = 0; i < 100; i++) {
            for(int j = 0; j < 100; j++) {
                Ciudad[i][j] = '.';
            }
        }

        for(int k = 0; k < nUs; ++k){
            Usuario u = users.get(k);
            if(u.isConductor()){
                Ciudad[u.getCoordDestinoX][u.getCoordDestinoY] = 'c';
                ArrayList<Usuario> v_aux = new ArrayList<>();
                v_aux.add(u);
                Orden_Inicial.add(v_aux);
            }
            else Ciudad[u.getCoordDestinoX][u.getCoordDestinoY] = 'u';
        }
        return BFS(Ciudad);
    }

    private ArrayList<ArrayList<Usuario>> BFS(char[][] Ciudad ){

    }




}
