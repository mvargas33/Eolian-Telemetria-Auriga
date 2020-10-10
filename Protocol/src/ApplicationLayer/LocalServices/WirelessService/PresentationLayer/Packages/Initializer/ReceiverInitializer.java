package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.ReceivedMessage;

import java.util.HashMap;
import java.util.LinkedList;

public class ReceiverInitializer extends Initializer{

    /**
     *
     * @param allStates StateReceivers ONLY
     * @param msgLimitSizeBits Size in bits of messages
     * @param baseHeader Base header value
     */
    public ReceiverInitializer(LinkedList<State> allStates, int msgLimitSizeBits, int baseHeader) {
        super(allStates, msgLimitSizeBits, baseHeader);
    }

    /**
     *
     * @return HashMap<Character, ReceivedMessage>
     * @throws Exception
     */
    @Override
    public HashMap<Character, Message> genMessages() throws Exception{
        if (allStates.size() == 0){
            throw new Exception("No hay componentes en la lista de todos los Componentes");
        }
        if(allStates.get(0).getMyValues().length == 0){
            throw new Exception("El primer componente no tiene valores");
        }
        if(allStates.get(0).getBitSig().length == 0){
            throw new Exception("El primer componente no tiene array de Bits significativos");
        }
        if(allStates.get(0).getBitSig()[0] <= 0){
            throw new Exception("El número de bits significativos es menor a cero, para primer valor de primer componente");
        }
        if(msgLimitSize < allStates.get(0).getBitSig()[0]){
            throw new Exception("No se puede poner valor en Mensaje, bits significativos mayor a tamaño del mensaje");
        }

        char header = baseHeader;                                   // Comenzamos desde baseHeader

        HashMap<Character, Message> messages = new HashMap<>();
        //ArrayList<Message> messages = new ArrayList<>();          // For debugging purposes

        int iCompActual = 0;                                        // Indice de componentes
        State compActual = allStates.get(iCompActual);              // Componente actual
        int iValorAtual = 0;                                        // Indice en array de bitSig[]
        int[] arrayBitSigActual = compActual.getBitSig();           // Bits significativos actuales
        int tamanoBitSig = arrayBitSigActual.length;                // Cantidad de valores del Componente, para no recalcular

        int bitSigInicio = 0;                                       // Desde que bit en bitSig me corresponde mensaje
        int raw_inicio = 8;                                         // Desde que bit en byte[] del Message me corresponde, 8 bits iniciales para header
        int raw_fin = 0;                                            // Hasta que bit en byte[] del Message me corresponde

        int componentNumber = 0;                                    // Para indicar el bit que se asigna a este componente para marcar 'ready' al enviar mensajes

        ReceivedMessage mensajeActual = new ReceivedMessage(header, msgLimitSizeInBytes);  // Mensaje Actual
        int tamanoMsgActual = 8;                                    // Cuenta de bits que ya llevamos en mensaje, 8 bits iniciales para header

        mensajeActual.addState(compActual);                      // Añadimos primer componente

        while(iCompActual < allStates.size()){ // Mientras tenga componentes por revisar

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
                compActual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio, componentNumber);

                iCompActual++; // Siguiente componente
                componentNumber++; // Avanzo en comp number

                if(iCompActual < allStates.size()){ // Si me queda otro componente por revisar

                    compActual = allStates.get(iCompActual); // Paso al siguiente componente
                    iValorAtual = 0; // Parto del indice 0
                    arrayBitSigActual = compActual.getBitSig(); // Extraigo array de bits significativos
                    tamanoBitSig = arrayBitSigActual.length; // Para no recalcular
                    bitSigInicio = 0; // Desde primer índice del nuevo componente

                    // Si puedo calzar al menos un valor más en el mensaje
                    if(tamanoMsgActual + arrayBitSigActual[iValorAtual] <= msgLimitSize - 8){
                        raw_inicio = tamanoMsgActual; // Desde bit siguiente al componente anterior
                        mensajeActual.addState(compActual); // Agrego nuevo componente al mensaje
                    }
                    // Acá voy a while de inicio y voy poniendo mas bits en el mensaje actual
                }
                // Sino, no tenía más componentes y se acaba iteración

            }
            // CAMBIO DE MENSAJE
            // No puedo poner nada más en el mensaje actual (teniendo un valor en mano i.e. no aumento iValorActual)
            else if (tamanoMsgActual + arrayBitSigActual[iValorAtual] > msgLimitSize - 8){
                raw_fin = tamanoMsgActual - 1; // Fin de bits en este mensaje
                compActual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio, componentNumber);
                bitSigInicio += (raw_fin - raw_inicio) + 1; // Nuevo inicio en bitSig para siguiente mensaje: Los que puse + 1
                componentNumber = 0; // Vuelvo a contador en 1 para componente en este mensaje

                messages.put(header, mensajeActual); // Para el HashMap del receiver
                //messages.add(mensajeActual); // Debug
                //System.out.println(mensajeActual.toString()); // Debug

                // Crear nuevo mensaje
                tamanoMsgActual = 8; // 8 bits header
                raw_inicio = 8; // 8 bits header
                header++;
                mensajeActual = new ReceivedMessage(header, msgLimitSizeInBytes);
                mensajeActual.addState(compActual); // Añado componente actual, [porque al menos tengo que poner otro valor, el que tengo en mano]
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
