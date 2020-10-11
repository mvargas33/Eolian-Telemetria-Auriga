package ApplicationLayer.AppComponents;

import ApplicationLayer.LocalServices.Service;

import java.util.LinkedList;

/**
 * Será una clase usada como interfaz entre un Componente del Protocolo y los datos básicos que definen una componente.
 * Se podrán cargar datos desde un archivo de configuración a esta componente.
 * Puede decirse que esta Componente está en la capa de aplicación.
 * Cada AppComponent tiene un State del protocolo asociado.
 * Se les asocia un SensorReader específico que, después de escalar, hará update directo del State del Protocolo.
 */
public class AppComponent{
    public String ID;                       // ID del Componente. Ej: "BMS"
    public double[] minimosConDecimal;      // Hardcodeados del input del usuario
    public double[] maximosConDecimal;      // Hardcodeados del input del usuario
    public String[] nombreParametros;       // Nombre de parámetros de cada valorRealActual, por ejemplo "Velocidad" se relaciona con ínidice 0 de valoresRealesActuales
    public double[] valoresRealesActuales;  // Valores reales provenientes de lecturas reales. Se actualizan cada vez
    public int len;                         // Deducido. Se calcula una vez. Número de valores en componente. Se usa en varios for()
    LinkedList<Service> mySubscriptions;    // Servicios a los que les comunico mis updates

    /**
     * Inicia los objetos con constructores default
     */
    private AppComponent(){
        this.mySubscriptions = new LinkedList<>();
    }

    /**
     * AppComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas (eventos de socket.io por ejemplo).
     * Debe tener asociado un State de la capa inferior.
     * @param id Nombre del AppComponent
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     * @param nombreParametros Nombre de los parámetros del componente actual
     */
    public AppComponent(String id, double[] minimosConDecimal, double[] maximosConDecimal, String[] nombreParametros) {
        this();
        this.ID = id;
        this.minimosConDecimal = minimosConDecimal;
        this.maximosConDecimal = maximosConDecimal;
        this.nombreParametros = nombreParametros;
        this.len = minimosConDecimal.length;
        this.valoresRealesActuales = new double[len];
    }

    /**
     * DIRECT UPDATE
     * Reemplaza valores double en capa de Aplicación.
     * Lo ejecutan los Sensors Readers. También llamadas en AppReceiver al hacer update con nuevos valores
     * @param newValoresReales Valores nuevos reales provienentes de lecturas/arrivo de mensajes
     * @throws Exception Si es el array de nuevos valores proporcionados no tiene el mismo largo que pre-configuraciones
     */
    public synchronized void updateValues(double[] newValoresReales) throws Exception{
        if(newValoresReales.length != len){
            throw new Exception("updateFromReading: Array de valores nuevo no tiene el mismo largo que preconfiguraciones");
        }
        this.valoresRealesActuales = newValoresReales; // TODO: Analizar eficiencia: si reemplaza puntero, o valor a valor, o si se crean objetos nuevos
    }

    /**
     * AppComponent se suscribe al servicio indicado
     * @param service Servicio a suscribirse
     */
    public void subscribeToService(Service service){
        this.mySubscriptions.add(service);
    }

    /**
     * Manda a todos las suscripciones, un "heads-up" que este componente tiene nuevos valores
     */
    public synchronized void informToServices(){
        for (Service s: mySubscriptions
             ) {
            s.putComponentInQueue(this); // Me pongo en cola del servicio
        }
    }

    /**
     * Hace un for secuencial para cada servicio, haciendo que usen los datos del AppComponente en forma secuencial.
     */
    public void sequentialInformToServices(){
        for (Service s: mySubscriptions
        ) {
            s.sequentialRun(this); // Me pongo en cola del servicio
        }
    }

    /**
     * Retorna el ID del AppComponent (= ID del State de capa inferior)
     * @return ID tipo String del AppComponent
     */
    public String getID() {
        return this.ID;
    }


    public double[] getValoresRealesActuales() {
        return this.valoresRealesActuales;
    }

    public double[] getMinimosConDecimal() {
        return minimosConDecimal;
    }

    public double[] getMaximosConDecimal() {
        return maximosConDecimal;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.ID);
        sb.append("\n");
        for(int i = 0; i < len; i++){
            sb.append(this.nombreParametros[i]);
            sb.append(" : [");sb.append(this.minimosConDecimal[i]);sb.append(" | ");sb.append(this.maximosConDecimal[i]);sb.append("] ");
            sb.append(this.valoresRealesActuales[i]);
            sb.append("\n");
        }
        return sb.toString();
    }
}
