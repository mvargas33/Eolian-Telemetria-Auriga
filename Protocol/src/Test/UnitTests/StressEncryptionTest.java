package Test.UnitTests;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class StressEncryptionTest {
    public static byte[] encrypt(byte[] input, SecretKey key, byte[] ivFull, byte[] ivEnd, int ivEndSize, int macSize) throws Exception {
        // MAC (6) || IV (12) || CIPHER TEXT (96) = (114 BYTES)
        //System.out.println("IV Full: " + BitOperations.ArraytoString(ivFull));

        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivFull));
        byte[] encrypted = cipher.doFinal(input);
        //System.out.println("Cipher text: " + BitOperations.ArraytoString(encrypted));

        // Get MAC.
        Mac mac = Mac.getInstance("HMACMD5");
        mac.init(key);
        byte[] macCalculated = mac.doFinal(encrypted);
        byte[] macConsidered = new byte[macSize];
        System.arraycopy(macCalculated, 0, macConsidered, 0, macSize);
        //System.out.println("Mac: " + BitOperations.ArraytoString(macConsidered));

        // Get IV end.
        //byte[] ivEnd = new byte[ivEndSize];
        //System.arraycopy(ivFull, 0, ivEnd, 0, ivEndSize); // byte[0] is the less significative byte in iv[]
        //System.out.println("IV End Calculated: " + BitOperations.ArraytoString(ivEnd));

        // Combine MAC, IV_END and encrypted part.
        byte[] encryptedMACandIVAndText = new byte[macSize + ivEndSize + encrypted.length];
        System.arraycopy(macConsidered, 0, encryptedMACandIVAndText, 0, macSize); //Append MAC at start
        System.arraycopy(ivEnd, 0, encryptedMACandIVAndText, macSize, ivEndSize); // Sppend just ivEndSize bits from IV
        System.arraycopy(encrypted, 0, encryptedMACandIVAndText, macSize + ivEndSize, encrypted.length); // Cipher Text

        //System.out.println("Encrypted package: " + BitOperations.ArraytoString(encryptedMACandIVAndText));
        return encryptedMACandIVAndText;
    }

    public static byte[] decrypt(byte[] encryptedMacIvTextBytes, SecretKey key, byte[] ivStart, int ivEndSize, int macSize) throws Exception {
        // MAC (7) || IV_END (11) || CIPHER TEXT (96) = (114 BYTES)
        //System.out.println("IV start provided: " + BitOperations.ArraytoString(ivStart));

        // Exctrat MAC.
        byte[] macExtracted = new byte[macSize];
        System.arraycopy(encryptedMacIvTextBytes, 0, macExtracted, 0, macSize);
        //System.out.println("Mac extracted: " + BitOperations.ArraytoString(macExtracted));

        // Extract encrypted part.
        int encryptedSize = encryptedMacIvTextBytes.length - macSize - ivEndSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedMacIvTextBytes, macSize + ivEndSize, encryptedBytes, 0, encryptedSize);
        //System.out.println("Encrypted part: " + BitOperations.ArraytoString(encryptedBytes));

        // Check MAC
        Mac mac = Mac.getInstance("HMACMD5");
        mac.init(key);
        byte[] macCalculated = mac.doFinal(encryptedBytes);
        byte[] macConsidered = new byte[macSize];
        System.arraycopy(macCalculated, 0, macConsidered, 0, macSize);
        //System.out.println("Hmac-MD5 size:" + macCalculated.length);

        if (!Arrays.equals(macExtracted, macConsidered)){
            throw new Exception("Mac no coincide");
        }

        // Exctract FULL IV.
        byte[] iv = new byte[16];
        System.arraycopy(encryptedMacIvTextBytes, macSize, iv, 0, ivEndSize); // IV End (0 is less significative)
        //System.out.println("IV end extracted: " + BitOperations.ArraytoString(iv));
        System.arraycopy(ivStart, 0, iv, ivEndSize, ivStart.length);    // IV Start (more to right is more significative)
        //System.out.println("IV full: " + BitOperations.ArraytoString(iv));

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipherDecrypt.doFinal(encryptedBytes);
    }

    public static void main(String[] args) throws Exception{
        //String text = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234"; // 95  bytes (+1 byte padding)
        String text = "eolian";
        byte[] textBytes = text.getBytes();

        // Generate Key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 256;
        keyGenerator.init(keyBitSize, secureRandom);
        SecretKey key = keyGenerator.generateKey();

        byte[] zeros = new byte[16-4];

        // Constant for IV
        byte[] genTop = {(byte) 0b00000000,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111};
        BigInteger genMod = new BigInteger(genTop);

        byte[] endingTop = {(byte) 0b00000000,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111};
        BigInteger endMod = new BigInteger(endingTop);

        // Generate IV
        SecureRandom ivRandom = new SecureRandom();
        //BigInteger iv = new BigInteger(128, ivRandom);
        //BigInteger iv = BigInteger.ZERO;
        //byte[] array = iv.toByteArray();

        byte[] ivFull = new byte[16];   // Full
        byte[] ivStart = {(byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111};   // Start
        byte[] ivEnd = {(byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111}; // End

        ivRandom.nextBytes(ivStart);
        ivRandom.nextBytes(ivEnd);
        BigInteger iv_end = new BigInteger(ivEnd);

        System.arraycopy(ivStart, 0, ivFull, 16-4, 4);
        System.arraycopy(ivEnd, 0, ivFull, 0, 12);

        //System.out.println("IV Full: " + BitOperations.ArraytoString(ivFull));
        //System.out.println("IV Start: " + BitOperations.ArraytoString(ivStart));
        //System.arraycopy(iv.toByteArray(), 5, ivEnd, 0, 12);
        //ivRandom.nextBytes(ivFull);

        //System.out.println(iv.toByteArray().length);
        //System.out.println(BitOperations.ArraytoString(iv.toByteArray()));
        //
        //System.out.println(BitOperations.ArraytoString(ivFull));
        //IvParameterSpec ivParameterSpec = new IvParameterSpec(ivFull);

        /* Test */
        int round_actual = 0;
        int rounds_totales = 100;//100
        double[] promedio_rounds = new double[rounds_totales];
        int target = 64*1000; // Mensajes/segundo de la Xbee 64*1000

        //double promedio = 0;
        long initial_time ;
        long last_time ;

        while(round_actual < rounds_totales){
            initial_time = System.currentTimeMillis();
            int paso = 0;


            while(paso < target){
                //System.out.println("Increment");
                iv_end = iv_end.add(BigInteger.ONE).mod(endMod); // IV_END += 1
                byte[] array = iv_end.toByteArray();
                //System.out.println("array: " + BitOperations.ArraytoString(array));
                if(array.length == 17-4){ // Viene con bit de signo
                    System.arraycopy(array, 1, ivFull, 0, 16-4); // No considero bit de signo
                }else{ // Viene con 16-4 bytes O MENOS
                    System.arraycopy(zeros, 0, ivFull, 0, 16-4); // Rellenar con 0s
                    System.arraycopy(array, 0, ivFull, 0, array.length); // Copiar los bytes de IV_END
                }
                //System.out.println("IV Full: " + BitOperations.ArraytoString(ivFull));
                //System.out.println("IV Start: " + BitOperations.ArraytoString(ivStart));
                //System.out.println(BitOperations.ArraytoString(ivFull));
                //System.arraycopy(iv.toByteArray(), 1, ivFull, 0, 16); // A veces vienen 17 bytes porque el primero es de sólo 0 por el signo
                //System.out.println(iv.toByteArray().length);
                //System.out.println(BitOperations.ArraytoString(iv.toByteArray()));
                byte[] cipherText = encrypt(text.getBytes(), key, ivFull, array,12, 6);
                //byte[] result = decrypt(cipherText, key, ivStart,12, 6);
                //System.out.println("Result: " + new String(result));
                paso++;
            }
            last_time = System.currentTimeMillis();
            promedio_rounds[round_actual] = (double )target /  ((double) (last_time  - initial_time) / 1000.0);
            System.out.println("Round: " + round_actual + " Encriptaciones por segundo: " + promedio_rounds[round_actual]);
            round_actual += 1;
        }
        double promedio_de_promedios = Arrays.stream(promedio_rounds).average().orElse(Double.NaN);
        System.out.println("Promedio de promedios: " + promedio_de_promedios + " [Encriptaciones/seg]");
    }
}
