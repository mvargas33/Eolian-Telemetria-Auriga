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
     * @param bitSig_inicio : Indice de inicio de bits, de 'bitSig'
     * @param rawBytes_inicio : Indice de inicio de extracción de bits, de 'rawBytes'
     * @param rawBytes_fin : Indice de fin de extracción de bits, de 'rawBytes'
     */
    public static void updateValuesFromByteArray(int[] destino, byte[] rawBytes, int[] bitSig, int bitSig_inicio, int rawBytes_inicio, int rawBytes_fin){
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

    public String byteArrayToString(byte[] array){
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(Integer.toBinaryString(b & 0xFF));
            sb.append(" ");
        }
        sb.append('\n');
        return sb.toString();
    }

    /**
     * Extrae de un byte[] el valor númerico de los 'cantidad' bits significativos del array, comenzando en el bit 'desde'.
     * Ej: Si el array es 0001 1000 | 1111 1101, desde = 4, cantidad = 8, debe extraer 1000 | 1111, y devolverlo como entero
     * @param raw : Arreglo de bits de donde extraer la información
     * @param desde : bit desde donde se extrae la información
     * @param cantidad : Cantidad de bits significativos del entero en el array. Máx valor: 32, por datatype int de Java
     * @return El valor interpretado de la extracción.
     */
    public static int extraerBits(byte[] raw, int desde, int cantidad){
        int index = desde / 8 ; // Índice en raw desde done comenzar a extraer
        int subIndex = desde % 8;  // Sub-índice de inicio en byte, ej : 2. Valor entre 0-7
        int valor = 0; // Valor final
        short mask; // Máscara para extracciones
        while( cantidad != 0 ){ // Mientras queden bits por extraer
            if(cantidad > (8 - subIndex)){ // Si tengo que extraer hasta el final del byte actual
                if(subIndex != 0){ // Si empiezo a extraer desde un punto al medio del byte hasta el final
                    mask = genMask(subIndex, 8 - subIndex); // Desde donde toque hasta el final (que es mayor a 8)
                    valor = (valor | (raw[index] & mask)) << 8; // << 8 para dejar espacio para siguiente iteración
                    cantidad -= 8 - subIndex; // Consumí lo que pude
                    index++; // Avanzo en el arreglo de bytes
                    subIndex = 0; // Para ingresar en else si cantidad es grande. Si cantidad < 8 en un principio, subIndex nunca cambia de desde % 8
                }else{ // Extraigo 8 bits, porque parto de 0 y
                    mask = 0x00FF; // Para evitar llamada a genMask
                    valor = (valor | (raw[index] & mask)) << 8;
                    cantidad -= 8; // Ya consumí 8 bits
                    index++; // Avanzo
                }
            }else{ // No tengo que extraer hasta el final, sólo de subIndex a cantidad. Puede ser la extracción final luego de varias extracciones pervias, o una extraccion directa
                mask = genMask(subIndex, cantidad); // Desde el sub-índice de inicio, hasta lo que tenga que sacar
                valor = (valor | ((raw[index] & mask) << subIndex)) >> (8 - cantidad); // Extraigo, << para quedar pegado a resultado general, >> para quedar pegado a la derecha
                cantidad = 0; // Termino while()
            }
        }
        //System.out.println(Integer.toBinaryString(valor));
        return valor;
    }
    // Raw tiene n bytes de la forma:
    //                          _______________________________________________________
    //  bytes[] raw             | xxxx xxxx | xxxx xxxx | ... | xxxx xxxx | xxxx xxxx |
    //  Índice de bytes         |‾0123‾4567‾|‾0123‾4567‾|‾...‾|‾0123‾4567‾|‾0123‾4567‾|
    //  Índice en arreglo       |‾‾‾‾‾0‾‾‾‾‾|‾‾‾‾‾1‾‾‾‾‾|‾...‾|‾‾‾n - 2‾‾‾|‾‾‾n - 1‾‾‾|
    //                          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
    // mask debe retornar short, porque los numeros 0b1XXX XXXX Java los extiende con 1's a la izquierda, no ceros
    public static short genMask(int posicionEnByte, int cantidadDeBits){
        // 0000  0001  0010  0011  0100  0101  0110  0111  1000  1001  1010  1011  1100  1101  1110  1111
        // 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
        short mask = 0x0000;
        switch(posicionEnByte){
            case 0: switch (cantidadDeBits){
                case 1: mask = 0x0080;break;
                case 2: mask = 0x00C0;break;
                case 3: mask = 0x00E0;break;
                case 4: mask = 0x00F0;break;
                case 5: mask = 0x00F8;break;
                case 6: mask = 0x00FC;break;
                case 7: mask = 0x00FE;break;
                case 8: mask = 0x00FF;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 1: switch (cantidadDeBits){
                case 1: mask = 0x0040;break;
                case 2: mask = 0x0060;break;
                case 3: mask = 0x0070;break;
                case 4: mask = 0x0078;break;
                case 5: mask = 0x007C;break;
                case 6: mask = 0x007E;break;
                case 7: mask = 0x007F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 2: switch (cantidadDeBits){
                case 1: mask = 0x0020;break;
                case 2: mask = 0x0030;break;
                case 3: mask = 0x0038;break;
                case 4: mask = 0x003C;break;
                case 5: mask = 0x003E;break;
                case 6: mask = 0x003F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 3: switch (cantidadDeBits){
                case 1: mask = 0x0010;break;
                case 2: mask = 0x0018;break;
                case 3: mask = 0x001C;break;
                case 4: mask = 0x001E;break;
                case 5: mask = 0x001F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 4: switch (cantidadDeBits){
                case 1: mask = 0x0008;break;
                case 2: mask = 0x000C;break;
                case 3: mask = 0x000E;break;
                case 4: mask = 0x000F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 5: switch (cantidadDeBits){
                case 1: mask = 0x0004;break;
                case 2: mask = 0x0006;break;
                case 3: mask = 0x0007;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 6: switch (cantidadDeBits){
                case 1: mask = 0x0002;break;
                case 2: mask = 0x0003;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            case 7: switch (cantidadDeBits){
                case 1: mask = 0x0001;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBits);
            }break;
            default: System.out.format("Posición en byte actual fuera de rango: %d \n", posicionEnByte);
        }
        //System.out.print("Máscara a utilizar: ");System.out.println(Integer.toBinaryString(mask&0xFF));
        return mask;
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
