package Test.UnitTests;

import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption.CryptoAdmin;
import ApplicationLayer.LocalServices.WirelessService.PresentationLayer.Encryption.KeyAdmin;

import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {
    int MAC_SIG_BYTES = 6; // Estos valores son los más suceptibles a usar por el tamaño del mensaje de las Xbee
    int IV_SIG_BYTES = 12;

    // Test de inicialización de llaves externas
    @org.junit.jupiter.api.Test
    void externalKeySetupForEncryptionDecryption() throws Exception {
        // New Key
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.genNewIV();
        keyAdmin.genNewKey();
        String key = keyAdmin.getKeyAsEncodedString();  // Exported Key
        String IV = keyAdmin.getIVAsEncodedString();    // Exported IV (para IV start)

        KeyAdmin keyAdmin2 = new KeyAdmin();
        keyAdmin2.setKeyFromEncodedString(key);         // Imported Key
        keyAdmin2.setIVFromEncodedString(IV);           // Imported IV

        // Encryption/Decryption
        String plainText = "012345678901234"; // 15 Bytes;
        int CONTENT_SIG_BYTES = plainText.length();

        CryptoAdmin encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES); // keyAdmin 1
        CryptoAdmin decryptor = new CryptoAdmin(keyAdmin2.getKey(), keyAdmin2.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES); // keyAdmin 2

        int tests = 0;
        byte[] encryptedText;
        byte[] decryptedText;
        while(tests < 1000) {
            encryptedText = encryptor.encrypt(plainText.getBytes());
            assertEquals((MAC_SIG_BYTES + IV_SIG_BYTES) % 16, (encryptedText.length % 16));
            decryptedText = decryptor.decrypt(encryptedText);
            assertArrayEquals(plainText.getBytes(), decryptedText);
            tests++;
        }
    }

    // Test de stress
    @org.junit.jupiter.api.Test
    void simpleEncryptionDecryption() throws Exception {
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.genNewIV();
        keyAdmin.genNewKey();
        String plainText = "012345678901234"; // 15 Bytes;
        int CONTENT_SIG_BYTES = plainText.length();

        CryptoAdmin encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
        CryptoAdmin decryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);

        int tests = 0;
        byte[] encryptedText;
        byte[] decryptedText;
        while(tests < 10000000) {
            encryptedText = encryptor.encrypt(plainText.getBytes());
            assertEquals((MAC_SIG_BYTES + IV_SIG_BYTES) % 16, (encryptedText.length % 16));
            decryptedText = decryptor.decrypt(encryptedText);
            assertArrayEquals(plainText.getBytes(), decryptedText);
            tests++;
        }
    }

    // Test de casos borde
    @org.junit.jupiter.api.Test
    void borderIVEncryptionDecryption() throws Exception {
        int tests = 0;
        KeyAdmin keyAdmin = new KeyAdmin();
        keyAdmin.genNewKey();
        keyAdmin.genMinIV();
        String plainText = "012345678901234"; // 15 Bytes;
        int CONTENT_SIG_BYTES = plainText.length();

        CryptoAdmin encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
        CryptoAdmin decryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
        byte[] encryptedText;
        byte[] decryptedText;

        while(tests < 1000) {
            encryptedText = encryptor.encrypt(plainText.getBytes());
            assertEquals((MAC_SIG_BYTES + IV_SIG_BYTES) % 16, (encryptedText.length % 16));
            decryptedText = decryptor.decrypt(encryptedText);
            //System.out.println(new String(decryptedText));
            assertArrayEquals(plainText.getBytes(), decryptedText);
            tests++;
        }

        keyAdmin.genMaxIV();
        encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);
        decryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, CONTENT_SIG_BYTES);

        tests = 0;
        while(tests < 1000) {
            encryptedText = encryptor.encrypt(plainText.getBytes());
            assertEquals((MAC_SIG_BYTES + IV_SIG_BYTES) % 16, (encryptedText.length % 16));
            decryptedText = decryptor.decrypt(encryptedText);
            assertArrayEquals(plainText.getBytes(), decryptedText);
            tests++;
        }
    }

    // Test de escalabilidad
    @org.junit.jupiter.api.Test
    void differentPlainTextSizeEncryptionDecryption() throws Exception {
        String end = "012345678901234"; // 15 Bytes;
        String sixteen = "0123456789012345"; // 16 Bytes;
        String texto = end;

        int tests = 0;
        while(tests < 100) {
            KeyAdmin keyAdmin = new KeyAdmin();
            keyAdmin.genNewIV();
            keyAdmin.genNewKey();
            CryptoAdmin encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, texto.length());
            CryptoAdmin decryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, texto.length());

            byte[] encryptedText = encryptor.encrypt(texto.getBytes());
            assertEquals((MAC_SIG_BYTES + IV_SIG_BYTES) % 16, (encryptedText.length % 16));
            byte[] decryptedText = decryptor.decrypt(encryptedText);
            //System.out.println(new String(decryptedText));
            assertArrayEquals(texto.getBytes(), decryptedText);
            tests++;
            texto += sixteen;
        }
    }

    // Test de cambio de IV y MAC sizes && Cambiando largo de los textos encriptados
    @org.junit.jupiter.api.Test
    void differentIVMACSizeAndTextEncryptionDecryption() throws Exception {
        String end = "012345678901234"; // 15 Bytes;
        String sixteen = "0123456789012345"; // 16 Bytes;
        String texto = end;

        int MAC_SIG_BYTES;
        int IV_SIG_BYTES;
        int tests = 0;
        while(tests < 100){ // Probar 100 veces todas las combinaciones
            for (int i = 1; i <= 16 ; i++) {
                for (int j = 1; j <= 16 ; j++) {
                    //System.out.println("i: " + i + "j:" + j);
                    MAC_SIG_BYTES = i;
                    IV_SIG_BYTES = j;

                    int e = 0;
                    texto = end; // Reset del texto
                    while(e < 100) { // Para cada combinación encriptar 100 veces
                        KeyAdmin keyAdmin = new KeyAdmin();
                        keyAdmin.genNewIV();
                        keyAdmin.genNewKey();
                        CryptoAdmin encryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, texto.length());
                        CryptoAdmin decryptor = new CryptoAdmin(keyAdmin.getKey(), keyAdmin.getIV(), MAC_SIG_BYTES, IV_SIG_BYTES, texto.length());

                        byte[] encryptedText = encryptor.encrypt(texto.getBytes());
                        assertEquals((i+j) % 16, (encryptedText.length % 16));
                        byte[] decryptedText = decryptor.decrypt(encryptedText);
                        assertArrayEquals(texto.getBytes(), decryptedText);
                        e++;
                        texto += sixteen; // Y cambiar el largo del texto en cada encriptación
                    }
                }
            }
            tests++;
        }

    }
}