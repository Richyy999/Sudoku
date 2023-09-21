package es.rbp.sudoku.modelo;

import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import es.rbp.sudoku.R;
import es.rbp.sudoku.entidad.Sudoku;

public class Util {

    public static int getFondoCasilla(int x, int y) {
        int coordenadaX = x % 3;
        int coordenadaY = y % 3;

        if (x == 0 && y == 0)
            return R.drawable.fondo_esquina_arr_izq;

        else if (x == 8 && y == 0)
            return R.drawable.fondo_esquina_arr_der;

        else if (x == 0 && y == 8)
            return R.drawable.fondo_esquina_aba_izq;

        else if (x == 8 && y == 8)
            return R.drawable.fondo_esquina_aba_der;

        else if (coordenadaX == 0 && coordenadaY == 0)
            return R.drawable.fondo_casilla_arr_izq;

        else if (coordenadaX == 1 && coordenadaY == 0)
            return R.drawable.fondo_casilla_arr_cen;

        else if (coordenadaX == 2 && coordenadaY == 0)
            return R.drawable.fondo_casilla_arr_der;

        else if (coordenadaX == 0 && coordenadaY == 1)
            return R.drawable.fondo_casilla_cen_izq;

        else if (coordenadaX == 1 && coordenadaY == 1)
            return R.drawable.fondo_casilla_cen_cen;

        else if (coordenadaX == 2 && coordenadaY == 1)
            return R.drawable.fondo_casilla_cen_der;

        else if (coordenadaX == 0 && coordenadaY == 2)
            return R.drawable.fondo_casilla_aba_izq;

        else if (coordenadaX == 1 && coordenadaY == 2)
            return R.drawable.fondo_casilla_aba_cen;

        else
            return R.drawable.fondo_casilla_aba_der;
    }

    public static String getNumeroBoton(int id) {
        if (id == R.id.uno)
            return Sudoku.UNO;

        else if (id == R.id.dos)
            return Sudoku.DOS;

        else if (id == R.id.tres)
            return Sudoku.TRES;

        else if (id == R.id.cuatro)
            return Sudoku.CUATRO;

        else if (id == R.id.cinco)
            return Sudoku.CINCO;

        else if (id == R.id.seis)
            return Sudoku.SEIS;

        else if (id == R.id.siete)
            return Sudoku.SIETE;

        else if (id == R.id.ocho)
            return Sudoku.OCHO;

        else if (id == R.id.nueve)
            return Sudoku.NUEVE;

        else
            return Sudoku.VACIO;
    }

    public static void limpiarSeleccionTablero(TextView[][] casillas) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                TextView casilla = casillas[y][x];
                casilla.setActivated(false);
                casilla.setSelected(false);
            }
        }
    }

    public static void seleccionarCasillas(Set<TextView> casillas) {
        for (TextView casilla : casillas) {
            casilla.setSelected(true);
        }
    }

    public static void marcarCasillas(Set<TextView> casillasSeleccionadas, TextView[][] casillas) {
        for (TextView casilla : casillasSeleccionadas) {
            int x = getX(casilla, casillas);
            int y = getY(casilla, casillas);

            marcarFila(casillas, y);
            marcarColumna(casillas, x);
            marcarCuadrante(casillas, x, y);
        }
    }

    public static String[][] getTableroString(TextView[][] casillas) {
        String[][] tablero = new String[Sudoku.TAMANO_TABLERO][Sudoku.TAMANO_TABLERO];
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                TextView casilla = casillas[y][x];
                String numero = casilla.getText().toString();
                tablero[y][x] = numero;
            }
        }
        return tablero;
    }

    public static void marcarMismoNumero(TextView[][] casillas, Set<TextView> casillasSeleccionadas) {
        for (TextView casilla : casillasSeleccionadas) {
            for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
                for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                    TextView casillaTablero = casillas[y][x];
                    String numeroTablero = casillaTablero.getText().toString();
                    String numeroCasilla = casilla.getText().toString();
                    if (Sudoku.VACIO.equals(numeroCasilla))
                        continue;

                    if (numeroTablero.equals(numeroCasilla))
                        casillaTablero.setActivated(true);
                }
            }
        }
    }

    public static int getX(TextView casilla, TextView[][] casillas) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                if (casillas[y][x].equals(casilla))
                    return x;
            }
        }

        return -1;
    }

    public static int getY(TextView casilla, TextView[][] casillas) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                if (casillas[y][x].equals(casilla))
                    return y;
            }
        }

        return -1;
    }

    public static boolean hayCasillasLlenas(Set<TextView> casillas) {
        for (TextView casilla : casillas) {
            String numero = casilla.getText().toString();
            if (!Sudoku.VACIO.equals(numero))
                return true;
        }

        return false;
    }

    public static Map<String, Integer> contarNumeros(TextView[][] casillas) {
        Map<String, Integer> numerosEscritos = new HashMap<>();
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                String numero = casillas[y][x].getText().toString();
                if (!Sudoku.VACIO.equals(numero)) {
                    int cuenta = numerosEscritos.getOrDefault(numero, 0);
                    cuenta++;
                    numerosEscritos.put(numero, cuenta);
                }
            }
        }

        return numerosEscritos;
    }

    private static void marcarFila(TextView[][] casillas, int y) {
        for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
            casillas[y][x].setActivated(true);
        }
    }

    private static void marcarColumna(TextView[][] casillas, int x) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            casillas[y][x].setActivated(true);
        }
    }

    private static void marcarCuadrante(TextView[][] casillas, int x, int y) {
        if (x < 3 && y < 3)
            marcarCuadranteIndividual(casillas, 0, 0);
        else if (x < 6 && y < 3)
            marcarCuadranteIndividual(casillas, 3, 0);
        else if (x < 9 && y < 3)
            marcarCuadranteIndividual(casillas, 6, 0);
        else if (x < 3 && y < 6)
            marcarCuadranteIndividual(casillas, 0, 3);
        else if (x < 6 && y < 6)
            marcarCuadranteIndividual(casillas, 3, 3);
        else if (x < 9 && y < 6)
            marcarCuadranteIndividual(casillas, 6, 3);
        else if (x < 3 && y < 9)
            marcarCuadranteIndividual(casillas, 0, 6);
        else if (x < 6 && y < 9)
            marcarCuadranteIndividual(casillas, 3, 6);
        else if (x < 9 && y < 9)
            marcarCuadranteIndividual(casillas, 6, 6);
    }

    private static void marcarCuadranteIndividual(TextView[][] casillas, int x, int y) {
        for (int i = 0; i < Sudoku.TAMANO_CUADRANTE; i++) {
            for (int j = 0; j < Sudoku.TAMANO_CUADRANTE; j++) {
                casillas[i + y][j + x].setActivated(true);
            }
        }
    }
}
