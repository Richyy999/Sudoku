package es.rbp.sudoku.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.rbp.sudoku.R;
import es.rbp.sudoku.entidad.Dificultad;
import es.rbp.sudoku.modelo.Partida;

public class Inicio extends AppCompatActivity {

    private LinearLayout layoutCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(android.R.color.transparent));

        Partida partida = Partida.getInstance();
        if (partida != null)
            toJuego(null);
        else
            cargarVista();
    }

    private void toJuego(Dificultad dificultad) {
        layoutCargando = findViewById(R.id.layoutCargando);
        layoutCargando.setVisibility(View.VISIBLE);

        if (dificultad != null)
            Partida.newInstance(dificultad, this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void cargarVista() {
        layoutCargando = findViewById(R.id.layoutCargando);

        TextView btnFacil = findViewById(R.id.btnFacil);
        btnFacil.setOnClickListener(view -> {
            layoutCargando.setVisibility(View.VISIBLE);
            toJuego(Dificultad.FACIL);
        });

        TextView btnMedio = findViewById(R.id.btnMedio);
        btnMedio.setOnClickListener(view -> {
            layoutCargando.setVisibility(View.VISIBLE);
            toJuego(Dificultad.MEDIO);
        });

        TextView btnDificil = findViewById(R.id.btnDificil);
        btnDificil.setOnClickListener(view -> {
            layoutCargando.setVisibility(View.VISIBLE);
            toJuego(Dificultad.DIFICIL);
        });
    }
}