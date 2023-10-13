package es.rbp.sudoku.modelo;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import es.rbp.sudoku.entidad.Accion;
import es.rbp.sudoku.entidad.Dificultad;
import es.rbp.sudoku.entidad.Sudoku;
import es.rbp.sudoku.vista.Casilla;

public class Partida {

    private static Partida partida;

    private final Sudoku sudoku;

    private final Dificultad dificultad;

    private final Stack<Accion> accionesRealizadas;
    private final Stack<Accion> accionesDeshechas;

    private final boolean mostrarNumerosValidos;

    private String[][] tableroActual;

    private int numeroPistas;

    private boolean iniciado;

    private Partida(Dificultad dificultad, Context context) {
        this.numeroPistas = dificultad.getNumPistas();
        this.accionesRealizadas = new Stack<>();
        this.accionesDeshechas = new Stack<>();
        this.mostrarNumerosValidos = dificultad.isMostrarNumerosValidos();
        this.dificultad = dificultad;
        this.iniciado = false;

        this.sudoku = Sudoku.newInstance(context);
    }

    public static Partida newInstance(Dificultad dificultad, Context context) {
        partida = new Partida(dificultad, context);

        return partida;
    }

    public static Partida getInstance() {
        return partida;
    }

    public void init() {
        if (!iniciado) {
            this.tableroActual = sudoku.getTableroInicial(dificultad.getNumCasillasIniciales());
            iniciado = true;
        }
    }

    public void escribir(Set<Casilla> casillas, String numero) {
        Map<Casilla, String> valoresAnteriores = new HashMap<>();
        Map<Casilla, String> valoresNuevos = new HashMap<>();
        boolean escrito = false;

        for (Casilla casilla : casillas) {
            if (casilla.isEnabled()) {
                escrito = true;
                valoresAnteriores.put(casilla, casilla.getNumero());
                valoresNuevos.put(casilla, numero);
                casilla.setNumero(numero);
            }
        }

        if (escrito) {
            accionesDeshechas.clear();
            accionesRealizadas.push(new Accion(valoresAnteriores, valoresNuevos));
        }
    }

    public void anotar(Set<Casilla> casillas, String numero) {
        for (Casilla casilla : casillas) {
            casilla.anotarNumero(numero);
        }
    }

    public void borrar(Set<Casilla> casillas) {
        Map<Casilla, String> valoresAnteriores = new HashMap<>();
        Map<Casilla, String> valoresNuevos = new HashMap<>();
        boolean borrado = false;

        for (Casilla casilla : casillas) {
            if (casilla.isEnabled()) {
                borrado = true;
                String numeroAnterior = casilla.getNumero();
                valoresAnteriores.put(casilla, numeroAnterior);
                valoresNuevos.put(casilla, Sudoku.VACIO);
                casilla.borrar();
            }
        }
        if (borrado) {
            accionesDeshechas.clear();
            accionesRealizadas.push(new Accion(valoresAnteriores, valoresNuevos));
        }
    }

    public void deshacer() {
        if (!accionesRealizadas.isEmpty()) {
            Accion accion = accionesRealizadas.pop();
            accion.deshacerAccion();
            accionesDeshechas.push(accion);
        }
    }

    public void rehacer() {
        if (!accionesDeshechas.isEmpty()) {
            Accion accion = accionesDeshechas.pop();
            accion.rehacerAccion();
            accionesRealizadas.push(accion);
        }
    }

    public void pista(Context context, List<String> coordenadasVacias) {
        if (numeroPistas > 0) {
            Casilla casillaRevelada = sudoku.revelarCasilla(coordenadasVacias);
            eliminarDeAccion(casillaRevelada);
            numeroPistas--;
        } else {
            Toast.makeText(context, "No te quedan pistas", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean contienePista(String coordenada) {
        return sudoku.getCoordenadasPistas().contains(coordenada);
    }

    public Casilla getCasilla(int x, int y) {
        return sudoku.getCasilla(x, y);
    }

    public boolean validarSudoku(String[][] sudoku) {
        return this.sudoku.validarSudoku(sudoku);
    }

    public String[][] getTableroActual() {
        return tableroActual;
    }

    public void setTableroActual(String[][] tableroActual) {
        this.tableroActual = tableroActual;
    }

    public boolean hayDeshacer() {
        return !accionesRealizadas.isEmpty();
    }

    public boolean hayRehacer() {
        return !accionesDeshechas.isEmpty();
    }

    public boolean seleccionMultiple() {
        return false;
    }

    public boolean isMostrarNumerosValidos() {
        return mostrarNumerosValidos;
    }

    public int getNumeroPistas() {
        return numeroPistas;
    }

    public Casilla[][] getCasillas() {
        return sudoku.getCasillas();
    }

    private void eliminarDeAccion(Casilla casilla) {
        eliminarDePila(casilla, accionesDeshechas);
        eliminarDePila(casilla, accionesRealizadas);
    }

    private void eliminarDePila(Casilla casilla, Stack<Accion> pila) {
        for (Accion accion : pila) {
            accion.eliminarCasilla(casilla);
        }
    }
}
