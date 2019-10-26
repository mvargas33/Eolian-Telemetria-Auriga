package Protocol.Messages;

import Components.Component;
import Utilities.BitOperations;

import java.util.LinkedList;

/*
 *  Piezas de bits que se intercambian entre las Xbees para hacer funcionar el protocolo.
 *  Los mensajes tienen un identificador y un largo definido.
 *  Adem&aacute;s le pertenecen a uno o m&aacute;s componentes, ellos saben que bits le corresponen del mensaje.
*/
public class Message {
    private char header;
    private int largoEnBytes;
    private byte[] bytes;
    private LinkedList<Component> myComponents;

    public Message(char header, int largoEnBytes){
        this.header = header;
        this.largoEnBytes = largoEnBytes;
        this.bytes = new byte[largoEnBytes];
        this.myComponents = new LinkedList<>();
    }
/*
    public Message(char header, int largoEnBytes, LinkedList<Component> components){
        this.Message(header, largoEnBytes);
        this.myComponents = components;
    }
*/
    /*-------------------------------------------------- INITIALIZING -------------------------------------------------*/

    public void addComponent(Component c){
        this.myComponents.add(c);
    }


    /*--------------------------------------------------- RECEIVING ---------------------------------------------------*/

    /**
     * Actualiza los bytes del Mensaje por los actualizados. Luego llama a uno a uno todos los componentes que le corresponden
     * para que miren el mensaje y estos extraigan los bits que necesiten.
     * @param newBytes : Valores actualizados de los bytes del mensaje
     */
    public void updateRawBytes(byte[] newBytes){
        this.bytes = newBytes; // Update myself
        // Notify my components to check at my values
        for (Component c: this.myComponents
             ) {
            c.updateMyValues(this.header); // Mírame! Me actualicé
        }
    }

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
