package ApplicationLayer;

public class AppCompSender extends  AppComponent implements Runnable{
    /**
     * SimpleComponent sólo se caracteriza por sus valores mínimos, máximos, y su ID que se usará para muchas cosas.
     * Incluyendo el nombre de eventos en Socket.IO
     *
     * @param id                Nombre del SimpleComponente
     * @param minimosConDecimal Valores mínimos de cada valor del componente
     * @param maximosConDecimal Valores máximos de cada valor del componente
     */
    public AppCompSender(String id, double[] minimosConDecimal, double[] maximosConDecimal) {
        super(id, minimosConDecimal, maximosConDecimal);
    }

    /**
     * Método principal de cada App Component.
     * 1 : Lee de su buffer en busca de nuevos valores entregados por su SensorReader
     * 2 : Actualiza los valores localmente
     * 3 : Se coloca en cola del objeto WebSocketService y DatabaseService
     * 4 : Hace las llamadas necesarias a Component-Message para hacer update de los mensajes
     * 5 : Si los mensajes están listos (todos los comp. que participan de él han actualizado una parte) se ponen los
     *     mensajes en cola de Xbee Sender
     */
    @Override
    public void run() {
        while(true) {
            // 1 : Leer buffer en busca de nuevos valores entregados

        }
    }
}
