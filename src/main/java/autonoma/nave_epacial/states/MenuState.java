package autonoma.nave_epacial.states;

import autonoma.nave_epacial.Ui.Action;
import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.Ui.Button;

import java.awt.*;
import java.util.ArrayList;

/**
 * La clase MenuState representa la interfaz principal de navegación del juego.
 * Gestiona una colección de botones interactivos que permiten al usuario iniciar
 * una partida, consultar las puntuaciones más altas o salir de la aplicación.
 * Implementa la lógica de actualización y renderizado de los componentes de la
 * interfaz de usuario (UI) definidos para el menú.
 * @version 1.0
 */
public class MenuState extends State {

    /** Lista de botones presentes en el menú principal. */
    private ArrayList<Button> buttons;

    /**
     * Inicializa el estado del menú creando y posicionando los botones de
     * Jugar, Salir y Puntajes Máximos. Cada botón se configura con una
     * acción específica mediante una clase anónima.
     */
    public MenuState() {
        buttons = new ArrayList<Button>();

        // Botón para iniciar el juego — va a LobbyState para capturar nombres
        buttons.add(new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 - Assets.greyBtn.getHeight() * 2,
                Constants.PLAY,
                new Action() {
                    @Override
                    public void doAction() {
                        State.changeState(new LobbyState());
                    }
                }
        ));

        // Botón para cerrar la aplicación
        buttons.add(new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 + Assets.greyBtn.getHeight() * 2,
                Constants.EXIT,
                new Action() {
                    @Override
                    public void doAction() {
                        System.exit(0);
                    }
                }
        ));

        // Botón para ver el historial de puntuaciones
        buttons.add(new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2,
                Constants.HIGH_SCORES,
                new Action() {
                    @Override
                    public void doAction() {
                        State.changeState(new ScoreState());
                    }
                }
        ));

        // Botón para ver las reglas del juego
        buttons.add(new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 + Assets.greyBtn.getHeight(),
                Constants.RULES,
                new Action() {
                    @Override
                    public void doAction() {
                        State.changeState(new RulesState());
                    }
                }
        ));
    }

    /**
     * Actualiza el estado lógico de cada botón, permitiendo gestionar
     * eventos como el paso del ratón o clics.
     */
    @Override
    public void update() {
        for (Button b : buttons) {
            b.update();
        }
    }

    /**
     * Renderiza gráficamente todos los botones del menú en el contexto proporcionado.
     * @param g El contexto gráfico {@link Graphics} para realizar el dibujo.
     */
    @Override
    public void draw(Graphics g) {
        for (Button b : buttons) {
            b.draw(g);
        }
    }
}