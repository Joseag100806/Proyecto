package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import clases.clases;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class EditorMapaController {

    private clases.Map map;
    private int[][] objetos; // mismo tamaño que map
    private int[][] mapa;
    private int tamañoCelda = 40;


    @FXML
    private Button btnAgua;

    @FXML
    private Button btnOro;

    @FXML
    private ScrollPane scrollPein;

    @FXML
    private Button btnPasto;

    @FXML
    private Button btnPasto10;

    @FXML
    private Button btnPasto11;

    @FXML
    private Button btnPasto12;

    @FXML
    private Button btnPasto13;

    @FXML
    private Button btnPasto2;

    @FXML
    private Button btnPasto3;

    @FXML
    private Button btnPasto4;

    @FXML
    private Button btnPasto5;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnPasto6;

    @FXML
    private Button btnPasto7;

    @FXML
    private Button btnPasto8;

    @FXML
    private Button btnPasto9;

    @FXML
    private Button btnTierra;

    @FXML
    private Button btnTierra2;

    @FXML
    private Button btnTierra3;

    @FXML
    private Button btnTierra4;

    @FXML
    private Button btnTierra5;

    @FXML
    private Button btnTierra6;

    @FXML
    private Label txtName;

    @FXML
    private GridPane gridElements;

    @FXML
    private ScrollPane scrollElements;

    @FXML
    private Button btnFlor;

    @FXML
    private Button btnFlor2;

    @FXML
    private Button btnLetrero;

    @FXML
    private Button btnArbol;

    @FXML
    private Button btnGuardar;

    @FXML private GridPane gridMapa;
    @FXML private Pane miniMapaPane;

    private Canvas miniMapaCanvas;
    private char terrenoActual = 'G';
    private int objetoActual = 0;
    private double zoomFactor = 1.0;

    public void setMap(clases.Map map) {
        this.map = map;
        int filas = map.getRow();
        int columnas = map.getColumn();
        this.mapa = new int[filas][columnas];
        this.objetos = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char terreno = map.getTerreno(i, j);
                mapa[i][j] = conversion(terreno);
            }
        }
        txtName.setText(map.getName());
        drawMap();
    }
    private int conversion(char terreno) {
        return switch (terreno) {
            case 'W' -> 0;
            case 'T' -> 1;
            case 'P' -> 2;
            case 'F' -> 3;
            case 'Q' -> 4;
            case 'X' -> 5;
            case 'E' -> 6;
            case 'R' -> 7;
            case 'Z' -> 8;
            case 'Y' -> 9;
            case 'U' -> 10;
            case 'I' -> 11;
            case 'O' -> 12;
            case 'L' -> 13;
            case 'M' -> 14;
            case 'a' -> 15;
            case 'b' -> 16;
            case 'c' -> 17;
            case 'd' -> 18;
            case 'e' -> 19;
            case 'u' -> 20;
            default -> -1;
        };
    }
    private String conversion2(int objeto) {
        return switch (objeto) {
            case 1 -> "/img/oro.png";
            case 2 -> "/img/arbol.png";
            case 3 -> "/img/letrero.png";
            case 4 -> "/img/flor.png";
            case 5 -> "/img/flor2.png";
            default -> null;
        };
    }
    int historialIndex = 0;
    private void agregarAHistorial(int tipoTerreno, int tipoObjeto) {
        StackPane celdaHistorial = new StackPane();
        celdaHistorial.setPrefSize(40, 40);

        ImageView terrenoImg = new ImageView();
        terrenoImg.setFitWidth(40);
        terrenoImg.setFitHeight(40);
        actualizarImagen(terrenoImg, tipoTerreno);

        celdaHistorial.getChildren().add(terrenoImg);

        if (tipoObjeto != 0) {
            ImageView objetoImg = new ImageView();
            objetoImg.setFitWidth(25);
            objetoImg.setFitHeight(25);

        }

        int columnasPorFila = 22;
        int colHist = historialIndex % columnasPorFila; // columna actual
        int row = historialIndex / columnasPorFila;     // fila actual
        gridElements.add(celdaHistorial, colHist, row);
        historialIndex++;
        Platform.runLater(() -> scrollElements.setVvalue(0.0));
    }


    public void drawMap() {
        gridMapa.getChildren().clear();

        for (int fila = 0; fila < map.getRow(); fila++) {
            for (int col = 0; col < map.getColumn(); col++) {
                StackPane celda = crearCelda(fila, col);
                gridMapa.add(celda, col, fila);
            }
        }

        inicializarMiniMapa(map.getRow(), map.getColumn(), mapa);
    }

    private StackPane crearCelda(int fila, int col) {
        ImageView fondo = new ImageView();
        fondo.setFitWidth(tamañoCelda);
        fondo.setFitHeight(tamañoCelda);
        actualizarImagen(fondo, mapa[fila][col]);

        ImageView objeto = new ImageView();
        objeto.setFitWidth(25);
        objeto.setFitHeight(25);

        StackPane celda = new StackPane(fondo, objeto);
        celda.setStyle("-fx-border-color: lightgray;");

        celda.setOnMouseClicked(e -> {
            pintarCelda(fondo, fila, col);
            ponerObjeto(objeto, fila, col);
        });

        celda.setOnMouseDragEntered(e -> {
            pintarCelda(fondo, fila, col);
            ponerObjeto(objeto, fila, col);
        });

        return celda;
    }

    private void pintarCelda(ImageView imageView, int fila, int col) {
        int valor = switch (terrenoActual) {
            case 'W' -> 0;
            case 'T' -> 1;
            case 'P' -> 2;
            case 'F' -> 3;
            case 'Q' -> 4;
            case 'X' -> 5;
            case 'E' -> 6;
            case 'R' -> 7;
            case 'Z' -> 8;
            case 'Y' -> 9;
            case 'U' -> 10;
            case 'I' -> 11;
            case 'O' -> 12;
            case 'L' -> 13;
            case 'M' -> 14;
            case 'a' -> 15;
            case 'b' -> 16;
            case 'c' -> 17;
            case 'd' -> 18;
            case 'e' -> 19;
            case 'u' -> 20;
            default -> -1;
        };

        if (valor != -1) {
            mapa[fila][col] = valor;
            actualizarImagen(imageView, valor);
            agregarAHistorial(mapa[fila][col], terrenoActual);
            agregarAHistorial(mapa[fila][col], objetoActual);
            actualizarMiniMapa();
        } else if(valor == 20){
            actualizarImagen(imageView, valor);
            agregarAHistorial(mapa[fila][col], terrenoActual);
            agregarAHistorial(mapa[fila][col], objetoActual);
            actualizarMiniMapa();
        }
    }

    private void actualizarImagen(ImageView imageView, int valor) {
        String ruta = switch (valor) {
            case 0 -> "/img/agua.png";
            case 1 -> "/img/tierra.png";
            case 2 -> "/img/pasto.png";
            case 3 -> "/img/pasto2.png";
            case 4 -> "/img/pasto3.png";
            case 5 -> "/img/pasto4.png";
            case 6 -> "/img/pasto5.png";
            case 7 -> "/img/pasto6.png";
            case 8 -> "/img/pasto7.png";
            case 9 -> "/img/pasto8.png";
            case 10 -> "/img/pasto9.png";
            case 11 -> "/img/pasto10.png";
            case 12 -> "/img/pasto11.png";
            case 13 -> "/img/pasto12.png";
            case 14 -> "/img/pasto13.png";
            case 15 -> "/img/tierra2.png";
            case 16 -> "/img/tierra3.png";
            case 17 -> "/img/tierra4.png";
            case 18 -> "/img/tierra5.png";
            case 19 -> "/img/tierra6.png";
            case 20 -> "/img/gris2.png";
            case -1 -> "/img/gris.png";
            default -> "";
        };

        Image imagen = new Image(getClass().getResourceAsStream(ruta));
        imageView.setImage(imagen);
    }

    private void ponerObjeto(ImageView objetoView, int fila, int col) {
        if (mapa[fila][col] == -1 || mapa[fila][col] == 0) {
            return;
        }
        if (
                mapa[fila][col] == 3
                || mapa[fila][col] == 4
                ||mapa[fila][col] == 5
                ||mapa[fila][col] == 6
                ||mapa[fila][col] == 7
                || mapa[fila][col] == 8
                ||mapa[fila][col] == 9
                ||mapa[fila][col] == 10
                ||mapa[fila][col] == 11
                ||mapa[fila][col] == 12
                ||mapa[fila][col] == 13
                ||mapa[fila][col] == 14
                ||mapa[fila][col] == 16
                ||mapa[fila][col] == 17
                ||mapa[fila][col] == 18
                ||mapa[fila][col] == 19
        ) {
            return;
        }
        objetos[fila][col] = objetoActual;
        String ruta = switch (objetoActual) {
            case 1 -> "/img/oro.png";
            case 2 -> "/img/arbol.png";
            case 3 -> "/img/letrero.png";
            case 4 -> "/img/flor.png";
            case 5 -> "/img/flor2.png";
            default -> null;
        };
        if (ruta != null) {
            Image imagen = new Image(getClass().getResourceAsStream(ruta));
            objetoView.setImage(imagen);
            agregarAHistorial(mapa[fila][col], objetoActual);
        } else {
            objetoView.setImage(null);
        }
        actualizarMiniMapa();
    }

    private void inicializarMiniMapa(int filas, int columnas, int[][] mapaDatos) {
        int tileSize = 20;
        if (map.getColumn() > 40) {
            tileSize = 2;
        } else if (map.getColumn() > 30) {
            tileSize = 5;
        } else if (map.getColumn() > 15) {
            tileSize = 10;
        }

        int tileObject = (int)(tileSize * 0.6);

        miniMapaCanvas = new Canvas(columnas * tileSize, filas * tileSize);
        GraphicsContext gc = miniMapaCanvas.getGraphicsContext2D();

        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                // Dibuja el terreno
                String rutaTerreno = switch (mapaDatos[y][x]) {
                    case 0 -> "/img/agua.png";
                    case 1 -> "/img/tierra.png";
                    case 2 -> "/img/pasto.png";
                    case 3 -> "/img/pasto2.png";
                    case 4 -> "/img/pasto3.png";
                    case 5 -> "/img/pasto4.png";
                    case 6 -> "/img/pasto5.png";
                    case 7 -> "/img/pasto6.png";
                    case 8 -> "/img/pasto7.png";
                    case 9 -> "/img/pasto8.png";
                    case 10 -> "/img/pasto9.png";
                    case 11 -> "/img/pasto10.png";
                    case 12 -> "/img/pasto11.png";
                    case 13 -> "/img/pasto12.png";
                    case 14 -> "/img/pasto13.png";
                    case 15 -> "/img/tierra2.png";
                    case 16 -> "/img/tierra3.png";
                    case 17 -> "/img/tierra4.png";
                    case 18 -> "/img/tierra5.png";
                    case 19 -> "/img/tierra6.png";
                    case 20 -> "/img/gris2.png";
                    case -1 -> "/img/gris.png";
                    default -> "";
                };
                Image imgTerreno = new Image(getClass().getResourceAsStream(rutaTerreno));
                gc.drawImage(imgTerreno, x * tileSize, y * tileSize, tileSize, tileSize);
                String rutaObjeto = switch (objetos[y][x]) {
                    case 1 -> "/img/oro.png";
                    case 2 -> "/img/arbol.png";
                    case 3 -> "/img/letrero.png";
                    case 4 -> "/img/flor.png";
                    case 5 -> "/img/flor2.png";
                    default -> null;
                };
                if (rutaObjeto != null) {
                    Image imgObjeto = new Image(getClass().getResourceAsStream(rutaObjeto));
                    double offset = (tileSize - tileObject) / 2.0;
                    gc.drawImage(imgObjeto, x * tileSize + offset, y * tileSize + offset, tileObject, tileObject);
                }
            }
        }

        miniMapaPane.getChildren().clear();
        miniMapaPane.getChildren().add(miniMapaCanvas);
    }



    private void actualizarMiniMapa() {
        inicializarMiniMapa(map.getRow(), map.getColumn(), mapa);
    }

    public void zoomIn() {
        zoomFactor += 0.1;
        gridMapa.setScaleX(zoomFactor);
        gridMapa.setScaleY(zoomFactor);
    }

    public void zoomOut() {
        zoomFactor = Math.max(0.1, zoomFactor - 0.1);
        gridMapa.setScaleX(zoomFactor);
        gridMapa.setScaleY(zoomFactor);
    }


    public void guardarMapa() {

        try (PrintWriter writer = new PrintWriter(map.getName())) {
            for (int i = 0; i < map.getRow(); i++) {
                for (int j = 0; j < map.getColumn(); j++) {
                    int valor = mapa[i][j];
                    int valorO = objetos[i][j];
                    char terreno = switch (valor) {
                        case 0 -> 'W';
                        case 1 -> 'T';
                        case 2 -> 'P';
                        case 3 -> 'F';
                        case 4 -> 'Q';
                        case 5 -> 'X';
                        case 6 -> 'E';
                        case 7 -> 'R';
                        case 8 -> 'Z';
                        case 9 -> 'Y';
                        case 10 -> 'U';
                        case 11 -> 'I';
                        case 12 -> 'O';
                        case 13 -> 'L';
                        case 14 -> 'M';
                        case 15 -> 'a';
                        case 16 -> 'b';
                        case 17 -> 'c';
                        case 18 -> 'd';
                        case 19 -> 'e';
                        case 20 -> 'u';
                        case -1 -> 'G';
                        default -> 'G';
                    };

                    writer.print(terreno);
                    writer.print(valorO);
                }
                writer.println();
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Guardado");
            alert.setHeaderText(null);
            alert.setContentText("Ha guardado su mapa.");
            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al guardar el mapa.");
            alert.showAndWait();
        }
    }


    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            scrollPein.setVvalue(0.0);
            scrollPein.setHvalue(0.0);
        });

        btnAgua.setOnAction(e -> {
            terrenoActual = 'W';
            objetoActual = 0;
        });

        btnPasto.setOnAction(e -> {
            terrenoActual = 'P';
            objetoActual = 0;
        });
        btnPasto2.setOnAction(e -> {
            terrenoActual = 'F';
            objetoActual = 0;
        });

        btnPasto3.setOnAction(e -> {
            terrenoActual = 'Q';
            objetoActual = 0;
        });

        btnPasto4.setOnAction(e -> {
            terrenoActual = 'X';
            objetoActual = 0;
        });

        btnPasto5.setOnAction(e -> {
            terrenoActual = 'E';
            objetoActual = 0;
        });

        btnPasto6.setOnAction(e -> {
            terrenoActual = 'R';
            objetoActual = 0;
        });

        btnPasto7.setOnAction(e -> {
            terrenoActual = 'Z';
            objetoActual = 0;
        });

        btnPasto8.setOnAction(e -> {
            terrenoActual = 'Y';
            objetoActual = 0;
        });

        btnPasto9.setOnAction(e -> {
            terrenoActual = 'U';
            objetoActual = 0;
        });

        btnTierra.setOnAction(e -> {
            terrenoActual = 'T';
            objetoActual = 0;
        });

        btnTierra2.setOnAction(e -> {
            terrenoActual = 'a';
            objetoActual = 0;
        });

        btnTierra3.setOnAction(e -> {
            terrenoActual = 'b';
            objetoActual = 0;
        });

        btnTierra4.setOnAction(e -> {
            terrenoActual = 'c';
            objetoActual = 0;
        });

        btnTierra5.setOnAction(e -> {
            terrenoActual = 'd';
            objetoActual = 0;
        });

        btnTierra6.setOnAction(e -> {
            terrenoActual = 'e';
            objetoActual = 0;
        });

        btnDelete.setOnAction(e ->{
           terrenoActual = 'u';
            objetoActual = 0;
        });

        btnOro.setOnAction(e -> {
            terrenoActual = 'G';
            objetoActual = 1;
        });

        btnArbol.setOnAction(e -> {
            terrenoActual = 'G';
            objetoActual = 2;
        });

        btnLetrero.setOnAction(e -> {
            terrenoActual = 'G';
            objetoActual = 3;
        });

        btnFlor.setOnAction(e -> {
            terrenoActual = 'G';
            objetoActual = 4;
        });

        btnFlor2.setOnAction(e -> {
            terrenoActual = 'G';
            objetoActual = 5;
        });
    }
}
