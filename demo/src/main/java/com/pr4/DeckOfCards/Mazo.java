package com.pr4.DeckOfCards;
/**
 * Describe lo que va utilizar un Mazo
 *
 * @author Jesus Manjarrez
 * @version (2025-2)
 */
import java.util.ArrayList;
import java.util.Collections;
import com.pr4.Estructuras.*;

public class Mazo {
    private ListaCircularDoble<CartaInglesa> cartas = new ListaCircularDoble<>();

    public Mazo() {
        llenar(); // crea todas las cartas, excluyendo Jokers
        mezclarCartas();
    }

    /**
     * Barajear el mazo con ayuda de un ArrayList
     * pero se añaden las cartas a la lista doble circular
     * 
     */
    public void mezclarCartas()
    {
        ArrayList<CartaInglesa> temporal = new ArrayList<>();
        while(!cartas.estaVacia())
        {
            //Se usa elimianr inicio de listaCircualrDoble para agregar al arrayList
            temporal.add(cartas.eliminaInicio()); 
        }

        //Mezclar las cartas temporales del ArrayList
        Collections.shuffle(temporal);

        //Se vuelven a ingresar las cartas despues de barajear a lista circular doble
        for(CartaInglesa carta : temporal)
        {
            cartas.insertarFin(carta);
        }
    }

    /**
     * Metodo que llena la baraja/mazo de 52 cartas
     * 
     */
     private void llenar() {
        for (int i = 1; i <=13 ; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa c = new CartaInglesa(i,palo, palo.getColor());
                cartas.insertarFin(c);//se llena la cola circualrDoble con insertarFin
            }
        }
    }


    public CartaInglesa obtenerUnaCarta()
    {
        return cartas.eliminaInicio();
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}
