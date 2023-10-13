package es.rbp.sudoku.entidad;

import java.util.Map;

import es.rbp.sudoku.vista.Casilla;

public class Accion {

    private final Map<Casilla, String> valoresAnteriores;
    private final Map<Casilla, String> valoresNuevos;

    public Accion(Map<Casilla, String> valoresAnteriores, Map<Casilla, String> valoresNuevos) {
        this.valoresAnteriores = valoresAnteriores;
        this.valoresNuevos = valoresNuevos;
    }

    public void deshacerAccion() {
        for (Map.Entry<Casilla, String> entry : valoresAnteriores.entrySet()) {
            String valor = entry.getValue();
            if (Sudoku.VACIO.equals(valor))
                entry.getKey().borrar();
            else
                entry.getKey().setNumero(entry.getValue());
        }
    }

    public void rehacerAccion() {
        for (Map.Entry<Casilla, String> entry : valoresNuevos.entrySet()) {
            String valor = entry.getValue();
            if (Sudoku.VACIO.equals(valor))
                entry.getKey().borrar();
            else
                entry.getKey().setNumero(entry.getValue());
        }
    }

    public void eliminarCasilla(Casilla casilla) {
        valoresAnteriores.remove(casilla);
        valoresNuevos.remove(casilla);
    }
}
