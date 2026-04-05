package autonoma.nave_epacial.models;


import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Player extends MovingObject {

    private Vector2D heading;

    public Player(Vector2D position, Vector2D velocity, BufferedImage texture) {

        super(position, velocity, texture);

        heading = new Vector2D(0,1);
    }

    @Override
    public void update() {

        if(KeyBoard.RIGHT) {
            heading = heading.setDirection(angle - Math.PI / 2);
        }

        if (KeyBoard.LEFT) {
            heading = heading.setDirection(angle + Math.PI / 2);
        }
    }

    @Override
    public void draw(Graphics g) {

       // g.drawImage(texture, (int) position.getX(), (int) position.getY(), null);

        Graphics2D g2d = (Graphics2D)g;

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        at.rotate(angle, Assets.player.getWidth()/2, Assets.player.getHeight()/2);

        g2d.drawImage(Assets.player, at, null);



    }
}
