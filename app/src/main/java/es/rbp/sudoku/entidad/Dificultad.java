package es.rbp.sudoku.entidad;

public class Dificultad {

    public static final Dificultad FACIL = new Dificultad(9, 20, true);

    public static final Dificultad MEDIO = new Dificultad(5, 15, true);

    public static final Dificultad DIFICIL = new Dificultad(3, 10, false);

    private final int numPistas;
    private final int numCasillasIniciales;

    private final boolean mostrarNumerosValidos;

    private Dificultad(int numPistas, int numCasillasIniciales, boolean mostrarNumerosValidos) {
        this.numPistas = numPistas;
        this.numCasillasIniciales = numCasillasIniciales;
        this.mostrarNumerosValidos = mostrarNumerosValidos;
    }

    public int getNumPistas() {
        return numPistas;
    }

    public int getNumCasillasIniciales() {
        return numCasillasIniciales;
    }

    public boolean isMostrarNumerosValidos() {
        return mostrarNumerosValidos;
    }
}
