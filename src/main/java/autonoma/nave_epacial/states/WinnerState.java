package autonoma.nave_epacial.states;

import autonoma.nave_epacial.gameObjects.Chronometer;
import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;

import java.awt.*;
import java.io.IOException;

/**
 * Estado que representa la pantalla de victoria del juego.
 *
 * Este estado muestra el nombre del ganador en pantalla y,
 * después de un tiempo determinado, regresa automáticamente
 * al menú principal.
 *
 * Utiliza un cronómetro para controlar la duración en pantalla.
 *
 * @author Mateo Quintero
 * @author Juan Jacobo Cañas
 * @author Juan Hernández
 * @author Juan José Morales
 *  * * @version 1.0
 */
public class WinnerState extends State {

    /**
     * Nombre del jugador ganador.
     */
    private final String winnerName;

    /**
     * Cronómetro utilizado para controlar el tiempo antes de volver al menú.
     */
    private Chronometer timer;

    /**
     * Constructor del estado de victoria.
     *
     * Inicializa el nombre del ganador y arranca un temporizador
     * que define cuánto tiempo se mostrará esta pantalla.
     *
     * @param winnerName nombre del jugador ganador
     */
    public WinnerState(String winnerName) {
        this.winnerName = winnerName;
        timer = new Chronometer();
        timer.run(5000); // vuelve al menú después de 5 segundos
    }

    /**
     * Actualiza la lógica del estado.
     *
     * Verifica el estado del cronómetro y, cuando finaliza,
     * cambia automáticamente al estado de menú.
     *
     * @throws IOException si ocurre un error durante la actualización
     */
    @Override
    public void update() throws IOException {
        timer.update();
        if (!timer.isRunning()) {
            State.changeState(new MenuState());
        }
    }

    /**
     * Dibuja la pantalla de victoria.
     *
     * Muestra un fondo negro, el texto "GANADOR",
     * el nombre del jugador ganador y un mensaje indicando
     * el retorno al menú.
     *
     * @param g objeto Graphics utilizado para renderizar en pantalla
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        Text.drawText(g2d, "GANADOR",
                new autonoma.nave_epacial.math.Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2 - 80),
                true, Color.YELLOW, Assets.fontBig);

        Text.drawText(g2d, winnerName,
                new autonoma.nave_epacial.math.Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2),
                true, Color.WHITE, Assets.fontBig);

        Text.drawText(g2d, "Volviendo al menu...",
                new autonoma.nave_epacial.math.Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2 + 80),
                true, Color.GRAY, Assets.fontMed);
    }
}