package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;
import PresentationLayer.Packages.Components.State;
import PresentationLayer.Packages.Components.StateSender;
import ZigBeeLayer.Sending.SenderAdmin;

import java.util.HashMap;
import java.util.List;

public class WirelessService extends Service{
    HashMap<String, State> states;

    public WirelessService(List<AppComponent> components, SenderAdmin senderAdmin){
        for (AppComponent c: components
             ) {
            states.put(c.getID(), new StateSender() {
            })
        }

    }

    /**
     * SenderAdmin se setea a posteriori. Para poder crear el AppSender por separado
     * @param mySenderAdmin : A quien se informa sobre nuevos valores y se pone ne cola
     */
    public void setSenderAdmin(SenderAdmin mySenderAdmin){
        this.myPresentationState.setMySenderAdmin(mySenderAdmin);
    }



    @Override
    void serve(AppComponent c) {

    }
}
