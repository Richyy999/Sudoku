package es.rbp.sudoku.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.rbp.sudoku.R;
import es.rbp.sudoku.entidad.Sudoku;
import es.rbp.sudoku.vista.Casilla;

public class UtilTablero {

    private static final String SEPARADOR_COORDENADA = ":";

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

    public static void limpiarSeleccionTablero(Casilla[][] casillas) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                Casilla casilla = casillas[y][x];
                casilla.setActivated(false);
                casilla.setSelected(false);
            }
        }
    }

    public static void seleccionarCasillas(Set<Casilla> casillas) {
        for (Casilla casilla : casillas) {
            casilla.setSelected(true);
        }
    }

    public static void marcarCasillas(Set<Casilla> casillasSeleccionadas, Casilla[][] casillas) {
        for (Casilla casilla : casillasSeleccionadas) {
            int x = getX(casilla, casillas);
            int y = getY(casilla, casillas);

            marcarFila(casillas, y);
            marcarColumna(casillas, x);
            marcarCuadrante(casillas, x, y);
        }
    }

    public static String[][] getTableroString(Casilla[][] casillas) {
        String[][] tablero = new String[Sudoku.TAMANO_TABLERO][Sudoku.TAMANO_TABLERO];
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                Casilla casilla = casillas[y][x];
                String numero = casilla.getNumero();
                tablero[y][x] = numero;
            }
        }
        return tablero;
    }

    public static void marcarMismoNumero(Casilla[][] casillas, Set<Casilla> casillasSeleccionadas) {
        for (Casilla casilla : casillasSeleccionadas) {
            for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
                for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                    Casilla casillaTablero = casillas[y][x];
                    String numeroTablero = casillaTablero.getNumero();
                    String numeroCasilla = casilla.getNumero();
                    if (Sudoku.VACIO.equals(numeroCasilla))
                        continue;

                    if (numeroTablero.equals(numeroCasilla))
                        casillaTablero.setActivated(true);
                }
            }
        }
    }

    public static int getX(Casilla casilla, Casilla[][] casillas) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                if (casillas[y][x].equals(casilla))
                    return x;
            }
        }

        return -1;
    }

    public static int getY(Casilla casilla, Casilla[][] casillas) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                if (casillas[y][x].equals(casilla))
                    return y;
            }
        }

        return -1;
    }

    public static boolean hayCasillasLlenas(Set<Casilla> casillas) {
        for (Casilla casilla : casillas) {
            String numero = casilla.getNumero();
            if (!Sudoku.VACIO.equals(numero))
                return true;
        }

        return false;
    }

    public static Map<String, Integer> contarNumeros(Casilla[][] casillas) {
        Map<String, Integer> numerosEscritos = new HashMap<>();
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                String numero = casillas[y][x].getNumero();
                if (!Sudoku.VACIO.equals(numero)) {
                    int cuenta = numerosEscritos.getOrDefault(numero, 0);
                    cuenta++;
                    numerosEscritos.put(numero, cuenta);
                }
            }
        }

        return numerosEscritos;
    }

    public static Map<Integer, Boolean> contarNumerosPosibles(Casilla[][] casillas, Set<Casilla> casillasSeleccionadas) {
        Map<Integer, Boolean> numerosPosibles = new HashMap<>();
        numerosPosibles.put(R.id.uno, true);
        numerosPosibles.put(R.id.dos, true);
        numerosPosibles.put(R.id.tres, true);
        numerosPosibles.put(R.id.cuatro, true);
        numerosPosibles.put(R.id.cinco, true);
        numerosPosibles.put(R.id.seis, true);
        numerosPosibles.put(R.id.siete, true);
        numerosPosibles.put(R.id.ocho, true);
        numerosPosibles.put(R.id.nueve, true);

        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                Casilla casilla = casillas[y][x];
                if (casillasSeleccionadas.contains(casilla)) {
                    contarFila(casillas, y, numerosPosibles);
                    contarColumna(casillas, x, numerosPosibles);
                    contarCuadrante(casillas, x, y, numerosPosibles);
                }
            }
        }

        return numerosPosibles;
    }

    public static String getCoordenada(int x, int y) {
        return x + SEPARADOR_COORDENADA + y;
    }

    public static int getCoordenadaX(String coordenadas) {
        return Integer.parseInt(coordenadas.split(SEPARADOR_COORDENADA)[0]);
    }

    public static int getCoordenadaY(String coordenadas) {
        return Integer.parseInt(coordenadas.split(SEPARADOR_COORDENADA)[1]);
    }

    public static List<String> getCoordenadasVacias(Casilla[][] tablero) {
        List<String> coordenadasVacias = new ArrayList<>();
        String[][] casillas = getTableroString(tablero);

        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                String numero = casillas[y][x];
                if (Sudoku.VACIO.equals(numero))
                    coordenadasVacias.add(getCoordenada(x, y));
            }
        }

        return coordenadasVacias;
    }

    private static void contarFila(Casilla[][] casillas, int y, Map<Integer, Boolean> numerosPosibles) {
        for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
            String numero = casillas[y][x].getNumero();
            if (!Sudoku.VACIO.equals(numero)) {
                int id = getBotonNumero(numero);
                numerosPosibles.put(id, false);
            }
        }
    }

    private static void contarColumna(Casilla[][] casillas, int x, Map<Integer, Boolean> numerosPosibles) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            String numero = casillas[y][x].getNumero();
            if (!Sudoku.VACIO.equals(numero)) {
                int id = getBotonNumero(numero);
                numerosPosibles.put(id, false);
            }
        }
    }

    private static void contarCuadrante(Casilla[][] casillas, int x, int y, Map<Integer, Boolean> numerosPosibles) {
        int fila = x - x % 3;
        int columna = y - y % 3;

        for (int i = columna; i < columna + Sudoku.TAMANO_CUADRANTE; i++) {
            for (int j = fila; j < fila + Sudoku.TAMANO_CUADRANTE; j++) {
                String numero = casillas[i][j].getNumero();
                if (!Sudoku.VACIO.equals(numero)) {
                    int id = getBotonNumero(numero);
                    numerosPosibles.put(id, false);
                }
            }
        }
    }

    private static int getBotonNumero(String numero) {
        switch (numero) {
            case Sudoku.UNO:
                return R.id.uno;

            case Sudoku.DOS:
                return R.id.dos;

            case Sudoku.TRES:
                return R.id.tres;

            case Sudoku.CUATRO:
                return R.id.cuatro;

            case Sudoku.CINCO:
                return R.id.cinco;

            case Sudoku.SEIS:
                return R.id.seis;

            case Sudoku.SIETE:
                return R.id.siete;

            case Sudoku.OCHO:
                return R.id.ocho;

            case Sudoku.NUEVE:
                return R.id.nueve;

            default:
                return 0;
        }
    }

    private static void marcarFila(Casilla[][] casillas, int y) {
        for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
            casillas[y][x].setActivated(true);
        }
    }

    private static void marcarColumna(Casilla[][] casillas, int x) {
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            casillas[y][x].setActivated(true);
        }
    }

    private static void marcarCuadrante(Casilla[][] casillas, int x, int y) {
        int fila = x - x % 3;
        int columna = y - y % 3;

        for (int i = columna; i < columna + Sudoku.TAMANO_CUADRANTE; i++) {
            for (int j = fila; j < fila + Sudoku.TAMANO_CUADRANTE; j++) {
                casillas[i][j].setActivated(true);
            }
        }
    }
}
