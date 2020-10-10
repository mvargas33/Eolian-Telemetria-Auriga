package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.Utilities.BitOperations;

/*
 *  Piezas de bits que se intercambian entre las Xbees para hacer funcionar el protocolo.
 *  Los mensajes tienen un identificador y un largo definido.
 *  Adem&aacute;s le pertenecen a uno o m&aacute;s componentes, ellos saben que bits le corresponen del mensaje.
*/
public abstract class Message {
    char header;
    int largoEnBytes;
    byte[] bytes;

    // Para ver que todos los componentes actualizaron el mensaje, después se enva
    int allComponentsUpdated; // Entero que cambia cada vez
    int numOfComponents; // Para generar valor inicial
    int initialValue; // Valor reset de allComponentsUpdated, al estilo 11111111.....11111100000, con 0's numOfComponents

    public Message(char header, int largoEnBytes){
        this.header = header;
        this.largoEnBytes = largoEnBytes;
        this.bytes = new byte[largoEnBytes];
        this.bytes[0] = (byte) header;          // Primer byte es el header
        //this.bytes[largoEnBytes - 1] = 0;       // Último byte es el CRC-8

        this.allComponentsUpdated = 0; // Flag de salida del mensaje, si quedan componentes que no han actualizado mensaje, este no sale
        this.numOfComponents = 0; // Para designar bits en allComponentsUpdated, este es el bit asignado también
        this.initialValue = 0;
    }
/*
    public Message(char header, int largoEnBytes, LinkedList<State> components){
        this.Message(header, largoEnBytes);
        this.myStates = components;
    }
*/
    public abstract void addState(State state);

    /*--------------------------------------------------- RESOURCES ---------------------------------------------------*/

    /**
     * Retorna el header del Message
     * @return : Header del message
     */
    public char getHeader() {
        return header;
    }

    /**
     * Retorna los bytes[] del mensaje
     * @return : bytes[] del mensaje
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Retorna el largo en bytes del mensaje, se usa para calcuo de CRC
     * @return el largo en bytes del mensaje
     */
    public int getLargoEnBytes(){return largoEnBytes;}
    /**
     * Imprime el contenido del mensaje
     * @return : Print del Message
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.header);sb.append(" | ");
        sb.append(BitOperations.ArraytoString(this.bytes));
        return sb.toString();
    }
}
