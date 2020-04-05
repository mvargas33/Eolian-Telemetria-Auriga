package LocalSystems;

import Components.Component;
import LocalSystems.DatabaseAdmin.DatabaseAdmin;
import LocalSystems.ServerAdmin.ServerAdmin;

import javax.xml.crypto.Data;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/*
    Administra a los encargados de base de datos y servidor con nueva información. Actúa como productor de información.
    Posee una Queue
 */
public class LocalMasterAdmin implements Runnable{
    private BlockingQueue<Component> componentsToBeChecked;
    private ServerAdmin serverAdmin;        // Para visualizar información en AppWeb
    private DatabaseAdmin databaseAdmin;    // Para almacenar información en Base de datos
    private boolean serverON;

    /**
     * Constructor para DEBUG
     * @param serverON : true si el servidor API para front end está encendido
     */
    public LocalMasterAdmin(boolean serverON){
        this.componentsToBeChecked = new LinkedBlockingDeque<>();
        this.serverON = serverON;
    }

    /**
     * Construcor de administrador de datos locales. Necesita un serveradmin para mandar información a servidor que tiene
     * montada la WebApp. Necesita DataBaseAdmin para guardar información en base de datos.
     * @param serverAdmin : Quien se encarga de enviar información a AppWeb
     * @param databaseAdmin : Quien se encarga de guardar información en Base de Datos
     * @param serverON : true si el servidor API para front end está encendido
     */
    public LocalMasterAdmin(ServerAdmin serverAdmin, DatabaseAdmin databaseAdmin, boolean serverON){
        this.componentsToBeChecked = new LinkedBlockingDeque<>();
        this.serverAdmin = serverAdmin;
        this.databaseAdmin = databaseAdmin;
        this.serverON = serverON;
    }

    /**
     * Añade un componente en la queue de Componentes por ser revisados
     * @param c : Componente a poner en la Queue
     */
    public void putComponentInQueue(Component c){
        if(!this.componentsToBeChecked.contains(c)){ // Sólo si no estoy, me agrego, esto toma a lo más O(largo lista) = O(número de Componentes) = O(5) ? = cte.
            this.componentsToBeChecked.add(c);
        }
    }


    /**
     * Consume y procesa un Componente de la Queue. Deben visualizar sus valores y guardarse en la base de datos
     * @throws Exception si falla en alguna parte
     */
    public void consumeComponent() throws Exception{
        while(!componentsToBeChecked.isEmpty()){
            //System.out.println("COMPONENTS TO BE CHECKED: " + this.componentsToBeChecked.size());
            Component c = this.componentsToBeChecked.poll();
            if(serverON) {
                serverAdmin.sendToServer(c.getID(), c.getMyValues()); // Mandar info al servidor
            }
            // TODO: Procesar valores del componente
            System.out.println(c.toString());
        }
    }

    /**
     * Debe sacar los componentes pendientes de su lista, si esta vacía no entra
     */
    @Override
    public void run() {
        while(true){
            try {
                consumeComponent();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
