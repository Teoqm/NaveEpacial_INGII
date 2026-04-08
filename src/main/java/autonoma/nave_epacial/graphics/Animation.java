package autonoma.nave_epacial.graphics;

import autonoma.nave_epacial.math.Vector2D;

import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * La clase Animation se encarga de gestionar la reproducción de una secuencia de imágenes
 * (frames) a una velocidad determinada.
 * Se utiliza principalmente para efectos visuales temporales, como explosiones o
 * propulsores, controlando su ciclo de vida desde el inicio hasta su finalización.
 * * @author Gemini
 * @version 1.0
 */
public class Animation {
    /** Arreglo de imágenes que componen la secuencia de la animación. */
    private BufferedImage[] frames;
    /** Tiempo de espera entre cada frame en milisegundos. */
    private int velocity;
    /** Índice del cuadro (frame) que se está mostrando actualmente. */
    private int index;
    /** Indica si la animación todavía está en reproducción. */
    private boolean running;
    /** Ubicación espacial donde se debe renderizar la animación. */
    private Vector2D position;
    /** Variables para el control interno del tiempo transcurrido. */
    private long time, lasTime;

    /**
     * Construye una nueva animación con un conjunto de cuadros, velocidad y posición.
     * * @param frames   Arreglo de {@link BufferedImage} con los cuadros de la animación.
     * @param velocity Tiempo en milisegundos que dura cada cuadro en pantalla.
     * @param position Coordenadas donde se dibujará la animación.
     */
    public  Animation(BufferedImage[] frames, int velocity, Vector2D position) {
        this.frames = frames;
        this.velocity = velocity;
        this.position = position;
        index = 0;
        this.running = true;
        time  = 0;
        lasTime = System.currentTimeMillis();
    }

    /**
     * Actualiza el estado de la animación. Calcula el tiempo transcurrido para
     * avanzar al siguiente cuadro y detiene la animación una vez que se han
     * reproducido todos los frames.
     */
    public void update(){
        time += System.currentTimeMillis()-lasTime;
        lasTime = System.currentTimeMillis();

        if(time > velocity){
            time=0;
            index++;
            if(index>=frames.length){
                running = false;
            }
        }
    }

    /**
     * Indica si la animación continúa activa.
     * * @return {@code true} si la animación no ha llegado a su último cuadro,
     * {@code false} en caso contrario.
     */
    public boolean isRunning(){
        return running;
    }

    /**
     * Obtiene la posición asignada para esta animación.
     * * @return Un {@link Vector2D} con las coordenadas de la animación.
     */
    public Vector2D getPosition(){
        return position;
    }

    /**
     * Recupera el cuadro actual de la secuencia basado en el índice de reproducción.
     * * @return La imagen {@link BufferedImage} correspondiente al frame actual.
     */
    public BufferedImage getCurrentFrame(){
        return frames[index];
    }

}