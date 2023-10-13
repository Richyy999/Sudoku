package es.rbp.sudoku.entidad;

import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import es.rbp.sudoku.R;
import es.rbp.sudoku.modelo.UtilTablero;
import es.rbp.sudoku.vista.Casilla;

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

    private final Casilla[][] casillas;

    private final Set<String> coordenadasPistas;
    private String[][] tableroResuelto;

    private Sudoku(Context context) {
        this.coordenadasPistas = new HashSet<>();
        this.casillas = new Casilla[TAMANO_TABLERO][TAMANO_TABLERO];
        inicializarCasillas(context);

        boolean correcto;
        do {
            this.tableroResuelto = generarSudokuVacio();
            correcto = resolverSudoku(this.tableroResuelto);
        } while (!correcto);
    }

    public static Sudoku newInstance(Context context) {
        sudoku = new Sudoku(context);
        return sudoku;
    }

    public static Sudoku getInstance() {
        return sudoku;
    }

    private void inicializarCasillas(Context context) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                Casilla casillaNueva = new Casilla(context);
                casillaNueva.setBackgroundResource(UtilTablero.getFondoCasilla(x, y));
                casillas[y][x] = casillaNueva;
            }
        }
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

    public Casilla getCasilla(int x, int y) {
        return casillas[y][x];
    }

    public String[][] getTableroInicial(int numCasillas) {
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
            casillas[y][x].setColorTexto(R.color.negro);
            casillas[y][x].setEnabled(false);
        }

        return tableroInicial;
    }

    public Casilla revelarCasilla(List<String> coordenadasVacias) {
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
        Casilla casilla = casillas[y][x];
        casilla.setEnabled(false);
        casilla.setColorTexto(R.color.texto_pista);
        casilla.setNumero(numero);
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

        Casilla casilla = casillas[y][x];
        String valorCasilla = casilla.getNumero();
        String valorResuelto = tableroResuelto[y][x];

        while (valorCasilla.equals(valorResuelto)) {
            x = r.nextInt(TAMANO_TABLERO);
            y = r.nextInt(TAMANO_TABLERO);

            casilla = casillas[y][x];

            valorResuelto = tableroResuelto[y][x];
            valorCasilla = casilla.getNumero();
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

    public Casilla[][] getCasillas() {
        return casillas;
    }
}
