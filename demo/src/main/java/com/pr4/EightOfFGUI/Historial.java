package com.pr4.EightOfFGUI; // (Asegúrate que el paquete sea el correcto)

import java.util.ArrayList;

import com.pr4.Estructuras.NodoDoble; // (Importa tu NodoDoble)

/**
 * Esta clase maneja el historial de movimientos usando una
 * Lista Doblemente Ligada para usar redo y undo.
 */
public class Historial {

    private NodoDoble<Movimiento> inicio;
    private NodoDoble<Movimiento> fin;
    /**
     *el puntero que nos va decir nuestro estadoActual
     *ya sea anterior o siguiente
     */
    private NodoDoble<Movimiento> estadoActual;

    public Historial() {
        this.inicio = null;
        this.fin = null;
        this.estadoActual = null; //incia vacio el hsitroail
    }

    /**
     * Este etodo aniade un movimeinto nuevo al historial
     */
    public void agregarMovimiento(Movimiento mov) {
        NodoDoble<Movimiento> nuevoNodo = new NodoDoble<>(mov);

        if (estadoActual == null) {
            // histrooial vacio
            this.inicio = nuevoNodo;
            this.fin = nuevoNodo;
        } else {
            // Si se navega entre el histroial y se queda a la mitad del mismo
            //aplicando redo, todo los movieintos psoteriores
            //van a desaparecer
            this.fin = estadoActual;
            this.fin.setSig(null); 

            // aniade el nuevo movimeinto despues del actual
            estadoActual.setSig(nuevoNodo);
            nuevoNodo.setAnt(estadoActual);
            this.fin = nuevoNodo; // nuevo movimiento ahora es el actual
        }
        
        // se mueve el puntero
        this.estadoActual = nuevoNodo;
    }

    /**
     * Mueve el puntero hacia atras
     * @return El movimiento se deshace
     */
    public Movimiento undo() {
        if (!puedeUndo()) {
            return null; // no ya nada para desahcer
        }
        
        Movimiento movParaDeshacer = estadoActual.getInfo();
        estadoActual = estadoActual.getAnt(); // mueve el puntero hacia atras
        
        return movParaDeshacer;
    }

    /**
     * Mueve el puntero hacia delante
     * @return El movimiento se rehace
     */
    public Movimiento redo() {
        if (!puedeRedo()) {
            return null; // No hay nada que rehacer
        }
        
        // Si estaba en el inicio y se da redo, el inicio se quda como el movimeinto actual
        if (estadoActual == null) {
             estadoActual = this.inicio;
        } else {
            // Si no moverse al siguiente
            estadoActual = estadoActual.getSig();
        }
        
        return estadoActual.getInfo();
    }

    /**
     *Verificar si se puede desahcer
     * @return true si hay movmientos para undo
     */
    public boolean puedeUndo() {
        return estadoActual != null;
    }

    /**
     * Verifica si se puede hacer rehacer
     * @return true si hay movimientos para redo
     */
    public boolean puedeRedo() {
        // si esta en el incio se puede reahcer sino es null
        if (estadoActual == null) {
            return this.inicio != null;
        }
        // Si el punteor esta en medio, y se da rehacer es porque hay un nodo siguiente
        return estadoActual.getSig() != null;
    }
    
    /**
     * Metodo que aplica los cambios despues de presioanr el boton de aplicar estado
     * el cual elimina todo lo que este despues del puntero acutal al presionar redo
     */
    public void aplicarEstadoActual() {
        if (estadoActual == null) {
            this.inicio = null;
            this.fin = null;
        } else {
            // corta la lista con redo
            this.fin = estadoActual;
            this.fin.setSig(null);
        }
    }

    /**
 * Devuelve el primer nodo
 */
public NodoDoble<Movimiento> getInicio() {
    return this.inicio;
}

/**
 * Devuevle el nodo donde esta el puntero
 */
public NodoDoble<Movimiento> getEstadoActual() {
    return this.estadoActual;
}

}