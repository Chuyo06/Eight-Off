package com.pr4.EightOfFGUI;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;

import java.util.ArrayList;

import com.pr4.DeckOfCards.CartaInglesa;
import com.pr4.EightOff.EightOffGame;
import com.pr4.Estructuras.ListaSimple;
import javafx.geometry.Pos;
import javafx.scene.control.ListView; 
import javafx.collections.FXCollections;

/*
 * Clase que es la vista general del tablero del solitario.
 */
public class TableroView extends BorderPane {

    private EightOffGame game; //Instancia del juego
    private ControladorGUI controlador; //Controlador
    private HBox filaSuperior; // fila superior donde esta el mazo, waste y foundattion
    private HBox filaInferior; //Tablero con 7 columnas
    private VBox filaIzquierda; //Foundation
    private Label labelPista; //label para mostrar la pista

    private Button pistaBoton ;
    private Button redoBoton ;
    private Button undoBoton ;
    private Button aplicarBoton ;

    private Label labelModoHistorial;
    private ListView<String> visorHistorial;
    /*
     * Constructor que recibe el juego y el controlador, ademas
     * que inicializa el tbalero con sus contendedores.
     */
    public TableroView(EightOffGame game, ControladorGUI controlador) {
        this.game = game;
        this.controlador = controlador;

        setPadding(new Insets(10)); //margenes del tablero

        //Fila izquierda
        filaIzquierda = new VBox(20);
        filaIzquierda.setPadding(new Insets(10)); 
        filaIzquierda.setAlignment(Pos.TOP_CENTER);
        renderFilaIzquierda(); // Dibuja la fundacion

        // Fila superior
        filaSuperior = new HBox(20); //espacio entre los campos
        filaSuperior.setPadding(new Insets(10));
        renderFilaSuperior(); //dibuja la fila superior

        // Fila inferior
        filaInferior = new HBox(20);//espacio entre los campos
        filaInferior.setAlignment(Pos.TOP_CENTER); 
        renderTableau();//dibuja el tablero

        //Titulo del tableau
        VBox tableauGroup = new VBox(5); 
        tableauGroup.setAlignment(Pos.TOP_CENTER);
        
        Label tableauTitulo = new Label("Columnas");
        tableauTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        tableauGroup.getChildren().addAll(tableauTitulo, filaInferior);

        // Colocar todo en el borderPane
        setTop(filaSuperior);
        setCenter(tableauGroup);
        setLeft(filaIzquierda); 

        labelModoHistorial = new Label("MODO HISTORIAL");
        labelModoHistorial.setMaxWidth(Double.MAX_VALUE); 
        labelModoHistorial.setAlignment(Pos.CENTER); 
        labelModoHistorial.setStyle(
            "-fx-font-size: 20px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: black; " +
            "-fx-background-color: rgba(230, 245, 5); " + //fondo amarillo
            "-fx-padding: 5 10; " +
            "-fx-background-radius: 10;"
        );
        labelModoHistorial.setVisible(false);

        setBottom(labelModoHistorial);
        BorderPane.setAlignment(labelModoHistorial, Pos.CENTER);
        BorderPane.setMargin(labelModoHistorial, new Insets(10));

        VBox historialGroup = new VBox(5);
        historialGroup.setAlignment(Pos.TOP_CENTER);
        historialGroup.setPadding(new Insets(10));

        Label historialTitulo = new Label("Historial");
        historialTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");


        visorHistorial = new ListView<>();
        visorHistorial.setPrefHeight(400); 
        visorHistorial.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    
                    String style;
                    if (isSelected()) {
                        
                        style = "-fx-background-color: #F07DFA; " + // Fondo azul
                                "-fx-background-radius: 10; " +     // Redondeado
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 3 5;";
                    } else {
                        style = "-fx-background-color: rgba(0, 0, 0, 0.3); " + // Fondo oscuro
                                "-fx-background-radius: 10; " +               // Redondeado
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 3 5;";
                    }
                    
                    setStyle(style);
                }
            }
        });

        
        
        visorHistorial.setStyle(
            "-fx-border-color: #AAAAAA; " +     
            "-fx-border-width: 2; " +
            "-fx-border-radius: 15; " +        
            "-fx-background-radius: 15; " +
            "-fx-background-color: transparent; " // ¡Para quitar el fondo blanco!
        );      

        
        
        
        historialGroup.getChildren().addAll(historialTitulo, visorHistorial);
        
        setRight(historialGroup);

        //Se pone fondo al tablero
        setBackground(new Background(
                new BackgroundImage(
                        new Image("file:demo/img/Large/fondo.jpg"), // Revisa esta ruta!!
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                100, 100, true, true, false, false 
                        ))));
    }

    private void renderFilaIzquierda()
    {
        filaIzquierda.getChildren().clear(); //limpiar

        //Fundacion
        Label titulo = new Label("Fundación");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        GridPane fundacionesGrid = new GridPane();
        fundacionesGrid.setHgap(50); 
        fundacionesGrid.setVgap(10); 

        for (int i = 0; i < 4; i++) {
            StackPane cuadro = new StackPane();
            cuadro.setPrefSize(80, 120);
            cuadro.setStyle(
                    "-fx-border-color: black; " +
                            "-fx-border-width: 2; " +
                            "-fx-background-color: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-radius: 15;");

            ListaSimple<CartaInglesa> fundacion = game.getFoundations()[i];
            if (!fundacion.estaVacia()) {
                CartaInglesa carta = fundacion.obtenerUltimo();
                CartaView cartaView = new CartaView(carta);
                cuadro.getChildren().add(cartaView); 
            }

            //Guardar el indice para el controlador
            final int fundacionIdx = i; 
            cuadro.setOnMouseClicked(e -> {
                controlador.clickFundacion(fundacionIdx);
            });
            
            //Calcular para el gridPane 2 x 2
            int fila = i / 2;
            int col = i % 2;
            fundacionesGrid.add(cuadro, col, fila);
        }   
        
        GridPane.setMargin(fundacionesGrid, new Insets(0, 0, 0, 60)); 

        filaIzquierda.getChildren().addAll(titulo, fundacionesGrid);
    }

    /*
     * Dibuja la fila superior
     */
    private void renderFilaSuperior() {
        filaSuperior.getChildren().clear();

        // campos libres
        VBox camposGroup = new VBox(5); 
        camposGroup.setAlignment(Pos.CENTER);
        Label camposTitulo = new Label("Campos Libres");
        camposTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        camposGroup.getChildren().add(camposTitulo);
        HBox camposBox = new HBox(10); 
        for(int i = 0; i < 8; i++) {
            //Campos con numeros
            VBox campoConNumero = new VBox(5); 
            campoConNumero.setAlignment(Pos.CENTER);
            Label numLabel = new Label("" + (i + 1));
            numLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
            campoConNumero.getChildren().add(numLabel); 
            StackPane campoPlaceholder = new StackPane();
            campoPlaceholder.setPrefSize(80, 120);
            campoPlaceholder.setStyle(
                    "-fx-border-color: gray; " +
                    "-fx-border-width: 2; " +
                    "-fx-background-color: rgba(255, 255, 255, 0.3); " +
                    "-fx-background-radius: 15; " +
                    "-fx-border-radius: 15;");
            ListaSimple<CartaInglesa> campo = game.getCampos()[i];
            if(!campo.estaVacia()) {
                CartaInglesa carta = campo.obtenerUltimo();
                CartaView cartaView = new CartaView(carta);
                campoPlaceholder.getChildren().add(cartaView);
            }
            //Se guarda el index del campo para el controlador
            final int campoIdx = i;
            campoPlaceholder.setOnMouseClicked(e -> {
                controlador.clickCampoLibre(campoIdx);
            });
            campoConNumero.getChildren().add(campoPlaceholder); 
            camposBox.getChildren().add(campoConNumero); 
        }
        camposGroup.getChildren().add(camposBox); 

        
        // Grupo de botones
        VBox buttonBox = new VBox(10); 
       
        pistaBoton = new Button("Pista");
        pistaBoton.setOnAction(e -> controlador.darPista());
        pistaBoton.setStyle("-fx-background-color: #FFFF08; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 5 10;");

        undoBoton = new Button("Deshacer");
        undoBoton.setOnAction(e -> controlador.deshacerMovimiento());
        undoBoton.setStyle("-fx-background-color: #DB0000; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 5 10;");

        redoBoton = new Button("Rehacer");
        redoBoton.setOnAction(e -> controlador.rehacerMovimiento());
        redoBoton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 5 10;");

        aplicarBoton = new Button("Aplicar Estado");
        aplicarBoton.setOnAction(e -> controlador.aplicarEstadoHistorial());
        aplicarBoton.setStyle("-fx-background-color: #0E9403; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 5 10;");
        aplicarBoton.setMaxWidth(Double.MAX_VALUE);

        buttonBox.getChildren().addAll(pistaBoton , undoBoton , redoBoton ,aplicarBoton);
        HBox.setMargin(buttonBox, new Insets(60, 0, 0, 0)); 

        
        // Pistas
        VBox pistaGroup = new VBox(5); 
        pistaGroup.setAlignment(Pos.CENTER_LEFT);

        Label pistaTitulo = new Label("Pista");
        pistaTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        pistaGroup.getChildren().add(pistaTitulo);

        labelPista = new Label(""); // Empieza vacio
        
        labelPista.setStyle(
            "-fx-text-fill: white; " + 
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold;"
        );
        pistaGroup.getChildren().add(labelPista);
        HBox.setMargin(pistaGroup, new Insets(60, 0, 0, 20)); // Margen (Arriba, Derecha, Abajo, Izquierda)
        filaSuperior.getChildren().addAll(camposGroup, buttonBox, pistaGroup);
    }

    /**
     * Muestra el texto de la pista en el Label de la fila superior.
     * Ahora también añade y quita el borde.
     */
    public void mostrarTextoPista(String texto) {
        if (labelPista != null) {
            
            String estiloVacio = "-fx-text-fill: white; " + 
                                 "-fx-font-size: 14px; " +
                                 "-fx-font-weight: bold;";
            
            String estiloPista = estiloVacio +
                                 " -fx-background-color: rgba(0,0,0,0.2);" +
                                 " -fx-border-color: white; " +
                                 " -fx-border-width: 2; " +
                                 " -fx-padding: 5 10; " +
                                 " -fx-border-radius: 5;";

            if (texto == null || texto.isEmpty()) {
                labelPista.setStyle(estiloVacio);
                labelPista.setText(""); 
            } else {
                labelPista.setStyle(estiloPista);
                labelPista.setText(texto);
            }
        }
    }

    /*
     * Dibuja el tablero
     */
    private void renderTableau() {
        filaInferior.getChildren().clear();

        // Siempre son 8 columnas
        for (int i = 0; i < 8; i++) {
            
            VBox columnaConNumero = new VBox(5); 
            columnaConNumero.setAlignment(Pos.TOP_CENTER);
            
            Label numLabel = new Label("" + (i + 1));
            numLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
            columnaConNumero.getChildren().add(numLabel); 
            VBox pilaDeCartas = new VBox();
            pilaDeCartas.setSpacing(-75);//espacio entre las cartas del tablero

            ListaSimple<CartaInglesa> deck = game.getTableau()[i];   

            final ListaSimple<CartaInglesa> deckFinal = deck;
            //se guarda el idex de lacolumna pra el controlador
            final int columnaIdx = i; 

            if (deckFinal != null && !deckFinal.estaVacia()) {
                ListaSimple<CartaInglesa> cartasCopia = new ListaSimple<>(deckFinal);
                ListaSimple<CartaInglesa> temp = new ListaSimple<>();

                while (!cartasCopia.estaVacia()) {
                    temp.insertarFin(cartasCopia.eliminarFin());
                }
                while (!temp.estaVacia()) {
                    CartaInglesa carta = temp.eliminarFin();
                    CartaView cartaView = new CartaView(carta);
                    cartaView.setOnMouseClicked(e -> {
                        controlador.clickTableau(columnaIdx, carta);
                    });
                    pilaDeCartas.getChildren().add(cartaView); 
                }
            } else {
                StackPane espacio = new StackPane();
                espacio.setPrefSize(80, 120);
                espacio.setStyle("-fx-border-color: gray; -fx-border-width: 2; -fx-background-color: transparent;");
                espacio.setOnMouseClicked(e -> {
                    controlador.clickTableauVacio(columnaIdx);
                });
                pilaDeCartas.getChildren().add(espacio); 
            }

            columnaConNumero.getChildren().add(pilaDeCartas); 
            
            filaInferior.getChildren().add(columnaConNumero); 
       }
   }

    /*
     * Refresca el tablero despues de cada movimiento
     * Dibuja todo el tablero
     */
    public void refrescarTablero() {
        renderFilaIzquierda();
        renderFilaSuperior();
        renderTableau();
    }

    /**
     * Se muestran los botones o no, si se esta en modo histral
     * @param puedeUndo true si debe estar activo
     * @param puedeRedo true si debe estar activo
     * @param enModoHistorial true si debe estar activo
     */
    public void actualizarBotones(boolean puedeUndo, boolean puedeRedo, boolean enModoHistorial) {
        //Desactivar y activar
        if (undoBoton != null) {
            undoBoton.setDisable(!puedeUndo); // desactivar si no se puede undo
        }
        if (redoBoton != null) {
            redoBoton.setDisable(!puedeRedo); // desactivar si no se puede redo
        }
        if (aplicarBoton != null) {
            aplicarBoton.setDisable(!enModoHistorial); // desactivar si no estamos en modo histroial
        }
        
        if (pistaBoton != null) {
            pistaBoton.setDisable(enModoHistorial); // desactivar si estamos en modo histroial
        }

        if (labelModoHistorial != null) {
            labelModoHistorial.setVisible(enModoHistorial);
        }
    }

    /**
     * Meotod que actuliza el listView con los movimeintos que haga el usuario
     */
    public void actualizarVisorHistorial(ArrayList<String> movimientos, int indiceActual) {
        if (visorHistorial != null) {
            //Se llenan la lisat con los movimeinto que envia desde el controaldor
            visorHistorial.setItems(FXCollections.observableArrayList(movimientos));
            
            // Visualente se selecciona el movimeinto actual en almlista
            //o cuando se recorre en el modo historial
            if (indiceActual >= 0) {
                visorHistorial.getSelectionModel().select(indiceActual);
                visorHistorial.scrollTo(indiceActual);
            } else {
                visorHistorial.getSelectionModel().clearSelection();
            }
        }
    }

}