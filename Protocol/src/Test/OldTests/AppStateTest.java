//package Test.OldTests;
//
//import ApplicationLayer.AppComponents.AppComponent;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AppStateTest {
//
//    @org.junit.jupiter.api.Test
//    void testAutomatedArraysCalculations() throws Exception {
//        double[] min = {0.9, -99.99, 0};
//        double[] max = {9.9, 100.01, 1};
//
//        AppComponent a = new AppComponent("BMS", min, max);
//        assertArrayEquals(new int[]{1, 2, 0}, a.decimales);
//        assertArrayEquals(new int[]{-9, 9999, 0}, a.offset);
//        assertArrayEquals(new int[]{91, 20001, 2}, a.delta);
//        assertArrayEquals(new int[]{7, 15, 1}, a.bitSignificativos);
//
//        a.updateFromReading(new double[]{6.1, 3.11, 1.0});
//        assertArrayEquals(new double[]{6.1, 3.11, 1.0}, a.valoresRealesActuales);
//
//        a.updateFromReceiving(new int[]{52, 10310, 1});
//        assertArrayEquals(a.valoresRealesActuales, new double[]{6.1, 3.11, 1.0}, Math.pow(10, -10)); // Expected, Actual, Delta
//    }
//}