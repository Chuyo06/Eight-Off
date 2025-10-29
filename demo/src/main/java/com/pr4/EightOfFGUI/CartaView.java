package com.pr4.EightOfFGUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

import com.pr4.DeckOfCards.Carta;

/*
 * Rpresenta uan vista de la carta en el tablero(con imagen).
 */
public class CartaView extends ImageView {

    /*
     * Constructos que crea como se va ver la carta.
     * Si la carta esta boca arriba, se armma el url dependiendo el palo y valor.
     * Si la carta esta boca abajo se muestra la imagen de la parte trasera.
     * @param carta Objeto de tipo Carta.
     */
    public CartaView(Carta carta) {
        setFitWidth(80);
        setPreserveRatio(true);

        String rutaImagen;
        if (carta.isFaceup()) {
            String palo = carta.getPalo().name().toLowerCase();
            int valor = carta.getValor();

            // Se arma el palo, con el nombre de las img 
            switch (palo) {
                case "diamante":
                    palo = "Diamond";
                    break;
                case "corazon":
                    palo = "Hearts";
                    break;
                case "trebol":
                    palo = "Clubs";
                    break;
                case "pica":
                    palo = "Spades";
                    break;
            }

            rutaImagen = "demo/img/Large/" + palo + " " + valor + ".png";
        } else {
            rutaImagen = "demo/img/Large/Back Grey 1.png";
        }

        File file = new File(rutaImagen);
        if (file.exists()) {
            setImage(new Image(file.toURI().toString()));
        } else {
            System.out.println("No hay imagen " + rutaImagen);
        }
    }

    /*
     * Contructor que crea una imagen con las medidas que se 
     * requieren
     * @param rutaImagen Ruta de la imagen para la carta.
     */
    public CartaView(String rutaImagen) {
        // Es parra la carta del reverso, en la cual no se necesita psar por las condicones
        setFitWidth(80);
        setPreserveRatio(true);
        File file = new File(rutaImagen);
        if (file.exists()) {
            setImage(new Image(file.toURI().toString()));
        }
    }
}
