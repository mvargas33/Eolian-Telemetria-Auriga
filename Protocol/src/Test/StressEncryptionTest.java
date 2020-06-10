package Test;

import Utilities.BitOperations;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class StressEncryptionTest {
    public static byte[] encrypt(byte[] clean, SecretKey key, IvParameterSpec ivParameterSpec) throws Exception {
        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[16 + encrypted.length];
        System.arraycopy(ivParameterSpec.getIV(), 0, encryptedIVAndText, 0, 16);
        System.arraycopy(encrypted, 0, encryptedIVAndText, 16, encrypted.length);

        // IV | Cipher Text
        return encryptedIVAndText;
    }

    public static byte[] decrypt(byte[] encryptedIvTextBytes, SecretKey key) throws Exception {
        // Extract IV.
        byte[] iv = new byte[16];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, 16);

        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - 16;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, 16, encryptedBytes, 0, encryptedSize);

        // IV and Key object.
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        //SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        return cipherDecrypt.doFinal(encryptedBytes);
    }

    public static void main(String[] args) throws Exception{
        String text = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234"; // 95  bytes (+1 byte padding)
        byte[] textBytes = text.getBytes();

        // Generate Key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 256;
        keyGenerator.init(keyBitSize, secureRandom);
        SecretKey key = keyGenerator.generateKey();


        // Constant for IV
        byte[] top = {(byte) 0b00000000,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111};
        BigInteger mod = new BigInteger(top);

        // Generate IV
        SecureRandom ivRandom = new SecureRandom();
        byte[] ivByte = new byte[16];
        //ivRandom.nextBytes(ivByte);
        BigInteger iv = new BigInteger(top);
        System.out.println(iv.toByteArray().length);
        System.out.println(BitOperations.ArraytoString(iv.toByteArray()));
        //System.arraycopy(iv.toByteArray(), 0, ivByte, 0, 16);
        //System.out.println(BitOperations.ArraytoString(ivByte));
        //IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);

        /* Test */
        int round_actual = 0;
        int rounds_totales = 100;
        double[] promedio_rounds = new double[rounds_totales];
        int target = 64*1000; // Mensajes/segundo de la Xbee

        //double promedio = 0;
        long initial_time ;
        long last_time ;

        while(round_actual < rounds_totales){
            initial_time = System.currentTimeMillis();
            int paso = 0;

            while(paso < target){
                iv = iv.add(BigInteger.ONE).mod(mod);
                byte[] array = iv.toByteArray();
                //System.out.println(BitOperations.ArraytoString(array));
                if(iv.toByteArray().length >= 17){
                    System.arraycopy(array, 0, ivByte, 0, 16);
                }else{
                    System.arraycopy(array, 0, ivByte, 0, array.length);
                }

                //System.out.println(BitOperations.ArraytoString(ivByte));
                //System.arraycopy(iv.toByteArray(), 1, ivByte, 0, 16); // A veces vienen 17 bytes porque el primero es de sólo 0 por el signo
                //System.out.println(iv.toByteArray().length);
                //System.out.println(BitOperations.ArraytoString(iv.toByteArray()));
                byte[] cipherText = encrypt(textBytes, key, new IvParameterSpec(ivByte));
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
