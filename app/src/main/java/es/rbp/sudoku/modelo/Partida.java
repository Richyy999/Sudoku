package es.rbp.sudoku.modelo;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import es.rbp.sudoku.entidad.Accion;
import es.rbp.sudoku.entidad.Dificultad;
import es.rbp.sudoku.entidad.Sudoku;

public class Partida {

    private static Partida partida;

    private Sudoku sudoku;

    private final Stack<Accion> accionesRealizadas;
    private final Stack<Accion> accionesDeshechas;

    private final TextView[][] casillas;

    private String[][] tableroActual;

    private int numeroPistas;

    private Partida(Dificultad dificultad) {
        this.numeroPistas = dificultad.getNumPistas();
        this.accionesRealizadas = new Stack<>();
        this.accionesDeshechas = new Stack<>();

        this.tableroActual = new String[9][9];
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                this.tableroActual[y][x] = Sudoku.VACIO;
            }
        }

        this.casillas = new TextView[Sudoku.TAMANO_TABLERO][Sudoku.TAMANO_TABLERO];
    }

    public static Partida newInstance(Dificultad dificultad) {
        partida = new Partida(dificultad);
        return partida;
    }

    public static Partida getInstance() {
        return partida;
    }

    public void escribir(Set<TextView> casillas, String numero) {
        Map<TextView, String> valoresAnteriores = new HashMap<>();
        Map<TextView, String> valoresNuevos = new HashMap<>();

        for (TextView casilla : casillas) {
            valoresAnteriores.put(casilla, casilla.getText().toString());
            valoresNuevos.put(casilla, numero);
            casilla.setText(numero);
        }

        accionesDeshechas.clear();
        accionesRealizadas.push(new Accion(valoresAnteriores, valoresNuevos));
    }

    public void borrar(Set<TextView> casillas) {
        Map<TextView, String> valoresAnteriores = new HashMap<>();
        Map<TextView, String> valoresNuevos = new HashMap<>();

        for (TextView casilla : casillas) {
            String numeroAnterior = casilla.getText().toString();
            valoresAnteriores.put(casilla, numeroAnterior);
            valoresNuevos.put(casilla, Sudoku.VACIO);
            casilla.setText(Sudoku.VACIO);
        }

        accionesDeshechas.clear();
        accionesRealizadas.push(new Accion(valoresAnteriores, valoresNuevos));
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

    public void pista(Context context) {
        if (numeroPistas > 0) {
            Toast.makeText(context, "Te quedan " + numeroPistas + " pistas", Toast.LENGTH_SHORT).show();
            numeroPistas--;
        } else {
            Toast.makeText(context, "No te quedan pistas", Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getCasilla(Context context, int x, int y) {
        TextView casilla = this.casillas[y][x];
        if (casilla == null) {
            casilla = new TextView(context);
            this.casillas[y][x] = casilla;
        }
        return casilla;
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

    public int getNumeroPistas() {
        return numeroPistas;
    }
}
