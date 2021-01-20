package Test.DelayControl;

public class DelayLimitTest {

    @org.junit.jupiter.api.Test
    public void xbeeSenderQueueDelayControlTest(){
        // Estima el delay de envío de datos para que la cola de la XbeeSender no cresca indefinidamente
        // Para eso usa el principio control de ventana deslizante: TCP slow start, Concegstion avoidance, Reno's Fast Recovery
        // https://www.cs.utexas.edu/users/lam/396m/slides/Sliding_window+congestion_control.pdf

    }


}
