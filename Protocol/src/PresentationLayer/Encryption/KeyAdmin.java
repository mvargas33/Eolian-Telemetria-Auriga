package PresentationLayer.Encryption;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
     * Retorna key como string
     * @return Key as string
     */
    public String getKeyAsString(){
        return this.key.toString();
    }

    /**
     * Retorna key como byte array
     * @return Key como bytearray
     */
    public byte[] getKeyAsByteArray(){
        return this.key.getEncoded();
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
