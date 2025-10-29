    package com.pr4.EightOfFGUI;

    import com.pr4.EightOff.EightOffGame;

import javafx.application.Application;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    /*
    * Clase principal que inicializa la aplicacion grafica
    */
    public class EightOffApp extends Application {

        /*
        * Metodo de JacFX par ainciar la aplicacion
        */
        @Override
        public void start(Stage primaryStage) {
            //Crea el modlo del juego
            EightOffGame game = new EightOffGame();
            //Crea el controlador conectada a la logica con lo grafico
            ControladorGUI controlador = new ControladorGUI(game);
            //crea el tablero, recibiendo el juego y el controlador
            TableroView tablero = new TableroView(game, controlador);
            //Se relaciona lo grafico con el controlador
            controlador.setView(tablero);

            //Se crea la escena pricniopal del tablero con un tamaño
            Scene scene = new Scene(tablero, 900, 900);
            primaryStage.setTitle("EightOff"); //Titulo de la ventana
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show(); //Muestra la ventana
        }

        /*
        * Metodo main Corre la aplicacion de JavaFX
        */
        public static void main(String[] args) {
            launch(args); //Inicia la aplicacion JavaFX
        }
    }
