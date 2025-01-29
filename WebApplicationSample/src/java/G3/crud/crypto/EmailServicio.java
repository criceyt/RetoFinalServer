package G3.crud.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailServicio {

    private static final Logger LOGGER = Logger.getLogger(EmailServicio.class.getName());
    private Symmetric sym = new Symmetric();
    private String transmitter;
    private String emailkey;

    /**
     * Metodo que envia el correo al usuario
     *
     * @param receiver el usuario que recibe el correo
     * @param contrasena la contraseña generada
     * @param body el mensaje en si.
     * @param subject el asunto del correo
     */
    public void sendEmail(String receiver, String contrasena, String body, String subject) {
        try {
            // Cargar propiedades de credenciales desencriptadas
            Properties properties = new Properties();
            InputStream input = new ByteArrayInputStream(sym.decryptText("abcd*1234"));
            properties.load(input);

            transmitter = properties.getProperty("TRANSMITTER");
            emailkey = properties.getProperty("EMAILKEY");

            LOGGER.info("Credenciales obtenidas para el envio de correo.");

            if (transmitter == null || emailkey == null) {
                LOGGER.severe("Las credenciales TRANSMITTER o EMAILKEY son nulas. No se puede enviar el correo.");
                return;
            }

            // Configuración del servidor SMTP
            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.auth", "true");
            mailProperties.put("mail.smtp.starttls.enable", "true");
            mailProperties.put("mail.smtp.host", "smtp.gmail.com");
            mailProperties.put("mail.smtp.port", "587");

            // Crear sesión de correo
            Session session = Session.getInstance(mailProperties);

            // Crear el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(transmitter));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(subject);
            message.setText(body);

            // Enviar el correo
            LOGGER.info("Enviando correo a: " + receiver);
            Transport.send(message, transmitter, emailkey);
            LOGGER.info("Correo enviado correctamente a: " + receiver);

        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error al enviar el correo: {0}", e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar las propiedades: {0}", e.getMessage());
        }
    }

    /**
     * Generador de contraseñas para los usuarios.
     *
     * @return la contraseña generada para el usuario
     */
    public static StringBuilder generateRandomPassword() {
        LOGGER.info("Generando una nueva contraseña.");
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        int length = random.nextInt(4) + 9; // Longitud entre 9 y 12 caracteres

        boolean hasSymbol = false;
        boolean hasNumber = false;

        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));

            if (Character.isDigit(randomChar)) {
                hasNumber = true;
            }

            if (isSymbol(randomChar)) {
                hasSymbol = true;
            }

            password.append(randomChar);
        }

        // Garantizar que haya al menos un símbolo
        if (!hasSymbol) {
            char randomSymbol = '!'; // Agregar un símbolo por defecto
            password.setCharAt(random.nextInt(password.length()), randomSymbol);
        }

        // Garantizar que haya al menos un número
        if (!hasNumber) {
            char randomNumber = '5'; // Agregar un número por defecto
            password.setCharAt(random.nextInt(password.length()), randomNumber);
        }

        return password;
    }

    private static boolean isSymbol(char c) {
        String symbols = "!@#$^&*()-_=+";
        return symbols.indexOf(c) != -1;
    }

    public static void main(String[] args) {
        EmailServicio emailServicio = new EmailServicio();

        // Datos de prueba para el correo
        String receiver = "example@example.com";  // Correo del destinatario
        String contrasena = generateRandomPassword().toString();  // Contraseña generada
        String subject = "Bienvenido a nuestro servicio";  // Asunto del correo
        String body = "Hola,\n\nEsta es tu contraseña generada: " + contrasena;  // Contenido del correo

        // Enviar el correo
        emailServicio.sendEmail(receiver, contrasena, body, subject);
        System.out.println("Correo enviado correctamente.");
    }
}