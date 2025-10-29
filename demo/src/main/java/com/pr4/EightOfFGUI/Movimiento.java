package com.pr4.EightOfFGUI;

import com.pr4.DeckOfCards.CartaInglesa;
import com.pr4.Estructuras.*;

/**
 * Enum que define los tipos de movimiento.
*/
enum TipoMovimiento {
    TABLEAU_A_TABLEAU,    
    TABLEAU_BLOQUE_A_TABLEAU, 
    TABLEAU_A_FUNDACION,
    TABLEAU_A_CAMPO,
    CAMPO_A_TABLEAU,
    CAMPO_A_FUNDACION,
    CAMPO_A_CAMPO
}

/**
 * Almacena la información de un movimiento para poder deshacerlo.
 */
public class Movimiento {
    TipoMovimiento tipo;
    ListaSimple<CartaInglesa> origen;
    ListaSimple<CartaInglesa> destino;
    ListaSimple<CartaInglesa> bloqueMovido;

    /**
     * Constructor para movimientos de un bloque .
     */
    public Movimiento(TipoMovimiento tipo, ListaSimple<CartaInglesa> origen,
                      ListaSimple<CartaInglesa> destino, ListaSimple<CartaInglesa> bloqueMovido) {
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;
        this.bloqueMovido = bloqueMovido;
    }

    /**
     * Constructor para movimientos de una sola carta
     */
    public Movimiento(TipoMovimiento tipo, ListaSimple<CartaInglesa> origen,
                      ListaSimple<CartaInglesa> destino, CartaInglesa cartaMovida) {
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;

        // Convertimos la carta individual en una ListaSimple de 1 elemento
        this.bloqueMovido = new ListaSimple<>();
        if (cartaMovida != null) { 
             this.bloqueMovido.insertarFin(cartaMovida);
        }
    }

    /**
     * Compara este movimiento con otro para ver si son iguales.
     */
    public boolean esIgual(Movimiento otro) {
        if (otro == null || this.bloqueMovido == null || otro.bloqueMovido == null) {
            return false;
        }

        // Se compara la carta superior del bloque 
        CartaInglesa estaCarta = this.bloqueMovido.obtenerInicio(); 
        CartaInglesa otraCarta = otro.bloqueMovido.obtenerInicio(); 

        // Comparamos referencias de listas origen/destino y tipo
        return this.tipo == otro.tipo &&
               this.origen == otro.origen &&
               this.destino == otro.destino &&
               estaCarta == otraCarta; // Compara si es la misma carta superior
    }
}