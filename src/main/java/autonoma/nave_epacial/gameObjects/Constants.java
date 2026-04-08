package autonoma.nave_epacial.gameObjects;

import javax.swing.filechooser.FileSystemView;
import java.nio.file.FileSystem;
import org.json.JSONObject;

/**
 * La clase Constants centraliza todos los valores de configuración y parámetros
 * globales del juego NaveEspacial.
 * Incluye configuraciones de red, dimensiones de la ventana, propiedades de los
 * objetos del juego (jugador, meteoros, OVNIs) y rutas de archivos.
 * @version 1.0
 */
public class Constants {

    // Red UDP
    /** Puerto local para la comunicación UDP. */
    public static final int    LOCAL_PORT  = 9001; // PC 1: 9001 | PC 2: 9002
    /** Puerto remoto para la comunicación UDP. */
    public static final int    REMOTE_PORT = 9002; // PC 1: 9002 | PC 2: 9001
    /** Dirección IP del equipo remoto. */
    public static final String REMOTE_IP   = "192.168.1.x"; // IP de la otra PC

    // frame dimensions
    /** Ancho de la ventana de juego en píxeles. */
    public static final int WIDTH = 1000;
    /** Alto de la ventana de juego en píxeles. */
    public static final int HEIGHT = 600;

    // player properties
    /** Cadencia de disparo del jugador en milisegundos. */
    public static final int FIRERATE = 300;
    /** Velocidad de rotación del jugador en radianes. */
    public static final double DELTAANGLE = 0.1;
    /** Aceleración de la nave del jugador. */
    public static final double ACC = 0.2;
    /** Velocidad máxima permitida para el jugador. */
    public static final double PLAYER_MAX_VEL = 7.0;
    /** Tiempo de parpadeo (flicker) tras recibir daño o aparecer. */
    public static final long FLICKER_TIME = 200;
    /** Tiempo de invulnerabilidad al reaparecer. */
    public static final long SPAWNING_TIME = 3000;
    /** Tiempo de espera en la pantalla de Game Over antes de reiniciar. */
    public static final long GAME_OVER_TIME = 3000;

    // Laser properties
    /** Velocidad de desplazamiento del láser. */
    public static final double LASER_VEL = 15.0;

    // Meteor properties
    /** Velocidad base de los meteoritos. */
    public static final double METEOR_VEL = 2.0;
    /** Puntos otorgados por destruir un meteorito. */
    public static final int METEOR_SCORE = 20;

    // Ufo properties
    /** Radio del nodo de proximidad para el comportamiento del OVNI. */
    public static final int NODE_RADIUS = 160;
    /** Masa del OVNI para cálculos de física/movimiento. */
    public static final double UFO_MASS = 60;
    /** Velocidad máxima del OVNI. */
    public static final int UFO_MAX_VEL = 3;
    /** Cadencia de disparo del OVNI. */
    public static long UFO_FIRE_RATE = 1000;
    /** Rango de ángulo de disparo del OVNI. */
    public static double UFO_ANGLE_RANGE = Math.PI / 2;
    /** Puntos otorgados por destruir un OVNI. */
    public static final int UFO_SCORE = 40;
    /** Frecuencia de aparición de nuevos OVNIs. */
    public static final long UFO_SPAWN_RATE = 10000;

    /** Texto para el estado o botón de iniciar juego. */
    public static final String PLAY = "PLAY";
    /** Texto para el estado o botón de salir. */
    public static final String EXIT = "EXIT";

    /** Ancho de la barra de carga en píxeles. */
    public static final int LOADING_BAR_WIDTH = 500;
    /** Alto de la barra de carga en píxeles. */
    public static final int LOADING_BAR_HEIGHT = 50;

    /** Texto para la acción de regresar. */
    public static final String RETURN = "RETURN";
    /** Texto para la pantalla de puntajes máximos. */
    public static final String HIGH_SCORES = "HIGHEST SCORES";

    /** Etiqueta para identificar el puntaje en archivos JSON. */
    public static final String SCORE = "SCORE";
    /** Etiqueta para identificar la fecha en archivos JSON. */
    public static final String DATE = "DATE";

    /** Ruta absoluta del archivo de persistencia de datos (puntajes). */
    public static final String SCORE_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+ "//NaveEspacial_INGII//data.json";

}