package autonoma.nave_epacial.models;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends MovingObject {

    private Vector2D heading;
    private final double ROTATION_SPEED = 0.01;

    public Player(Vector2D position, Vector2D velocity, BufferedImage texture) {
        super(position, velocity, texture);
        heading = new  Vector2D(0,1);
    }

    @Override
    public void update() {
        if (KeyBoard.RIGHT)
            angle += ROTATION_SPEED;
        else if (KeyBoard.LEFT)
            angle -= ROTATION_SPEED;

        heading = heading.setDirection(angle);
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, (double) Assets.player.getWidth() /2,  (double) Assets.player.getHeight() /2);
        g2d.drawImage(Assets.player,at,null);
    }
}
