package ApplicationLayer.LocalServices.WirelessService;

import ApplicationLayer.AppComponents.AppComponent;
import ApplicationLayer.AppComponents.AppSender;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption.CryptoAdmin;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.State;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Components.StateSender;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Packages.Initializer.SenderInitializer;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Receiving.XbeeReceiver;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.SenderAdmin;
import ApplicationLayer.LocalServices.WirelessService.ZigBeeLayer.Sending.XbeeSender;

import java.util.LinkedList;
import java.util.List;

public class WirelessSender extends WirelessService{
    StateSender actualStateSender; // To save memory
    XbeeSender xbeeSender;

    public WirelessSender(List<AppSender> components, String XBEE_PORT) throws Exception{
        this.XBEE_PORT = XBEE_PORT;

        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
        xbeeSender = new XbeeSender(XBEE_BAUD, XBEE_PORT, (int) MSG_RAW_SIZE_BYTES);
        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender, cryptoAdmin);

        LinkedList<State> state_list = new LinkedList<>(); // Only for initializer

        for (AppSender c: components ) {
            StateSender newState = new StateSender(c.getID(), c.getMinimosConDecimal(), c.getMaximosConDecimal(), senderAdmin);
            state_list.add(newState);
            states.put(c.getID(), newState); // Local global map
        }

        // Initializer of States/Messages
        SenderInitializer senderInitializer = new SenderInitializer(state_list,MSG_SIZE_BITS, FIRST_HEADER);
        map = senderInitializer.genMessages();

    }

    /**
     * Testing Constructor
     * @param components List of AppComponents
     * @param xbeeReceiver Offline testing xbeeReceiver object
     * @throws Exception
     */
    public WirelessSender(List<AppSender> components, XbeeReceiver xbeeReceiver) throws Exception{
        this.XBEE_PORT = XBEE_PORT;

        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
        xbeeSender = new XbeeSender(xbeeReceiver);
        SenderAdmin senderAdmin = new SenderAdmin(xbeeSender, cryptoAdmin);

        LinkedList<State> state_list = new LinkedList<>(); // Only for initializer

        for (AppSender c: components ) {
            StateSender newState = new StateSender(c.getID(), c.getMinimosConDecimal(), c.getMaximosConDecimal(), senderAdmin);
            state_list.add(newState);
            states.put(c.getID(), newState); // Local global map
        }

        // Initializer of States/Messages
        SenderInitializer senderInitializer = new SenderInitializer(state_list,MSG_SIZE_BITS, FIRST_HEADER);
        map = senderInitializer.genMessages();

    }

    /**
     * Necessary to initiallize its thread (run() method)
     * @return
     */
    public XbeeSender getXbeeSender() {
        return xbeeSender;
    }

    @Override
    protected void serve(AppComponent c) {
        actualStateSender = (StateSender) this.states.get(c.getID());
        actualStateSender.updatePresentationValuesAndEnQueue(c.getValoresRealesActuales());

    }

}
