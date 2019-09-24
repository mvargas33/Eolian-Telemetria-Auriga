package Utilities;

public class BitOperations {

    /**
     * Coloca en 'destino' los valores correspondientes a la lectura hecha en 'rawBytes' desde el bit 'rawBytes_inicio'
     * hasta el bit 'rawBytes_fin' correspondientes con los largos en 'bitSig', calzando 'rawBytes_inicio' con 'bitSig_inicio'
     * extrayendo los bits significativos de 'bitSig', leyendo el valor correspondiente y colocandolo en el índice
     * correspondiente en 'destino'. Infiere el índice en 'destino' con el índice inferido de 'bitSig' a partir de 'bitSig_inicio'
     * NOTA: Los bytes están indexados de 0 a 7 y NO de 1 a 8.
     * @param destino : Array donde se actualizarán los valores
     * @param rawBytes : Nuevos Bytes a extraer
     * @param bitSig : Contiene la información de bits significativos de los valores a actualizar
     * @param rawBytes_inicio : Indice de inicio de extracción de bits, de 'rawBytes'
     * @param rawBytes_fin : Indice de fin de extracción de bits, de 'rawBytes'
     * @param bitSig_inicio : Indice de inicio de bits, de 'bitSig'
     */
    public static void updateValuesFromByteArray(int[] destino, byte[] rawBytes, int[] bitSig, int rawBytes_inicio, int rawBytes_fin, int bitSig_inicio){
        // Inferir índice del primer valor a actualizar y del largo a extraer, dejandolo en 'indiceValor'
        int indiceValor = 0;
        int aux = bitSig_inicio;
        while(aux != 1){// Si antes hay 10 bits, y quiero empezar del 11, habrán 10 bits antes de llegar, quedando en 1 al final
            aux -= bitSig[indiceValor];
            indiceValor++;
        }

        // Infiero el índice final de extracción
        int indiceFinal = indiceValor; // Empezamos desde donde inicia para iterar menos
        aux = rawBytes_fin - rawBytes_inicio; // Bits a avanzar desde 'bitSig_inicio' en indice 'indiceValor'
        while(aux != 1){
            aux -= bitSig[indiceFinal];
            if(aux == -1){break;} // Ej: Necesitaba avanzar dos, estando en uno, y resto 3, me da -1, entonces estoy en índice correcto
            indiceFinal++;
        }

        // Calcular el índice del byte inicial de extracción
        aux = (int) Math.floor(rawBytes_inicio / 8.0); // Si quiero bit 7, entonces es byte 0, bit 8 es byte 1





    }

    public static void main(String... args){
        // Calzar 4 y 5 en bitSig (bit 13 al 21), con 7 y 2 en rawBytes (bit 1 al 9)
        // indiceValor = 3 (el 4)
        int[] a = {7, 2, 3, 4, 5, 6};

        int indiceValor = 0;
        int aux = 10;
        while(aux != 1){// Si antes hay 10 bits, y quiero empezar del 11, habrán 10 bits antes de llegar, quedando en 1 al final
            aux -= a[indiceValor];
            indiceValor++;
        }

        int indiceFinal = 3; // Empezamos desde donde inicia para iterar menos
        aux = 11 - 9; // Bits a avanzar
        while(aux != -1){
            aux -= a[indiceFinal];
            if(aux == -1){break;}
            indiceFinal++;
        }


    }


}
