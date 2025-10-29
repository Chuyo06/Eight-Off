package com.pr4.Estructuras;

public class Nodo<T> {
    
private T info;
private Nodo<T>  sig;

public Nodo(T nuevaInfo , Nodo nuevoSig)
{
    info = nuevaInfo;
    sig = nuevoSig;
}

public Nodo(T dato)
{
 this.info = dato;
 this.sig = null;
}
    @Override
    public String toString() {
        return "Nodo{" + "info=" + info + ", sig=" + sig + '}';
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public Nodo<T> getSig() {
        return sig;
    }

    public void setSig(Nodo<T> sig) {
        this.sig = sig;
    }

}

