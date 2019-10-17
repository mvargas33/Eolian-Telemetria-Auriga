package Protocol.Messages;

import Components.Component;

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
    private LinkedList<Component> components;

    public void Message(char header, int largoEnBytes, byte[] myBytes){
        this.header = header;
        this.largoEnBytes = largoEnBytes;
        this.bytes = myBytes;
        this.components = new LinkedList<>();
    }

    public void Message(char header, int largoEnBytes, byte[] myBytes, LinkedList<Component> components){
        this.Message(header, largoEnBytes, myBytes);
        this.components = components;
    }

    public void addComponent(Component c){
        this.components.add(c);
    }

    public void replaceBytes(byte[] newBytes){
        this.bytes = newBytes; // Update myself
        // Notify my components to check at my values
    }

    public char getHeader() {
        return header;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
