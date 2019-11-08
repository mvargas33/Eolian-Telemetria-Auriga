public class ProtocolSystem {/*
    String type;                        // Significative bytes of components's data
    ArrayList<Component> components;    // All components
    ArrayList<Message> messages;        // Array of Messages => Array of Headers & Components
    int messageSize;                    // Máx amount of bits in a Message

    public ProtocolSystem(String type, ArrayList<Component> components, int messageSize) {
        this.type = type;
        this.components = components;
        this.messageSize = messageSize;
    }

    public static ArrayList<Message> genMessagesV2(int limit, ArrayList<Component> c, String compression) throws Exception {
        if (c.size() == 0) {                                          // Si no hay componentes arrojar excepción
            throw new Exception();
        }
        if (c.get(0).getArrayOfValues().length == 0) {                // Si el primer componente no tiene valores, arrojar excepción
            throw new Exception();
        }
        if (c.get(0).getSizes(compression)[0] <= 0) {
            throw new Exception();
        }
        if (limit < (c.get(0).getSizes(compression)[0])) { // La lógica va a fallar si estoy en componente 0, valor 0 y no puedo poner un valor en el primer mensaje
            throw new Exception();
        }

        ArrayList<Message> mensajes = new ArrayList<>();            // Crear lsita vacía de mensajes

        int baseHeader = 65;                                        //  Comenzar con la 'A'
        int h = baseHeader & 0xFF;

        int iCompActual = 0;
        Component compActual = c.get(iCompActual);
        int iValorActual = 0;

        Message mensajeActual = new Message((char) h, compression, iValorActual);
        int tamanoMsgActual = 0;

        mensajeActual.addComponent(compActual);

        int[] bitsSignificativos = compActual.getSizes(compression);

        while (iCompActual < c.size()) { // Si estoy en un componente real

            // Si tentativamente puedo poner algo & Estoy en un valor real
            while (tamanoMsgActual < limit && iValorActual < bitsSignificativos.length) {
                // Si efectivamente puedo poner un valor más
                if (tamanoMsgActual + bitsSignificativos[iValorActual] <= limit) {
                    tamanoMsgActual += bitsSignificativos[iValorActual];
                    iValorActual++;
                } else {
                    break;
                }

            }

            // CAMBIO DE COMPONENTE
            // Si se me acabaron los valores en componente actual
            if (iValorActual >= bitsSignificativos.length) {

                if (tamanoMsgActual == limit) { // Y además tengo que cambiar de mensaje porque calzé justo
                    mensajeActual.setLastComponentValueIndex(iValorActual - 1); // Marco el fin en el índice actual
                    mensajes.add(mensajeActual); // Añadir a lista general antes de pasar ak siguiente componente
                    //System.out.print(mensajeActual.toString());

                }

                iCompActual++; // Paso al siguiente componente
                iValorActual = 0; // Parto desde el primer valor del nuevo componente

                if (iCompActual < c.size()) { // Si realmente me quedan componentes
                    compActual = c.get(iCompActual);
                    bitsSignificativos = compActual.getSizes(compression);    // Extraigo largos de bits en componente actual

                    if (tamanoMsgActual + bitsSignificativos[iValorActual] <= limit) { // Y puedo calzar al menos un valor del nuevo componente
                        mensajeActual.addComponent(compActual); // Lo añado al mensaje actual
                    }
                }

            }

            // CAMBIO DE MENSAJE
            // Si no puedo poner nada más en el mensaje actual (teniendo un valor en mano)
            else if (tamanoMsgActual + bitsSignificativos[iValorActual] > limit) {
                if (iValorActual != 0) { // Si no estoy en un nuevo componente => Sólo no puedo poner el valor que tengo en la mano ahora
                    mensajeActual.setLastComponentValueIndex(iValorActual - 1); // Marco el fin hasta antes del valor que tengo en mano
                    //mensajes.add(mensajeActual); // Añadir a lista general

                    // Sino es que no puedo calzar el primer valor de este nuevo componente, marco el fin con el ultimo componente en el mensaje porque ya avancé
                } else {
                    mensajeActual.setLastComponentValueIndex(mensajeActual.getComponents().get(mensajeActual.getComponents().size() - 1).totalValues() - 1);
                }


                mensajes.add(mensajeActual);
                System.out.print(mensajeActual.toString());

                // Crear nuevo mensaje
                tamanoMsgActual = 0;
                baseHeader++;
                h = baseHeader & 0xFF;
                mensajeActual = new Message((char) h, compression, iValorActual); // Pongo el índice del valor que tengo en mano que no pude poner antes
                mensajeActual.addComponent(compActual); // Pongo el mismo componente de antes, porque al menos puedo poner un valor


            }


        }
        // En este punto no me quedan más componentes pero me queda un último mensaje al aire

        if (iCompActual >= c.size()) { // Si se me acabaron los componentes,
            if (tamanoMsgActual > 0) { // Y mi mensaje actual tenía valores, quizás no hasta el final (Esto por el caso de que el [ultimo mensaje hecho calce justo con el largo solicitado)
                mensajeActual.setLastComponentValueIndex(mensajeActual.getComponents().get(mensajeActual.getComponents().size() - 1).totalValues() - 1);
                mensajes.add(mensajeActual); // Añadir a lista general
                System.out.print(mensajeActual.toString());
            }
        }
        if (checkMessagesSize(mensajes, limit, compression)) {
            return mensajes;
        }
        throw new Exception();
    }

    public static boolean checkMessagesSize(ArrayList<Message> mensajes, int limit, String compression) throws Exception {
        int[] largos = new int[mensajes.size()];

        for (int nMsg = 0; nMsg < mensajes.size(); nMsg++) {
            Message m = mensajes.get(nMsg);
            int largoActual = 0;
            int iCompActual = 0;
            int iValorActual = m.getFirstComponentValueIndex();

            while (iCompActual < m.getComponents().size()) {
                Component cActual = m.getComponents().get(iCompActual);
                int[] bitsSignificativos = cActual.getSizes(compression);


                if (iCompActual == m.getComponents().size() - 1) { // Si soy el último componente
                    for (int i = iValorActual; i < m.getLastComponentValueIndex(); i++) {
                        largoActual += bitsSignificativos[i];
                    }
                } else { // Si no soy el último voy hasta el final de los valores del componente actual
                    for (int i = iValorActual; i < bitsSignificativos.length; i++) {
                        largoActual += bitsSignificativos[i];
                    }
                }

                largos[nMsg] = largoActual;
                largoActual = 0;
            }
        }

        System.out.print(largos.toString());

        for (int largo : largos
        ) {
            if (largo > limit) {
                return false;
            }
        }
        return true;
    }*/
}