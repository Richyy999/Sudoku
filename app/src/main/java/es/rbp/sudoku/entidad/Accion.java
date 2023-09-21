package es.rbp.sudoku.entidad;

import android.widget.TextView;

import java.util.Map;

public class Accion {

    private final Map<TextView, String> valoresAnteriores;
    private final Map<TextView, String> valoresNuevos;

    public Accion(Map<TextView, String> valoresAnteriores, Map<TextView, String> valoresNuevos) {
        this.valoresAnteriores = valoresAnteriores;
        this.valoresNuevos = valoresNuevos;
    }

    public void deshacerAccion() {
        for (Map.Entry<TextView, String> entry : valoresAnteriores.entrySet()) {
            entry.getKey().setText(entry.getValue());
        }
    }

    public void rehacerAccion() {
        for (Map.Entry<TextView, String> entry : valoresNuevos.entrySet()) {
            entry.getKey().setText(entry.getValue());
        }
    }
}
