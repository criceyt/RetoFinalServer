/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package G3.crud.entities;

/**
 *
 * @author 2dam
 */
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        String rutaImagen = "C:\\Users\\2dam\\Documentos\\HondaCivicTypeR.jpg";
        File file = new File(rutaImagen);

        if (file.exists()) {
            try {
                byte[] imageBytes = convertImageToBinary(file);

                // Insertar la imagen en la base de datos
                insertImageIntoDatabase(imageBytes);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("El archivo no existe: " + rutaImagen);
        }
    }

    private byte[] convertImageToBinary(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }

    private void insertImageIntoDatabase(byte[] imageBytes) throws SQLException {
        String url = "jdbc:mysql://tu_host/tu_base_de_datos";
        String user = "tu_usuario";
        String password = "tu_contraseña";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO vehiculo (marca, modelo, color, potencia, km, precio, fechaAlta, tipoVehiculo, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, "Toyota");
            pstmt.setString(2, "Corolla");
            pstmt.setString(3, "Rojo");
            pstmt.setInt(4, 150);
            pstmt.setInt(5, 120000);
            pstmt.setInt(6, 15000);
            pstmt.setDate(7, java.sql.Date.valueOf("2025-01-23"));
            pstmt.setString(8, "COCHE");
            pstmt.setBytes(9, imageBytes);

            pstmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
