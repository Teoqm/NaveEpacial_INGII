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
 * * Proporciona una interfaz visual donde se listan los puntajes y sus respectivas fechas,
 * además de un botón funcional para regresar al menú principal.
 * * @version 1.0
 */
public class ScoreState extends State{

    /** Botón para volver a la pantalla del menú principal. */
    private Button returnButton;

    /** Cola de prioridad que almacena los objetos ScoreData de forma ordenada. */
    private PriorityQueue<ScoreData> highScores;

    /** Comparador personalizado para definir el orden ascendente de los puntajes. */
    private Comparator<ScoreData> scoreComparator;

    /** Arreglo auxiliar utilizado para ordenar y renderizar los datos en pantalla. */
    private ScoreData[] auxArray;

    /**
     * Construye el estado de puntuaciones.
     * Configura el comparador de puntajes, carga los datos desde el archivo JSON
     * mediante {@link JSONParser} y asegura que solo se conserven los 10 mejores registros.
     */
    public ScoreState() {
        returnButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Assets.greyBtn.getHeight(),
                Constants.HEIGHT - Assets.greyBtn.getHeight() * 2,
                Constants.RETURN,
                new Action() {
                    @Override
                    public void doAction() {
                        State.changeState(new MenuState());
                    }
                }
        );

        // Definición del comparador: ordena de menor a mayor puntuación
        scoreComparator = new Comparator<ScoreData>() {
            @Override
            public int compare(ScoreData e1, ScoreData e2) {
                return e1.getScore() < e2.getScore() ? -1: e1.getScore() > e2.getScore() ? 1: 0;
            }
        };

        highScores = new PriorityQueue<ScoreData>(10, scoreComparator);

        try {
            ArrayList<ScoreData> dataLst = JSONParser.readFile();

            for (ScoreData d : dataLst) {
                highScores.add(d);
            }

            // Mantiene el tamaño de la cola en un máximo de 10 elementos
            while (highScores.size()>10){
                highScores.poll();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza la lógica del botón de retorno para detectar interacciones del mouse.
     */
    @Override
    public void update() {
        returnButton.update();
    }

    /**
     * Renderiza la tabla de posiciones en pantalla.
     * Convierte la cola de prioridad en un arreglo, lo ordena y dibuja cada
     * entrada de puntaje y fecha utilizando la clase {@link Text}.
     * * @param g Contexto gráfico para el dibujo.
     */
    @Override
    public void draw(Graphics g) {
        returnButton.draw(g);

        auxArray = highScores.toArray(new ScoreData[highScores.size()]);

        // Ordenamiento necesario ya que el iterador de PriorityQueue no garantiza orden
        Arrays.sort(auxArray, scoreComparator);


        Vector2D scorePos = new Vector2D(
                Constants.WIDTH / 2 - 200,
                100
        );
        Vector2D datePos = new Vector2D(
                Constants.WIDTH / 2 + 200,
                100
        );

        // Dibujo de encabezados
        Text.drawText(g, Constants.SCORE, scorePos, true, Color.BLUE, Assets.fontBig);
        Text.drawText(g, Constants.DATE, datePos, true, Color.BLUE, Assets.fontBig);

        scorePos.setY(scorePos.getY() + 40);
        datePos.setY(datePos.getY() + 40);

        // Dibujo de los puntajes en orden descendente (del más alto al más bajo)
        for(int i = auxArray.length - 1; i > -1; i--) {

            ScoreData d = auxArray[i];

            Text.drawText(g, Integer.toString(d.getScore()), scorePos, true, Color.WHITE, Assets.fontMed);
            Text.drawText(g, d.getDate(), datePos, true, Color.WHITE, Assets.fontMed);

            scorePos.setY(scorePos.getY() + 40);
            datePos.setY(datePos.getY() + 40);

        }

    }

}