package com.pr4.Estructuras;

public class NodoDoble<T> {

    private T info;
    private NodoDoble<T> sig; 
    private NodoDoble<T> ant; 


    public NodoDoble(T dato) {
        this.info = dato;
        this.sig = null;
        this.ant = null;
    }


    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public NodoDoble<T> getSig() {
        return sig;
    }

    public void setSig(NodoDoble<T> sig) {
        this.sig = sig;
    }

    public NodoDoble<T> getAnt() {
        return ant;
    }

    public void setAnt(NodoDoble<T> ant) {
        this.ant = ant;
    }
}
