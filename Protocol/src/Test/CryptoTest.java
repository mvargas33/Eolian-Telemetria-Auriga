package Test;

import ApplicationLayer.ApplicationComponent;
import PresentationLayer.Encryption.CryptoAdmin;
import PresentationLayer.Encryption.KeyAdmin;
import org.junit.jupiter.api.BeforeEach;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {
    KeyAdmin keyAdmin;
    CryptoAdmin encryptor;
    CryptoAdmin decryptor;
    String plainText;

    @BeforeEach
    void setUp() throws Exception{
        keyAdmin = new KeyAdmin();
        keyAdmin.genNewIV();
        keyAdmin.genNewKey();

        plainText = "012345678901234"; // 15 Bytes

        int MAC_SIG_BYTES = 6;
        int IV_SIG_BYTES = 12;
        int CONTENT_SIG_BYTES = plainText.length();


        encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
        decryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
    }

    @org.junit.jupiter.api.Test
    void simpleEncryptionDecryption() throws Exception {
        byte[] encryptedText = encryptor.encrypt(plainText.getBytes());
        byte[] decryptedText = decryptor.decrypt(encryptedText);
        assertArrayEquals(plainText.getBytes(), decryptedText);
    }

    // TODO: Test de casos límites superiores e inferiores para IV
    // TODO: Probar para textos planos de distinto largo k = 16*m + 15, m entero positivo
}