package Utilidades;

public class ByteArray {
    // Abstract byte representation:

    // BitsArray :   | 8765 4321 | 8765 4321 | 8765 4321 | -->  | ...
    // N°Byte:       | 0         | 1         | 2         | -->  | ...

    private int[] valores;
    private int[] largos;
    private byte[] byteArray;

    public ByteArray(byte[] byteArray, int[] largos){
        this.byteArray = byteArray;
        this.largos = largos;
        this.valores = decodeByteArray();
    }

    public ByteArray(int[] valores, int[] largos){
        if(valores.length != largos.length){
            System.out.println("Cantidad de valores a colocar en array con coincide con la cantidad de largos proporcionados");
        }else{
            this.valores = valores;
            this.largos = largos;
            this.byteArray = encodeByteArray();
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(Integer.toBinaryString(b & 0xFF));
            sb.append(" ");
        }
        sb.append('\n');
        return sb.toString();
    }

    private byte[] encodeByteArray() {
        int total = calcularBytesNecesarios();
        byte[] array = new byte[total];
        int byteNumero = 0; // Parte del byte más significativo

        int posicionEnByte = 8;     // Parte en el bit más significativo

        int i = 0;                  // Parte con el primer valor
        int bitsLeft;               // Bits que quedan por poner del valor actual
        byte actual = 0x00;         // Parto de un byte vacío

        while(i != largos.length) {                         // Mientras queden valores por poner
            //System.out.format("Vamos en el valor número: %d \n", i);
            bitsLeft = largos[i];                           // Avanzo al siguiente valor a poner
            while(bitsLeft > 0) {                           // Mientras queden bits por poner de este valor
                if(posicionEnByte > 0){                     // Si la posicion en el byte actual es 1 o más (quedan bits disponibles)

                    if(posicionEnByte - bitsLeft >= 0){     // Si los bits que tengo que poner me alzan en este byte
                        actual = insertarBitsEnByte(actual, posicionEnByte, bitsLeft, i, bitsLeft); // Poner 'bitsLeft' bits en byte actual
                        //System.out.print("Valor del byte actual: ");System.out.println(Integer.toBinaryString(actual));
                        posicionEnByte -= bitsLeft;         // Correrse a la izquierda
                        bitsLeft = 0;                       // Puse todos los que tenía que poner
                    }else{                                  // No me alcanzan todos los bits en este byte
                        actual = insertarBitsEnByte(actual, posicionEnByte, posicionEnByte, i, bitsLeft); // Usar la cantidad de bits disponibles en este byte
                        //System.out.print("Valor del byte actual: ");System.out.println(Integer.toBinaryString(actual));
                        bitsLeft -= posicionEnByte;         // Puse los que me alcanzaron, actualizar bitsLeft
                        posicionEnByte = 8;                 // Avanzar al siguiente byte
                        array[byteNumero] = actual;
                        actual = 0x00;
                        byteNumero++;
                    }

                }else{                                      // No quedan espacios para poner bits en esete byte
                    posicionEnByte = 8;                     // Avanzar al siguiente byte
                    array[byteNumero] = actual;
                    actual = 0x00;
                    byteNumero++;
                }
                //bitsAPonerAhora = (int) Math.floor(largos[i] / 8.0);   // Parte colocando
            }
            if(byteNumero == total - 1 && i == largos.length - 1){    // Si estoy en el último byte y no tengo más bits que poner
                array[byteNumero] = actual;
            }
            i++;
        }
        return array;
    }

    private int calcularBytesNecesarios(){
        double n = 0.0;
        for (int largo : largos) {
            n += largo;
        }
        return (int) Math.ceil(n / 8.0);
    }

    private byte insertarBitsEnByte(byte byteActual, int posicionEnByte, int cantidadDeBitsAPoner, int iEsimo, int posicionDeBitEniEsimo){
        int valorAux = this.valores[iEsimo];
        byte mask = calcularMascara(posicionEnByte, cantidadDeBitsAPoner);
        int shift = posicionDeBitEniEsimo - posicionEnByte;

        // Hacer coincidir el bit mas significativo a colocar de valor con la posicion disponible en el byte
        //System.out.println(shift);
        //System.out.println(Integer.toBinaryString(valorAux));
        if(shift > 0){  // Shift a la derecha
            valorAux = valorAux >> shift;
        }else{          // Shift a la izquierda
            valorAux = valorAux << -shift;
        }
        //System.out.println(Integer.toBinaryString(valorAux));
        //System.out.println(Integer.toBinaryString(byteActual));
        byteActual = (byte) ((byteActual & 0xFF) | (valorAux & mask));
        //System.out.println(Integer.toBinaryString(byteActual));
        return (byte) (byteActual & 0xFF);
    }

    public static byte calcularMascara(int posicionEnByte, int cantidadDeBitsAPoner){
        // 0000  0001  0010  0011  0100  0101  0110  0111  1000  1001  1010  1011  1100  1101  1110  1111
        // 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
        byte mask = 0x00;
        switch(posicionEnByte){
            case 8: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x80;break;
                case 2: mask = (byte) 0xC0;break;
                case 3: mask = (byte) 0xE0;break;
                case 4: mask = (byte) 0xF0;break;
                case 5: mask = (byte) 0xF8;break;
                case 6: mask = (byte) 0xFC;break;
                case 7: mask = (byte) 0xFE;break;
                case 8: mask = (byte) 0xFF;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 7: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x40;break;
                case 2: mask = (byte) 0x60;break;
                case 3: mask = (byte) 0x70;break;
                case 4: mask = (byte) 0x78;break;
                case 5: mask = (byte) 0x7C;break;
                case 6: mask = (byte) 0x7E;break;
                case 7: mask = (byte) 0x7F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 6: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x20;break;
                case 2: mask = (byte) 0x30;break;
                case 3: mask = (byte) 0x38;break;
                case 4: mask = (byte) 0x3C;break;
                case 5: mask = (byte) 0x3E;break;
                case 6: mask = (byte) 0x3F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 5: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x10;break;
                case 2: mask = (byte) 0x18;break;
                case 3: mask = (byte) 0x1C;break;
                case 4: mask = (byte) 0x1E;break;
                case 5: mask = (byte) 0x1F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 4: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x08;break;
                case 2: mask = (byte) 0x0C;break;
                case 3: mask = (byte) 0x0E;break;
                case 4: mask = (byte) 0x0F;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 3: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x04;break;
                case 2: mask = (byte) 0x06;break;
                case 3: mask = (byte) 0x07;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 2: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x02;break;
                case 2: mask = (byte) 0x03;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            case 1: switch (cantidadDeBitsAPoner){
                case 1: mask = (byte) 0x01;break;
                default: System.out.format("Error en cálculo de máscara: Posición en Byte %d , Bits solicitados; %d \n", posicionEnByte, cantidadDeBitsAPoner);
            }break;
            default: System.out.format("Posición en byte actual fuera de rango: %d \n", posicionEnByte);
        }
        //System.out.print("Máscara a utilizar: ");System.out.println(Integer.toBinaryString(mask&0xFF));
        return mask;
    }

    public byte[] getByteArray() {
        return byteArray;
    }
    public int[] getValores(){ return valores;}
    public int[] getLargos(){return largos;}

    private int[] decodeByteArray(){
        int total = calcularBytesNecesarios();
        if(this.byteArray.length != total){
            System.out.print("Bytes necesarios no coincide con el largo de bytes ingresado");
            return valores;
        }
        int[] valores = new int[this.largos.length];
        int i = 0;
        int byteActual = 0;
        int posicionEnByteActual = 8;

        while(i != this.largos.length){ // Mientras queden valores a sacar
            //System.out.format("Valor de i: %d \n", i);
            int valor = 0;
            int bitsLeft = this.largos[i];

            while(bitsLeft > 0 ){ // Mientras queden bits por sacar
                if(posicionEnByteActual - bitsLeft >= 0){ // Si tengo que sacar menos de 8 bits
                    // Saco los que corresponen y corro a la derecha;
                    valor |= ((byteArray[byteActual] & (calcularMascara(posicionEnByteActual, bitsLeft) & 0xFF)) >> (posicionEnByteActual - bitsLeft));
                    //System.out.println(Integer.toBinaryString(valor));
                    posicionEnByteActual -= bitsLeft;
                    bitsLeft = 0;
                }else { // Si tengo que sacar hasta el final del byte
                    valor |= ((byteArray[byteActual] & (calcularMascara(posicionEnByteActual, posicionEnByteActual) & 0xFF)) << (bitsLeft - posicionEnByteActual));
                    bitsLeft -= posicionEnByteActual;
                    posicionEnByteActual = 8;               // Paso al siguiente byte
                    byteActual++;
                }

            }
            //System.out.print("Valor a la fecha: ");System.out.println(Integer.toBinaryString(valor));
            //System.out.format("Bits significativos: %d \n", this.largos[i]);
            int mask;
            if(this.largos[i] < 32){
                mask = ((1 << (this.largos[i])) - 1);
            }else{
                mask = -1; // Max 32 bits de presición
            }
            //System.out.print("Valor de mascara: ");System.out.println(Integer.toBinaryString(mask));
            valores[i] = valor & mask;
            //valores[i] = valor;
            //System.out.print("Valor de salida:  ");System.out.println(Integer.toBinaryString(valor));
            i++;
        }
        return valores;
    }



}
