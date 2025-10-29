package com.pr4.Estructuras;

import com.pr4.DeckOfCards.CartaInglesa;

public class ListaSimple<T>  {
    private Nodo<T> inicio;
    

    /**
     * Constructor que recipe una ListaSimple y la cual hace una copia
     * para usarla en diferentes metodos y no modificar la listaSimple
     * original.
     */
    public ListaSimple(ListaSimple<T> otraLista) {
        this.inicio = null; //Lista simple vacia
        
        if (otraLista == null || otraLista.estaVacia()) {
            return; // Si la otra lista esta vacia, no hacer nada
        }

        Nodo<T> rOriginal = otraLista.inicio;
        
        // copiar el primer nodo
        this.inicio = new Nodo<>(rOriginal.getInfo());
        Nodo<T> rNuevo = this.inicio; // puntero para la nueva lista
        
        rOriginal = rOriginal.getSig(); // Moverse al segundo nodo de la lista original
        
        //Copiar toda la lista
        while (rOriginal != null) {
            Nodo<T> nuevoNodo = new Nodo<>(rOriginal.getInfo()); // Crear nuevo nodo
            rNuevo.setSig(nuevoNodo);   // enlazar el nodo anterior al nuevo
            rNuevo = nuevoNodo;         // mover el puntero de la nueva lista
            rOriginal = rOriginal.getSig(); // mover el puntero de la lista original
        }
    }

        // (Constructur Vacio
    public ListaSimple() {
        this.inicio = null;
    }
    
    public void insertaInicio(T dato)
    {
        Nodo n = new Nodo(dato);
        n.setInfo(dato);
        n.setSig(inicio);
        inicio = n;
    }
    
    public void insertarFin(T dato)
    {
        
        Nodo n = new Nodo(dato);
        n.setInfo(dato);
        if( inicio == null)
        {
            n.setSig(inicio);
            inicio = n;
        }
        else
        {
            Nodo<T> r = inicio;
            
            while(r.getSig() != null)
            {
                r = r.getSig();
            }
            
            r.setSig(n);
            n.setSig(null);
            
        }
    }

    public boolean estaVacia() {
        return inicio == null;
    }

    public int getTamanio() {
        int cont = 0;
        Nodo<T> r = inicio;
        while (r != null) {
            cont++;
            r = r.getSig();
        }
        return cont;
    }

    public T obtenerUltimo() {
        if (estaVacia()) {
            return null;
        }
        Nodo<T> r = inicio;
        while (r.getSig() != null) {
            r = r.getSig();
        }
        return r.getInfo();
    }


    public T obtenerInicio() {
        if (estaVacia()) {
            return null;
        }
        return inicio.getInfo(); // Regresa el valor del incio
    }

    public T eliminarFin() {
        if (estaVacia()) {
            return null;
        }

        T info;
        if (inicio.getSig() == null) { 
            info = inicio.getInfo();
            inicio = null;
        } else { 
            Nodo<T> r = inicio;
            Nodo<T> ant = null;
            while (r.getSig() != null) {
                ant = r;
                r = r.getSig();
            }
            info = r.getInfo();
            ant.setSig(null); 
        }
        return info;
    }

    // Metodo para eliminar inicio
    public T eliminarInicio() {
        if (estaVacia()) {
            return null;
        }
        T info = inicio.getInfo();
        inicio = inicio.getSig();
        return info;
    }
    
    public void invertir() {
        // Si la lista esta vacia no se inveirte
        if (estaVacia() || inicio.getSig() == null) {
            return;
        }

        Nodo<T> prev = null;
        Nodo<T> actual = inicio;
        Nodo<T> sig = null;

        while (actual != null) {
            sig = actual.getSig(); // guardar el siguiente nodo
            actual.setSig(prev);   // invertir e puntero con el nodo actual
            prev = actual;         // mover prev al nodo actual
            actual = sig;          // mover actual siguiente nodo
        }
        
        // preve ahora es el nuevo inicio
        inicio = prev;
    }

    /**
     * Este metodo es para dividir la lista, es decir, 
     * para mover escaleras de cartas en la lista se busca el 
     * dato y a partir de ahi se divide y se guarda en una nueva lista.
     *
     * @param datoBuscado La carta que el usuario eligio
     * @return Una nueva lista que tiene la escalera
     */
    public ListaSimple<T> dividirEn(T datoBuscado) {
        if (estaVacia()) {
            return null;
        }

        // La carta es el inicio de la ListaSimpel
        if (inicio.getInfo().equals(datoBuscado)) {
            ListaSimple<T> nuevaLista = new ListaSimple<>();
            nuevaLista.inicio = this.inicio;
            this.inicio = null; // la lista originial queda vacia
            return nuevaLista;
        }

        // La carta esta en medio o al final
        Nodo<T> r = inicio;
        Nodo<T> ant = null;
        while (r != null && !r.getInfo().equals(datoBuscado)) {
            ant = r;
            r = r.getSig();
        }

        // Si se encontro la carta (r != null) y no es el inicio (ant != null)
        if (r != null && ant != null) {
            ListaSimple<T> nuevaLista = new ListaSimple<>();
            nuevaLista.inicio = r; // la nueva lista empieza con el nuevo nodo
            ant.setSig(null); // se divide la lista original
            return nuevaLista;
        }

        return null; // No esta eldato 
    }

    /**
     * Pegar una lista dentro de otra lista, este metodo es 
     * necesario para mover una escalera de cartas a otra y
     * cuando se deshace
     *
     * @param la lista que se dividio
     */
    public void insertarListaAlFin(ListaSimple<T> otraLista) {
        
        if (otraLista == null || otraLista.estaVacia() || otraLista.inicio == null) {
            return;
        }

        // Si la otra lista esta vacia apunta al inicio de la otra
        if (this.estaVacia()) {
            this.inicio = otraLista.inicio;
            return;
        }

        // Se encuentra el ultimo nodo de la lista que recibe este metodo
        Nodo<T> r = this.inicio;
        while (r.getSig() != null) {
            r = r.getSig();
        }
            //Enlazar la lista con el incio de la otra
        r.setSig(otraLista.inicio);
    }

}
