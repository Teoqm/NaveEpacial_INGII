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

public class Player extends MovingObject {

    public enum InputScheme { ARROWS, WASD }

    private final InputScheme inputScheme;

    // Tipos de mensaje según qué jugador es
    private final MessageType typeState;
    private final MessageType typeCross;
    private final MessageType typeSpawnLaser;
    private final MessageType typeYouDied;

    private Vector2D    heading;
    private Vector2D    acceleration;
    private boolean     accelerating = false;
    private Chronometer fireRate;
    private boolean     spawning, visible;
    private Chronometer spawnTime, flickerTime;
    private Sound       shoot, lose;

    private boolean   isRemote   = false;
    private boolean   onMyScreen = true;
    private UdpClient udpClient;

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

    public void setRemote(boolean remote)      { this.isRemote   = remote; }
    public void setOnMyScreen(boolean value)   { this.onMyScreen = value;  }
    public void setUdpClient(UdpClient client) { this.udpClient  = client; }
    public boolean isRemote()                  { return isRemote;   }
    public boolean isOnMyScreen()              { return onMyScreen; }
    public boolean isSpawning()                { return spawning;   }

    private boolean keyUp()    { return inputScheme == InputScheme.ARROWS ? KeyBoard.UP    : KeyBoard.UP2;    }
    private boolean keyLeft()  { return inputScheme == InputScheme.ARROWS ? KeyBoard.LEFT  : KeyBoard.LEFT2;  }
    private boolean keyRight() { return inputScheme == InputScheme.ARROWS ? KeyBoard.RIGHT : KeyBoard.RIGHT2; }
    private boolean keyShoot() { return inputScheme == InputScheme.ARROWS ? KeyBoard.SHOOT : KeyBoard.SHOOT2; }

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

        if (position.getY() > Constants.HEIGHT) position.setY(0);
        if (position.getY() < -height)          position.setY(Constants.HEIGHT);

        if (onMyScreen) collidesWith();

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

    @Override
    public void destroy() {
        lose.play();
        if (!isRemote) {
            boolean canRespawn = gameState.subtractLife(this);
            if (canRespawn) {
                // Tiene vidas propias: respawnea
                spawning = true;
                spawnTime.run(Constants.SPAWNING_TIME);
                resetValues();
            } else {
                // Sin vidas: desaparece permanentemente
                // gameOver() ya fue llamado dentro de subtractLife si ambos murieron
                super.destroy();
            }
        } else if (udpClient != null) {
            udpClient.send(new GameMessage(typeYouDied));
            spawning = true;
            spawnTime.run(Constants.SPAWNING_TIME);
            resetValues();
        }
    }

    private void resetValues() {
        angle      = 0;
        velocity   = new Vector2D();
        onMyScreen = true;
        position   = new Vector2D(
                Constants.WIDTH  / 2 - Assets.player.getWidth()  / 2,
                Constants.HEIGHT / 2 - Assets.player.getHeight() / 2);
    }

    public void setPosition(Vector2D pos) { this.position = pos; }
    public void setVelocity(Vector2D vel) { this.velocity = vel; }

    @Override
    public void draw(Graphics g) {
        if (!visible || !onMyScreen) return;

        Graphics2D g2d = (Graphics2D) g;

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

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2, height / 2);
        g2d.drawImage(texture, at, null);
    }
}