package ApplicationLayer.LocalServices.WirelessService.Utilities;

/**
 * Clase que implementa funciones complementarias a usar por el proyecto.
 */
public class DoubleOperations {

    /**
     * Dado un valor en double, retorna la cantidad de decimales que tiene.
     * Su referencia es el último valor distinto a 0. Ej: 8.XXX9 retorna 4, pero 8.XXX0 retorna 3.
     * Se usa en AppComponent
     * @param value Valor en double a calcular decimales
     * @return Cantidad de decimales del double proveído.
     */
    public static int extractDecimals(double value){
        int d = 0;
        double one = 1;
        double tolerance = Math.pow(10, -10); // 1E-10
        double copy = value;

        while(true){
            double rest = copy % one;
            if(rest < tolerance){
                break;
            }else{
                d++;
                copy = value * Math.pow(10, d);
            }
        }
        return d;
    }
}
