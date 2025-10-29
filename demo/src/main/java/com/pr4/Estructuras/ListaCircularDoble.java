package com.pr4.Estructuras;

public class ListaCircularDoble<T>{
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;
    private int tamanio = 0;

    public ListaCircularDoble() {
        this.inicio = null;
        this.fin = null;
        this.tamanio = 0; 
    }

     public boolean estaVacia() {
        return inicio == null;
    }
     
    public void insertaInicio(T dato)
    {
        NodoDoble n = new NodoDoble(dato);
        n.setInfo(dato);
        
        if(inicio == null)
        {
            inicio = fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        }
        else
        {
            n.setSig(inicio);
            inicio.setAnt(n);
            inicio = n;
            fin.setSig(inicio);
            n.setAnt(fin);
        }
        
        
    }
    
    public void insertarFin(T dato)
    {
        NodoDoble n = new NodoDoble(dato);
        n.setInfo(dato);
        
        if(inicio == null)
        {
            inicio = fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        }
        else
        {
            n.setSig(inicio);
            inicio.setAnt(n);
            fin.setSig(n);
            n.setAnt(fin);
            fin = n;
        }
        
        
    }
    
    
    public T eliminaInicio()
    {
        if (inicio == null)
        {
            System.out.println("Lista vacia");
        }
        
            T info = inicio.getInfo();

            if(inicio == fin)
            {
                inicio = fin = null;
            }
            else
            {
                fin.setSig(inicio.getSig());
                inicio = inicio.getSig();
                inicio.setAnt(fin);
            }
        
            tamanio--;
            return info; 
    }
    
    public void elmiinarFin()
    {
        if(inicio == null)
        {
            System.out.println("Lista vacia");
        }
        else
        {
            if(inicio == fin)
            {
                inicio = fin = null;
            }
            else
            {
            NodoDoble<T> r = fin.getAnt();
            r.setSig(inicio);
            inicio.setAnt(r);
            fin = r;
            }
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    private NodoDoble<T> getNodo(int pos) {
        if (pos < 0 || pos >= tamanio || estaVacia()) {
            return null;
        }
        
        NodoDoble<T> actual = inicio;
        for (int i = 0; i < pos; i++) {
            actual = actual.getSig();
        }
        return actual;
    }

    /**
     * Obtener el valor de una carta teniendo la ubicacion
     */
    public T getValor(int pos) {
        NodoDoble<T> nodo = getNodo(pos);
        return (nodo == null) ? null : nodo.getInfo();
    }

    /**
     * Pone una carta en una posicion especifica,
     * sobrescribiendo el que estaba.
     */
    public void setValor(int pos, T dato) {
        NodoDoble<T> nodo = getNodo(pos);
        if (nodo != null) {
            nodo.setInfo(dato);
        }
    }

    /**
     * Obtener ultima carta de la lista
     */

    public T getUltimoValor() {
        return (fin == null) ? null : fin.getInfo();
    }
}