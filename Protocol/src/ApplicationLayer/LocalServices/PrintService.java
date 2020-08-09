package ApplicationLayer.LocalServices;

import ApplicationLayer.AppComponents.AppComponent;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Simple print AppComponent's real values
 */
public class PrintService extends Service{
    @Override
    void serve(AppComponent c) {
        System.out.println(c.getID() + " : " + Arrays.toString(c.getRealValues()));
    }
}
