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
 * La clase Ufo representa a un enemigo inteligente que sigue una ruta predefinida
 * y dispara proyectiles dirigidos hacia la posición del jugador.
 * Utiliza un algoritmo de búsqueda de objetivos (seek) y una lista de nodos
 * para navegar por el espacio de juego.
 * * @author Gemini
 * @version 1.0
 */
public class Ufo extends MovingObject {

    /** Lista de puntos (nodos) que conforman la ruta de navegación del OVNI. */
    private ArrayList<Vector2D> path;

    /** Nodo actual al que el OVNI intenta dirigirse. */
    private Vector2D currentNode;

    /** Índice del nodo actual dentro de la lista de ruta. */
    private  int index;

    /** Indica si el OVNI aún tiene nodos pendientes por seguir en su ruta. */
    private  boolean following;

    /** Controla la cadencia de disparo del OVNI. */
    private  Chronometer fireRate;

    /** Efecto de sonido para los disparos del OVNI. */
    private Sound shoot;

    /**
     * Construye un nuevo Ufo con una ruta de movimiento y comportamiento de disparo.
     * * @param position  Posición inicial.
     * @param velocity  Vector de velocidad inicial.
     * @param maxVel    Velocidad máxima de desplazamiento.
     * @param texture   Imagen visual del OVNI.
     * @param path      Lista de coordenadas que definen la ruta a seguir.
     * @param gameState Referencia al estado del juego para interactuar con el jugador y láseres.
     */
    public Ufo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture,
               ArrayList<Vector2D> path,GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);
        this.path = path;
        index = 0;
        following = true;
        fireRate = new Chronometer();
        fireRate.run(Constants.UFO_FIRE_RATE);
        shoot = new Sound(Assets.ufoShoot);
    }

    /**
     * Calcula la fuerza necesaria para seguir el siguiente nodo de la ruta.
     * Cambia al siguiente nodo cuando el OVNI entra en el radio de proximidad definido.
     * * @return Vector2D con la fuerza de dirección hacia el nodo actual.
     */
    private Vector2D pathFollowing() {
        currentNode = path.get(index);

        double distanceToNode=currentNode.subtract(getCenter()).getMagnitude();

        if (distanceToNode<Constants.NODE_RADIUS) {
            index++;
            if (index>=path.size()) {
                following = false;
            }
        }
        return seekForce(currentNode);
    }

    /**
     * Calcula la fuerza de "búsqueda" (seek) para dirigirse hacia un objetivo.
     * * @param target Punto de destino hacia el cual se desea mover.
     * @return Vector de fuerza corregido por la velocidad actual.
     */
    private  Vector2D seekForce(Vector2D target) {
        Vector2D desiredVelocity = target.subtract(getPosition());
        desiredVelocity = desiredVelocity.normalize().scale(maxVel);
        return desiredVelocity.subtract(velocity);

    }

    /**
     * Actualiza la posición del OVNI siguiendo su ruta, gestiona el auto-despachado
     * si sale de los límites y ejecuta la lógica de disparo apuntando al jugador
     * con un margen de error aleatorio.
     */
    @Override
    public void update() {
        Vector2D pathFollowing;
        if (following) {
            pathFollowing=pathFollowing();
        }
        else{
            pathFollowing=new Vector2D();}

        pathFollowing=pathFollowing.scale((double) 1 /Constants.UFO_MASS);

        velocity=velocity.add(pathFollowing);

        velocity=velocity.limit(maxVel);
        position=position.add(velocity);

        if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT
                || position.getX() < 0 || position.getY() < 0) {
            destroy();
        }

        // Lógica de disparo dirigida al jugador
        if (!fireRate.isRunning()) {
            Vector2D toPlayer= gameState.getPlayer().getPosition().subtract(getCenter());
            toPlayer = toPlayer.normalize();

            double currentAngle = toPlayer.getAngle();

            // Añade variación aleatoria al ángulo de disparo
            currentAngle += Math.random()*Constants.UFO_ANGLE_RANGE - Constants.UFO_ANGLE_RANGE/2;

            if(toPlayer.getX() < 0) {
                currentAngle += currentAngle + Math.PI;
            }

            toPlayer= toPlayer.setDirection(currentAngle);

            Laser laser = new Laser(
                    getCenter().add(toPlayer.scale(width)),
                    toPlayer,
                    Constants.LASER_VEL,
                    currentAngle + Math.PI / 2,
                    Assets.redLaser,
                    gameState
            );
            gameState.getMovingObjects().add(0, laser);
            fireRate.run(Constants.UFO_FIRE_RATE);

            shoot.play();
        }

        if(shoot.getFramePosition() > 8500){
            shoot.stop();
        }

        angle+= 0.05;

        collidesWith();
        fireRate.update();
    }

    /**
     * Destruye el OVNI, otorgando los puntos correspondientes al jugador
     * y activando el efecto de explosión base.
     */
    @Override
    public void destroy(){
        gameState.addScore(Constants.UFO_SCORE, position);
        super.destroy();
    }

    /**
     * Dibuja el OVNI en pantalla con una rotación constante sobre su propio eje.
     * * @param g Contexto gráfico para el renderizado.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width/2, height/2);

        g2d.drawImage(texture, at, null);

    }
}