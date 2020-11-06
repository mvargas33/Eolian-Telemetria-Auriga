package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;

/**
 * Se encarga de guardar la información de los componentes a la base de datos.
 * Debe haber una tabla para cada AppComponent en la base de datos.
 * Hace insert de los datos actuales con un timestamp.
 */
public class DatabaseService extends Service implements Runnable{

    public DatabaseService(){
        super();
    }

    /**
     * Debe tomar el AppComponent y guardar sus valores en su tabla respectiva en la base de datos
     * Debe guardar el timestamp también
     * @param c AppComponent a guardar en la base de datos
     */
    @Override
    protected void serve(AppComponent c){
        try{
            // INSERT double[] en su tabla, sacar el timestamp del momento en que guarda

        }catch (Exception e){
            e.printStackTrace(); // Sólo se hace print, el sistema no se puede caer
        }
    }


}
