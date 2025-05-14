package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import clases.clases;
import java.io.IOException;


public class FormMap {

    @FXML
    private TextField txtColumn;

    @FXML
    private TextField txtRow;

    @FXML
    private TextField fielName;


    public void rescueInformation (ActionEvent event){
        try {
            int rows = Integer.parseInt(txtRow.getText());
            int columns = Integer.parseInt(txtColumn.getText());
            String name = fielName.getText();


            if (rows > 30 || columns > 30){
                mostrarAlerta("El número no puede ser mayor a 60.");
            }  else if (rows < 10 || columns < 10){
                mostrarAlerta("El número no puede ser menor a 10.");
            } else if(name == ""){
                mostrarAlerta("Porfavor, ingresar nombre");
            }
            else {
                clases.Map newMap = new clases.Map(columns, rows, name);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
                    Parent root = loader.load();
                    EditorMapaController controller = loader.getController();
                    controller.setMap(newMap);
                    Stage stage = new Stage();
                    stage.setTitle(name);
                    stage.setScene(new Scene(root));
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/icon.png")));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();

                } catch (IOException e) {

                    System.err.println("Error al cargar el archivo FXML: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e){
            mostrarAlerta("Por favor, ingresa un número válido.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Entrada inválida");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
