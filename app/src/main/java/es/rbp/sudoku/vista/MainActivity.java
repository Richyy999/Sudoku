package es.rbp.sudoku.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.rbp.sudoku.R;
import es.rbp.sudoku.entidad.Dificultad;
import es.rbp.sudoku.entidad.Sudoku;
import es.rbp.sudoku.modelo.Partida;
import es.rbp.sudoku.modelo.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Set<TextView> casillasSeleccionadas;

    private TextView[][] casillas;

    private GridLayout tablero;

    private Partida partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(android.R.color.transparent));

        partida = Partida.getInstance();
        if (partida == null)
            partida = Partida.newInstance(Dificultad.FACIL);

        cargarTablero();
        cargarBotonesNumero();
        cargarBotonesAccion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pintarTablero();
    }

    @Override
    public void onClick(View v) {
        if (!casillasSeleccionadas.isEmpty()) {
            String numero = Util.getNumeroBoton(v.getId());
            partida.escribir(casillasSeleccionadas, numero);
            actualizarTablero();
        }
    }

    public void deshacer() {
        if (partida.hayDeshacer()) {
            partida.deshacer();
            actualizarTablero();
        }
    }

    public void rehacer() {
        if (partida.hayRehacer()) {
            partida.rehacer();
            actualizarTablero();
        }
    }

    public void borrar() {
        if (!casillasSeleccionadas.isEmpty() && Util.hayCasillasLlenas(casillasSeleccionadas)) {
            partida.borrar(casillasSeleccionadas);
            actualizarTablero();
        }
    }

    public void pista() {
        partida.pista(this);
        actualizarTablero();
    }

    private void actualizarTablero() {
        casillasSeleccionadas.clear();
        Util.limpiarSeleccionTablero(casillas);
        cargarBotonesAccion();
        cargarBotonesNumero();
        partida.setTableroActual(Util.getTableroString(casillas));
    }

    private void pintarTablero() {
        String[][] tableroActual = partida.getTableroActual();
        for (int y = 0; y < Sudoku.TAMANO_TABLERO; y++) {
            for (int x = 0; x < Sudoku.TAMANO_TABLERO; x++) {
                TextView casilla = casillas[y][x];
                casilla.setText(tableroActual[y][x]);
            }
        }
    }

    private void cargarBotonesAccion() {
        ConstraintLayout btnDeshacer = findViewById(R.id.btnDeshacer);
        btnDeshacer.setOnClickListener(view -> deshacer());
        ImageView imgDeshacer = findViewById(R.id.iconoBotonDeshacer);
        if (partida.hayDeshacer())
            imgDeshacer.setImageResource(R.drawable.deshacer);
        else
            imgDeshacer.setImageResource(R.drawable.deshacer_desactivado);

        ConstraintLayout btnRehacer = findViewById(R.id.btnRehacer);
        btnRehacer.setOnClickListener(view -> rehacer());
        ImageView imgRehacer = findViewById(R.id.iconoBotonRehacer);
        if (partida.hayRehacer())
            imgRehacer.setImageResource(R.drawable.rehacer);
        else
            imgRehacer.setImageResource(R.drawable.rehacer_desactivado);

        ConstraintLayout btnBorrar = findViewById(R.id.btnBorrar);
        btnBorrar.setOnClickListener(view -> borrar());
        ImageView imgBorrar = findViewById(R.id.iconoBotonBorrar);
        if (Util.hayCasillasLlenas(casillasSeleccionadas))
            imgBorrar.setImageResource(R.drawable.borrar);
        else
            imgBorrar.setImageResource(R.drawable.borrar_desactivado);

        ConstraintLayout btnPista = findViewById(R.id.btnPista);
        btnPista.setOnClickListener(view -> pista());
        ImageView imgPista = findViewById(R.id.iconoBotonPista);
        if (partida.getNumeroPistas() > 0)
            imgPista.setImageResource(R.drawable.pista);
        else
            imgPista.setImageResource(R.drawable.pista_desactivada);
    }

    private void cargarBotonesNumero() {
        Map<String, Integer> numerosEscritos = Util.contarNumeros(casillas);

        TextView uno = findViewById(R.id.uno);
        uno.setOnClickListener(this);
        int numUnos = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.uno), 0);
        if (numUnos >= 9)
            uno.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            uno.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView dos = findViewById(R.id.dos);
        dos.setOnClickListener(this);
        int numDos = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.dos), 0);
        if (numDos >= 9)
            dos.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            dos.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView tres = findViewById(R.id.tres);
        tres.setOnClickListener(this);
        int numTres = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.tres), 0);
        if (numTres >= 9)
            tres.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            tres.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView cuatro = findViewById(R.id.cuatro);
        cuatro.setOnClickListener(this);
        int numCuatros = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.cuatro), 0);
        if (numCuatros >= 9)
            cuatro.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            cuatro.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView cinco = findViewById(R.id.cinco);
        cinco.setOnClickListener(this);
        int numCincos = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.cinco), 0);
        if (numCincos >= 9)
            cinco.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            cinco.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView seis = findViewById(R.id.seis);
        seis.setOnClickListener(this);
        int numSeis = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.seis), 0);
        if (numSeis >= 9)
            seis.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            seis.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView siete = findViewById(R.id.siete);
        siete.setOnClickListener(this);
        int numSietes = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.siete), 0);
        if (numSietes >= 9)
            siete.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            siete.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView ocho = findViewById(R.id.ocho);
        ocho.setOnClickListener(this);
        int numOchos = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.ocho), 0);
        if (numOchos >= 9)
            ocho.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            ocho.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));

        TextView nueve = findViewById(R.id.nueve);
        nueve.setOnClickListener(this);
        int numNueves = numerosEscritos.getOrDefault(Util.getNumeroBoton(R.id.nueve), 0);
        if (numNueves >= 9)
            nueve.setTextColor(ContextCompat.getColor(this, R.color.texto_azul_desactivado));
        else
            nueve.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));
    }

    private void cargarTablero() {
        tablero = findViewById(R.id.tablero);

        casillas = new TextView[9][9];

        casillasSeleccionadas = new HashSet<>();

        tablero.removeAllViews();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                TextView casillaNueva = partida.getCasilla(this, x, y);
                casillaNueva.setText(Sudoku.VACIO);
                casillaNueva.setFreezesText(true);
                casillaNueva.setClickable(true);
                casillaNueva.setTextColor(ContextCompat.getColor(this, R.color.texto_azul));
                int orientacion = getResources().getConfiguration().orientation;
                casillaNueva.setTextSize(TypedValue.COMPLEX_UNIT_SP, orientacion == Configuration.ORIENTATION_PORTRAIT ? 23 : 20);
                Typeface typeface = ResourcesCompat.getFont(this, R.font.mooli_regular);
                casillaNueva.setTypeface(typeface);
                casillaNueva.setBackgroundResource(Util.getFondoCasilla(x, y));
                casillaNueva.setGravity(Gravity.CENTER);
                casillas[y][x] = casillaNueva;
                casillaNueva.setOnClickListener(v -> {
                    TextView casilla = (TextView) v;
                    if (casillasSeleccionadas.contains(casilla))
                        casillasSeleccionadas.remove(casilla);
                    else {
                        casillasSeleccionadas.clear();
                        casillasSeleccionadas.add(casilla);
                    }
                    Util.limpiarSeleccionTablero(casillas);
                    Util.seleccionarCasillas(casillasSeleccionadas);
                    Util.marcarCasillas(casillasSeleccionadas, casillas);
                    Util.marcarMismoNumero(casillas, casillasSeleccionadas);
                    cargarBotonesAccion();
                });
                ViewParent padre = casillaNueva.getParent();
                if (padre != null)
                    ((ViewGroup) padre).removeView(casillaNueva);

                tablero.addView(casillaNueva);
            }
        }

        tablero.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = tablero.getWidth();
                int height = tablero.getHeight();

                int w = width / 9;
                int h = height / 9;

                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        TextView casilla = casillas[y][x];
                        GridLayout.LayoutParams params = (GridLayout.LayoutParams) casilla.getLayoutParams();
                        params.width = w;
                        params.height = h;
                        params.setMargins(0, 0, 0, 0);
                        casilla.setLayoutParams(params);
                    }
                }

                tablero.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}