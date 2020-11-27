package Test.UnitTests;

import ApplicationLayer.LocalServices.WirelessService.Utilities.BitOperations;


import static org.junit.jupiter.api.Assertions.*;

class BitOperationsTest {
    @org.junit.jupiter.api.Test
    void TestDePonerValorEnArray(){
        byte[] raw = {0x00, 0x00};
        int val = 3; // 11
        int bitSig = 2;
        int desdeBit = 0;

        byte[] res = {(byte) 0b11000000, (byte) 0b00000000};
        BitOperations.ponerValorEnArray(raw, desdeBit, val, bitSig);
        assertArrayEquals(res, raw);

        raw = new byte[]{0x00, 0x00};
        val = 0xFF03; // 1 111 1111 0000 0011
        bitSig = 15;
        desdeBit = 1;

        res = new byte[]{(byte) 0b01111111, (byte) 0b00000011};
        BitOperations.ponerValorEnArray(raw, desdeBit, val, bitSig);
        assertArrayEquals(res, raw);
    }

    @org.junit.jupiter.api.Test
    void TestDeCorrectitud2() {
        int[] source = {0b00011101, 0xFFFFFFFF, 0b01110000, 0x00FF};
        int[] bitSig = {5, 32, 7, 8};
        //int minBytes = (int) Math.ceil(Arrays.stream(bitSig).sum() / 8.0);
        byte[] rawBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; // 7*8= 56 bits, tengo 52 bits
        int bitSigInicio = 0;
        int rawBytes_inicio = 0; // Del 0
        int rawBytes_fin = 51; // Al 51 hay 52 bits
        BitOperations.updateByteArrayFromValues(source, rawBytes, bitSig, bitSigInicio, rawBytes_inicio, rawBytes_fin);

        //                (11101) (111 | 1111 1111          | 1111 1111      | 1111 1111        | 1111 1)(111       | 0000) (1111       | 1111) 0000
        byte[] res = {(byte) 0b11101111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b00001111, (byte) 0b11110000};
        assertArrayEquals(res, rawBytes);
    }


    /**
     * Varifica el funcionamiento del método updateValuesFromByteArray() que actualiza un array destino extrayendo bits
     * de un arreglo de bytes, segun los bits significativos de un arreglo de enteros para un rango dado.
     */
    @org.junit.jupiter.api.Test
    void TestDeCorrectitud1() {
        int[] destino = {0,1,2,3};
        byte[] rawBytes = {(byte) 0b00110011, (byte) 0b10101010, (byte) 0b00001111, (byte) 0b00100010}; // Array de ejemplo
        int[] bitSig = {8, 8, 8, 8}; // Sacar ocho cada vez
        int bitSigInicio = 0;
        int rawBytes_inicio = 0;
        int rawBytes_fin = 8*4 - 1;

        int[] res = {0b0000000000110011, 0b0000000010101010, 0b0000000000001111, 0b0000000000100010};
        BitOperations.updateValuesFromByteArray(destino, rawBytes, bitSig, bitSigInicio, rawBytes_inicio, rawBytes_fin);
        assertArrayEquals(res, destino);
    }

    /**
     * Varifica el correcto funcionamiento de la extracción de bits de un arreglo. i.e, el método extraerBits()
     */
    @org.junit.jupiter.api.Test
    void TestDeExtraerBits() {
        int res; // Respuestas
        byte[] raw = {(byte) 0b00110011, (byte) 0b10101010, (byte) 0b00001111, (byte) 0b00100010}; // Array de ejemplo

        res = BitOperations.extraerBits(raw, 0, 3); // Simple
        assertEquals(1, res); // Debe ser 001
        res = BitOperations.extraerBits(raw, 2, 7); // Combinar dos bytes en array
        assertEquals(0b01100111, res); // Debe ser 1100111
        res = BitOperations.extraerBits(raw, 9, 1); // Buscar lejano
        assertEquals(0, res); // Debe ser 0
        res = BitOperations.extraerBits(raw, 9, 2); // Buscar lejano
        assertEquals(1, res); // Debe ser 01
        res = BitOperations.extraerBits(raw, 9, 3); // Buscar lejano
        assertEquals(2, res); // Debe ser 010
        res = BitOperations.extraerBits(raw, 0, 32); // 32 bits, tamaño de entero
        assertEquals(866783010, res); // 00110011101010100000111100100010
        res = BitOperations.extraerBits(raw, 1, 31); // 31 bits, tamaño de entero
        assertEquals(866783010, res); // 0110011101010100000111100100010
        res = BitOperations.extraerBits(raw, 10, 8); // desde segundo indice en byte[]
        assertEquals(168, res); // 10101000
    }

    /**
     * Verifica el funcionamiento del reset de bytes en un array, para actualizar vyteArray en envío
     */
    @org.junit.jupiter.api.Test
    void TestDeResetDeBits(){
        byte[] a = {0b01111111};
        byte[] b = {0b01011111};
        BitOperations.resetToZeroBitRange(a, 2, 2);
        assertArrayEquals(a, b);

        byte[] c = {0b00001111, 0b01111111};
        byte[] d = {0b00001000, 0b00001111};
        BitOperations.resetToZeroBitRange(c, 5, 11);
        assertArrayEquals(c, d);
    }
}