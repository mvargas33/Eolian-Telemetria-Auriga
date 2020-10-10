package ApplicationLayer.LocalServices.WirelessService.Utilities;

public class BitOperations {
    // Raw tiene n bytes de la forma:
    //                          _______________________________________________________
    //  bytes[] raw             | xxxx xxxx | xxxx xxxx | ... | xxxx xxxx | xxxx xxxx |
    //  Indice de bytes         |‾0123‾4567‾|‾0123‾4567‾|‾...‾|‾0123‾4567‾|‾0123‾4567‾|
    //  Indice en arreglo       |‾‾‾‾‾0‾‾‾‾‾|‾‾‾‾‾1‾‾‾‾‾|‾...‾|‾‾‾n - 2‾‾‾|‾‾‾n - 1‾‾‾|
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
        // Indice en arreglo bitSig y dest.
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
        int index = rawBytes_inicio / 8; // Posición en array byte[]
        int subIndex = rawBytes_inicio % 8; // Va de 0 a 7 y es subindice en Byte actual

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
        // Indice en arreglo bitSig y dest.
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
        int index = desde / 8 ; // Indice en raw desde done comenzar a extraer
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
     * Pone en 0 todos los bits en el rango señalado, esto para poder limpiar bits anteriores antes de poner los nuevos
     * @param raw : Arreglo a intervenir
     * @param desde : Bit de inicio
     * @param hasta : Bit de fin
     */
    public static void resetToZeroBitRange(byte[] raw, int desde, int hasta){
        int cantidad = hasta - desde + 1; // Si tengo desde 2 hasta 2, tengo que neutralizar ese bit
        int index = desde / 8 ; // Indice en raw desde done comenzar a extraer
        int subIndex = desde % 8;  // Sub-índice de inicio en byte, ej : 2. Valor entre 0-7
        int zero = 0; // Valor final
        short mask; // Máscara para extracciones

        while( cantidad != 0 ){ // Mientras queden bits por poner en zero
            if(cantidad > (8 - subIndex)){ // Si tengo que poner en zero hasta el final del byte actual
                if(subIndex != 0){ // Si empiezo a extraer desde un punto al medio del byte hasta el final
                    mask = genMask(subIndex, 8 - subIndex); // Desde donde toque hasta el final (que es mayor a 8)
                    raw[index] &= ~ mask; // Nego la máscara para dejar vivos los bits a la izquierda mío, no los que tengo que borrar. Mascara del estilo 1111 1000
                    cantidad -= 8 - subIndex; // Consumí lo que pude
                    index++; // Avanzo en el arreglo de bytes
                    subIndex = 0; // Para ingresar en else si cantidad es grande. Si cantidad < 8 en un principio, subIndex nunca cambia de desde % 8
                }else{ // Extraigo 8 bits, porque parto de 0 y
                    raw[index] = 0;
                    cantidad -= 8; // Ya consumí 8 bits
                    index++; // Avanzo
                }
            }else{ // No tengo que extraer hasta el final, sólo de subIndex a cantidad. Puede ser la extracción final luego de varias extracciones pervias, o una extraccion directa
                mask = genMask(subIndex, cantidad); // Desde el sub-índice de inicio, hasta lo que tenga que sacar
                raw[index] &= ~ mask; // Nego la máscara para dejar vivos los bits a la izquierda mío, no los que tengo que borrar. Mascara del estilo 1111 1000
                cantidad = 0; // Termino while()
            }
        }

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

    /*---------------------------- CRC-8 --------------------------*/
    // CRC-8, poly = x^8 + x^2 + x^1 + x^0, init = 0
    private static final byte[] CRC8_TABLE =
        new byte[] {(byte)0x00,(byte)0x07,(byte)0x0E,(byte)0x09,(byte)0x1C,(byte)0x1B,(byte)0x12,(byte)0x15,(byte)0x38,
                    (byte)0x3F,(byte)0x36,(byte)0x31,(byte)0x24,(byte)0x23,(byte)0x2A,(byte)0x2D,(byte)0x70,(byte)0x77,
                    (byte)0x7E,(byte)0x79,(byte)0x6C,(byte)0x6B,(byte)0x62,(byte)0x65,(byte)0x48,(byte)0x4F,(byte)0x46,
                    (byte)0x41,(byte)0x54,(byte)0x53,(byte)0x5A,(byte)0x5D,(byte)0xE0,(byte)0xE7,(byte)0xEE,(byte)0xE9,
                    (byte)0xFC,(byte)0xFB,(byte)0xF2,(byte)0xF5,(byte)0xD8,(byte)0xDF,(byte)0xD6,(byte)0xD1,(byte)0xC4,
                    (byte)0xC3,(byte)0xCA,(byte)0xCD,(byte)0x90,(byte)0x97,(byte)0x9E,(byte)0x99,(byte)0x8C,(byte)0x8B,
                    (byte)0x82,(byte)0x85,(byte)0xA8,(byte)0xAF,(byte)0xA6,(byte)0xA1,(byte)0xB4,(byte)0xB3,(byte)0xBA,
                    (byte)0xBD,(byte)0xC7,(byte)0xC0,(byte)0xC9,(byte)0xCE,(byte)0xDB,(byte)0xDC,(byte)0xD5,(byte)0xD2,
                    (byte)0xFF,(byte)0xF8,(byte)0xF1,(byte)0xF6,(byte)0xE3,(byte)0xE4,(byte)0xED,(byte)0xEA,(byte)0xB7,
                    (byte)0xB0,(byte)0xB9,(byte)0xBE,(byte)0xAB,(byte)0xAC,(byte)0xA5,(byte)0xA2,(byte)0x8F,(byte)0x88,
                    (byte)0x81,(byte)0x86,(byte)0x93,(byte)0x94,(byte)0x9D,(byte)0x9A,(byte)0x27,(byte)0x20,(byte)0x29,
                    (byte)0x2E,(byte)0x3B,(byte)0x3C,(byte)0x35,(byte)0x32,(byte)0x1F,(byte)0x18,(byte)0x11,(byte)0x16,
                    (byte)0x03,(byte)0x04,(byte)0x0D,(byte)0x0A,(byte)0x57,(byte)0x50,(byte)0x59,(byte)0x5E,(byte)0x4B,
                    (byte)0x4C,(byte)0x45,(byte)0x42,(byte)0x6F,(byte)0x68,(byte)0x61,(byte)0x66,(byte)0x73,(byte)0x74,
                    (byte)0x7D,(byte)0x7A,(byte)0x89,(byte)0x8E,(byte)0x87,(byte)0x80,(byte)0x95,(byte)0x92,(byte)0x9B,
                    (byte)0x9C,(byte)0xB1,(byte)0xB6,(byte)0xBF,(byte)0xB8,(byte)0xAD,(byte)0xAA,(byte)0xA3,(byte)0xA4,
                    (byte)0xF9,(byte)0xFE,(byte)0xF7,(byte)0xF0,(byte)0xE5,(byte)0xE2,(byte)0xEB,(byte)0xEC,(byte)0xC1,
                    (byte)0xC6,(byte)0xCF,(byte)0xC8,(byte)0xDD,(byte)0xDA,(byte)0xD3,(byte)0xD4,(byte)0x69,(byte)0x6E,
                    (byte)0x67,(byte)0x60,(byte)0x75,(byte)0x72,(byte)0x7B,(byte)0x7C,(byte)0x51,(byte)0x56,(byte)0x5F,
                    (byte)0x58,(byte)0x4D,(byte)0x4A,(byte)0x43,(byte)0x44,(byte)0x19,(byte)0x1E,(byte)0x17,(byte)0x10,
                    (byte)0x05,(byte)0x02,(byte)0x0B,(byte)0x0C,(byte)0x21,(byte)0x26,(byte)0x2F,(byte)0x28,(byte)0x3D,
                    (byte)0x3A,(byte)0x33,(byte)0x34,(byte)0x4E,(byte)0x49,(byte)0x40,(byte)0x47,(byte)0x52,(byte)0x55,
                    (byte)0x5C,(byte)0x5B,(byte)0x76,(byte)0x71,(byte)0x78,(byte)0x7F,(byte)0x6A,(byte)0x6D,(byte)0x64,
                    (byte)0x63,(byte)0x3E,(byte)0x39,(byte)0x30,(byte)0x37,(byte)0x22,(byte)0x25,(byte)0x2C,(byte)0x2B,
                    (byte)0x06,(byte)0x01,(byte)0x08,(byte)0x0F,(byte)0x1A,(byte)0x1D,(byte)0x14,(byte)0x13,(byte)0xAE,
                    (byte)0xA9,(byte)0xA0,(byte)0xA7,(byte)0xB2,(byte)0xB5,(byte)0xBC,(byte)0xBB,(byte)0x96,(byte)0x91,
                    (byte)0x98,(byte)0x9F,(byte)0x8A,(byte)0x8D,(byte)0x84,(byte)0x83,(byte)0xDE,(byte)0xD9,(byte)0xD0,
                    (byte)0xD7,(byte)0xC2,(byte)0xC5,(byte)0xCC,(byte)0xCB,(byte)0xE6,(byte)0xE1,(byte)0xE8,(byte)0xEF,
                    (byte)0xFA,(byte)0xFD,(byte)0xF4,(byte)0xF3};

    /**
     * Calcula el CRC-8 mediante el polinomio x^8 + x^2 + x^1 + x^0
     * Con valor crc inicial 0. Desde el indice 0 hasta el largo entregado del array.
     * @param array : bytes
     * @param len: marca fin del calculo CRC
     * @return : CRC calculado
     */
    public static byte calcCRC8(byte[] array, int len){
        byte crc = 0;
        for(int i = 0; i < len; i++){
            crc = CRC8_TABLE[(crc ^ array[i]) & 0xff];
        }
        return crc;
    }

    public static void main(String ... args){
        byte[] a = {(byte) 0x00, (byte) 0x01,(byte) 0x02,(byte) 0x03};
        byte b = calcCRC8(a, 4);
        System.out.println(b);
    }


}
