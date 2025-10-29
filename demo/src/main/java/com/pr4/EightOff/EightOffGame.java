package com.pr4.EightOff;

import java.util.ArrayList;

import com.pr4.DeckOfCards.*;
import com.pr4.DeckOfCards.*;
import com.pr4.EightOfFGUI.*;
import com.pr4.Estructuras.*;

/**
 * Juego de Eight Off.
 *
 * @author Jesus Manjarrez
 * @version 2025
 */
public class EightOffGame {
    //Arreglos de listas que representan los diferentes campos donde se podran poenr cartas
    private ListaSimple<CartaInglesa>[] tableau; //8 columnas del tableau
    private ListaSimple<CartaInglesa>[] campos; // 8 campos libres
    private ListaSimple<CartaInglesa>[] foundation; // 4 campos para la fundacion
    private ListaSimple<CartaInglesa> ultimoFoundation;

    /**
     * Constructor que inicializa el juego 
     * */    public EightOffGame() {
        //Inicializar las listas
        tableau = new ListaSimple[8];
        campos = new ListaSimple[8];
        foundation = new ListaSimple[4];

        //llenar las listas
        for(int i = 0; i < 8; i++)
        {
            tableau[i] = new ListaSimple<>();
            campos[i] = new ListaSimple<>();
        }

        for(int i = 0; i < 4; i++)
        {
            foundation[i] = new ListaSimple<>();
        }

        //Se crea la baraja
        Mazo baraja = new Mazo();

        //Se reparten las barajas
        repartirJuego(baraja);

    }

    /**
     * Se reparten las 52 cartas, donde 4 van a 4 
     * campos libres y las demas 48 se reparten en 8
     * columnas con 6 cartas cada una.
     */
    private void repartirJuego(Mazo baraja) {
        
        for (int i = 0; i < 8; i++) {
            //6 cartas por campo
            int numCartas = 6; 

            for (int j = 0; j < numCartas; j++) {
                
                CartaInglesa carta = baraja.obtenerUnaCarta(); 
                if (carta != null) {
                    carta.makeFaceUp(); // todas boca arriba
                    tableau[i].insertarFin(carta); // usamos listaSimple
                }
            }
        }


        for (int i = 0; i < 4; i++) {
            CartaInglesa carta = baraja.obtenerUnaCarta();
            if (carta != null) {
                carta.makeFaceUp();
                // tambien se llenan 4 campos
                campos[i].insertarFin(carta); 
            }
        }
    }

    //Metodos de getter y setters que van a regresar los arreglos de lo que se pide
    public ListaSimple<CartaInglesa>[] getTableau() {
        return tableau;
    }

    public ListaSimple<CartaInglesa>[] getCampos() {
        return campos;
    }

    public ListaSimple<CartaInglesa>[] getFoundations() {
        return foundation;
    }

    public void setUltimaFoundation(ListaSimple<CartaInglesa> f) {
        this.ultimoFoundation = f;
    }
    
    public ListaSimple<CartaInglesa> getUltimaFoundation() {
        return ultimoFoundation;
    }


}