package com.pr4.EightOff;


import java.util.ArrayList;
import java.util.List;

import com.pr4.DeckOfCards.CartaInglesa;
import com.pr4.EightOfFGUI.Pila;
import com.pr4.Estructuras.*;

/**
 * Modela un montículo donde se ponen las cartas
 * de manera descendente y del mismo color
 *
 * @author Jesus Manjarrez
 * @version 2025
 */
public class TableauDeck {
    //Lista de cartas en el tabelro
    private ListaSimple<CartaInglesa> cartas = new ListaSimple<>();

    /**
     * Carga las cartas iniciales y voltea la última, todo con listasSimples.
     *
     * @param cartas iniciales ListaSimple de cartas
     */
    public void inicializar(ListaSimple<CartaInglesa> cartasIniciales) {
        ListaSimple<CartaInglesa> temp =new ListaSimple<>();
        //Invertir el orden para mantener la secuencia
        while (!cartasIniciales.estaVacia()) {
            temp.insertarFin(cartasIniciales.eliminarFin());
        }
        while (!temp.estaVacia()) {
            cartas.insertarFin(temp.eliminarFin());
        }
    
        // voltear la última carta recibida
        CartaInglesa ultima = cartas.obtenerUltimo();
        if(ultima != null)
        {
            ultima.makeFaceUp();
        }
    }

    /**
     * Rmueve todas las cartas desde la carta que se eligio
     *
     * @param value valor de la carta desde donde se va remover
     * @return ListaSimple con las cartas removidas, o vacia
     */
    public ListaSimple<CartaInglesa> removeStartingAt(int value) {
        ListaSimple<CartaInglesa> removed = new ListaSimple<>();
        ListaSimple<CartaInglesa> temp = new ListaSimple<>();

        boolean encontrado = false;
        while(!cartas.estaVacia())
        {
            CartaInglesa carta = cartas.eliminarFin();
            temp.insertarFin(carta);
            if(carta.isFaceup() && carta.getValor() == value)
            {
                encontrado = true;
                break;
            }
        }

        if(encontrado)
        {
            while(!temp.estaVacia()){
                removed.insertarFin(temp.eliminarFin());
            }

        }
        else{
            while(!temp.estaVacia())
            {
                cartas.insertarFin(temp.eliminarFin());
            }

        }
        
        return removed;
    }

    /**
     * Busca y retorna la carta con el valor que recibio sin removerla
     * @param value Valor de la carta que se busca
     * @return La carta encontrada o null 
     */
    public CartaInglesa viewCardStartingAt(int value) {
        ListaSimple<CartaInglesa> temp = new ListaSimple<>();
        CartaInglesa encontrada = null;
        while(!cartas.estaVacia())
        {
            CartaInglesa carta = cartas.eliminarFin();
            temp.insertarFin(carta);
            if(carta.isFaceup() && carta.getValor() == value)
            {
                encontrada = carta;
                break;
            }
        }

        // Regresar las cartas a la listaSimple original
        while (!temp.estaVacia()) {
            cartas.insertarFin(temp.eliminarFin());
        }
  
        return encontrada;
    }

     /**
     * Agrega una carta al tableau si cumple las reglas.
     *
     * @param carta Carta a agregar
     * @return true si se pudo agregar, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;

        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.insertarFin(carta);
            agregado = true;
        }
        return agregado;
    }

    /**
     * Obtener la última carta del montículo sin removerla
     *
     * @return la carta que está al final, null si estaba vacio
     */
    CartaInglesa verUltimaCarta() {
        return cartas.obtenerUltimo();
    }

    /**
     * Remover la última carta del montículo.
     *
     * @return la carta que removió, null si estaba vacio
     */
    public CartaInglesa removerUltimaCarta() {
        return cartas.eliminarFin();
    }

    @Override
    public String toString() {
      //Solo se muestra la carta del tope
      if(cartas.estaVacia()){
        return "---";
      }
      else{
        return cartas.obtenerUltimo().toString();
      }
    }

    /**
     * Intentar agreagr  un bloque de cartas al Tableau si la primera carta 
     * coincide a donde se va mover
     *
     * @param cartasRecibidas
     * @return true si se pudo agregar el bloque, false si no
     */    
    public boolean agregarBloqueDeCartas(ListaSimple<CartaInglesa> cartasRecibidas) {
    boolean resultado = false;

    if (!cartasRecibidas.estaVacia()) {
        // Buscar la carta mas baja , la que esta al final
        ListaSimple<CartaInglesa> temp = new ListaSimple<>();
        CartaInglesa cartaMasBaja = null;
        while (!cartasRecibidas.estaVacia()) {
            cartaMasBaja = cartasRecibidas.eliminarFin();
            temp.insertarFin(cartaMasBaja);
        }
        // Las cartas se guardan en una listaSimple temporal 
        if (sePuedeAgregarCarta(cartaMasBaja)) {
            // regresar las cartas
            while (!temp.estaVacia()) {
                cartasRecibidas.insertarFin(temp.eliminarFin());
            }
            // AAqui se inveirte la lista para que la carta quede al final
            cartasRecibidas.invertir();
            while(!cartasRecibidas.estaVacia())
            {
                cartas.insertarFin(cartasRecibidas.eliminarFin());
            }
            resultado = true;
        } else {
            // Si no se puede agregar, regresar las cartas a cartasRecibidas
            while (!temp.estaVacia()) {
                cartasRecibidas.insertarFin(temp.eliminarFin());
            }
        }
    }
    return resultado;
}


    /**
     * Indica si está vacío  el Tableau
     *
     * @return true si no tiene cartas restantes, false si tiene cartas.
     */
    public boolean isEmpty() {
        return cartas.estaVacia();
    }

    /**
     * Verifica si la carta que recibe puede ser la siguiente del tableau actual.
     *
     * @param cartaInicialDePrueba
     * @return true si puede ser la siguiente, false si no
     */
    public boolean sePuedeAgregarCarta(CartaInglesa cartaInicialDePrueba) {
        boolean resultado = false;
        if (!cartas.estaVacia()) {
            CartaInglesa ultima = cartas.obtenerUltimo();
            if (!ultima.getColor().equals(cartaInicialDePrueba.getColor())) {
                if (ultima.getValor() == cartaInicialDePrueba.getValor() + 1) {
                    resultado = true;
                }
            }
        } else {
            // Está vacio el tableau, solo se puede agregar la cara si es rey
            if (cartaInicialDePrueba.getValor() == 13) {
                resultado = true;
            }
        }
        return resultado;
    }

    /**
     * Obtiene la ultima carta del Tableau sin removerla.
     * @return última carta, null si no hay cartas
     */
    public CartaInglesa getUltimaCarta() {
        return cartas.obtenerUltimo();
    }

    public void regresarCarta(CartaInglesa carta) {
        cartas.insertarFin(carta);
    }

    /**
 * Devuelve una copia de la Lista de cartas que hay en el tableau ordenada
 * Se usa para el GUI
 * @return Lista con las cartas ordenadas(abajo hacia rriba)
 */
public ListaSimple<CartaInglesa> getCards() {
   return new ListaSimple<>(this.cartas);
}

/**
 * Regresa una escalera de cartas(bloque) sin realizar validaciones
 * @param bloque Lista con las cartas a regresar.
 */
public void regresarBloqueDeCartas(ListaSimple<CartaInglesa> bloque) {
    bloque.invertir();
    // Invertir la Lista para que se agreguen en orden
    while (!bloque.estaVacia()) {
        cartas.insertarFin(bloque.eliminarFin());
    }
    
}

}
