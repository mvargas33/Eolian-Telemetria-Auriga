package PresentationLayer.Encryption;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoAdmin {
    private final Cipher cipher;
    private final Mac mac;
    private SecretKey key;

    private byte[] fullMessage;         // MAC_END | IV_END | CIPHERED MESSAGE
    private byte[] inputEncrypted;      // CIPHERED MESSAGE
    private byte[] fullMac;             // MAC
    private byte[] macEnd;              // MAC_END

    private byte[] macEndDecrypt;
    private byte[] inputPlainText;

    private byte[] IV;
    private byte[] IV_START;        // Last bytes
    private byte[] IV_END;          // First bytes
    private byte[] IV_END_MOD;      // MAX IV_END Value
    private byte[] IV_END_ZEROS;    // Array with zeros
    private BigInteger IV_END_INTEGER;  // IV as BigInteger object
    private BigInteger IV_END_MOD_INTEGER;
    int IV_FIXED_BYTES;             // 4 Bytes fixed
    int IV_SIG_BYTES;               // 12 Bytes send in packages
    int MAC_SIG_BYTES;              // MAC Sinificative bytes for message
    int CONTENT_SIG_BYTES;

    /**
     * Base Encryptor.
     */
    public CryptoAdmin(SecretKey key, byte[] IV, int MAC_SIG_BYTES, int IV_SIG_BYTES, int CONTENT_SIG_BYTES) throws Exception {
        if(CONTENT_SIG_BYTES % 16 != 15){
            throw new Exception("CONTENT BYTES SIZE IS NOT OPTIMAL. USE MESSAGE LIKE 16 | 16 | 16 | 15 + 1 (PADDING)");
        }

        this.IV_FIXED_BYTES = 16 - IV_SIG_BYTES;
        this.IV_SIG_BYTES = IV_SIG_BYTES;
        this.MAC_SIG_BYTES = MAC_SIG_BYTES;
        this.CONTENT_SIG_BYTES = CONTENT_SIG_BYTES;
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // AES CBC y Padding
        this.key = key;
        this.mac = Mac.getInstance("HMACMD5"); // HMAC MD5
        this.mac.init(key);
        this.IV = IV;
        this.IV_START = new byte[IV_FIXED_BYTES];
        this.IV_END = new byte[IV_SIG_BYTES];
        this.IV_END_ZEROS = new byte[IV_SIG_BYTES];

        this.IV_END_MOD = new byte[IV_SIG_BYTES + 1]; // + 1 Sign byte

        this.inputEncrypted = new byte[CONTENT_SIG_BYTES + 1]; // +1 PADDING BYTE
        this.inputPlainText = new byte[CONTENT_SIG_BYTES];
        this.fullMac = new byte[16]; // 16 Byte MD5
        this.macEnd = new byte[MAC_SIG_BYTES];
        this.macEndDecrypt = new byte[MAC_SIG_BYTES];
        this.fullMessage = new byte[MAC_SIG_BYTES + IV_SIG_BYTES + CONTENT_SIG_BYTES + 1]; // 1 PADDING BYTE AT LEAST


        System.arraycopy(IV, IV_SIG_BYTES, IV_START, 0, IV_FIXED_BYTES);
        System.arraycopy(IV, 0, IV_END, 0, IV_SIG_BYTES);
        this.IV_END_INTEGER = new BigInteger(IV_END);

        byte[] zero = {(byte) 0b00000000};
        byte[] one = {(byte) 0b11111111};
        System.arraycopy(zero, 0, IV_END_MOD, 0, 1);
        for (int i = 1; i < IV_SIG_BYTES; i++) {
            System.arraycopy(one, 0, IV_END_MOD, i, 1);
        }
        this.IV_END_MOD_INTEGER = new BigInteger(IV_END_MOD);
    }

    /**
     * Suma 1 al IV_END en BigInteger. Luego actualiza el byte array asociado.
     */
    private void addOneToIVEND(){
        this.IV_END_INTEGER = this.IV_END_INTEGER.add(BigInteger.ONE).mod(IV_END_MOD_INTEGER); // ++1 mod (limit)
        byte[] result = IV_END_INTEGER.toByteArray();

        // Update IV_END array
        if(result.length == 17 - IV_FIXED_BYTES){ // Viene con bit de signo
            System.arraycopy(result, 1, IV_END, 0, IV_SIG_BYTES); // No considero bit de signo
        }else if (result.length == 16 - IV_FIXED_BYTES){ // Viene con 16-4 bytes O MENOS
            System.arraycopy(result, 0, IV_END, 0, result.length); // Copiar los bytes de IV_END
        }else{
            System.arraycopy(result, 0, IV_END, 0, result.length); // Copiar los bytes de IV_END
            System.arraycopy(IV_END_ZEROS, 0, IV_END, result.length, 16 - result.length); // Rellenar con 0s
        }
        //Update IV array
        System.arraycopy(IV_END, 0, IV, 0, IV_SIG_BYTES);
    }

    /**
     * Encripta el input proporcionado en forma local y eficiente. Retorna bytes con HMAC | IV | CIPHERED MSG
     * @param input bytes a ser encriptados
     * @return HMAC | IV | CIPHERED MSG
     * @throws Exception excepciones de encriptación
     */
    public byte[] encrypt(byte[] input) throws Exception {
        // Encrypt.
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
        inputEncrypted = cipher.doFinal(input);
        // MAC.
        fullMac = mac.doFinal(input);
        System.arraycopy(fullMac, 0, macEnd, 0, MAC_SIG_BYTES);
        // Combine.
        System.arraycopy(macEnd, 0, fullMessage, 0, MAC_SIG_BYTES);
        System.arraycopy(IV_END, 0, fullMessage, MAC_SIG_BYTES, IV_SIG_BYTES);
        System.arraycopy(inputEncrypted, 0, fullMessage, MAC_SIG_BYTES + IV_SIG_BYTES, CONTENT_SIG_BYTES + 1);
        // Change IV for a clean new encryption
        this.addOneToIVEND();
        // Return.
        return this.fullMessage;
    }

    public byte[] decrypt(byte[] cipheredMessage) throws Exception {
        // MAC_END.
        System.arraycopy(cipheredMessage, 0, macEndDecrypt, 0, MAC_SIG_BYTES);
        // IV_END.
        System.arraycopy(cipheredMessage, MAC_SIG_BYTES, IV_END, 0, IV_SIG_BYTES);
        // CIPHERED TEXT.
        System.arraycopy(cipheredMessage, MAC_SIG_BYTES + IV_SIG_BYTES, inputEncrypted, 0, CONTENT_SIG_BYTES + 1);

        // CALCULATE AND VERIFY MAC.
        fullMac = mac.doFinal(inputEncrypted);
        System.arraycopy(fullMac, 0, macEnd, 0, MAC_SIG_BYTES);

        if (!Arrays.equals(macEndDecrypt, macEnd)){
            throw new Exception("MAC NO COINCIDE!");
        }

        // Update IV array.
        System.arraycopy(IV_END, 0, IV, 0, IV_SIG_BYTES);

        // Decrypt.
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
        inputPlainText = cipher.doFinal(inputEncrypted);
        return inputPlainText;
    }



}
