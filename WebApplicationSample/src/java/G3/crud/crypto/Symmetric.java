package G3.crud.crypto;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
    private byte[] readInputStream(InputStream inputStream) throws IOException  {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[1024]; // Buffer de 1 KB
    int nRead;
    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
    }
    buffer.flush();
    return buffer.toByteArray(); // Retorna el contenido como un arreglo de bytes
}
    /**
     * @param nKey la clave necesaria para descifrar el archivo
     * @return las credenciales ya descriptadas
     */
    public byte[] decryptText(String nKey) throws IOException {
    LOGGER.info("Decrypting the file.");
    byte[] decodedMessage = null;

    // Imprimir el directorio de trabajo
    System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));

    // Leer el IV y las credenciales usando ClassLoader
    try (InputStream ivStream = getClass().getClassLoader().getResourceAsStream("G3/crud/crypto/simetryc_RSA_Private.key");
         InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("G3/crud/crypto/emailCredentials.properties")) {

        if (ivStream == null) {
            LOGGER.severe("El archivo de clave privada no se encontró en el classpath.");
            return null; // Manejar el error adecuadamente
        }
        byte[] contentIv = readInputStream(ivStream); // Método para leer InputStream

        if (credentialsStream == null) {
            LOGGER.severe("El archivo de credenciales no se encontró en el classpath.");
            return null; // Manejar el error adecuadamente
        }
        byte[] contentCredentials = readInputStream(credentialsStream); // Método para leer InputStream

        // Verificar que el IV no sea nulo y tenga la longitud correcta
        if (contentIv == null || contentIv.length != 16) {
            LOGGER.severe("El IV es nulo o no tiene la longitud correcta (debe ser 16 bytes).");
            return null; // Manejar el error adecuadamente
        }

        KeySpec keySpec;
        SecretKeyFactory secretKeyFactory;
        try {
            // Generar la clave a partir de la contraseña
            keySpec = new PBEKeySpec(nKey.toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, "AES");

            // Inicializar el cifrador
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParam = new IvParameterSpec(contentIv); // Usar el IV leído
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);

            // Desencriptar el mensaje
            decodedMessage = cipher.doFinal(contentCredentials);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
                | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException
                | NoSuchPaddingException e) {
            LOGGER.severe("Error durante el descifrado: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace para más detalles
        }

    return decodedMessage; // Retornar el mensaje desencriptado o null si hubo un error
}

    /**
     * @param path el path para escribir
     * @param text el texto a introducir
     *//*
    private void fileWriter(String path, byte[] text) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(text);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }*/


    /**
     * @param path El path de donde esta
     * @return los bytes del archivo
     */
    }

    public static void main(String[] args) {
        Symmetric sym = new Symmetric();
        sym.encryptText("abcd*1234", "TRANSMITTER=concesionario163@gmail.com"
                + "\nEMAILKEY=jbpm budf wvpw yqlf");
        LOGGER.info("clave simetrica generada!");
    }

    public static void fileWriter(String filePath, byte[] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(new String(data)); // Convertir bytes a String y escribir en el archivo
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}
