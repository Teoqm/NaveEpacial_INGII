package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.GameMessage;
import autonoma.nave_epacial.network.MessageType;
import autonoma.nave_epacial.network.UdpClient;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Meteor extends MovingObject {

    private final Size      size;
    private UdpClient udpClient;

    public Meteor(Vector2D position, Vector2D velocity, double maxVel,
                  BufferedImage texture, GameState gameState, Size size) {
        super(position, velocity, maxVel, texture, gameState);
        this.size     = size;
        this.velocity = velocity.scale(maxVel);
    }

    public void setUdpClient(UdpClient client) { this.udpClient = client; }

    @Override
    public void update() {
        position = position.add(velocity);

        if (position.getX() > Constants.WIDTH) {
            sendCross("RIGHT");
            destroyQuiet();
            return;
        }
        if (position.getX() < -width) {
            sendCross("LEFT");
            destroyQuiet();
            return;
        }

        if (position.getY() > Constants.HEIGHT) position.setY(-height);
        if (position.getY() < -height)          position.setY(Constants.HEIGHT);

        angle += Constants.DELTAANGLE / 2;

        // FIX: verificar colisiones con jugadores y lasers
        collidesWith();
    }

    private void sendCross(String side) {
        if (udpClient == null) return;
        udpClient.send(new GameMessage(MessageType.METEOR_CROSS,
                side,
                String.valueOf(position.getY()),
                String.valueOf(velocity.getX()),
                String.valueOf(velocity.getY()),
                String.valueOf(maxVel),
                size.name()));
    }

    /** Destrucción silenciosa: solo lo elimina de la lista, sin dividir ni puntuar. */
    private void destroyQuiet() {
        super.destroy();
    }

    /** Destrucción real: divide y da puntos. Solo ocurre al ser golpeado. */
    @Override
    public void destroy() {
        gameState.divideMeteor(this);
        gameState.addScore(Constants.METEOR_SCORE, position);
        super.destroy();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width / 2, height / 2);
        g2d.drawImage(texture, at, null);
    }

    public Size getSize() { return size; }
}
