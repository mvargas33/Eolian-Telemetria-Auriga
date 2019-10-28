package LocalSystems;

import Components.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/*
    Administra a los encargados de base de datos y servidor con nueva información. Actúa como productor de información.
    Posee una Queue
 */
public class LocalMasterAdmin {
    private BlockingQueue<Component> componentsToBeChecked;

    public LocalMasterAdmin(){
        this.componentsToBeChecked = new LinkedBlockingDeque<>();
    }

    /**
     * Añade un componente en la queue de Componentes por ser revisados
     * @param c : Componente a poner en la Queue
     */
    public void putComponentInQueue(Component c){
        this.componentsToBeChecked.add(c);
    }


    /**
     * Consume y procesa un Componente de la Queue. Deben visualizar sus valores y guardarse en la base de datos
     */
    public void consumeComponent(){
        Component c = this.componentsToBeChecked.poll();
        // TODO: Procesar valores del componente
        System.out.println(c.toString());
    }
}
