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
        if (!esBase64Valido(encryptedPassword)) {
            throw new IllegalArgumentException("La cadena no es Base64 válida.");
        }

        // Cargar la clave privada desde un archivo
        byte[] privateKeyBytes;
        try (InputStream keyInputStream = Servidor.class.getResourceAsStream("Private.key");
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (keyInputStream == null) {
                throw new FileNotFoundException("No se encontró el archivo de clave privada.");
            }
            byte[] buffer = new byte[2048];
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
        byte[] encryptedData = Base64.getDecoder().decode(encryptedPassword);

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
        String encryptedPassword = "HsnK+6qrqxPYQINegQlR6cKwfMf2UqUFrerU9h41lCiGKAygu1QpV7/Qh6tDb4fV/uT0cUSdBp4wdJV6QXklL17T5QzEf+6elm0RHABB2UE8OAutyK8g7gqe6NmPMAv1BimRXaJHCIVrJcNarDB9gf+ltMEvH7hrOzgZj2L/0clzC8Dw7q6icfXh7LrIAgBnCppJ5A0vTJ/GrxzPeZkSDyp2EG0rB84jcnTTmhx/TN4iMxa5TmB/+mJ3o/JgfI3oxvsDWzGfzFXipi3hGTGtD5Zv79s0jePzVxRvsgp9JjWrvO1Q5HG6KDOZGvYuZDAmoy9M3oRa4sOmR3ZKiofuQQ=="; // Aquí iría la contraseña cifrada enviada desde el cliente
        String decryptedPassword = Servidor.desencriptarContraseña(encryptedPassword);
        if (decryptedPassword != null) {
            System.out.println("Contraseña desencriptada: " + decryptedPassword);
        } else {
            System.out.println("Error al desencriptar la contraseña.");
        }
    }
}