package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
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

        byte[] copy_of_IV = new byte[IV.length]; // Se debe hacer una copia de los valores del IV, sino se sobreescribirá sobre el objeto byte[] IV de quien pasa el argumento al constructor
        System.arraycopy(IV, 0, copy_of_IV, 0, IV.length);
        this.IV = copy_of_IV;

        this.IV_START = new byte[this.IV_FIXED_BYTES];
        this.IV_END = new byte[IV_SIG_BYTES];
        this.IV_END_ZEROS = new byte[IV_SIG_BYTES];

        this.IV_END_MOD = new byte[IV_SIG_BYTES + 1]; // + 1 Sign byte

        this.inputEncrypted = new byte[CONTENT_SIG_BYTES + 1]; // +1 PADDING BYTE
        this.inputPlainText = new byte[CONTENT_SIG_BYTES];
        this.fullMac = new byte[16]; // 16 Byte MD5
        this.macEnd = new byte[MAC_SIG_BYTES];
        this.macEndDecrypt = new byte[MAC_SIG_BYTES];
        this.fullMessage = new byte[MAC_SIG_BYTES + IV_SIG_BYTES + CONTENT_SIG_BYTES + 1]; // 1 PADDING BYTE AT LEAST


        System.arraycopy(IV, IV_SIG_BYTES, this.IV_START, 0, this.IV_FIXED_BYTES);
        System.arraycopy(IV, 0, this.IV_END, 0, IV_SIG_BYTES);
        //System.out.println(BitOperations.ArraytoString(IV_END));
        byte[] END_WITH_SIGN = new byte[IV_SIG_BYTES + 1];
        System.arraycopy(this.IV_END, 0, END_WITH_SIGN, 1, IV_SIG_BYTES);
        this.IV_END_INTEGER = new BigInteger(END_WITH_SIGN);
        //System.out.println(BitOperations.ArraytoString(IV_END_INTEGER.toByteArray()));

        byte[] zero = {(byte) 0b00000000};
        byte[] one = {(byte) 0b11111111};
        System.arraycopy(zero, 0, this.IV_END_MOD, 0, 1);
        for (int i = 1; i <= IV_SIG_BYTES; i++) {
            System.arraycopy(one, 0, this.IV_END_MOD, i, 1);
        }
        this.IV_END_MOD_INTEGER = new BigInteger(this.IV_END_MOD);
    }

    /**
     * Suma 1 al IV_END en BigInteger. Luego actualiza el byte array asociado.
     */
    private void addOneToIVEND(){
        this.IV_END_INTEGER = this.IV_END_INTEGER.add(BigInteger.ONE).mod(this.IV_END_MOD_INTEGER); // ++1 mod (limit)
        byte[] result = this.IV_END_INTEGER.toByteArray();
        //System.out.println(BitOperations.ArraytoString(result));
        //System.out.println("IV SIG BYTES:" + this.IV_SIG_BYTES + "result.len: " + result.length);

        // Update IV_END array
        if(result.length == 17 - this.IV_FIXED_BYTES){ // Viene con bit de signo
            System.arraycopy(result, 1, this.IV_END, 0, this.IV_SIG_BYTES); // No considero bit de signo
        }else if (result.length == 16 - this.IV_FIXED_BYTES){ // Viene justo con 16-4 bytes
            System.arraycopy(result, 0, this.IV_END, 0, this.IV_SIG_BYTES); // Copiar los bytes de IV_END
        }else{ // Viene con menos de 16 -4 bytes
            System.arraycopy(result, 0, this.IV_END, 0, result.length); // Copiar los bytes de IV_END (puede tener signo)
            System.arraycopy(this.IV_END_ZEROS, 0, this.IV_END, result.length, this.IV_SIG_BYTES - result.length); // Rellenar con 0s
        }
        //Update IV array
        System.arraycopy(this.IV_END, 0, this.IV, 0, this.IV_SIG_BYTES);
        //System.out.println(BitOperations.ArraytoString(this.IV));
    }

    /**
     * Encripta el input proporcionado en forma local y eficiente. Retorna bytes con HMAC | IV | CIPHERED MSG
     * @param input bytes a ser encriptados
     * @return HMAC | IV | CIPHERED MSG
     * @throws Exception excepciones de encriptación
     */
    public byte[] encrypt(byte[] input) {
        //System.out.println("FULL IV SRC: " + BitOperations.ArraytoString(this.IV));
        // Encrypt.
        try{
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(this.IV)); // Re-init porque IV cambia cada vez
            this.inputEncrypted = this.cipher.doFinal(input);
        }catch (Exception e){
            e.printStackTrace();
        }
        // MAC.
        this.fullMac = this.mac.doFinal(this.inputEncrypted); // Mac sobre cipher text
        System.arraycopy(this.fullMac, 0, this.macEnd, 0, this.MAC_SIG_BYTES);
        //System.out.println("MAC SRC: " + BitOperations.ArraytoString(this.macEnd));
        //System.out.println("IV SRC : " + BitOperations.ArraytoString(this.IV_END));
        //System.out.println("TXT SRC: " + BitOperations.ArraytoString(this.inputEncrypted));
        // Combine.
        System.arraycopy(this.macEnd, 0, this.fullMessage, 0, this.MAC_SIG_BYTES);
        System.arraycopy(this.IV_END, 0, this.fullMessage, this.MAC_SIG_BYTES, this.IV_SIG_BYTES);
        System.arraycopy(this.inputEncrypted, 0, this.fullMessage, this.MAC_SIG_BYTES + this.IV_SIG_BYTES, this.CONTENT_SIG_BYTES + 1);
        // Change IV for a clean new encryption
        this.addOneToIVEND();
        //System.out.println(BitOperations.ArraytoString(this.IV));
        // Return.
        return this.fullMessage;
    }

    public byte[] decrypt(byte[] cipheredMessage) throws Exception {
        // MAC_END.
        System.arraycopy(cipheredMessage, 0, this.macEndDecrypt, 0, this.MAC_SIG_BYTES);
        // IV_END.
        System.arraycopy(cipheredMessage, this.MAC_SIG_BYTES, this.IV_END, 0, this.IV_SIG_BYTES);
        // CIPHERED TEXT.
        System.arraycopy(cipheredMessage, this.MAC_SIG_BYTES + this.IV_SIG_BYTES, this.inputEncrypted, 0, this.CONTENT_SIG_BYTES + 1);
        //System.out.println("MAC DST: " + BitOperations.ArraytoString(macEndDecrypt));
        //System.out.println("IV DST : " + BitOperations.ArraytoString(this.IV_END));
        //System.out.println("TXT DST: " + BitOperations.ArraytoString(inputEncrypted));

        // CALCULATE AND VERIFY MAC.
        this.fullMac = this.mac.doFinal(this.inputEncrypted);
        System.arraycopy(this.fullMac, 0, this.macEnd, 0, this.MAC_SIG_BYTES);

        if (!Arrays.equals(this.macEndDecrypt, this.macEnd)){
            throw new Exception("MAC NO COINCIDE!");
        }

        // Update IV array.
        System.arraycopy(this.IV_END, 0, this.IV, 0, this.IV_SIG_BYTES);
        //System.out.println("FULL IV DST: " + BitOperations.ArraytoString(IV));

        // Decrypt.
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(IV));
            this.inputPlainText = this.cipher.doFinal(this.inputEncrypted);
        }catch (Exception e){
            e.printStackTrace();
        }
        return this.inputPlainText;
    }



}
