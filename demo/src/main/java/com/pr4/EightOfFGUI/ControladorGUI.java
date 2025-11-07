package com.pr4.EightOfFGUI;

import java.util.ArrayList;

import com.pr4.DeckOfCards.*;
import com.pr4.EightOff.EightOffGame;
import com.pr4.Estructuras.*;
import javafx.scene.control.Alert;

/**
 *Controlador principal del juego EigthOFF que conecta la logica
 con la interfaz grafica. 
 * @author Jesus Manjarrez
 * @version 2025
 */

 //Enum para conocer el tipo de origen
 enum TipoOrigen {
    NONE,
    TABLEAU, 
    TABLEAU_BLOQUE,
    CAMPO
}

/*
 * Controlador que conecta lo grafico con la logica al seleccionar
 * y mover cartas
 */
public class ControladorGUI {
    private EightOffGame game; //Objeto de tipo EightGame
    private TableroView view; //Objeto de tipo tablero
    private Movimiento ultimaPista = null;
    private ArrayList<Movimiento> pistasMostradas = new ArrayList<>();
    private Movimiento pistaActiva = null;
    //private Pila<Movimiento> historialMovimientos = new Pila<>(600); //Pila con 600 espacios para el deshacer
    private Historial historialJuego = new Historial(); //Historial con nodo doble para la lista doble
    private boolean modoHistorial = false; //boolean para saber si esta en modo no mover
    //Variable para conocer la carta seleccionada y la de columna va
    private CartaInglesa cartaSeleccionada = null; //Carta seleccionada
    private ListaSimple<CartaInglesa> listaOrigen = null;
    private TipoOrigen tipoOrigen = TipoOrigen.NONE;
    private ListaSimple<CartaInglesa> bloqueSeleccionado = null;

    /*
     * Construcor que recibe el objeto del juego.
     * @param game Crea una unstancia de solitaireGame
     */
    public ControladorGUI(EightOffGame game) {
        this.game = game;
    }

    /*
     * Conceta el tablero grafico al controlador
     * @param view Crea una instancia del TableroView.
     */
    public void setView(TableroView view) {
        this.view = view;
        actualizarEstadoGUI(); //actualizar el estado despues de aplicar uno
    }

    /**
     * Verifica si una carta se puede mover a un campo libre.
     */
    private boolean esMovimientoValidoCampoLibre(ListaSimple<CartaInglesa> campo) {
        // La celda debe estar vacía
        return campo.estaVacia();
    }

    /**
     * Verifica si una carta se puede mover a una lista de la fundación.
     */
    private boolean esMovimientoValidoFundacion(CartaInglesa cartaAMover, ListaSimple<CartaInglesa> fundacion) {
        // Si la fundacion esyta vacia, solo acepta un As 
        if (fundacion.estaVacia()) {
            return cartaAMover.getValor() == 1;
        } else {
            // Si no, acepta la siguiente carta del mismo palo
            CartaInglesa primero = fundacion.obtenerUltimo();
            return (primero.getPalo() == cartaAMover.getPalo()) && 
                   (primero.getValor() == cartaAMover.getValor() - 1);
        }
    }

    private boolean esMovimientoValidoTableau(CartaInglesa cartaAMover, ListaSimple<CartaInglesa> tableauDestino) {
        if (tableauDestino.estaVacia()) {
            return cartaAMover.getValor() == 13; // Solo Reyes
        } else {
            CartaInglesa cartaCima = tableauDestino.obtenerUltimo();
            // mismo palo y color
            return (cartaCima.getPalo() == cartaAMover.getPalo()) &&
                   (cartaCima.getValor() == cartaAMover.getValor() + 1);
        }
    }

    
    /**
     * Guarda la carta seleccionada y su origen.
     */
    private void seleccionarCarta(CartaInglesa carta, ListaSimple<CartaInglesa> origen, TipoOrigen tipo) {
        cartaSeleccionada = carta;
        listaOrigen = origen;
        tipoOrigen = tipo;

    }
        

    private void resetSeleccion() {
        cartaSeleccionada = null;
        listaOrigen = null;
        tipoOrigen = tipoOrigen.NONE; 
        bloqueSeleccionado = null;  
    }

    /**
     * Verificar si la escalera es valida, con su valor y 
     * color.
     * @param bloque La ListaSimple que contiene la escalera
     * @return true si es una escalera valida
     */
    private boolean esEscaleraValida(ListaSimple<CartaInglesa> escalera) {
        if (escalera == null || escalera.estaVacia()) {
            return false;
        }

        // Se usa la listaSimple con el contructor para copiar
        ListaSimple<CartaInglesa> copia = new ListaSimple<>(escalera);

        CartaInglesa cartaAnterior = copia.eliminarInicio(); // Saca la primera carta 
        if (cartaAnterior == null || copia.estaVacia()) {
            return true; 
        }

        while (!copia.estaVacia()) {
            CartaInglesa cartaActual = copia.eliminarInicio(); // Saca la siguiente carta hacia abajo

            // verificar es del mismo palo
            if (cartaAnterior.getPalo() != cartaActual.getPalo()) {
                return false;
            }

            // verificar que es orden descendente
            if (cartaAnterior.getValor() != cartaActual.getValor() + 1) { 
                return false;
            }

            cartaAnterior = cartaActual; // cambiar de carta
        }

        return true; // si es valida
    }

  /**
     * Logica que se uso en solitario para mover las cartas con 
     * los diferentes tipos de movimientos que hay
     */
    private void intentarMover(ListaSimple<CartaInglesa> listaDestino, TipoMovimiento tipo) {
        if (modoHistorial) {
            return; 
        } //Si esta en modo historial no se deja hacer ningun movimineto

        boolean movido = false;
        Movimiento movimientoActual = null;

        switch (tipo) {
            case TABLEAU_A_CAMPO: case CAMPO_A_CAMPO:
                if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE && bloqueSeleccionado != null && bloqueSeleccionado.getTamanio() > 1) movido = false;
                else movido = esMovimientoValidoCampoLibre(listaDestino);
                break;
            case TABLEAU_A_FUNDACION: case CAMPO_A_FUNDACION:
                if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE && bloqueSeleccionado != null && bloqueSeleccionado.getTamanio() > 1) movido = false;
                else movido = esMovimientoValidoFundacion(cartaSeleccionada, listaDestino);
                break;
            case TABLEAU_A_TABLEAU: case CAMPO_A_TABLEAU: case TABLEAU_BLOQUE_A_TABLEAU:
                movido = esMovimientoValidoTableau(cartaSeleccionada, listaDestino);
                break;
        }
       
        if (movido) {
            this.ultimaPista = null; // Reiniciar pistas simples

            if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE) {
                if (tipo == TipoMovimiento.TABLEAU_BLOQUE_A_TABLEAU) {
                    ListaSimple<CartaInglesa> copiaDelBloqueParaHistorial = new ListaSimple<>(bloqueSeleccionado);
                    listaDestino.insertarListaAlFin(bloqueSeleccionado);
                    movimientoActual = new Movimiento(tipo, listaOrigen, listaDestino, copiaDelBloqueParaHistorial);
                } else { 
                    if (bloqueSeleccionado != null && bloqueSeleccionado.getTamanio() == 1) {
                        CartaInglesa carta = bloqueSeleccionado.eliminarInicio(); // Consumir el bloque
                        if (carta != null) {
                            listaDestino.insertarFin(carta);
                            movimientoActual = new Movimiento(tipo, listaOrigen, listaDestino, carta); // Usa constructor de carta única
                        } else {
                              if(listaOrigen != null) listaOrigen.insertarListaAlFin(bloqueSeleccionado); // Restaurar
                              resetSeleccion(); return; 
                        }
                    } else {
                         if(listaOrigen != null && bloqueSeleccionado != null) listaOrigen.insertarListaAlFin(bloqueSeleccionado); // Restaurar
                         resetSeleccion(); return; 
                    }
                }
            } else if (tipoOrigen == TipoOrigen.TABLEAU || tipoOrigen == TipoOrigen.CAMPO) {
                CartaInglesa carta = null;
                if (listaOrigen != null) {
                    carta = listaOrigen.eliminarFin(); // Quitar del final
                } else {
                    System.err.println("Error crítico: listaOrigen es null al mover carta única.");
                    resetSeleccion(); return; 
                }

                if (carta != null) {
                    listaDestino.insertarFin(carta);
                    movimientoActual = new Movimiento(tipo, listaOrigen, listaDestino, carta); // Usa constructor de carta única
                } else {
                    resetSeleccion(); return; 
                }
                        } else {
                 resetSeleccion(); return; 
            }

            if (movimientoActual != null) {
                historialJuego.agregarMovimiento(movimientoActual);

                if (pistaActiva != null && pistaActiva.esIgual(movimientoActual)) {
                    pistaActiva = null;
                    view.mostrarTextoPista("");
                }
                if (tipo == TipoMovimiento.TABLEAU_A_FUNDACION || tipo == TipoMovimiento.CAMPO_A_FUNDACION) {
                     game.setUltimaFoundation(listaDestino);
                 }

                view.refrescarTablero();
                detectarFinDeJuego();
            } else {
                  if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE && listaOrigen != null && bloqueSeleccionado != null) {
                      listaOrigen.insertarListaAlFin(bloqueSeleccionado); // Restaurar por si acaso
                  }
            }
            resetSeleccion(); 

        } else { 
            if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE && bloqueSeleccionado != null && listaOrigen != null) {
                listaOrigen.insertarListaAlFin(bloqueSeleccionado);
            }
            resetSeleccion(); 
            view.refrescarTablero();
            view.mostrarTextoPista("Movimiento invalido.Seleccioanr otra carta"); //Mostarr mensaje cuando el movieminto es invalido

        }
        actualizarEstadoGUI(); //Actualizar esatdo
    } 
    /**
     * Se llama al hacer click en una carta de una columna
     */
    public void clickTableau(int colIndex, CartaInglesa cartaClickeada) {
        if (modoHistorial) {
        return; //si modo historialesta activiao no deja hcer anda
    }
                if (cartaClickeada == null) {
            resetSeleccion(); return;
        }
        ListaSimple<CartaInglesa> tableauOrigen = game.getTableau()[colIndex];
        //1er click cuando se elige el origen
        if (tableauOrigen == null) {
            resetSeleccion(); return;
        }

        //1er click origen
        if (cartaSeleccionada == null) {
            if (!cartaClickeada.isFaceup()) {
                resetSeleccion();
                return;
            }

            // Intenta cortar la lista
            ListaSimple<CartaInglesa> posibleBloque = tableauOrigen.dividirEn(cartaClickeada);

            if (posibleBloque != null) { 
                if (esEscaleraValida(posibleBloque)) {
                    seleccionarBloque(posibleBloque.obtenerInicio(), posibleBloque, tableauOrigen);
                } else {
                    tableauOrigen.insertarListaAlFin(posibleBloque); 
                    resetSeleccion();
                    view.refrescarTablero();
                    view.mostrarTextoPista("No es una escalera valida.Seleccionar otra carta");
                }
            } else { 
                 if (cartaClickeada == tableauOrigen.obtenerUltimo()) {
                     seleccionarCarta(cartaClickeada, tableauOrigen, TipoOrigen.TABLEAU);
                 } else {
                    resetSeleccion();
                 }
            }
        
        } else {
            //Condicion que maneja la deseleccion de un click
            if (game.getTableau()[colIndex] == listaOrigen) {
                if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE && bloqueSeleccionado != null) {
                    listaOrigen.insertarListaAlFin(bloqueSeleccionado);
                }
                resetSeleccion();
                view.refrescarTablero();
                if (cartaClickeada == game.getTableau()[colIndex].obtenerUltimo()) {
                    seleccionarCarta(cartaClickeada, game.getTableau()[colIndex], TipoOrigen.TABLEAU);
                }

                return; 
            }

            ListaSimple<CartaInglesa> tableauDestino = game.getTableau()[colIndex]; 
            TipoMovimiento tipo;
            if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE) {
                tipo = TipoMovimiento.TABLEAU_BLOQUE_A_TABLEAU;
            } else if (tipoOrigen == TipoOrigen.TABLEAU) {
                 if (cartaSeleccionada != listaOrigen.obtenerUltimo()) {
                    resetSeleccion();
                    return;
                 }
                tipo = TipoMovimiento.TABLEAU_A_TABLEAU;
            } else {
                tipo = TipoMovimiento.CAMPO_A_TABLEAU;
            }
            intentarMover(tableauDestino, tipo); 
        }
    }

    /**
     * Guarda la escalera seleccionado y su origen.
     */
    private void seleccionarBloque(CartaInglesa cartaSuperior, ListaSimple<CartaInglesa> bloque, ListaSimple<CartaInglesa> origen) {
        cartaSeleccionada = cartaSuperior; // Guarda la carta de arriba
        bloqueSeleccionado = bloque;       // Guardamos la escalera completa
        listaOrigen = origen;              // Guardamos la columna origen
        tipoOrigen = TipoOrigen.TABLEAU_BLOQUE;
    }

    /**
     * Se llama al hacer click en un espacio vacio del tableau
     */
    public void clickTableauVacio(int colIndex) {
        if (modoHistorial) {
        System.out.println("En modo historial, no se puede jugar.");
        return; // No hacer nada si estamos en modo visor
    }
        if (cartaSeleccionada == null) {
            resetSeleccion();
            return; 
        }
        
        ListaSimple<CartaInglesa> tableauDestino = game.getTableau()[colIndex];
        
        TipoMovimiento tipo;
         if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE) {
                tipo = TipoMovimiento.TABLEAU_BLOQUE_A_TABLEAU;
            } 
            else if(tipoOrigen == TipoOrigen.TABLEAU){
                tipo = TipoMovimiento.TABLEAU_A_TABLEAU;
            }
            else {
                tipo = TipoMovimiento.CAMPO_A_TABLEAU;
            }
                             

        intentarMover(tableauDestino, tipo);
    }

    /**
     * Se llama al hacer click en un campo libre
     */
    public void clickCampoLibre(int celdaIndex) {
        if (modoHistorial) {
        System.out.println("En modo historial, no se puede jugar.");
        return; // No hacer nada si estamos en modo visor
    }
        ListaSimple<CartaInglesa> celda = game.getCampos()[celdaIndex];

        if (celda == null) {
            resetSeleccion();
            return;
        }

        if (cartaSeleccionada == null) {
            if (!celda.estaVacia()) {
                seleccionarCarta(celda.obtenerUltimo(), celda, TipoOrigen.CAMPO);
            }
        
        } else {
            
            //Mover de campo a campo
            if (tipoOrigen == TipoOrigen.CAMPO) {
                intentarMover(celda, TipoMovimiento.CAMPO_A_CAMPO);
            } else {
                // Mover de Tableau a campo
                intentarMover(celda, TipoMovimiento.TABLEAU_A_CAMPO);
            }
        }
    }

    /**
     * Se llama al hacer click en una fundacion.
     */
    public void clickFundacion(int fundacionIndex) {
        if (modoHistorial) {
        System.out.println("En modo historial, no se puede jugar.");
        return; // No hacer nada si estamos en modo visor
    }
        if (cartaSeleccionada == null) {
            resetSeleccion();
            return; 
        }

        ListaSimple<CartaInglesa> fundacion = game.getFoundations()[fundacionIndex];
        
        if (fundacion == null) {
            resetSeleccion();
            return;
        }

        TipoMovimiento tipo;
        if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE) {
            tipo = TipoMovimiento.TABLEAU_A_FUNDACION;
        } else if (tipoOrigen == TipoOrigen.TABLEAU) {
            tipo = TipoMovimiento.TABLEAU_A_FUNDACION;
        } else { 
            tipo = TipoMovimiento.CAMPO_A_FUNDACION;
        }                             
        intentarMover(fundacion, tipo);
    }
    /**
     * logica para deshacer que se usa en un boton pero no se aplica
     * hasat que el usuario lo acepta
     */
    public void deshacerMovimiento() {

        if (cartaSeleccionada != null && !modoHistorial) {
            
            // Si era un bloque, lo devolvemos
            if (tipoOrigen == TipoOrigen.TABLEAU_BLOQUE && bloqueSeleccionado != null && listaOrigen != null) {
                listaOrigen.insertarListaAlFin(bloqueSeleccionado);
            }
            
            // Limpiamos
            resetSeleccion();
            view.refrescarTablero();
            actualizarEstadoGUI(); // Actualiza la GUI 
            view.mostrarTextoPista("Seleccion cancelada.");
            
            return; 
        }

        //Se entra al modo de vista(Historial)
        modoHistorial = true;

        // Se obtiene el movieminto
        Movimiento mov = historialJuego.undo();
        
        // Si no hay nada que deshacer se cancela
        if (mov == null) {
            return;
        }
        
        // Se aplica el movimiento visualemnte y se revierte
        revertirMovimiento(mov);
        
        // actualizar la vista y GUI
        resetSeleccion();
        view.refrescarTablero();

        actualizarEstadoGUI();
    }
    
    /**
     *  logica para rehacer que se usa en un boton pero no se aplica
     * hasat que el usuario lo acepta
     */
    public void rehacerMovimiento() {
        //Se entra al modo de vista(Historial)
        modoHistorial = true;
        System.out.println("Entrando a Modo Visor (Redo)");

        // Se obtiene el movieminto
        Movimiento mov = historialJuego.redo();
        
        // Si no hay nada que reahcer se cancela
        if (mov == null) {
            System.out.println("No hay más movimientos para rehacer.");
            return;
        }
        
        // se aplica el movimeinto visaulmene
        aplicarMovimiento(mov);
        
        // actualizarGUI
        resetSeleccion();
        view.refrescarTablero();
        actualizarEstadoGUI();
    }

    /**
     * Confrirmar el estado del historial
     */
    public void aplicarEstadoHistorial() {
        
        // Metodo para cortar la lista si se preisona redo
        historialJuego.aplicarEstadoActual();
        
        // salir del modo historial
        modoHistorial = false;
        
        actualizarEstadoGUI();//actualizar gui
    }

    /**
     * Revierte cartas del destino al origen 
     * se usa para dehacer
     */
    private void revertirMovimiento(Movimiento mov) {
        if (mov == null) return;
        
        // Se quita el bloque destino
        int tamanoBloque = mov.bloqueMovido.getTamanio();
        for (int i = 0; i < tamanoBloque; i++) {
            CartaInglesa cartaQuitada = mov.destino.eliminarFin();
            if (cartaQuitada == null) {
                 break;
            }
        }
        
        // Pegar el bloque al origen
        mov.origen.insertarListaAlFin(mov.bloqueMovido);

        // voltear la carta dinal
        boolean origenEraTableau = false;
        for(int i=0; i<8; i++){
            if(game.getTableau()[i] == mov.origen) {
                origenEraTableau = true;
                break;
            }
        }
        if (origenEraTableau) {
             CartaInglesa nuevaCartaSuperior = mov.origen.obtenerUltimo();
             if (nuevaCartaSuperior != null && !nuevaCartaSuperior.isFaceup()) {
                 nuevaCartaSuperior.makeFaceUp();
             }
        }
    }

    /**
     * Aplica un momvimeinto se usa cuando se presiona rehacer
     */
    private void aplicarMovimiento(Movimiento mov) {
        if (mov == null || mov.bloqueMovido == null || mov.bloqueMovido.estaVacia()) {            return;
        }

        // Se obtiene la carta que se movio siendo bloque o carta indiidual
        CartaInglesa cartaBase = mov.bloqueMovido.obtenerInicio();
        ListaSimple<CartaInglesa> bloqueReal; // El bloque que vamos a mover AHORA
        
        // revisar si el movimiento se guardo como bloque
        if (mov.tipo == TipoMovimiento.TABLEAU_BLOQUE_A_TABLEAU) {
            
            // Si es bloque, se corta y se regresa al origen
            bloqueReal = mov.origen.dividirEn(cartaBase);
            
            // verificacion si hay algu fallo
            if (bloqueReal == null) {
                 // y se vuevle a mandar al origen
                 mov.origen.insertarListaAlFin(mov.bloqueMovido);
                 return;
            }

        } else {
            // Fue un movieinto de carta unica
            bloqueReal = new ListaSimple<>();
            CartaInglesa cartaUnica = mov.origen.eliminarFin(); // Quitar la carta superior
            
            if (cartaUnica == null) {
                 return;
            }
            bloqueReal.insertarFin(cartaUnica);
        }

        // 2. Pegar el bloque o carta al destino
        mov.destino.insertarListaAlFin(bloqueReal);

        //voltear la ultima carta
        boolean origenEraTableau = false;
        for(int i=0; i<8; i++){
            if(game.getTableau()[i] == mov.origen) {
                origenEraTableau = true;
                break;
            }
        }
        if (origenEraTableau) {
             CartaInglesa nuevaCartaSuperior = mov.origen.obtenerUltimo();
             if (nuevaCartaSuperior != null && !nuevaCartaSuperior.isFaceup()) {
                 nuevaCartaSuperior.makeFaceUp();
             }
        }
    }

/*
     * Busca un movimiento válido, ignorando una LISTA de pistas ya mostradas.
     * @param hintsAIgnorar La lista (ArrayList) de movimientos a ignorar.
     * @return Un objeto Movimiento, o null si no hay.
     */
    private Movimiento buscarMovimientoValido(ArrayList<Movimiento> pistasIgnorar) {

        // Tablero a fundacion
        for (int i = 0; i < 8; i++) {
            ListaSimple<CartaInglesa> origen = game.getTableau()[i];
            if (origen.estaVacia()) continue;
            CartaInglesa carta = origen.obtenerUltimo();
            for (int j = 0; j < 4; j++) {
                if (esMovimientoValidoFundacion(carta, game.getFoundations()[j])) {
                    // Usar constructor de carta unica
                    Movimiento mov = new Movimiento(TipoMovimiento.TABLEAU_A_FUNDACION, origen, game.getFoundations()[j], carta);
                    if (!listaContiene(pistasIgnorar, mov)) 
                    return mov; 
                }
            }
        }
        // Campo a fundacion
        for (int i = 0; i < 8; i++) {
            ListaSimple<CartaInglesa> origen = game.getCampos()[i];
            if (origen.estaVacia()) continue;
            CartaInglesa carta = origen.obtenerUltimo();
            for (int j = 0; j < 4; j++) {
                if (esMovimientoValidoFundacion(carta, game.getFoundations()[j])) {
                     // Usar constructor de carta unica
                    Movimiento mov = new Movimiento(TipoMovimiento.CAMPO_A_FUNDACION, origen, game.getFoundations()[j], carta);
                    if (!listaContiene(pistasIgnorar, mov)) 
                    return mov; 
                }
            }
        }

        // Campo a tablero
        for (int i = 0; i < 8; i++) {
            ListaSimple<CartaInglesa> origen = game.getCampos()[i];
            if (origen.estaVacia()) continue;
            CartaInglesa carta = origen.obtenerUltimo();
            for (int j = 0; j < 8; j++) {
                if (esMovimientoValidoTableau(carta, game.getTableau()[j])) {
                    // Usar constructor de carta unica
                    Movimiento mov = new Movimiento(TipoMovimiento.CAMPO_A_TABLEAU, origen, game.getTableau()[j], carta);
                    if (!listaContiene(pistasIgnorar, mov)) 
                    return mov; 
                }
            }
        }
        // Tablero a tablero
        for (int i = 0; i < 8; i++) {
            ListaSimple<CartaInglesa> origen = game.getTableau()[i];
            if (origen.estaVacia()) continue;
            CartaInglesa carta = origen.obtenerUltimo();
            for (int j = 0; j < 8; j++) {
                if (i == j) continue;
                if (esMovimientoValidoTableau(carta, game.getTableau()[j])) {
                    // Usar constructor de carta unica
                    Movimiento mov = new Movimiento(TipoMovimiento.TABLEAU_A_TABLEAU, origen, game.getTableau()[j], carta);
                    if (!listaContiene(pistasIgnorar, mov)) 
                    return mov; 
                }
            }
        }

        // Tablero a campo
        for (int i = 0; i < 8; i++) {
            ListaSimple<CartaInglesa> origen = game.getTableau()[i];
            if (origen.estaVacia()) continue;
            CartaInglesa carta = origen.obtenerUltimo();
            for (int j = 0; j < 8; j++) {
                if (esMovimientoValidoCampoLibre(game.getCampos()[j])) {
                     // Usar constructor de carta unica
                    Movimiento mov = new Movimiento(TipoMovimiento.TABLEAU_A_CAMPO, origen, game.getCampos()[j], carta);
                    if (!listaContiene(pistasIgnorar, mov)) 
                    return mov; 
                }
            }
        }

        return null; 
    }
    /**
     * Metodo para saber si una lista es un arrayList
     */
    private boolean listaContiene(ArrayList<Movimiento> lista, Movimiento mov) {
         if (lista == null) 
         return false;
        for (Movimiento hint : lista) {
            if (hint != null && hint.esIgual(mov)) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Revisa si el juego termino
     */
    private void detectarFinDeJuego() {
        //Si todas las fundaciones estan llenas, gano
        boolean ganado = true;
        for (int i = 0; i < 4; i++) {
            if (game.getFoundations()[i].getTamanio() != 13) {
                ganado = false;
                break;
            }
        }
        if (ganado) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Felicidades!");
            alert.setHeaderText(null);
            alert.setContentText("¡Has ganado el juego!");
            alert.showAndWait();
            return;
        }

        // Revisar si no hay mv=ovimientos
        if (buscarMovimientoValido(null) == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText(null);
            alert.setContentText("No hay mas movimientos, se termino el juego");
            alert.showAndWait();
        }
    }

    /**
     * Devuelve un nombre descriptivo, se usa para crear el texto de la pista.
     * @param lista La pila de la que queremos el nombre.
     * @return Un string como "Columna 1" o "Celda 3"
     */
    private String getNombreLista(ListaSimple<CartaInglesa> lista) {
        // Busca en las columnas
        for (int i = 0; i < 8; i++) {
            if (game.getTableau()[i] == lista) {
                return "Columna " + (i + 1); 
            }
        }

        // Buscar en campos
        for (int i = 0; i < 8; i++) {
            if (game.getCampos()[i] == lista) {
                return "Campo " + (i + 1); 
            }
        }

        // Buscar en las fundaciones
        for (int i = 0; i < 4; i++) {
            if (game.getFoundations()[i] == lista) {
                return "Fundación "; 
            }
        }
        return "Lugar Desconocido";
    }

    /**
     * Metodo para dar Pista
     */
    public void darPista() {
        // Buscar una pista
        Movimiento pista = buscarMovimientoValido(this.pistasMostradas);

        if (pista != null) {
            // Si hay pista aniadirlo a la pista
            this.pistasMostradas.add(pista); 

            String nombreOrigen = getNombreLista(pista.origen);
            String nombreDestino = getNombreLista(pista.destino);
            CartaInglesa cartaPista = pista.bloqueMovido.obtenerInicio();
            String textoPista = "Pista: Mover " + cartaPista.toString() +
                                " de " + nombreOrigen + " a " + nombreDestino;

            view.mostrarTextoPista(textoPista);
            this.pistaActiva = pista;

        } else {
            //Verificar si hay alguna pista
            Movimiento primeraPistaPosible = buscarMovimientoValido(new ArrayList<>()); // Busca desde cero

            if (primeraPistaPosible == null) {
                view.mostrarTextoPista("No hay movimientos posibles.");
                this.pistaActiva = null;
                this.pistasMostradas.clear(); // Limpiar

            } else {
                this.pistasMostradas.clear();
                pista = primeraPistaPosible;
                this.pistasMostradas.add(pista);
                String nombreOrigen = getNombreLista(pista.origen);
                String nombreDestino = getNombreLista(pista.destino);
                CartaInglesa cartaPista = pista.bloqueMovido.obtenerInicio();
                String textoPista = "Pista: Mover " + cartaPista.toString() +
                                    " de " + nombreOrigen + " a " + nombreDestino;
                view.mostrarTextoPista(textoPista + " (Ciclo reiniciado)"); // Añadimos nota
                this.pistaActiva = pista;
            }
        }
    }

    /**
     * Metodo para llamar a todos los botones para actualizar el GUI.
     */
    private void actualizarEstadoGUI() {
        if (view != null) {
            boolean puedeUndo = historialJuego.puedeUndo();
            boolean puedeRedo = historialJuego.puedeRedo();
            
            view.actualizarBotones(puedeUndo, puedeRedo, modoHistorial);

            // ArrayList con los movimeintos 
            ArrayList<String> listaStrings = new ArrayList<>();
            int indiceActual = -1; // - para el indice
            int i = 0; // Contador 

            //obtener infromacion en base a los nodos de la lsita doble
            NodoDoble<Movimiento> r = historialJuego.getInicio();
            NodoDoble<Movimiento> nodoActual = historialJuego.getEstadoActual();

            // Recorrer la lista doble
            while (r != null) {
                Movimiento mov = r.getInfo();
                
                // Se  hace el string
                String cartaStr = mov.bloqueMovido.obtenerInicio().toString();
                String origenStr = getNombreLista(mov.origen);
                String destinoStr = getNombreLista(mov.destino);
                
                String desc = (i + 1) + ". Movió " + cartaStr + " de " + origenStr + " a " + destinoStr;
                listaStrings.add(desc);
                
                // Comprobar si este es el nodo actual
                if (r == nodoActual) {
                    indiceActual = i;
                }
                
                r = r.getSig();
                i++;
            }
            //enviar el string a la lista
            view.actualizarVisorHistorial(listaStrings, indiceActual);
        }
    }

}

