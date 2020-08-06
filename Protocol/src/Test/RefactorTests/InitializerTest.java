package Test.RefactorTests;

import PresentationLayer.Encryption.CryptoAdmin;
import PresentationLayer.Encryption.KeyAdmin;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {
    int MAC_SIG_BYTES = 6; // Estos valores son los más suceptibles a usar por el tamaño del mensaje de las Xbee
    int IV_SIG_BYTES = 12;
    int CONTENT_SIG_BYTES = 16*5 + 15; // 5 bloques de 16 bytes, + 1 bloque de 15 bytes + 1 bloque de 16 bytes (MAC+IV)

    void genNewKeyAndIV() throws NoSuchAlgorithmException {
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.genNewKey();
        System.out.println("Key: " + keyAdmin.getKeyAsEncodedString());
        keyAdmin.genNewIV();
        System.out.println("IV : " + keyAdmin.getIVAsEncodedString());
    }

    CryptoAdmin setupCryptoAdmin() throws Exception {
        String encryptionKey = "uBb2BqdBtfJYyqOh5BmBX+HlqPGLz8/wdiXRgg8WnMs=";
        String IV = "eoCvPqhwOTO6FvGXGGPyhw==";
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.setKeyFromEncodedString(encryptionKey);
        keyAdmin.setIVFromEncodedString(IV);

        return new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
    }

    void senderSetup() throws Exception {
        CryptoAdmin cryptoAdmin = setupCryptoAdmin();

    }

    void receiverSetup() throws Exception {
        CryptoAdmin cryptoAdmin = setupCryptoAdmin();
    }



    @Test
    void basicTest() throws Exception {
        genNewKeyAndIV();


    }
}