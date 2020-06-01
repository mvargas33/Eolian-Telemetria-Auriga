package ApplicationLevel;

import PresentationLevel.Components.Component;

/**
 * Será una clase usada como interfaz entre un Componente del Protocolo y los datos básicos que definen una componente.
 * Se podrán cargar datos desde un archivo de configuración a esta componente.
 * Puede decirse que esta Componente está en la capa de aplicación.
 * Cada SimpleComponent tiene un Component del protocolo asociado.
 * Se les asocia un SensorReader específico que, después de escalar, hará update directo del Component del Protocolo.
 */
public class ApplicationComponent {
    private final String ID;
    private final double[] minimosConDecimal;
    private final double[] maximosConDecimal;

    private Component myProtocolComponent;

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
    }



}
