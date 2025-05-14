package controllers;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import clases.clases;
import java.io.File;
import java.io.IOException;


public class SizeMap {
    @FXML


    public void Cargar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de mapa");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo", "*"));
        File archivo = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (archivo != null) {
            try {
                List<String> lineas = java.nio.file.Files.readAllLines(archivo.toPath());
                int filas = lineas.size();
                int columnas = lineas.get(0).length();
                clases.Map mapa = new clases.Map(filas, columnas, archivo.getName());
                for (int i = 0; i < filas; i++) {
                    String linea = lineas.get(i);
                    for (int j = 0; j < columnas; j++) {
                        char terreno = linea.charAt(j);
                        int objeto = Integer.parseInt(linea);
                        mapa.setTerreno(i, j, terreno);
                        mapa.setObjeto(i, j, objeto);

                    }
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
                Parent root = loader.load();

                // Obtener controlador y pasarle el mapa
                EditorMapaController controller = loader.getController();
                controller.setMap(mapa);

                Stage stage = new Stage();
                stage.setTitle(mapa.getName());
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/icon.png")));
                stage.show();

                // Cerrar ventana actual
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void abrirFormulario(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/juan.jpg")));
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
