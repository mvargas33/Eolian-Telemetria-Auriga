package Protocol.Initializer;

import Components.Component;
import Protocol.Messages.Message;

import java.util.ArrayList;

/**
 * Clase que se encarga sólo de genrar los Menssages para cada Componente según los parámetros de la red
 */
public class Initializer {
    private final ArrayList<Component> allComponents; // Components que son parte del sistema actual de telemetría
    private final int msgLimitSize; // Parámetro de red: Límite del tamaño del mensaje
    private final char baseHeader; // Parámetro de red: Header de inicio de mensajes

    public Initializer(ArrayList<Component> allComponents, int msgLimitSize, int baseHeader){
        this.allComponents = allComponents;
        this.msgLimitSize = msgLimitSize;
        this.baseHeader = (char) (baseHeader & 0x00FF);
    }

    /**
     * Genera los mensajes correspondientes para cada Componente, en realidad los MessagesWithIndexes para cada Componente
     * @throws Exception : Por mala definición de Componentes o parámetros de la red
     */
    public void genMessages() throws Exception{
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

        ArrayList<Message> messages = new ArrayList<>();            // For debugging purposes

        int iCompActual = 0;                                        // Indice de componentes
        Component compAtual = allComponents.get(iCompActual);       // Componente actual
        int iValorAtual = 0;                                        // Indice en array de bitSig[]
        int[] arrayBitSigActual = compAtual.getBitSig();                 // Bits significativos actuales
        int tamanoBitSig = arrayBitSigActual.length;                     // Cantidad de valores del Componente, para no recalcular

        int bitSigInicio = 0;                                      // Desde que bit en bitSig me corresponde mensaje
        int raw_inicio = 0;                                    // Desde que bit en byte[] del Message me corresponde
        int raw_fin = 0;                                       // Hasta que bit en byte[] del Message me corresponde

        Message mensajeActual = new Message(header, msgLimitSize);  // Mensaje Actual
        int tamanoMsgActual = 0;                                    // Cuenta de bits que ya llevamos en mensaje

        mensajeActual.addComponent(compAtual);                      // Añadimos primer componente

        while(iCompActual < allComponents.size()){ // Mientras tenga componentes por revisar

            // Si me queda espacio en el mensaje actual Y Tengo un valor más en el Componente
            while(tamanoMsgActual < msgLimitSize & iValorAtual < tamanoBitSig){
                // Si puedo poner un valor más
                if(tamanoMsgActual + arrayBitSigActual[iValorAtual] <= msgLimitSize){
                    tamanoMsgActual += arrayBitSigActual[iValorAtual]; // Aumento tamaño del mensaje actual
                    bitSigInicio += arrayBitSigActual[iValorAtual];  // Sumo los bits que llevo
                }else{
                    break; // Cambio de Mensaje o Componente
                }
            }

            // CAMBIO DE COMPONENTE
            // No tengo más valores que poner del componente actual
            if(iValorAtual >= tamanoBitSig){

                raw_fin = tamanoMsgActual - 1; // Bit de final en Mensaje actual para componente en indice iCompActual

                // Le digo al Componente que tiene un nuevo mensaje, y los bits que le corresponden
                compAtual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio);

                iCompActual++; // Siguiente componente

                if(iCompActual < allComponents.size()){ // Si me queda otro componente por revisar

                    compAtual = allComponents.get(iCompActual); // Paso al siguiente componente
                    iValorAtual = 0; // Parto del indice 0
                    arrayBitSigActual = compAtual.getBitSig(); // Extraigo array de bits significativos
                    tamanoBitSig = arrayBitSigActual.length; // Para no recalcular
                    bitSigInicio = 0; // Desde primer índice del nuevo componente

                    // Si puedo calzar al menos un valor más en el mensaje
                    if(tamanoMsgActual + arrayBitSigActual[iValorAtual] <= msgLimitSize){
                        raw_inicio = tamanoMsgActual; // Desde bit siguiente al componente anterior
                        mensajeActual.addComponent(compAtual); // Agrego nuevo componente al mensaje
                    }
                    // Acá voy a while de inicio y voy poniendo mas bits en el mensaje actual
                }
                // Sino, no tenía más componentes y se acaba iteración

            }
            // CAMBIO DE MENSAJE
            // No puedo poner nada más en el mensaje actual (teniendo un valor en mano)
            else if (tamanoMsgActual + arrayBitSigActual[iValorAtual] > msgLimitSize){
                raw_fin = tamanoMsgActual - 1;
                compAtual.addNewMessage(mensajeActual, raw_inicio, raw_fin, bitSigInicio);

            }


        }
    }


    public static ArrayList<Message> genMessagesV2(int limit, ArrayList<Component> c, String compression) throws Exception {

        while (iCompActual < c.size()) { // Si estoy en un componente real

            // Si tentativamente puedo poner algo & Estoy en un valor real
            while (tamanoMsgActual < limit && iValorActual < bitsSignificativos.length) {
                // Si efectivamente puedo poner un valor más
                if (tamanoMsgActual + bitsSignificativos[iValorActual] <= limit) {
                    tamanoMsgActual += bitsSignificativos[iValorActual];
                    iValorActual++;
                } else {
                    break;
                }

            }

            // CAMBIO DE COMPONENTE
            // Si se me acabaron los valores en componente actual
            if (iValorActual >= bitsSignificativos.length) {

                if (tamanoMsgActual == limit) { // Y además tengo que cambiar de mensaje porque calzé justo
                    mensajeActual.setLastComponentValueIndex(iValorActual - 1); // Marco el fin en el índice actual
                    mensajes.add(mensajeActual); // Añadir a lista general antes de pasar ak siguiente componente
                    //System.out.print(mensajeActual.toString());

                }

                iCompActual++; // Paso al siguiente componente
                iValorActual = 0; // Parto desde el primer valor del nuevo componente

                if (iCompActual < c.size()) { // Si realmente me quedan componentes
                    compActual = c.get(iCompActual);
                    bitsSignificativos = compActual.getSizes(compression);    // Extraigo largos de bits en componente actual

                    if (tamanoMsgActual + bitsSignificativos[iValorActual] <= limit) { // Y puedo calzar al menos un valor del nuevo componente
                        mensajeActual.addComponent(compActual); // Lo añado al mensaje actual
                    }
                }

            }

            // CAMBIO DE MENSAJE
            // Si no puedo poner nada más en el mensaje actual (teniendo un valor en mano)
            else if (tamanoMsgActual + bitsSignificativos[iValorActual] > limit) {
                if (iValorActual != 0) { // Si no estoy en un nuevo componente => Sólo no puedo poner el valor que tengo en la mano ahora
                    mensajeActual.setLastComponentValueIndex(iValorActual - 1); // Marco el fin hasta antes del valor que tengo en mano
                    //mensajes.add(mensajeActual); // Añadir a lista general

                    // Sino es que no puedo calzar el primer valor de este nuevo componente, marco el fin con el ultimo componente en el mensaje porque ya avancé
                } else {
                    mensajeActual.setLastComponentValueIndex(mensajeActual.getComponents().get(mensajeActual.getComponents().size() - 1).totalValues() - 1);
                }


                mensajes.add(mensajeActual);
                System.out.print(mensajeActual.toString());

                // Crear nuevo mensaje
                tamanoMsgActual = 0;
                baseHeader++;
                h = baseHeader & 0xFF;
                mensajeActual = new Message((char) h, compression, iValorActual); // Pongo el índice del valor que tengo en mano que no pude poner antes
                mensajeActual.addComponent(compActual); // Pongo el mismo componente de antes, porque al menos puedo poner un valor


            }


        }
        // En este punto no me quedan más componentes pero me queda un último mensaje al aire

        if (iCompActual >= c.size()) { // Si se me acabaron los componentes,
            if (tamanoMsgActual > 0) { // Y mi mensaje actual tenía valores, quizás no hasta el final (Esto por el caso de que el [ultimo mensaje hecho calce justo con el largo solicitado)
                mensajeActual.setLastComponentValueIndex(mensajeActual.getComponents().get(mensajeActual.getComponents().size() - 1).totalValues() - 1);
                mensajes.add(mensajeActual); // Añadir a lista general
                System.out.print(mensajeActual.toString());
            }
        }
        if (checkMessagesSize(mensajes, limit, compression)) {
            return mensajes;
        }
        throw new Exception();
    }
}
