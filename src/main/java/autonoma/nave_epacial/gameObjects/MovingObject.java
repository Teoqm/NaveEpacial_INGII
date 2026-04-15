package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Sound;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * La clase abstracta MovingObject extiende de {@link GameObject} y sirve como base
 * para todos los elementos del juego que poseen movimiento, rotación y capacidad de colisión.
 * Gestiona aspectos comunes como la velocidad, el centro del objeto, sonidos de explosión
 * y la lógica de interacción física entre diferentes entidades.
 * @version 1.0
 */
public abstract class MovingObject extends GameObject {

    /** Vector que representa la dirección y magnitud del movimiento actual. */
    protected Vector2D velocity;
    /** Transformación afín utilizada para el renderizado con rotación y traslación. */
    protected AffineTransform at;
    /** Ángulo de orientación del objeto en radianes. */
    protected double angle;
    /** Velocidad máxima permitida para el objeto. */
    protected double maxVel;
    /** Ancho del objeto basado en su textura. */
    protected int width;
    /** Alto del objeto basado en su textura. */
    protected int height;
    /** Referencia al estado global del juego para acceder a otras entidades y efectos. */
    protected GameState gameState;

    /** Efecto de sonido que se reproduce al destruirse el objeto. */
    private Sound explosion;

    /** Estado de vida del objeto. */
    protected boolean Dead;

    /**
     * Construye un nuevo MovingObject inicializando sus propiedades físicas y sonoras.
     *
     * @param position  Posición inicial.
     * @param velocity  Vector de velocidad inicial.
     * @param maxVel    Velocidad máxima permitida.
     * @param texture   Imagen que representa al objeto.
     * @param gameState Estado del juego para la gestión de colisiones.
     */
    public MovingObject(Vector2D position, Vector2D velocity, double maxVel,
                        BufferedImage texture, GameState gameState) {
        super(position, texture);
        this.velocity  = velocity;
        this.maxVel    = maxVel;
        this.gameState = gameState;
        width          = texture.getWidth();
        height         = texture.getHeight();
        angle          = 0;
        explosion      = new Sound(Assets.explosion);
        Dead           = false;
    }

    /**
     * Evalúa colisiones con otros objetos en la pantalla local.
     *
     * Reglas de filtrado:
     * 1. Jugadores REMOTOS nunca colisionan en esta PC — sus colisiones
     *    las procesa su propia PC y se notifican por YOU_DIED.
     * 2. Jugadores locales fuera de pantalla: ignorar.
     * 3. Si YO soy remoto, no proceso colisiones.
     * 4. Si YO soy jugador local fuera de pantalla, no colisionar.
     */
    protected void collidesWith() {
        ArrayList<MovingObject> movingObjects = gameState.getMovingObjects();
        for (int i = 0; i < movingObjects.size(); i++) {
            MovingObject m = movingObjects.get(i);
            if (m.equals(this)) continue;

            if (m instanceof Player) {
                Player p = (Player) m;
                // Solo colisionar con jugador (local o remoto) si está físicamente aquí
                if (!p.isOnMyScreen()) continue;
                // Jugador spawneando: inmune
                if (p.isSpawning()) continue;
            }

            // Si YO soy jugador fuera de pantalla, no colisionar
            if (this instanceof Player && !((Player) this).isOnMyScreen()) continue;

            double distance = m.getCenter().subtract(getCenter()).getMagnitude();
            if (distance < m.width / 2 + width / 2
                    && movingObjects.contains(this) && !m.Dead && !Dead) {
                objectCollision(m, this);
            }
        }
    }

    /**
     * Gestiona las consecuencias de una colisión confirmada entre dos objetos.
     * Nunca destruye jugadores remotos — eso lo hace el otro PC.
     *
     * @param a Primer objeto involucrado en la colisión.
     * @param b Segundo objeto involucrado en la colisión.
     */
    private void objectCollision(MovingObject a, MovingObject b) {
        // Dos jugadores no se destruyen entre sí
        if (a instanceof Player && b instanceof Player) return;
        // Jugador spawneando: inmune
        if (a instanceof Player && ((Player) a).isSpawning()) return;
        if (b instanceof Player && ((Player) b).isSpawning()) return;
        // Dos meteoros no colisionan
        if (a instanceof Meteor && b instanceof Meteor) return;

        gameState.playExplosion(getCenter());
        a.destroy();
        b.destroy();
    }

    /**
     * Marca al objeto como muerto y reproduce el sonido de explosión.
     * Los objetos de tipo {@link Laser} no disparan el sonido de explosión por defecto.
     */
    protected void destroy() {
        Dead = true;
        if (!(this instanceof Laser))
            explosion.play();
    }

    /**
     * Calcula el punto central del objeto basándose en su posición y dimensiones.
     *
     * @return Un {@link Vector2D} que representa el centro del objeto.
     */
    protected Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2, position.getY() + height / 2);
    }

    /**
     * Verifica si el objeto ha sido destruido.
     *
     * @return {@code true} si el objeto está muerto, {@code false} de lo contrario.
     */
    public boolean isDead() { return Dead; }

    /**
     * Obtiene el ángulo actual de rotación.
     *
     * @return Ángulo en radianes.
     */
    public double getAngle() { return angle; }

    /**
     * Establece el ángulo de rotación del objeto.
     *
     * @param a Nuevo ángulo en radianes.
     */
    public void setAngle(double a) { this.angle = a; }
}