package es.rbp.sudoku.entidad;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import es.rbp.sudoku.R;
import es.rbp.sudoku.modelo.UtilTablero;

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

    private static final int NUMERO_INICIAL_PERMITIDO = 7;

    private static Sudoku sudoku;

    private final TextView[][] casillas;

    private final Set<String> coordenadasPistas;
    private String[][] tableroResuelto;

    private Sudoku() {
        this.casillas = new TextView[TAMANO_TABLERO][TAMANO_TABLERO];
        this.coordenadasPistas = new HashSet<>();

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

            casilla.setFreezesText(true);
            casilla.setClickable(true);
            int orientacion = context.getResources().getConfiguration().orientation;
            casilla.setTextSize(TypedValue.COMPLEX_UNIT_SP, orientacion == Configuration.ORIENTATION_PORTRAIT ? 23 : 20);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.mooli_regular);
            casilla.setTypeface(typeface);
            casilla.setBackgroundResource(UtilTablero.getFondoCasilla(x, y));
            casilla.setGravity(Gravity.CENTER);

            casillas[y][x] = casilla;
        }
        return casilla;
    }

    public String[][] getTableroInicial(int numCasillas, Context context) {
        String[][] tableroInicial = generarSudokuVacio();
        Set<String> posiciones = new HashSet<>();
        for (int i = 0; i < numCasillas; i++) {
            Random r = new Random();
            int x = r.nextInt(TAMANO_TABLERO);
            int y = r.nextInt(TAMANO_TABLERO);
            String coordenada = UtilTablero.getCoordenada(x, y);

            while (posiciones.contains(coordenada) || !validarPosicion(tableroInicial, x, y)) {
                x = r.nextInt(TAMANO_TABLERO);
                y = r.nextInt(TAMANO_TABLERO);
                coordenada = UtilTablero.getCoordenada(x, y);
            }

            posiciones.add(coordenada);
            tableroInicial[y][x] = tableroResuelto[y][x];
            casillas[y][x].setTextColor(ContextCompat.getColor(context, R.color.negro));
            casillas[y][x].setEnabled(false);
        }

        return tableroInicial;
    }

    public TextView revelarCasilla(List<String> coordenadasVacias, Context context) {
        String coordenadaElegida;

        if (coordenadasVacias.isEmpty()) {
            coordenadaElegida = buscarCoordenada();
        } else {
            Random r = new Random();
            int indice = r.nextInt(coordenadasVacias.size());
            coordenadaElegida = coordenadasVacias.get(indice);
        }
        int x = UtilTablero.getCoordenadaX(coordenadaElegida);
        int y = UtilTablero.getCoordenadaY(coordenadaElegida);

        String numero = tableroResuelto[y][x];
        TextView casilla = casillas[y][x];
        casilla.setEnabled(false);
        casilla.setTextColor(ContextCompat.getColor(context, R.color.texto_pista));
        casilla.setText(numero);
        coordenadasPistas.add(coordenadaElegida);

        return casilla;
    }

    private boolean validarPosicion(String[][] tableroInicial, int x, int y) {
        int numFila = contarFila(tableroInicial, y);
        int numColumna = contarColumna(tableroInicial, x);
        int numSector = contarSector(tableroInicial, x, y);

        return numFila < NUMERO_INICIAL_PERMITIDO && numColumna < NUMERO_INICIAL_PERMITIDO && numSector < NUMERO_INICIAL_PERMITIDO;
    }

    private String buscarCoordenada() {
        Random r = new Random();
        int x = r.nextInt(TAMANO_TABLERO);
        int y = r.nextInt(TAMANO_TABLERO);

        TextView casilla = casillas[y][x];
        String valorCasilla = casilla.getText().toString();
        String valorResuelto = tableroResuelto[y][x];

        while (valorCasilla.equals(valorResuelto)) {
            x = r.nextInt(TAMANO_TABLERO);
            y = r.nextInt(TAMANO_TABLERO);

            casilla = casillas[y][x];

            valorResuelto = tableroResuelto[y][x];
            valorCasilla = casilla.getText().toString();
        }

        return UtilTablero.getCoordenada(x, y);
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
        return contarFila(sudoku, y) == TAMANO_TABLERO;
    }

    private int contarFila(String[][] sudoku, int y) {
        Set<String> saco = new HashSet<>();
        for (int x = 0; x < TAMANO_TABLERO; x++) {
            String numero = sudoku[y][x];
            if (!VACIO.equals(numero))
                saco.add(numero);
        }

        return saco.size();
    }

    private boolean validarColumna(String[][] sudoku, int x) {
        return contarColumna(sudoku, x) == TAMANO_TABLERO;
    }

    private int contarColumna(String[][] sudoku, int x) {
        Set<String> saco = new HashSet<>();
        for (int y = 0; y < TAMANO_TABLERO; y++) {
            String numero = sudoku[y][x];
            if (!VACIO.equals(numero))
                saco.add(numero);
        }

        return saco.size();
    }

    private boolean validarSector(String[][] sudoku, int x, int y) {
        return contarSector(sudoku, x, y) == TAMANO_TABLERO;
    }

    private int contarSector(String[][] sudoku, int x, int y) {
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

        return saco.size();
    }

    public Set<String> getCoordenadasPistas() {
        return coordenadasPistas;
    }

    public TextView[][] getCasillas() {
        return casillas;
    }
}
