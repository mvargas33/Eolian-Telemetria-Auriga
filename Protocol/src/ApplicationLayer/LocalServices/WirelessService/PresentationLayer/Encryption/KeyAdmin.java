package ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Administador de generación de llaves para AES y el IV de CBC.
 */
public class KeyAdmin {
    private final KeyGenerator keyGenerator;
    private final SecureRandom secureRandom;
    private SecretKey key;
    private byte[] IV;

    public KeyAdmin() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES"); // AES Hardoced
        this.secureRandom = new SecureRandom();
        this.IV = new byte[16]; // 16 bytes de tamaño
    }

    /**
     * Genera un nuevo array IV y lo guarda localmente
     */
    public void genNewIV(){
        int rounds = 0;
        while(rounds < 3) { // Only due to paranoia
            this.secureRandom.nextBytes(this.IV);
            rounds++;
        }
    }

    /**
     * Genera una nueva key AES y la guarda localmente
     */
    public void genNewKey(){
        keyGenerator.init(256, secureRandom); // KeySize: 256 bits
        this.key = keyGenerator.generateKey();
    }

    /**
     * Obtiene el IV FULL
     * @return todos los bytes de IV
     */
    public byte[] getIV(){
        return this.IV;
    }

    /**
     * Obtiene el objeto SecretKey del Administrador
     * @return SecretKey del Administrador
     */
    public SecretKey getKey(){
        return this.key;
    }

    /**
     * Retorna key como string en base 64
     * @return Key como string base 64
     */
    public String getKeyAsEncodedString(){
        return Base64.getEncoder().encodeToString(this.key.getEncoded());
    }

    /**
     * Retorna IV como string en base 64
     * @return IV como string base 64
     */
    public String getIVAsEncodedString(){
        return Base64.getEncoder().encodeToString(this.IV);
    }

    /**
     * Setea la key desde un string encodeado externo
     * @param encodedKey Llave en formato string encodeado base 64
     */
    public void setKeyFromEncodedString(String encodedKey){
        // decode the base64 encoded string
        System.out.println("Cryptography Key: " + encodedKey);
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        // rebuild key using SecretKeySpec
        try {
            this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        }catch (Exception e){
            System.out.println("Problema al incializar SecretKeySpec");
            e.printStackTrace();
        }
    }

    /**
     * Setea la key desde un string encodeado externo
     * @param encodedIV IV en formato string encodeado base 64
     */
    public void setIVFromEncodedString(String encodedIV){
        this.IV= Base64.getDecoder().decode(encodedIV);
    }


    /**
     * Asigna a IV su máximo valor, para testing de casos bordes
     */
    public void genMaxIV(){
        byte[] a = {
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111};
        System.arraycopy(a, 0, this.IV, 0, 16);
    }

    public void genMinIV(){
        byte[] a = {
                (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000,
                (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000,
                (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000,
                (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000};
        System.arraycopy(a, 0, this.IV, 0, 16);
    }

}
