package Test;

import Utilities.BitOperations;

import static org.junit.jupiter.api.Assertions.*;

class BitOperationsTest {

    @org.junit.jupiter.api.Test
    void TestDeCorrectitud1() {
        int[] destino = {0,1,2,3};
        byte[] rawBytes = {0b00000000, 0b00000001, 0b00000010, 0b00000011};
        int[] bitSig = {8, 8, 8, 8};
        int bitSigInicio = 0;
        int rawBytes_inicio = 0;
        int rawBytes_fin = 8*4 - 1;

        BitOperations.updateValuesFromByteArray(destino, rawBytes, bitSig, bitSigInicio, rawBytes_inicio, rawBytes_fin);

        int[] respuesta = {0, 1, 2, 3};


        assertArrayEquals(destino, respuesta);
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
}