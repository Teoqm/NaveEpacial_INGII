package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Sound;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.GameMessage;
import autonoma.nave_epacial.network.MessageType;
import autonoma.nave_epacial.network.UdpClient;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * La clase Player representa la nave controlada por el usuario o por un tercero a través de la red.
 * Gestiona el movimiento físico basado en aceleración, la rotación, el disparo de láseres,
 * y la lógica de red para sincronizar la posición y el estado entre diferentes computadoras.
 * * Soporta diferentes esquemas de control (Flechas o WASD) y maneja estados de invulnerabilidad
 * temporal mediante un efecto de parpadeo al reaparecer.
 * * @author Gemini
 * @version 1.0
 */
public class Player extends MovingObject {

    /** Esquemas de control disponibles para el jugador. */
    public enum InputScheme { ARROWS, WASD }

    /** El esquema de entrada asignado a esta instancia de jugador. */
    private final InputScheme inputScheme;

    // Tipos de mensaje según qué jugador es
    private final MessageType typeState;
    private final MessageType typeCross;
    private final MessageType typeSpawnLaser;
    private final MessageType typeYouDied;

    /** Vector que indica la dirección hacia donde apunta la frente de la nave. */
    private Vector2D    heading;
    /** Vector de aceleración aplicado al motor de la nave. */
    private Vector2D    acceleration;
    /** Indica si el jugador está presionando la tecla de aceleración. */
    private boolean     accelerating = false;
    /** Controla la cadencia de disparo. */
    private Chronometer fireRate;
    /** Estado de reaparición y visibilidad para el efecto de parpadeo. */
    private boolean     spawning, visible;
    /** Cronómetros para gestionar la duración del respawn y la frecuencia del parpadeo. */
    private Chronometer spawnTime, flickerTime;
    /** Efectos de sonido para disparar y perder una vida. */
    private Sound       shoot, lose;

    /** Indica si esta instancia representa a un jugador en otra computadora. */
    private boolean   isRemote   = false;
    /** Indica si el jugador está presente en la pantalla local actual. */
    private boolean   onMyScreen = true;
    /** Cliente UDP para enviar actualizaciones de estado al par remoto. */
    private UdpClient udpClient;

    /**
     * Construye un nuevo Player configurando sus sistemas de red, sonido y control.
     * * @param position  Coordenadas iniciales.
     * @param velocity  Vector de velocidad inicial.
     * @param maxVel    Velocidad máxima permitida.
     * @param texture   Imagen de la nave.
     * @param gameState Referencia al estado global del juego.
     * @param scheme    Esquema de controles (ARROWS o WASD).
     */
    public Player(Vector2D position, Vector2D velocity, double maxVel,
                  BufferedImage texture, GameState gameState, InputScheme scheme) {
        super(position, velocity, maxVel, texture, gameState);
        this.inputScheme = scheme;

        // Asignar tipos de mensaje según el esquema
        if (scheme == InputScheme.ARROWS) {
            typeState       = MessageType.PLAYER_STATE;
            typeCross       = MessageType.PLAYER_CROSS;
            typeSpawnLaser  = MessageType.SPAWN_LASER;
            typeYouDied     = MessageType.YOU_DIED;
        } else {
            typeState       = MessageType.PLAYER2_STATE;
            typeCross       = MessageType.PLAYER2_CROSS;
            typeSpawnLaser  = MessageType.SPAWN_LASER2;
            typeYouDied     = MessageType.YOU_DIED2;
        }

        heading     = new Vector2D(0, 1);
        acceleration = new Vector2D();
        fireRate    = new Chronometer();
        spawnTime   = new Chronometer();
        flickerTime = new Chronometer();
        shoot = new Sound(Assets.playerShoot);
        lose  = new Sound(Assets.playerLoose);
        visible = true;
    }

    /** @param remote Define si el jugador es controlado remotamente. */
    public void setRemote(boolean remote)      { this.isRemote   = remote; }
    /** @param value Define si el jugador es visible en esta pantalla local. */
    public void setOnMyScreen(boolean value)   { this.onMyScreen = value;  }
    /** @param client Cliente UDP para sincronización. */
    public void setUdpClient(UdpClient client) { this.udpClient  = client; }
    /** @return true si el jugador es remoto. */
    public boolean isRemote()                  { return isRemote;   }
    /** @return true si el jugador está en la pantalla del usuario. */
    public boolean isOnMyScreen()              { return onMyScreen; }
    /** @return true si el jugador está en proceso de reaparición (invulnerable). */
    public boolean isSpawning()                { return spawning;   }

    // Métodos auxiliares para abstraer la entrada de teclado según el esquema
    private boolean keyUp()    { return inputScheme == InputScheme.ARROWS ? KeyBoard.UP    : KeyBoard.UP2;    }
    private boolean keyLeft()  { return inputScheme == InputScheme.ARROWS ? KeyBoard.LEFT  : KeyBoard.LEFT2;  }
    private boolean keyRight() { return inputScheme == InputScheme.ARROWS ? KeyBoard.RIGHT : KeyBoard.RIGHT2; }
    private boolean keyShoot() { return inputScheme == InputScheme.ARROWS ? KeyBoard.SHOOT : KeyBoard.SHOOT2; }

    /**
     * Actualiza la lógica del jugador.
     * Si es local: procesa entradas, físicas, disparos y colisiones.
     * Si es remoto: solo actualiza la dirección y colisiones si es visible.
     * Gestiona también la transferencia de la nave entre pantallas mediante red.
     */
    @Override
    public void update() {
        if (isRemote) {
            heading = heading.setDirection(angle - Math.PI / 2);
            if (onMyScreen) collidesWith();
            return;
        }

        if (!spawnTime.isRunning()) { spawning = false; visible = true; }
        if (spawning && !flickerTime.isRunning()) {
            flickerTime.run(Constants.FLICKER_TIME);
            visible = !visible;
        }

        if (keyShoot() && !fireRate.isRunning() && !spawning) {
            Vector2D spawnPos = getCenter().add(heading.scale(width));
            if (onMyScreen) {
                gameState.getMovingObjects().add(0, new Laser(
                        spawnPos, heading, Constants.LASER_VEL,
                        angle, Assets.blueLaser, gameState));
            } else if (udpClient != null) {
                udpClient.send(new GameMessage(typeSpawnLaser,
                        String.valueOf(spawnPos.getX()),
                        String.valueOf(spawnPos.getY()),
                        String.valueOf(angle)));
            }
            fireRate.run(Constants.FIRERATE);
            shoot.play();
        }
        if (shoot.getFramePosition() > 8500) shoot.stop();

        if (keyRight()) angle += Constants.DELTAANGLE;
        if (keyLeft())  angle -= Constants.DELTAANGLE;

        if (keyUp()) {
            acceleration = heading.scale(Constants.ACC);
            accelerating = true;
        } else {
            if (velocity.getMagnitude() != 0)
                acceleration = velocity.scale(-1).normalize().scale(Constants.ACC / 2);
            accelerating = false;
        }

        velocity = velocity.add(acceleration).limit(maxVel);
        heading  = heading.setDirection(angle - Math.PI / 2);
        position = position.add(velocity);

        // Lógica de cruce de pantalla horizontal (Transmisión UDP)
        if (position.getX() > Constants.WIDTH) {
            position.setX(0);
            onMyScreen = !onMyScreen;
            if (udpClient != null)
                udpClient.send(new GameMessage(typeCross,
                        "RIGHT",
                        String.valueOf(position.getY()),
                        String.valueOf(velocity.getX()),
                        String.valueOf(velocity.getY()),
                        String.valueOf(angle)));
        }
        if (position.getX() < -width) {
            position.setX(Constants.WIDTH);
            onMyScreen = !onMyScreen;
            if (udpClient != null)
                udpClient.send(new GameMessage(typeCross,
                        "LEFT",
                        String.valueOf(position.getY()),
                        String.valueOf(velocity.getX()),
                        String.valueOf(velocity.getY()),
                        String.valueOf(angle)));
        }

        // Envoltura vertical (Wrap-around)
        if (position.getY() > Constants.HEIGHT) position.setY(0);
        if (position.getY() < -height)          position.setY(Constants.HEIGHT);

        if (onMyScreen) collidesWith();

        // Envío de estado constante para sincronización de posición y ángulo
        if (udpClient != null)
            udpClient.send(new GameMessage(typeState,
                    String.valueOf(position.getX()),
                    String.valueOf(position.getY()),
                    String.valueOf(angle),
                    String.valueOf(onMyScreen)));

        fireRate.update();
        spawnTime.update();
        flickerTime.update();
    }

    /**
     * Gestiona la muerte del jugador. Resta una vida en el GameState local
     * y, si aún tiene vidas, inicia el proceso de reaparición.
     * Si es un jugador remoto, notifica al dueño de la nave que ha muerto.
     */
    @Override
    public void destroy() {
        lose.play();
        if (!isRemote) {
            boolean canRespawn = gameState.subtractLife(this);
            if (canRespawn) {
                spawning = true;
                spawnTime.run(Constants.SPAWNING_TIME);
                resetValues();
            } else {
                super.destroy();
            }
        } else if (udpClient != null) {
            udpClient.send(new GameMessage(typeYouDied));
            spawning = true;
            spawnTime.run(Constants.SPAWNING_TIME);
            resetValues();
        }
    }

    /** Restablece los valores físicos y de posición de la nave a su estado inicial. */
    private void resetValues() {
        angle      = 0;
        velocity   = new Vector2D();
        onMyScreen = true;
        position   = new Vector2D(
                Constants.WIDTH  / 2 - Assets.player.getWidth()  / 2,
                Constants.HEIGHT / 2 - Assets.player.getHeight() / 2);
    }

    /** @param pos Nueva posición del jugador. */
    public void setPosition(Vector2D pos) { this.position = pos; }
    /** @param vel Nuevo vector de velocidad. */
    public void setVelocity(Vector2D vel) { this.velocity = vel; }

    /**
     * Renderiza la nave y los efectos de propulsión (fuego) si está acelerando.
     * No dibuja nada si el jugador no es visible (durante el parpadeo de respawn)
     * o si la nave se encuentra en la pantalla del otro computador.
     * * @param g Contexto gráfico.
     */
    @Override
    public void draw(Graphics g) {
        if (!visible || !onMyScreen) return;

        Graphics2D g2d = (Graphics2D) g;

        // Transformaciones para el efecto de los propulsores traseros
        AffineTransform at1 = AffineTransform.getTranslateInstance(
                position.getX() + width / 2 + 5, position.getY() + height / 2 + 10);
        AffineTransform at2 = AffineTransform.getTranslateInstance(
                position.getX() + 5, position.getY() + height / 2 + 10);
        at1.rotate(angle, -5, -10);
        at2.rotate(angle, width / 2 - 5, -10);

        if (accelerating) {
            g2d.drawImage(Assets.speed, at1, null);
            g2d.drawImage(Assets.speed, at2, null);
        }

        // Renderizado del cuerpo principal de la nave
        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2, height / 2);
        g2d.drawImage(texture, at, null);
    }
}