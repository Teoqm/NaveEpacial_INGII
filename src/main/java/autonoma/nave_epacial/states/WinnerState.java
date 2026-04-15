package autonoma.nave_epacial.states;

import autonoma.nave_epacial.gameObjects.Chronometer;
import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.io.IOException;

/**
 * Estado que representa la pantalla de victoria del juego.
 *
 * Muestra el nombre del equipo ganador, los puntajes de ambos equipos
 * y el tiempo total de la partida. Después de 8 segundos regresa
 * automáticamente al menú principal.
 *
 * @author Mateo Quintero
 * @author Juan Jacobo Cañas
 * @author Juan Hernández
 * @author Juan José Morales
 * @version 1.0
 */
public class WinnerState extends State {

    /** Nombre del equipo ganador. */
    private final String winnerName;

    /** Puntaje del equipo local. */
    private final int scoreLocal;

    /** Puntaje del equipo rival. */
    private final int scoreRival;

    /** Duración total de la partida en segundos. */
    private final long tiempoJuego;

    /** Cronómetro para controlar el tiempo en pantalla antes de volver al menú. */
    private Chronometer timer;

    /**
     * Constructor del estado de victoria.
     *
     * @param winnerName  nombre del equipo ganador
     * @param scoreLocal  puntaje del equipo local
     * @param scoreRival  puntaje del equipo rival
     * @param tiempoJuego duración total de la partida en segundos
     */
    public WinnerState(String winnerName, int scoreLocal, int scoreRival, long tiempoJuego) {
        this.winnerName  = winnerName;
        this.scoreLocal  = scoreLocal;
        this.scoreRival  = scoreRival;
        this.tiempoJuego = tiempoJuego;
        timer = new Chronometer();
        timer.run(8000);
    }

    @Override
    public void update() throws IOException {
        timer.update();
        if (!timer.isRunning()) {
            State.changeState(new MenuState());
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        boolean isGameOver = winnerName.equals("GAME OVER");

        // Título
        Color tituloColor = isGameOver ? Color.RED : Color.YELLOW;
        String titulo     = isGameOver ? "GAME OVER" : "GANADOR";
        Text.drawText(g2d, titulo,
                new Vector2D(Constants.WIDTH / 2, 100),
                true, tituloColor, Assets.fontBig);

        // Nombre del ganador
        if (!isGameOver) {
            Text.drawText(g2d, winnerName,
                    new Vector2D(Constants.WIDTH / 2, 185),
                    true, Color.WHITE, Assets.fontBig);
        }

        // Línea separadora
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(Constants.WIDTH / 2 - 300, 230, 600, 2);

        // Puntajes — dos columnas
        Text.drawText(g2d, "PUNTAJES",
                new Vector2D(Constants.WIDTH / 2, 265),
                true, Color.YELLOW, Assets.fontMed);

        // Equipo local
        g2d.setColor(Color.CYAN);
        g2d.setFont(Assets.fontMed);
        Text.drawText(g2d, "Tu equipo",
                new Vector2D(Constants.WIDTH / 2 - 150, 310),
                true, Color.CYAN, Assets.fontMed);
        Text.drawText(g2d, String.valueOf(scoreLocal),
                new Vector2D(Constants.WIDTH / 2 - 150, 375),
                true, Color.WHITE, Assets.fontBig);

        // Equipo rival
        Text.drawText(g2d, "Equipo rival",
                new Vector2D(Constants.WIDTH / 2 + 150, 310),
                true, Color.ORANGE, Assets.fontMed);
        Text.drawText(g2d, String.valueOf(scoreRival),
                new Vector2D(Constants.WIDTH / 2 + 150, 375),
                true, Color.WHITE, Assets.fontBig);

        // Determinar ganador por puntaje
        if (!isGameOver) {
            String resultado;
            Color  colorResultado;
            if (scoreLocal > scoreRival) {
                resultado      = "¡Tu equipo gano!";
                colorResultado = Color.GREEN;
            } else if (scoreRival > scoreLocal) {
                resultado      = "El equipo rival gano";
                colorResultado = Color.RED;
            } else {
                resultado      = "¡Empate!";
                colorResultado = Color.YELLOW;
            }
            Text.drawText(g2d, resultado,
                    new Vector2D(Constants.WIDTH / 2, 420),
                    true, colorResultado, Assets.fontMed);
        }

        // Tiempo de partida
        long min = tiempoJuego / 60;
        long seg = tiempoJuego % 60;
        Text.drawText(g2d, "Tiempo: " + min + "m " + seg + "s",
                new Vector2D(Constants.WIDTH / 2, 470),
                true, Color.GRAY, Assets.fontMed);

        Text.drawText(g2d, "Volviendo al menu...",
                new Vector2D(Constants.WIDTH / 2, 540),
                true, Color.DARK_GRAY, Assets.fontMed);
    }
}