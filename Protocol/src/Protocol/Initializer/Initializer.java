package Protocol.Initializer;

import Components.Component;
import Protocol.Messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Clase que se encarga sólo de genrar los Menssages para cada Componente según los parámetros de la red
 */
public class Initializer {
    private final LinkedList<Component> allComponents; // Components que son parte del sistema actual de telemetría
    private final int msgLimitSize;         // Parámetro de red: Límite del tamaño del mensaje en bits
    private final int msgLimitSizeInBytes;  // Parámetro de red: Límite del tamaño del mensaje en bytes
    private final char baseHeader; // Parámetro de red: Header de inicio de mensajes

    public Initializer(LinkedList<Component> allComponents, int msgLimitSize, int baseHeader){
        this.allComponents = allComponents;
        this.msgLimitSize = msgLimitSize;
        this.msgLimitSizeInBytes = (int) Math.ceil(msgLimitSize / 8.0);
        this.baseHeader = (char) (baseHeader & 0x00FF);
    }

    /**
     * Genera los mensajes correspondientes para cada Componente, en realidad los MessagesWithIndexes para cada Componente
     * @throws Exception : Por mala definición de Componentes o parámetros de la red
     */
    public HashMap<Character, Message> genMessages() throws Exception{
        if (allComponents.size() == 0){
            throw new Exception("No hay componentes en la lista de todos los Componentes");
        }
        if(allComponents.get(0).getMyValues().length == 0){
            throw new Exception("El primer componente no tiene valores");
        }
        if(allComponents.get(0).getBitSig().length == 0){
            throw new Exception("El primer componente no tiene array de Bits significativos");
        }
        if(allComponents.get(0).getBitSig()[0] <= 0){
            throw new Exception("El número de bits significativos es menor a cero, para primer valor de primer componente");
        }
        if(msgLimitSize < allComponents.get(0).getBitSig()[0]){
            throw new Exception("No se puede poner valor en Mensaje, bits significativos mayor a tamaño del mensaje");
        }

        char header = baseHeader;                                   // Comenzamos desde baseHeader

        HashMap<Character, Message> messages = new HashMap<>();
        //ArrayList<Message> messages = new ArrayList<>();          // For debugging purposes

        int iCompActual = 0;                                        // Indice de componentes
        Component compActual = allComponents.get(iCompActual);      // Componente actual
        int iValorAtual = 0;                                        // Indice en array de bitSig[]
        int[] arrayBitSigActual = compActual.getBitSig();           // Bits significativos actuales
        int tamanoBitSig = arrayBitSigActual.length;                // Cantidad de valores del Componente, para no recalcular

        int bitSigInicio = 0;                                       // Desde que bit en bitSig me corresponde mensaje
        int raw_inicio = 8;                                         // Desde que bit en byte[] del Message me corresponde, 8 bits iniciales para header
        int raw_fin = 0;                                            // Hasta que bit en byte[] del Message me corresponde

        Message mensajeActual = new Message(header, msgLimitSizeInBytes);  // Mensaje Actual
        int tamanoMsgActual = 8;                                    // Cuenta de bits que ya llevamos en mensaje, 8 bits iniciales para header

        mensajeActual.addComponent(compActual);                      // Añadimos primer componente

        while(iCompActual < allComponents.size()){ // Mientras tenga componentes por revisar

            // Si me queda espacio en el mensaje actual Y Tengo un valor más en el Componente
            while(tamanoMsgActual < msgLimitSize - 8 & iValorAtual < tamanoBitSig){ // 8 Ultimos bits para CRC
                // Si puedo poner un valor más
                if(tamanoMsgActual + arrayBitSigActual[iValorAtual] <= msgLimitSize - 8){
                    tamanoMsgActual += arrayBitSigActual[iValorAtual]; // Aumento tamaño del mensaje actual
                    iValorAtual++; // Avanzo al siguiente
                }else{
                    break; // Cambio de Mensaje o Componente
                }
            }

            // CAMBIO DE COMPONENTE
            // No tengo más valores que poner del componente actual
            if(iValorAtual >= tamanoBitSig){

                raw_fin = tamanoMsgActual - 1; // Bit de final en Mensaje actual para componente en indice iCompActual

                // Le digo al Componente que tiene un nuevo mensaje, y los bits que le corresponden
                compActual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio);

                iCompActual++; // Siguiente componente

                if(iCompActual < allComponents.size()){ // Si me queda otro componente por revisar

                    compActual = allComponents.get(iCompActual); // Paso al siguiente componente
                    iValorAtual = 0; // Parto del indice 0
                    arrayBitSigActual = compActual.getBitSig(); // Extraigo array de bits significativos
                    tamanoBitSig = arrayBitSigActual.length; // Para no recalcular
                    bitSigInicio = 0; // Desde primer índice del nuevo componente

                    // Si puedo calzar al menos un valor más en el mensaje
                    if(tamanoMsgActual + arrayBitSigActual[iValorAtual] <= msgLimitSize - 8){
                        raw_inicio = tamanoMsgActual; // Desde bit siguiente al componente anterior
                        mensajeActual.addComponent(compActual); // Agrego nuevo componente al mensaje
                    }
                    // Acá voy a while de inicio y voy poniendo mas bits en el mensaje actual
                }
                // Sino, no tenía más componentes y se acaba iteración

            }
            // CAMBIO DE MENSAJE
            // No puedo poner nada más en el mensaje actual (teniendo un valor en mano i.e. no aumento iValorActual)
            else if (tamanoMsgActual + arrayBitSigActual[iValorAtual] > msgLimitSize - 8){
                raw_fin = tamanoMsgActual - 1; // Fin de bits en este mensaje
                compActual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio);
                bitSigInicio += (raw_fin - raw_inicio) + 1; // Nuevo inicio en bitSig para siguiente mensaje: Los que puse + 1

                messages.put(header, mensajeActual); // Para el HashMap del receiver
                //messages.add(mensajeActual); // Debug
                //System.out.println(mensajeActual.toString()); // Debug

                // Crear nuevo mensaje
                tamanoMsgActual = 8; // 8 bits header
                raw_inicio = 8; // 8 bits header
                header++;
                mensajeActual = new Message(header, msgLimitSizeInBytes);
                mensajeActual.addComponent(compActual); // Añado componente actual, [porque al menos tengo que poner otro valor, el que tengo en mano]
            }
        }
        // Ya no me quedan más componentes por revisar, pero tengo mensaje en la mano

        if(tamanoMsgActual > 0){ // Si mi mensaje tenía valores, no estaba vacío (necesario por si cambie de mensaje y componente en última iteración)
            //raw_fin = tamanoMsgActual - 1;
            //compActual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio);

            messages.put(header, mensajeActual); // Para el HashMap del receiver
            //messages.add(mensajeActual); // Debug
            //System.out.println(mensajeActual.toString()); // Debug
        }
        return messages; // HashMap para receiver
    }
}
