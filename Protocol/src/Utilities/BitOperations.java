package Utilities;

public class BitOperations {
    // Raw tiene n bytes de la forma:
    //                          _______________________________________________________
    //  bytes[] raw             | xxxx xxxx | xxxx xxxx | ... | xxxx xxxx | xxxx xxxx |
    //  Índice de bytes         |‾0123‾4567‾|‾0123‾4567‾|‾...‾|‾0123‾4567‾|‾0123‾4567‾|
    //  Índice en arreglo       |‾‾‾‾‾0‾‾‾‾‾|‾‾‾‾‾1‾‾‾‾‾|‾...‾|‾‾‾n - 2‾‾‾|‾‾‾n - 1‾‾‾|
    //                          ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾

    public static String ArraytoString(byte[] byteArray){
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(Integer.toBinaryString(b & 0x00FF));
            sb.append(" ");
        }
        sb.append('\n');
        return sb.toString();
    }

    /**
     * Realiza cálculo de CRC y coloca el resultado en últimos dos bytes del arreglo
     * @param raw : byte[] al cual recalcular CRC
     */
    public static void appendCRC(byte[] raw){

    }

    /**
     * Coloca en 'rawBytes' los 'bitSig[i]' bits sinificativos del valor 'source[i]', para cada i comenzando en el bit
     * 'bitSig_inicio' de 'bitSig', desde el bit 'rawBytes_inicio' de 'rawBytes'.
     * @param source : valores reales a poner en 'rawBytes' según los bits significativos de 'bitsSig'
     * @param rawBytes : array de bytes destino
     * @param bitSig : contiene la información de bits significativos
     * @param bitSig_inicio : bit de inicio de valores reales
     * @param rawBytes_inicio : bit de inicio en array destino
     * @param rawBytes_fin : bit de fin en array destino
     */
    public static void updateByteArrayFromValues(int[] source, byte[] rawBytes, int[] bitSig, int bitSig_inicio, int rawBytes_inicio, int rawBytes_fin) {
        // Índice en arreglo bitSig y dest.
        int indiceBitSigYSource = 0;
        int count = 0;
        while(count != bitSig_inicio){
            count += bitSig[indiceBitSigYSource];
            indiceBitSigYSource++;
        }

        int valorAponer;
        int bitsSignificativos;

        while(rawBytes_inicio != rawBytes_fin + 1) {// Hasta que no haya puesto todos los bits | es +1, ya que por ej, si tengo desde 0 hasta 1, pongo 2, entonces 0 + 2 = 2 > 1
            valorAponer = source[indiceBitSigYSource];
            //System.out.println(Integer.toBinaryString(valorAponer));
            bitsSignificativos = bitSig[indiceBitSigYSource];
            //System.out.println(ArraytoString(rawBytes));
            ponerValorEnArray(rawBytes, rawBytes_inicio, valorAponer, bitsSignificativos);
            //System.out.println(ArraytoString(rawBytes));
            rawBytes_inicio += bitsSignificativos; // Acabo de poner 'bitsSignificativos' bits ahora
            indiceBitSigYSource++;
        }

    }

    /**
     * Pone en 'rawBbytes' los 'bitsSignificativos' del valor 'valorAponer' desde el bit 'rawBytes_inicio'
     * @param rawBytes : array donde poner valor
     * @param rawBytes_inicio : bit de array desde donde poner valor
     * @param valorAponer : valor a poner
     * @param bitsAPoner : bits significativos del valor a poner
     */
    public static void ponerValorEnArray(byte[] rawBytes, int rawBytes_inicio, int valorAponer, int bitsAPoner) {
        int index = rawBytes_inicio / 8;
        int subIndex = rawBytes_inicio % 8;

        short mask;
        while(bitsAPoner != 0){
            if(bitsAPoner > (8 - subIndex)){ // Pongo hasta el final del byte actual
                if(subIndex != 0){
                    mask = genMask(subIndex, (8 - subIndex)); // desde subIndex, voy a poner (8 - subIndex) bits
                    rawBytes[index] |= (byte) ((valorAponer >> (bitsAPoner - (8 - subIndex))) & mask); // Dejo los( 8 - subindex) bits a la derecha y los pongo
                    bitsAPoner -= (8 - subIndex); // Puse (8 - subIndex) bits
                    index++; // Avanzo en arreglo de bytes para siguientes bits, ya que este esta lleno ahora
                    subIndex = 0; // Ahora parto desde el 0 del byte siguiente
                }else{
                    mask = 0x00FF; // Ahora llamada a genMask
                    rawBytes[index] |= (byte) ((valorAponer >> (bitsAPoner - 8)) & mask); // Dejo los 8 bits que necesito
                    bitsAPoner -= 8;
                    index++; // Paso a siguiente byte, ya que puse 8
                }
            }else{
                mask = genMask(subIndex, bitsAPoner);
                rawBytes[index] |= (byte) ((valorAponer << ((8 - bitsAPoner) - subIndex)) & mask); // (8 - bitsAPoner) me da el indice en source, subIndex el indice en raw. Los calzo
                bitsAPoner = 0;
            }
        }
    }


    /**
     * Coloca en 'destino' los valores correspondientes a la lectura hecha en 'rawBytes' desde el bit 'rawBytes_inicio'
     * hasta el bit 'rawBytes_fin' correspondientes con los largos en 'bitSig', extrayendo los bits significativos de 'bitSig',
     * desde el bit 'bitSig_inicio', leyendo el valor correspondiente y colocandolo en el índice correspondiente en 'destino'.
     * Infiere el índice en 'destino' con el índice inferido de 'bitSig' a partir de 'bitSig_inicio'
     * NOTA: Los bytes están indexados de 0 a 7 y NO de 1 a 8.
     * @param destino : Array donde se actualizarán los valores
     * @param rawBytes : Nuevos Bytes a extraer
     * @param bitSig : Contiene la información de bits significativos de los valores a actualizar
     * @param bitSig_inicio : Indice de inicio de bits, de 'bitSig'
     * @param rawBytes_inicio : Indice de inicio de extracción de bits, de 'rawBytes'
     * @param rawBytes_fin : Indice de fin de extracción de bits, de 'rawBytes'
     */
    public static void updateValuesFromByteArray(int[] destino, byte[] rawBytes, int[] bitSig, int bitSig_inicio, int rawBytes_inicio, int rawBytes_fin){
        // Índice en arreglo bitSig y dest.
        int indiceBitSigYDest = 0;
        int count = 0;
        while(count != bitSig_inicio){
            count += bitSig[indiceBitSigYDest];
            indiceBitSigYDest++;
        }

        int bitsASacar; // Bits a sacar en cada iteración
        int valor; // Valor a extraer en cada iteración

        while(rawBytes_inicio != rawBytes_fin + 1){ // Hasta que no haya sacado todos los bits | es +1, ya que por ej, si tengo desde 0 hasta 1, extraigo 2, entonces 0 + 2 = 2 > 1
            bitsASacar = bitSig[indiceBitSigYDest]; // Veo cuantos bits tengo que sacar ahora
            valor = extraerBits(rawBytes, rawBytes_inicio, bitsASacar); // Extraigo desde rawBytesInicio, la cantidad de bitSig
            destino[indiceBitSigYDest] = valor; // Actualizo valor interpretado
            rawBytes_inicio += bitsASacar; // Sumo la cantidad que saqué
            indiceBitSigYDest++; // Avanzo en bitSig & dest
        }
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

    /**
     * Retorna una máscara para rescatar 'cantidadDeBits' desde 'posicionEnByte' en un byte.
     * Se retorna como short para tener la extensión a 0 a la izquierda y no de 1, ya que
     * 0b1XXX XXXX extiende con 1's a la izquierda en Java. Esto evita código sucio y casteos en otros métodos.
     * @param posicionEnByte : Indice del bit izquierdo: Valores de 0-7
     * @param cantidadDeBits: Cantidad de bits a rescatar: Valores de 0-7 dependiendo de posicionEnByte
     * @return máscara para rescatar bits
     */
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
