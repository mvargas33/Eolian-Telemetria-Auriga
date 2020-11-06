package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Service repesenta los servicios locales que se corren en paralelo
 * Por ejemplo: Visualización de datos o Almacenamiento de datos.
 * La API son los AppComponents que se ponen en cola de los servicios.
 * Luego los servicios consumen AppComponents sacando datos de ellos.
 * Además cada servicio es un thread independiente, e implementa run().
 */
public abstract class Service implements Runnable{
    BlockingQueue<AppComponent> componentsToBeChecked; // Cola de AppComponents

    /**
     * Constructor de objetos default
     */
    public Service(){
        this.componentsToBeChecked = new LinkedBlockingDeque<>();
    }

    /**
     * Añade un AppComponent en la queue por ser revisados
     * Varios AppComponent pueden llamar a este método a la vez, por eso el synchronized
     * @param c : AppComponent a poner en la Queue
     */
    public synchronized void putComponentInQueue(AppComponent c){
        if(!this.componentsToBeChecked.contains(c)){ // Sólo si no estoy, me agrego, esto toma a lo más O(largo lista) = O(número de Componentes) = O(5) ? = cte.
            this.componentsToBeChecked.add(c);
        }
    }

    /**
     * Consume y procesa todos los AppComponent de la Queue.
     * Procesan el componente de acuerdo al servicio que brindan.
     */
    private void consumeComponent(){
        while(!componentsToBeChecked.isEmpty()){
            //System.out.println("COMPONENTS TO BE CHECKED: " + this.componentsToBeChecked.size());
            AppComponent c = this.componentsToBeChecked.poll();
            this.serve(c); // Método para hacer algo con el AppComponent
        }
    }

    /**
     * Todos los servicios deben hacer algo con cada AppComponent.
     * Y por lo tanto deben implementar el método serve
     * @param c AppComponent a consumir
     */
    protected abstract void serve(AppComponent c);

    /**
     * Debe sacar los componentes pendientes de su lista, si esta vacía no entra
     */
    @Override
    public void run() {
        while(true){
            try {
                consumeComponent(); // Siempre consume componentes y ejecuta el servicio
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Ejecución secuencial pasando AppComponent como argumento
     * @param c Componente a ser revisado por servicio
     */
    public void sequentialRun(AppComponent c){
        this.serve(c);
    }
}
