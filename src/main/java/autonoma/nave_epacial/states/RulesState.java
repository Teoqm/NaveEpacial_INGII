package autonoma.nave_epacial.states;

import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.Ui.Action;
import autonoma.nave_epacial.Ui.Button;

import java.awt.*;
import java.io.IOException;

/**
 * Estado que muestra las reglas del juego y los controles de cada jugador.
 *
 * Presenta de forma clara el objetivo, sistema de vidas, combate,
 * obstáculos, enemigos y condiciones de victoria organizados en dos
 * columnas para facilitar la lectura.
 *
 * @version 1.0
 */
public class RulesState extends State {

    /** Botón para volver al menú principal. */
    private Button returnButton;

    /**
     * Construye el estado de reglas inicializando el botón de retorno.
     */
    public RulesState() {
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
    }

    @Override
    public void update() throws IOException {
        returnButton.update();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        // Título
        Text.drawText(g2d, "REGLAS DEL JUEGO",
                new Vector2D(Constants.WIDTH / 2, 50),
                true, Color.YELLOW, Assets.fontBig);

        // Línea separadora horizontal
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(80, 75, Constants.WIDTH - 160, 2);

        // Línea divisora vertical central
        g2d.fillRect(Constants.WIDTH / 2, 80, 2, Constants.HEIGHT - 160);

        int colIzq = Constants.WIDTH / 4;
        int colDer = Constants.WIDTH / 4 * 3;
        int y      = 100;
        int sep    = 32;

        // ── Columna izquierda ─────────────────────────────
        dibujarSeccion(g2d, "OBJETIVO", new String[]{
                "Acumula puntos como equipo",
                "eliminando rivales y sobreviviendo."
        }, colIzq, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "EQUIPOS", new String[]{
                "2 equipos de 2 jugadores.",
                "Puntos colectivos por equipo."
        }, colIzq, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "VIDAS", new String[]{
                "Cada jugador tiene 3 vidas.",
                "Sin vidas: eliminado de la partida."
        }, colIzq, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "PUNTAJE", new String[]{
                "+ Destruir meteorito o UFO.",
                "+ Eliminar jugador rival."
        }, colIzq, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "VICTORIA", new String[]{
                "Gana el equipo con mayor",
                "puntaje al final de la partida."
        }, colIzq, y, sep);

        // ── Columna derecha ───────────────────────────────
        y = 100;

        // COMBATE manual para el rojo
        Text.drawText(g2d, "COMBATE",
                new Vector2D(colDer, y), true, Color.CYAN, Assets.fontMed);
        Text.drawText(g2d, "Dispara para eliminar enemigos.",
                new Vector2D(colDer, y + sep), true, Color.WHITE, Assets.fontMed);
        Text.drawText(g2d, "FUEGO AMIGO ACTIVO.",
                new Vector2D(colDer, y + sep * 2), true, Color.RED, Assets.fontMed);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "OBSTACULOS", new String[]{
                "Meteoritos: peligro para todos.",
                "Se dividen al destruirse."
        }, colDer, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "ENEMIGOS", new String[]{
                "UFOs aparecen y disparan.",
                "Pueden destruir cualquier jugador."
        }, colDer, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "COMO MORIR", new String[]{
                "Colision con meteorito o UFO.",
                "Recibir un disparo."
        }, colDer, y, sep);
        y += sep * 3 + 20;

        dibujarSeccion(g2d, "CONTROLES", new String[]{
                "J1: Flechas para mover, P disparar.",
                "J2: WASD para mover, R disparar."
        }, colDer, y, sep);

        returnButton.draw(g);
    }

    private void dibujarSeccion(Graphics2D g2d, String titulo,
                                String[] lineas, int x, int y, int sep) {
        Text.drawText(g2d, titulo,
                new Vector2D(x, y), true, Color.CYAN, Assets.fontMed);
        for (int i = 0; i < lineas.length; i++) {
            Text.drawText(g2d, lineas[i],
                    new Vector2D(x, y + sep * (i + 1)), true, Color.WHITE, Assets.fontMed);
        }
    }
}
