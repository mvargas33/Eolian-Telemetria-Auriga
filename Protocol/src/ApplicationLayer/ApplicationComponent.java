package ApplicationLayer;

import ApplicationLayer.LocalSystems.LocalMasterAdmin;
import PresentationLayer.Packages.Components.Component;
import ZigBeeLayer.Sending.SenderAdmin;
import Utilities.DoubleOperations;

/**
 * Será una clase usada como interfaz entre un Componente del Protocolo y los datos básicos que definen una componente.
 * Se podrán cargar datos desde un archivo de configuración a esta componente.
 * Puede decirse que esta Componente está en la capa de aplicación.
 * Cada SimpleComponent tiene un Component del protocolo asociado.
 * Se les asocia un SensorReader específico que, después de escalar, hará update directo del Component del Protocolo.
 */
public class ApplicationComponent {
    private final String ID;
    private final double[] minimosConDecimal; // Hardcodeados
    private final double[] maximosConDecimal; // Hardcodeados

    private int len; // Deducido. Se calcula una vez. Número de valores en componente. Se usa en varios for()
    public int[] decimales; // Deducido. Se calcula una vez. Cantidad de decimales de los valores
    public int[] offset; // Deducido. Se calcula una vez. Offset para llegar del mínimo al 0
    public int[] delta; // Deducido. Se calcula una vez. Cantidad de valores a representar
    public int[] bitSignificativos; // Deducido. Se calculauna vez. Cantidad mínima de bits para representar 'delta' valores

    public double[] valoresRealesActuales; // Valores reales provenientes de lecturas reales. Se actualizan cada vez

    public int[] valoresAEnviar; // Valores en formato de capap de presentación.
    //private double[] valoresRecibidos; // Valores decodeados de capa de presentación. Se usa en variable global para optimizar uso de memoria

    private Component myPresentationComponent;

    private LocalMasterAdmin myLocalMasterAdmin; // Display de datos y base de datos deben estar en capa de aplicación



    /**
     * SimpleComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas.
     * Incluyendo el nombre de eventos en Socket.IO
     * @param id Nombre del SimpleComponente
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     */
    public ApplicationComponent(String id, double[] minimosConDecimal, double[] maximosConDecimal) {
        this.ID = id;
        this.minimosConDecimal = minimosConDecimal;
        this.maximosConDecimal = maximosConDecimal;

        this.len = minimosConDecimal.length;
        this.decimales = new int[len];
        this.offset = new int[len];
        this.delta = new int[len];
        this.bitSignificativos = new int[len];

        this.valoresRealesActuales = new double[len];

        this.valoresAEnviar = new int[len];

        for (int i = 0; i < len; i++){
            int min = DoubleOperations.extractDecimals(minimosConDecimal[i]);
            int max = DoubleOperations.extractDecimals(maximosConDecimal[i]);
            this.decimales[i] = Math.max(min, max);
            this.offset[i] = (int) Math.floor(- (minimosConDecimal[i] * Math.pow(10, decimales[i])));
            this.delta[i] = (int) Math.floor(1 + (maximosConDecimal[i] - minimosConDecimal[i]) * Math.pow(10, decimales[i]));
            this.bitSignificativos[i] = (int) Math.ceil(Math.log(delta[i]) / Math.log(2));
        }
    }

    /**
     SECOND
     * Componente híbrido: Envía datos y hace display (dentro de auto solar)
     * Se ejecuta desde fuera porque necesita localmasterAdmin y senderAdmin.
     * Asigna un componente de capa de Presentación a este componente.
     * Ejecutar después de calcular array de bitsSignificativos.
     * @param localMasterAdmin thread de localmasteradmin externo
     * @param senderAdmin thread de senderAdmin externo
     */
    public void asignarComponenteHibrido(LocalMasterAdmin localMasterAdmin, SenderAdmin senderAdmin){
        this.myPresentationComponent = new Component(localMasterAdmin, senderAdmin, this.valoresAEnviar, this.bitSignificativos, this.ID);
    }

    /**
     * Receiver Component (Outside solar car)
     * Recibe mensajes nuevos, updatea sus valores, y luego usa al localMasterAdmin para hacer display
     * @param localMasterAdmin thread de localMasterAdmin externo
     */
    public void asignarComponenteRecibidor(LocalMasterAdmin localMasterAdmin){
        this.myPresentationComponent = new Component(localMasterAdmin, this.valoresAEnviar, this.bitSignificativos, this.ID);
    }

    /**
     * DIRECT READING
     * Método que ejecuta el SensorReader. Reemplaza valores en capa de Aplicación.
     * @param newValoresReales Valores nuevos reales provienentes de lecturas
     * @throws Exception Si es el array de nuevos valores proporcionados no tiene el mismo largo que preconfiguraciones
     */
    public synchronized void updateFromReading(double[] newValoresReales) throws Exception{
        if(newValoresReales.length != len){
            throw new Exception("updateFromReading: Array de valores nuevo no tiene el mismo largo que preconfiguraciones");
        }
        this.valoresRealesActuales = newValoresReales;
    }


    /**
     * RECEIVING DATA
     * Método que ejecuta un Componente de capa de Presentación, para avisarle al Componente de Aplicación que llegaron nuevos valores.
     * Traduce los valores de la capa inferior a valores reales en double.
     * @param valoresDeCapaPresentacion Array de int[] proveniente de capa de Presentación
     * @throws Exception Si el largo del array int[] no coincide con el largo de precongifuraciones
     */
    public void updateFromReceiving(int[] valoresDeCapaPresentacion) {

        for (int i = 0; i < len; i++) {
            this.valoresRealesActuales[i] = (valoresDeCapaPresentacion[i] - offset[i]) * Math.pow(10, -decimales[i]);
        }
    }

    /**
     * SENDING DATA
     * Calcula valores para capa de Presentación. "Compresión".
     * Se usa variable global para optimizar uso de memoria
     */
    public void updatePresentationValues(){
        for (int i = 0; i < len; i++) {
            this.valoresAEnviar[i] = (int) Math.floor( valoresRealesActuales[i] * Math.pow(10, decimales[i]) ) + offset[i];
        }
    }

    /**
     * SENDING DATA
     * Pasa los valores actuales de capa de presentación a capa de presentación.
     */
    public void passValues(){
        this.myPresentationComponent.replaceMyValues(this.valoresAEnviar);
    }

    public void receiveValues(int[] valoresDeCapaPresentacion) throws Exception{
        if(valoresDeCapaPresentacion.length != len){
            throw new Exception("updateFromReceiving: Array de valores nuevo no tiene el mismo largo que preconfiguraciones");
        }
        /*for (int i = 0; i < len; i++) {
            this.valores
        }*/
    }

    /**
     * DISPLAYING DATA
     * Desencadena métodos que mandan eventos a Aplicación Web por medio de socket.io
     *//*
    public void displayDataInWebApp(){
        this.myLocalMasterAdmin
        return;
    }*/

}
