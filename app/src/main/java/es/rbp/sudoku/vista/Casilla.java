package es.rbp.sudoku.vista;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.rbp.sudoku.R;
import es.rbp.sudoku.entidad.Sudoku;

public class Casilla extends ConstraintLayout {

    private ConstraintLayout layoutAnotaciones;

    private TextView lblNumero;

    private List<String> anotaciones;

    public Casilla(Context context) {
        super(context);
        init(context);
    }

    public Casilla(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Casilla(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_casilla, this);

        lblNumero = findViewById(R.id.numeroCasilla);
        lblNumero.setFreezesText(true);
        layoutAnotaciones = findViewById(R.id.layoutAnotaciones);

        anotaciones = new ArrayList<>();
    }

    public void setNumero(String numero) {
        if (Sudoku.VACIO.equals(numero))
            borrar();
        else {
            layoutAnotaciones.setVisibility(INVISIBLE);

            lblNumero.setVisibility(VISIBLE);
            lblNumero.setText(numero);
        }
    }

    public void borrar() {
        lblNumero.setText(Sudoku.VACIO);
        lblNumero.setVisibility(INVISIBLE);

        layoutAnotaciones.setVisibility(VISIBLE);
    }

    public void anotarNumero(String numero) {
        if (lblNumero.getVisibility() == INVISIBLE) {
            if (anotaciones.contains(numero))
                anotaciones.remove(numero);
            else
                anotaciones.add(numero);
            layoutAnotaciones.setVisibility(VISIBLE);
            mostrarAnotaciones();
        }
    }

    public void setTamanoTexto(float tamano) {
        lblNumero.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamano);
    }

    public void setColorTexto(int color) {
        lblNumero.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    public String getNumero() {
        return lblNumero.getText().toString();
    }

    private void mostrarAnotaciones() {
        Collections.sort(anotaciones);
        for (int i = 0; i < layoutAnotaciones.getChildCount(); i++) {
            TextView anotacion = (TextView) layoutAnotaciones.getChildAt(i);
            anotacion.setFreezesText(true);
            if (i < anotaciones.size())
                anotacion.setText(anotaciones.get(i));
            else
                anotacion.setText(Sudoku.VACIO);
        }
    }
}
