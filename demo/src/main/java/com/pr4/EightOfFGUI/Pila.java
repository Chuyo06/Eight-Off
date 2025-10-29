package com.pr4.EightOfFGUI;

import com.pr4.DeckOfCards.CartaInglesa;

public class Pila<T> {
    private T[] pila;
    private int tope = -1;
    
    public Pila(int size)
    {
        pila = (T[]) new Object[size];
        this.tope = -1;
    }

    public Pila(T[] pila) {
        this.pila = pila;
    }

    public Pila() {
    }
    
    //Construtor para copiar una pila a otra
    public Pila(Pila<T> otraPila) {
        this.pila = (T[]) new Object[otraPila.pila.length];
        this.tope = otraPila.tope;

        for (int i = 0; i <= otraPila.tope; i++) {
            this.pila[i] = otraPila.pila[i];
        }
    }

    //Metodo en el cual se inserta un dato a la pila
    public void push(T dato)
    {
        if(pila_llena())
        {
            System.out.println("Desbordamiento");
        }
        else
        {
            tope++;
           pila[tope] = dato ;
        }
        
    }
    
        //Metodo en el cual obtienes el dato que esta en el tope 
        //sacandolo modificando la pila
    public T pop()
    {
        T dato = null;
        if(pila_vacia())
        {
            System.out.println("Subdesbordamiento");
        }
        else
        {
            dato = pila[tope];
            tope--;
        }
        
        return dato;
    }

    //Metodo en el cual obtienes el dato que etsa en el tope de la pila sin modifificar la pila
    public T peek()
    {
        if(pila_vacia())
        {
            return null;
        }
        return pila[tope];
    }

        public boolean pila_llena()
    {
        return tope == pila.length -1;
    }

        public boolean pila_vacia()
    {
        return tope == -1;
    }

    //Metodo para saber cuantos elementos hay en la Pila
    public int getNumeroDeElementos() {
        return tope + 1;
    }

    //Metodo para invertir la Pila
    public void invertir() {
        int inicio = 0;
        int fin = tope;
        while (inicio < fin) {
            // Intercambia el elemento del inicio con el del final
            T temporal = pila[inicio];
            pila[inicio] = pila[fin];
            pila[fin] = temporal;
            //se mueven los datos
            inicio++;
            fin--;
        }
    }
}