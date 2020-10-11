package ApplicationLayer.LocalServices.WirelessService;

import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.AppComponents.AppReceiver;
import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.LocalServices.Service;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption.CryptoAdmin;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.StateReceiver;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.StateSender;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer.ReceiverInitializer;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer.SenderInitializer;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.ReceiverAdmin;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.XbeeReceiver;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.SenderAdmin;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.XbeeSender;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WirelessReceiver extends WirelessService{
    ReceiverAdmin receiverAdmin;
    XbeeReceiver xbeeReceiver;

    public WirelessReceiver(List<AppReceiver> components, String XBEE_PORT) throws Exception {
        this.XBEE_PORT = XBEE_PORT;

        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
        xbeeReceiver = new XbeeReceiver(XBEE_BAUD, XBEE_PORT);

        LinkedList<State> state_list = new LinkedList<>(); // Only for initializer

        for (AppReceiver c: components ) {
            StateReceiver newState = new StateReceiver(c.getID(), c.getMinimosConDecimal(), c.getMaximosConDecimal(), c);
            state_list.add(newState);
            states.put(c.getID(), newState); // Local global map
        }

        // Initializer of States/Messages
        ReceiverInitializer receiverInitializer = new ReceiverInitializer(state_list, MSG_SIZE_BITS, FIRST_HEADER);
        map = receiverInitializer.genMessages();

        // Receiver Admin
        receiverAdmin = new ReceiverAdmin(xbeeReceiver, map, cryptoAdmin);

    }

    /**
     * Debe sacar los componentes pendientes de su lista, si esta vacía no entra
     */
    @Override
    public void run() {
        // Dispatch Xbee & xbeeReceiver threads
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(xbeeReceiver);
        executorService.submit(receiverAdmin);
    }


}
