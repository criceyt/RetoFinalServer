package G3.crud.crypto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Servidor {

    // Método para verificar si una cadena es un Base64 válido
    public static boolean esBase64Valido(String str) {
        return str != null && str.matches("^[A-Za-z0-9+/=]+$");
    }

    public static String desencriptarContraseña(String encryptedPassword) {
        try {
            // Verificamos si la contraseña cifrada es un Base64 válido

            // Cargar la clave privada desde un archivo
            byte[] privateKeyBytes;
            try (InputStream keyInputStream = Servidor.class.getResourceAsStream("Private.key");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                if (keyInputStream == null) {
                    throw new FileNotFoundException("No se encontró el archivo de clave privada.");
                }
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = keyInputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                privateKeyBytes = baos.toByteArray();
            }

            // Reconstruir la clave privada
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // Convertir la contraseña cifrada de Base64 a bytes
            // En el servidor
        byte[] encryptedData = javax.xml.bind.DatatypeConverter.parseBase64Binary(encryptedPassword);

            // Usar RSA/ECB/PKCS1Padding (asegúrate de que esto es consistente con el cifrado)
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedData = cipher.doFinal(encryptedData);

            // Convertir los datos descifrados a String
            return new String(decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // Ejemplo de recibir la contraseña cifrada (simulada)
        String encryptedPassword = "AcIyZv47BTEYQFWNuAywwVxhU58H2klrGhuIJcmKo8eLRkBMe44cN81zD0V72ITToZZINNciEi%2FoJJa5QbsjbYvWo0sdIVmuWEicTnxJD3jKP4qttDmeKdEYMphETk0WS4X1eV63hB7WuQjBlfW3DClS1BDEuXCZLXCCSoQFlEY%3D"; // Aquí iría la contraseña cifrada enviada desde el cliente
        String decryptedPassword = Servidor.desencriptarContraseña(encryptedPassword);
        if (decryptedPassword != null) {
            System.out.println("Contraseña desencriptada: " + decryptedPassword);
        } else {
            System.out.println("Error al desencriptar la contraseña.");
        }
    }
}
