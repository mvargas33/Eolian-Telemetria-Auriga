package Test.Sandboxes;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
//mport javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ChaCha20Sandbox
{
    static String plainText = "hola";
    static byte[] bytes = {0x00, 0x01, 0x02, 0x03};

    public static void main(String[] args) throws Exception
    {
        // Generate Key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 256;
        keyGenerator.init(keyBitSize, secureRandom);
        SecretKey key = keyGenerator.generateKey();

        byte[] keyB = Base64.getDecoder().decode(key.getEncoded());
        System.out.println("Key" + Base64.getEncoder().encodeToString(keyB) + " largo: " + Base64.getEncoder().encodeToString(keyB).length());


        /*for (Provider provider: Security.getProviders()) {
            System.out.println(provider.getName());
            for (String key: provider.stringPropertyNames())
                System.out.println("\t" + key + "\t" + provider.getProperty(key));
        }
        */

        //String key64 = "Gz+06hC7o68d0GbiE9F9D82qh1uikxVBfFN6UrOWc/k=";
        System.out.println("Original Text  : " + plainText);

        byte[] cipherText = encrypt(plainText.getBytes(), keyB);
        System.out.println(cipherText.length);
        System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(cipherText));

        String decryptedText = decrypt(cipherText, keyB);
        System.out.println("DeCrypted Text : " + decryptedText);

    }
r
    public static byte[] encrypt(byte[] plaintext, byte[] key) throws Exception
    {
        byte[] nonceBytes = new byte[12];

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("ChaCha20/None/NoPadding");
        ChaCha20ParameterSpec chaCha20ParameterSpec = new ChaCha20ParameterSpec(nonceBytes, 1);

        // Create IvParamterSpec
        //AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(nonceBytes);

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key, "ChaCha20");

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, chaCha20ParameterSpec);

        // Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    public static String decrypt(byte[] cipherText, byte[] key) throws Exception
    {
        byte[] nonceBytes = new byte[12];

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("ChaCha20/None/NoPadding");
        ChaCha20ParameterSpec chaCha20ParameterSpec = new ChaCha20ParameterSpec(nonceBytes, 1);

        // Create IvParamterSpec
        //AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(nonceBytes);

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key, "ChaCha20");

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, chaCha20ParameterSpec);

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }
}
