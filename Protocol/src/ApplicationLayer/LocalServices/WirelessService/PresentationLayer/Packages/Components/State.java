package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;
import ApplicationLayer.LocalServices.WirelessService.Utilities.DoubleOperations;

/*
    Los Componentes virtualizan los componentes del auto. Tienen un arreglo de valores int y un arreglo de bits significativos.
    Además guardan qué mensajes contienen bits (información) relevante para el mismo, para extraerla cuando se actualizan.
    Receiving:  Siempre está atento a heads-ups de Mensajes para actualizarse, luego avisa al LocalMasterAdmin.
    Sending:    Recive información directa del DataAdmin (lecturals locales). Luego avisa a sus mensajes que se actualicen.
                Luego ellos se ponen en contacto con QueueAdmin para ser enviados.
 */
public abstract class State {

    /* Estructura necesaria para guardar correlación mensage-state, guarda que intervalos de bits de un mensaje le conciernen a qué intervalos de bits en este componente */
    public class MessagesWithIndexes {
        Message message;        // RAW bits
        int raw_inicio;         // De donde este componente inicia sus bits en mensaje
        int raw_fin;            // Donde este componente termina sus bits en mensaje
        int myBitSig_inicio;    // Desde que bit en mi array tengo que poner en mensaje
        int componentNumber;    // Para indicar el bit que se asigna en mensaje para marcar el 'ready'

        MessagesWithIndexes(Message m, int raw_inicio, int raw_fin, int myBitSig_inicio, int componentNumber) {
            this.message = m;
            this.raw_inicio = raw_inicio;
            this.raw_fin = raw_fin;
            this.myBitSig_inicio = myBitSig_inicio;
            this.componentNumber = componentNumber;
        }
    }

    private String ID;                      // State ID, can be the name
    public int[] myValues;                  // True values
    public int len;                         // Deducido. Se calcula una vez. Número de valores en componente. Se usa en varios for()
    public int[] decimales;                 // Deducido. Se calcula una vez. Cantidad de decimales de los valores
    public int[] offset;                    // Deducido. Se calcula una vez. Offset para llegar del mínimo al 0
    public int[] delta;                     // Deducido. Se calcula una vez. Cantidad de valores a representar
    public int[] bitSignificativos;         // Deducido. Se calculauna vez.  Cantidad mínima de bits para representar 'delta' valores


    /**
     * Base State, encargado de lecturas directas de sensores y envío de datos por SenderAdmin
     * @param ID : ID del Componente
     */
    public State(String ID, double[] minimosConDecimal, double[] maximosConDecimal){
        this.ID = ID;
        this.len = minimosConDecimal.length;
        this.myValues = new int[len];
        this.decimales = new int[len];
        this.offset = new int[len];
        this.delta = new int[len];
        this.bitSignificativos = new int[len];

        // Calculation of bitSignificativos array
        for (int i = 0; i < len; i++){
            int min = DoubleOperations.extractDecimals(minimosConDecimal[i]);
            int max = DoubleOperations.extractDecimals(maximosConDecimal[i]);
            this.decimales[i] = Math.max(min, max);
            this.offset[i] = (int) Math.floor(- (minimosConDecimal[i] * Math.pow(10, decimales[i])));
            this.delta[i] = (int) Math.floor(1 + (maximosConDecimal[i] - minimosConDecimal[i]) * Math.pow(10, decimales[i]));
            this.bitSignificativos[i] = (int) Math.ceil(Math.log(delta[i]) / Math.log(2));
        }
    }


    /*-------------------------------------------------- INITIALIZING -------------------------------------------------*/
    /**
     * Añade un Mensaje e informacion extra a este Componente. Para que luego sepa como actualizarse, sabiendo que
     * bit le corresponden. Se añade como MessageWithHeader en un Map para tener coste O(1).
     * @param m Message
     * @param raw_inicio Bit de inicio en Mensaje
     * @param raw_fin Bit de fin en Mensaje
     * @param bitSigInicio Bit de inicio en componente
     * @param componentNumber : Numero del componente
     */
    public abstract void addNewMessage(Message m, int raw_inicio, int raw_fin, int bitSigInicio, int componentNumber);


    /*--------------------------------------------------- RESOURCES ---------------------------------------------------*/

    /**
     * Retorna el ID del componente
     * @return : ID del componente
     */
    public String getID(){
        return this.ID;
    }

    /**
     * Retorna int[] de values
     * @return : Array de valores reales
     */
    public int[] getMyValues(){return this.myValues;}

    /**
     * Retorna int[] de bitSignificativos
     * @return : Retorna array de bits significativos
     */
    public int[] getBitSig(){return this.bitSignificativos;}

    /**
     * Retorna el array de valores como String
     * @return : Array de valores como String
     */
    public String valuesToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int valor: this.myValues
             ) {
            sb.append(valor);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Retorna el array de bits significativos como String
     * @return : Array de bits significativos como String
     */
    public String bitSigToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int valor: this.bitSignificativos
        ) {
            sb.append(valor);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Retorna visualización de Componente como String
     * @return : Componente como String
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("State ID       : ");sb.append(this.ID);sb.append("\n");
        sb.append("Valores            : ");sb.append(valuesToString());sb.append("\n");
        sb.append("Bits significativos: ");sb.append(bitSigToString());sb.append("\n");
        return sb.toString();
    }


}
