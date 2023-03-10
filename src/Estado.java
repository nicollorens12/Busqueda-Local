import java.util.ArrayList;

public class Estado {

    public static Usuarios users;
    public ArrayList<ArrayList<Usuario>> Orden;
    
    public Estado(int nUs, int nCond, int seed ){
        seed = 0;
        users = new Usuarios(nUs,nCond,seed);

    }

    private BFS_Inicial (){
        //Usamos numero maximo conductores y recoje a los usuarios mas  con maximo de ocupacion de un coche de N/M
        
    }




}
