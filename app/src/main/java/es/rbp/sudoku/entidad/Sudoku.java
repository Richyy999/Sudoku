package es.rbp.sudoku.entidad;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import es.rbp.sudoku.R;

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

    private static final String[] NUMEROS = new String[]{UNO, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE};

    private static Sudoku sudoku;

    private final TextView[][] casillas;
    private String[][] tableroResuelto;

    private Sudoku() {
        this.casillas = new TextView[TAMANO_TABLERO][TAMANO_TABLERO];

        boolean correcto;
        do {
            this.tableroResuelto = generarSudokuVacio();
            correcto = resolverSudoku(this.tableroResuelto);
        } while (!correcto);
    }

    public static Sudoku getInstance() {
        if (sudoku == null)
            sudoku = new Sudoku();

        return sudoku;
    }

    public boolean validarSudoku(String[][] sudoku) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                if (!validarFila(sudoku, y) || !validarColumna(sudoku, x) || !validarSector(sudoku, x, y))
                    return false;
            }
        }
        return true;
    }

    public TextView getCasilla(Context context, int x, int y) {
        TextView casilla = casillas[y][x];
        if (casilla == null) {
            casilla = new TextView(context);
            casillas[y][x] = casilla;
        }
        return casilla;
    }

    public String[][] getTableroInicial(int numCasillas, Context context) {
        String[][] tableroInicial = generarSudokuVacio();
        Set<String> posiciones = new HashSet<>();
        for (int i = 0; i < numCasillas; i++) {
            Random r = new Random();
            int x = r.nextInt(9);
            int y = r.nextInt(9);
            String coordenada = x + ":" + y;

            while (posiciones.contains(coordenada)) {
                x = r.nextInt(9);
                y = r.nextInt(9);
                coordenada = x + ":" + y;
            }

            posiciones.add(coordenada);
            tableroInicial[y][x] = tableroResuelto[y][x];
            casillas[y][x].setTextColor(ContextCompat.getColor(context, R.color.negro));
            casillas[y][x].setEnabled(false);
        }

        return tableroInicial;
    }

    private String[][] generarSudokuVacio() {
        String[][] sudokuVacio = new String[TAMANO_TABLERO][TAMANO_TABLERO];

        for (int y = 0; y < TAMANO_TABLERO; y++) {
            for (int x = 0; x < TAMANO_TABLERO; x++) {
                sudokuVacio[y][x] = VACIO;
            }
        }

        return sudokuVacio;
    }


    private boolean resolverSudoku(String[][] sudoku) {
        for (int y = 0; y < TAMANO_TABLERO; y++) {
            for (int x = 0; x < TAMANO_TABLERO; x++) {
                String elemento = sudoku[y][x];
                if (elemento == null || VACIO.equals(elemento)) {
                    for (int i = 0; i < TAMANO_TABLERO; i++) {
                        Random r = new Random();
                        String numero = NUMEROS[r.nextInt(NUMEROS.length)];
                        if (validarNumero(sudoku, x, y, numero)) {
                            sudoku[y][x] = numero;
                            if (resolverSudoku(sudoku))
                                return true;
                            else
                                sudoku[y][x] = VACIO;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validarNumero(String[][] sudoku, int x, int y, String numero) {
        return validarNumeroFila(sudoku, y, numero) && validarNumeroColumna(sudoku, x, numero) && validarNumeroSector(sudoku, x, y, numero);
    }

    private boolean validarNumeroFila(String[][] sudoku, int y, String numero) {
        for (int x = 0; x < TAMANO_TABLERO; x++) {
            String valor = sudoku[y][x];
            if (!VACIO.equals(valor) && valor.equals(numero))
                return false;
        }

        return true;
    }

    private boolean validarNumeroColumna(String[][] sudoku, int x, String numero) {
        for (int y = 0; y < TAMANO_TABLERO; y++) {
            String valor = sudoku[y][x];
            if (!VACIO.equals(valor) && valor.equals(numero))
                return false;
        }

        return true;
    }

    private boolean validarNumeroSector(String[][] sudoku, int x, int y, String numero) {
        int fila = x - x % 3;
        int columna = y - y % 3;

        for (int i = columna; i < columna + TAMANO_CUADRANTE; i++) {
            for (int j = fila; j < fila + TAMANO_CUADRANTE; j++) {
                String valor = sudoku[i][j];
                if (!VACIO.equals(valor) && valor.equals(numero))
                    return false;
            }
        }

        return true;
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

    private boolean validarColumna(String[][] sudoku, int x) {
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
        int fila = x - x % 3;
        int columna = y - y % 3;

        for (int i = columna; i < columna + TAMANO_CUADRANTE; i++) {
            for (int j = fila; j < fila + TAMANO_CUADRANTE; j++) {
                String numero = sudoku[i][j];
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
