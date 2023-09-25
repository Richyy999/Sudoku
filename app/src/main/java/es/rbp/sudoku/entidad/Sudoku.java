package es.rbp.sudoku.entidad;

import android.content.Context;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class Sudoku {

    public static final String VACIO = "";
    public static final String UNO = "1";
    public static final String DOS = "2";
    public static final String TRES = "3";
    public static final String CUATRO = "4";
    public static final String CINCO = "5";
    public static final String SEIS = "6";
    public static final String SIETE = "7";
    public static final String OCHO = "8";
    public static final String NUEVE = "9";

    public static final int TAMANO_TABLERO = 9;
    public static final int TAMANO_CUADRANTE = 3;

    private static Sudoku sudoku;

    private final TextView[][] casillas;
    private String[][] tableroResuelto;

    private Sudoku() {
        this.casillas = new TextView[Sudoku.TAMANO_TABLERO][Sudoku.TAMANO_TABLERO];
    }

    public static Sudoku getInstance() {
        if (sudoku == null)
            sudoku = new Sudoku();

        return sudoku;
    }

    public boolean validarSudoku(String[][] sudoku) {
        boolean correcto = true;

        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            if (!validarFila(sudoku, y))
                correcto = false;
        }

        for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
            if (!validarCoumna(sudoku, x))
                correcto = false;
        }

        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y += 3) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x += 3) {
                if (!validarSector(sudoku, x, y))
                    correcto = false;
            }
        }
        return correcto;
    }

    public TextView getCasilla(Context context, int x, int y) {
        TextView casilla = casillas[y][x];
        if (casilla == null) {
            casilla = new TextView(context);
            casillas[y][x] = casilla;
        }
        return casilla;
    }

    private boolean validarFila(String[][] sudoku, int y) {
        Set<String> saco = new HashSet<>();
        for (int x = 0; x < TAMANO_TABLERO; x++) {
            String numero = sudoku[y][x];
            if (!VACIO.equals(numero))
                saco.add(numero);
        }

        return saco.size() == 9;
    }

    private boolean validarCoumna(String[][] sudoku, int x) {
        Set<String> saco = new HashSet<>();
        for (int y = 0; y < TAMANO_TABLERO; y++) {
            String numero = sudoku[y][x];
            if (!VACIO.equals(numero))
                saco.add(numero);
        }

        return saco.size() == 9;
    }

    private boolean validarSector(String[][] sudoku, int x, int y) {
        Set<String> saco = new HashSet<>();
        for (int i = 0; i < TAMANO_CUADRANTE; i++) {
            for (int j = 0; j < TAMANO_CUADRANTE; j++) {
                String numero = sudoku[i + y][j + x];
                if (!VACIO.equals(numero))
                    saco.add(numero);
            }
        }

        return saco.size() == 9;
    }

    public TextView[][] getCasillas() {
        return casillas;
    }
}
