package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;

import java.util.Arrays;

/**
 * Simple print AppComponent's real values
 */
public class PrintService extends Service{
    @Override
    protected void serve(AppComponent c) {
        System.out.println(c.getID() + " : " + Arrays.toString(c.getValoresRealesActuales()));
    }
}
