package autonoma.nave_epacial.gui;

import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.input.MouseInput;
import autonoma.nave_epacial.states.GameState;
import autonoma.nave_epacial.states.LoadingState;
import autonoma.nave_epacial.states.MenuState;
import autonoma.nave_epacial.states.State;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

/**
 * La clase Window representa la ventana principal de la aplicación y el motor del juego.
 * Extiende de {@link JFrame} e implementa {@link Runnable} para manejar el ciclo de vida
 * del juego en un hilo separado.
 * * Gestiona la inicialización de recursos, la captura de entradas (teclado y ratón) y el
 * Game Loop sincronizado a 60 FPS mediante un sistema de temporización por nanosegundos.
 * @version 1.0
 */
public class Window extends JFrame implements Runnable{

    /** Componente donde se realiza físicamente el dibujo del juego. */
    private Canvas canvas;
    /** Hilo principal dedicado a la ejecución del Game Loop. */
    private Thread thread;
    /** Estado de ejecución del hilo del juego. */
    private boolean running = false;

    /** Estrategia de búfer para gestionar el renderizado sin parpadeos (triple búfer). */
    private BufferStrategy bs;
    /** Contexto gráfico utilizado para dibujar en el canvas. */
    private Graphics g;

    /** Fotogramas por segundo (FPS) objetivo del juego. */
    private final int FPS = 60;

    /** Tiempo objetivo para cada frame en nanosegundos. */
    private double TARGETTIME = 1000000000/FPS;
    /** Acumulador de tiempo para sincronizar la actualización y el renderizado. */
    private double delta = 0;
    /** Valor calculado de FPS promedio para mostrar en pantalla. */
    private int AVERAGEFPS = FPS;

    /** Controlador de entrada para eventos de teclado. */
    private KeyBoard keyBoard;
    /** Controlador de entrada para eventos de ratón. */
    private MouseInput mouseInput;

    /**
     * Construye la ventana del juego configurando sus dimensiones, comportamiento
     * de cierre y listeners de entrada. Inicializa el canvas de renderizado.
     */
    public Window()
    {
        setTitle("Space Ship Game");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        canvas = new Canvas();
        keyBoard = new KeyBoard();
        mouseInput = new MouseInput();

        canvas.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setMaximumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setMinimumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
        canvas.addKeyListener(keyBoard);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);
        setVisible(true);
    }

    /**
     * Actualiza la lógica del teclado y delega la actualización al estado actual
     * del juego (Menú, Juego, Carga, etc.).
     */
    private void update(){
        keyBoard.update();

        try {
            State.getCurrentState().update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gestiona el renderizado de cada frame. Utiliza una BufferStrategy para
     * evitar el flickering y delega el dibujo de los objetos al estado actual.
     */
    private void draw(){
        bs = canvas.getBufferStrategy();

        if(bs == null)
        {
            canvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        //-----------COMIENZA A DIBUJAR------------

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        State.getCurrentState().draw(g);

        g.setColor(Color.WHITE);
        g.drawString(""+AVERAGEFPS, 10, 20);

        //---------------------
        g.dispose();
        bs.show();
    }

    /**
     * Inicializa los recursos del juego. Inicia un hilo secundario para la carga
     * de {@link Assets} y cambia el estado inicial del juego a {@link LoadingState}.
     */
    private void init()
    {
        Thread loadingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Assets.init();
            }
        });

        Assets.init();

        State.changeState(new LoadingState(loadingThread));
    }


    /**
     * Método principal del hilo de ejecución. Implementa un Game Loop robusto
     * que controla la ejecución de {@link #update()} y {@link #draw()} para
     * mantener la constancia de los FPS.
     */
    @Override
    public void run() {

        long now = 0;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;

        init();

        while(running)
        {
            now = System.nanoTime();
            delta += (now - lastTime)/TARGETTIME;
            time += (now - lastTime);
            lastTime = now;

            if(delta >= 1)
            {
                update();
                draw();
                delta --;
                frames ++;
            }
            if(time >= 1000000000)
            {
                AVERAGEFPS = frames;
                frames = 0;
                time = 0;

            }
        }

        stop();
    }

    /**
     * Inicia el hilo principal del juego.
     */
    public void start(){

        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Detiene el hilo del juego de forma segura utilizando {@code join()}.
     */
    private void stop(){
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}