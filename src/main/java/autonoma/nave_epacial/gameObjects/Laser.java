package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.GameMessage;
import autonoma.nave_epacial.network.MessageType;
import autonoma.nave_epacial.network.UdpClient;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Laser extends MovingObject {

    private static final int IMMUNITY_FRAMES = 5;
    private int immunityFrames = IMMUNITY_FRAMES;
    private UdpClient udpClient;

    public Laser(Vector2D position, Vector2D velocity, double maxVel,
                 double angle, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);
        this.angle    = angle;
        this.velocity = velocity.scale(maxVel);
    }

    public void setUdpClient(UdpClient client) { this.udpClient = client; }

    @Override
    public void update() {
        position = position.add(velocity);

        // Izquierda/derecha: transferir al otro PC
        if (position.getX() > Constants.WIDTH) {
            sendCross("RIGHT");
            destroy();
            return;
        }
        if (position.getX() < -width) {
            sendCross("LEFT");
            destroy();
            return;
        }

        // Arriba/abajo: destruir normal
        if (position.getY() < 0 || position.getY() > Constants.HEIGHT) {
            destroy();
            return;
        }

        // Inmunidad inicial: evita que el laser mate al jugador que lo disparó
        if (immunityFrames > 0) {
            immunityFrames--;
            return;
        }

        collidesWith();
    }

    private void sendCross(String side) {
        if (udpClient == null) return;
        udpClient.send(new GameMessage(MessageType.LASER_CROSS,
                side,
                String.valueOf(position.getY()),
                String.valueOf(velocity.getX()),
                String.valueOf(velocity.getY()),
                String.valueOf(angle)));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        at = AffineTransform.getTranslateInstance(position.getX() - width / 2, position.getY());
        at.rotate(angle, width / 2, 0);
        g2d.drawImage(texture, at, null);
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2, position.getY() + width / 2);
    }
}
