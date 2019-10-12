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
}