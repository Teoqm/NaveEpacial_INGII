package autonoma.nave_epacial.states;

import autonoma.nave_epacial.Io.JSONParser;
import autonoma.nave_epacial.Io.ScoreData;
import autonoma.nave_epacial.Ui.Action;
import autonoma.nave_epacial.Ui.Button;
import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * La clase ScoreState se encarga de mostrar el historial de las mejores puntuaciones.
 * Utiliza una {@link PriorityQueue} para filtrar y mantener únicamente los 10 puntajes
 * más altos registrados en el archivo de persistencia JSON.
 * Proporciona botones para regresar al menú y limpiar la tabla de puntajes.
 * @version 1.0
 */
public class ScoreState extends State {

    /** Botón para volver a la pantalla del menú principal. */
    private Button returnButton;

    /** Botón para limpiar todos los puntajes registrados. */
    private Button clearButton;

    /** Cola de prioridad que almacena los objetos ScoreData de forma ordenada. */
    private PriorityQueue<ScoreData> highScores;

    /** Comparador personalizado para definir el orden ascendente de los puntajes. */
    private Comparator<ScoreData> scoreComparator;

    /** Arreglo auxiliar utilizado para ordenar y renderizar los datos en pantalla. */
    private ScoreData[] auxArray;

    /**
     * Construye el estado de puntuaciones.
     * Configura el comparador, carga los datos desde JSON y prepara los botones.
     */
    public ScoreState() {

        returnButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() - 20,
                Constants.HEIGHT - Assets.greyBtn.getHeight() - 45,
                Constants.RETURN,
                new Action() {
                    @Override
                    public void doAction() {
                        State.changeState(new MenuState());
                    }
                }
        );

        clearButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 + 20,
                Constants.HEIGHT - Assets.greyBtn.getHeight() - 45,
                "LIMPIAR",
                new Action() {
                    @Override
                    public void doAction() {
                        try {
                            JSONParser.writeFile(new ArrayList<>());
                            highScores.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        scoreComparator = new Comparator<ScoreData>() {
            @Override
            public int compare(ScoreData e1, ScoreData e2) {
                return Integer.compare(e1.getScore(), e2.getScore());
            }
        };

        highScores = new PriorityQueue<>(10, scoreComparator);

        try {
            ArrayList<ScoreData> dataLst = JSONParser.readFile();
            for (ScoreData d : dataLst) highScores.add(d);
            while (highScores.size() > 10) highScores.poll();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza la lógica de los botones para detectar interacciones del mouse.
     */
    @Override
    public void update() {
        returnButton.update();
        clearButton.update();
    }

    /**
     * Renderiza la tabla de posiciones con equipo, puntaje y fecha.
     * Incluye botones de retorno y limpieza en la parte inferior.
     *
     * @param g Contexto gráfico para el dibujo.
     */
    @Override
    public void draw(Graphics g) {

        // Encabezados
        Text.drawText(g, "EQUIPO",
                new Vector2D(Constants.WIDTH / 2 - 300, 80),
                true, Color.BLUE, Assets.fontBig);
        Text.drawText(g, "SCORE",
                new Vector2D(Constants.WIDTH / 2 + 50, 80),
                true, Color.BLUE, Assets.fontBig);
        Text.drawText(g, "FECHA",
                new Vector2D(Constants.WIDTH / 2 + 300, 80),
                true, Color.BLUE, Assets.fontBig);

        auxArray = highScores.toArray(new ScoreData[0]);
        Arrays.sort(auxArray, scoreComparator);

        int y = 140;

        if (auxArray.length == 0) {
            Text.drawText(g, "No hay puntajes registrados.",
                    new Vector2D(Constants.WIDTH / 2, y),
                    true, Color.GRAY, Assets.fontMed);
        }

        // Mostrar de mayor a menor puntaje
        for (int i = auxArray.length - 1; i >= 0; i--) {
            ScoreData d = auxArray[i];

            Text.drawText(g, d.getTeam() != null ? d.getTeam() : "Equipo",
                    new Vector2D(Constants.WIDTH / 2 - 300, y),
                    true, Color.CYAN, Assets.fontMed);
            Text.drawText(g, Integer.toString(d.getScore()),
                    new Vector2D(Constants.WIDTH / 2 + 50, y),
                    true, Color.WHITE, Assets.fontMed);
            Text.drawText(g, d.getDate() != null ? d.getDate() : "-",
                    new Vector2D(Constants.WIDTH / 2 + 300, y),
                    true, Color.WHITE, Assets.fontMed);

            y += 45;
        }

        returnButton.draw(g);
        clearButton.draw(g);
    }
}