package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Messages.Message;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Clase que se encarga sólo de genrar los Menssages para cada Componente según los parámetros de la red
 */
public abstract class Initializer {
    final LinkedList<State> allStates;  // Lista de Sates actuales
    final int msgLimitSize;             // Parámetro de red: Límite del tamaño del mensaje en bits
    final int msgLimitSizeInBytes;      // Parámetro de red: Límite del tamaño del mensaje en bytes
    final char baseHeader;              // Parámetro de red: Header de inicio de mensajes

    public Initializer(LinkedList<State> allStates, int msgLimitSizeBits, int baseHeader){
        this.allStates = allStates;
        this.msgLimitSize = msgLimitSizeBits;
        this.msgLimitSizeInBytes = (int) Math.ceil(msgLimitSize / 8.0);
        this.baseHeader = (char) (baseHeader & 0x00FF);
    }

    /**
     * Genera los mensajes correspondientes para cada State, en realidad los MessagesWithIndexes para cada State
     * @throws Exception : Por mala definición de Sates o parámetros de la red
     * @return HashMap de caracter, Mensaje (SentMessage o ReceivedMessage)
     */
    public abstract HashMap<Character, Message> genMessages() throws Exception;
}
