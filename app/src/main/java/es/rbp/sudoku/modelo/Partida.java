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

    private final Sudoku sudoku;

    private final Dificultad dificultad;

    private final Stack<Accion> accionesRealizadas;
    private final Stack<Accion> accionesDeshechas;

    private final boolean mostrarNumerosValidos;

    private String[][] tableroActual;

    private int numeroPistas;

    private boolean iniciado;

    private Partida(Dificultad dificultad) {
        this.numeroPistas = dificultad.getNumPistas();
        this.accionesRealizadas = new Stack<>();
        this.accionesDeshechas = new Stack<>();
        this.mostrarNumerosValidos = dificultad.isMostrarNumerosValidos();
        this.dificultad = dificultad;
        this.iniciado = false;

        this.sudoku = Sudoku.getInstance();
    }

    public static Partida newInstance(Dificultad dificultad) {
        partida = new Partida(dificultad);

        return partida;
    }

    public static Partida getInstance() {
        return partida;
    }

    public void init(Context context) {
        if (!iniciado) {
            this.tableroActual = sudoku.getTableroInicial(dificultad.getNumCasillasIniciales(), context);
            iniciado = true;
        }
    }

    public void escribir(Set<TextView> casillas, String numero) {
        Map<TextView, String> valoresAnteriores = new HashMap<>();
        Map<TextView, String> valoresNuevos = new HashMap<>();
        boolean escrito = false;

        for (TextView casilla : casillas) {
            if (casilla.isEnabled()) {
                escrito = true;
                valoresAnteriores.put(casilla, casilla.getText().toString());
                valoresNuevos.put(casilla, numero);
                casilla.setText(numero);
            }
        }

        if (escrito) {
            accionesDeshechas.clear();
            accionesRealizadas.push(new Accion(valoresAnteriores, valoresNuevos));
        }
    }

    public void borrar(Set<TextView> casillas) {
        Map<TextView, String> valoresAnteriores = new HashMap<>();
        Map<TextView, String> valoresNuevos = new HashMap<>();
        boolean borrado = false;

        for (TextView casilla : casillas) {
            if (casilla.isEnabled()) {
                borrado = true;
                String numeroAnterior = casilla.getText().toString();
                valoresAnteriores.put(casilla, numeroAnterior);
                valoresNuevos.put(casilla, Sudoku.VACIO);
                casilla.setText(Sudoku.VACIO);
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

    public void pista(Context context) {
        if (numeroPistas > 0) {
            Toast.makeText(context, "Te quedan " + numeroPistas + " pistas", Toast.LENGTH_SHORT).show();
            numeroPistas--;
        } else {
            Toast.makeText(context, "No te quedan pistas", Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getCasilla(Context context, int x, int y) {
        return sudoku.getCasilla(context, x, y);
    }

    public boolean validarSudoku(String[][] sudoku) {
        return this.sudoku.validarSudoku(sudoku);
    }

    public TextView[][] getCasillas() {
        return sudoku.getCasillas();
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
        return true;
    }

    public boolean isMostrarNumerosValidos() {
        return mostrarNumerosValidos;
    }

    public int getNumeroPistas() {
        return numeroPistas;
    }
}
