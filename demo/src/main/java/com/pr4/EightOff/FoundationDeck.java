package com.pr4.EightOff;

import com.pr4.DeckOfCards.CartaInglesa;
import com.pr4.DeckOfCards.Palo;
import com.pr4.Estructuras.*;

/**
 * Modela un monticulo donde se ponen las cartas
 * de un solo palo.
 *
 * @author Jesus Manjarrez
 * @version 2025
 */
public class FoundationDeck {
    Palo palo; //Palo que representa el foundation
    ListaSimple<CartaInglesa> cartas; //ListaSimple de cartas de este foundation

    /**
     * Constructor que crea un foundation vacio para un palo
     * @param palo que tendra ese foundation
     */
    public FoundationDeck(Palo palo) {
        this.palo = palo;
        this.cartas = new ListaSimple<>();
    }

    /**
     * Constructor que crea un foundation con una carta inicial
     * @param carta
     */
    public FoundationDeck(CartaInglesa carta) {
        this.palo = carta.getPalo();
        this.cartas = new ListaSimple<>();
        // solo agrega la carta si es un As
        if (carta.getValorBajo() == 1) {
            cartas.insertarFin(carta);
        }
    }

    /**
     * Intentar agregar una carta al palo. Sólo la agrega si
     * la carta es del mismo palo y el la siguiente
     * carta en la secuencia.
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.estaVacia()) {
                if (carta.getValorBajo() == 1) {
                    // si no hay cartas entonces la carta debe ser un A
                    cartas.insertarFin(carta);
                    agregado = true;
                }
            } else {
                // si hay cartas entonces debe haber secuencia
                CartaInglesa ultimaCarta = cartas.obtenerUltimo();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    // agregar la carta si el la siguiente a la última
                    cartas.insertarFin(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }

    /**
     * Remover la última carta del foundation.
     *
     * @return la carta que removió, null si estaba vacio
     */
    public CartaInglesa removerUltimaCarta() {
        return cartas.eliminarFin();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cartas.estaVacia()) {
            builder.append("---");
        } else {
            CartaInglesa carta = cartas.obtenerUltimo();
                builder.append(carta.toString());
        }
        return builder.toString();
    }

    /**
     * Determina si hay cartas en el Foundation.
     * @return true hay al menos una carta, false no hay cartas
     */
    public boolean estaVacio() {
        return cartas.estaVacia();
    }

    /**
     * Obtiene la última carta del Foundation sin removerla.
     * @return última carta, null si no hay cartas
     */
    public CartaInglesa getUltimaCarta() {
        return cartas.obtenerUltimo();
    }
}
