package Test.Sandboxes;

import ApplicationLayer.LocalServices.WirelessService.Utilities.BitOperations;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
//mport javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncryptionSandbox
{

    public static void main(String[] args) throws Exception
    {
        // Plain text.
        String text = "01234567890123456789012345678901";
        byte[] e = text.getBytes();
        System.out.println("Text length bytes: " + e.length);

        // Generate Key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 256;
        keyGenerator.init(keyBitSize, secureRandom);
        SecretKey key = keyGenerator.generateKey();

        System.out.println("Key: " + new String(key.getEncoded()));
        System.out.println("Bit length: " + key.getEncoded().length*8);

        // Generating 128-bit IV.
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        byte[] a = {(byte) 0b00000000,
                    (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                    (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                    (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                    (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111};

        BigInteger ivv = new BigInteger(a);
        System.out.println(ivv);
        BigInteger top = new BigInteger(a);
        ivv = ivv.add(BigInteger.ONE).mod(top) ;
        //ivv = ivv.add(BigInteger.ONE).mod(top) ;
        System.out.println(ivv);

        BigInteger randomm = new BigInteger(128, secureRandom);
        byte[] array = randomm.toByteArray();
        System.out.println(array.length);
        System.out.println(BitOperations.ArraytoString(array));


        //String key64 = "Gz+06hC7o68d0GbiE9F9D82qh1uikxVBfFN6UrOWc/k=";
        System.out.println("Original Text  : " + text);

        byte[] cipherText = encrypt(e, key, ivParameterSpec);

        System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(cipherText));
        System.out.println("Length in bytes: " + cipherText.length);

        String decryptedText = new String(decrypt(cipherText, key));
        System.out.println("DeCrypted Text : " + decryptedText);

    }

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
}
