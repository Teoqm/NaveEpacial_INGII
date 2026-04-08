package autonoma.nave_epacial.states;

import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Loader;
import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;

/**
 * La clase LoadingState representa el estado de carga inicial del juego.
 * Se encarga de mostrar una barra de progreso visual mientras los recursos
 * (imágenes, sonidos, fuentes) se cargan en un hilo separado.
 * Una vez finalizada la carga, realiza la transición automática al menú principal.
 * * @version 1.0
 */
public class LoadingState extends State{

    /** Hilo responsable de la carga de recursos de la clase Assets. */
    private Thread loadingThread;

    /** Fuente utilizada para mostrar los textos de carga en pantalla. */
    private Font font;

    /**
     * Construye el estado de carga e inicia el hilo de carga de recursos.
     * * @param loadingThread Hilo que ejecuta la carga de {@link Assets}.
     */
    public LoadingState(Thread loadingThread) {
        this.loadingThread = loadingThread;
        this.loadingThread.start();
        font = Loader.loadFont("/fonts/futureFont.ttf", 38);
    }

    /**
     * Verifica si la carga de activos ha finalizado.
     * Si {@link Assets#loaded} es true, cambia el estado a {@link MenuState}
     * y finaliza el hilo de carga de forma segura.
     */
    @Override
    public void update() {
        if(Assets.loaded) {
            State.changeState(new MenuState());
            try {
                loadingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Renderiza la interfaz visual de carga, incluyendo una barra de progreso
     * con degradado y textos informativos.
     * * @param g Contexto gráfico para el dibujo.
     */
    @Override
    public void draw(Graphics g) {
        GradientPaint gp = new GradientPaint(
                Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2,
                Color.WHITE,
                Constants.WIDTH / 2 + Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 + Constants.LOADING_BAR_HEIGHT / 2,
                Color.BLUE
        );

        Graphics2D g2d = (Graphics2D)g;

        g2d.setPaint(gp);

        // Cálculo del porcentaje basado en los recursos cargados actualmente
        float percentage = (Assets.count / Assets.MAX_COUNT);

        // Dibujo de la barra de carga
        g2d.fillRect(Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2,
                (int)(Constants.LOADING_BAR_WIDTH * percentage),
                Constants.LOADING_BAR_HEIGHT);

        // Dibujo del contorno de la barra
        g2d.drawRect(Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
                Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2,
                Constants.LOADING_BAR_WIDTH,
                Constants.LOADING_BAR_HEIGHT);

        // Textos descriptivos
        Text.drawText(g2d, "SPACE SHIP GAME", new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2 - 50),
                true, Color.WHITE, font);


        Text.drawText(g2d, "LOADING...", new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2 + 40),
                true, Color.WHITE, font);

    }

}