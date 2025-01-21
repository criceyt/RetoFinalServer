
package G3.crud.crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class Symmetric {

    static String sSalt = "abcdefghijklmnop";
    private static byte[] salt = sSalt.getBytes();
    private static final Logger LOGGER = Logger.getLogger("Symmetric.class");

    /**
     * @param nKey la clave necesaria
     * @param data la informacion a encriptar.
     */
    public void encryptText(String nKey, String data) {
        LOGGER.info("encriptando..");
        String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {
            keySpec = new PBEKeySpec(nKey.toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encodedMessage = c.doFinal(data.getBytes());
            byte[] iv = c.getIV();
            fileWriter("./src/java/files/simetryc_RSA_Private.key", iv);
            fileWriter("./src/java/files/emailCredentials.properties", encodedMessage);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * @param nKey la clave necesaria para descifrar el archivo
     * @return las credenciales ya descriptadas
     */
    public byte[] decryptText(String nKey) {
        LOGGER.info("Decrypting the file.");
        byte[] decodedMessage = null;
        byte[] contentIv = fileReader(Symmetric.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/files/simetryc_RSA_Private.key");
        byte[] contentCredentials = fileReader(Symmetric.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/files/emailCredentials.properties");
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {
            keySpec = new PBEKeySpec(nKey.toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(contentIv, 0, contentIv.length));
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
            decodedMessage = cipher.doFinal(Arrays.copyOfRange(contentCredentials, 0, contentCredentials.length));
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            LOGGER.severe(e.getMessage());
        }
        return decodedMessage;
    }

    /**
     * @param path el path para escribir
     * @param text el texto a introducir
     */
    private void fileWriter(String path, byte[] text) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(text);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * @param path El path de donde esta
     * @return los bytes del archivo
     */
    private byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return ret;
    }

    public static void main(String[] args) {
        Symmetric sym = new Symmetric();
        sym.encryptText("abcd*1234", "TRANSMITTER=concesionario163@gmail.com"
                + "\nEMAILKEY=jbpm budf wvpw yqlf");
        LOGGER.info("clave simetrica generada!");
    }
}