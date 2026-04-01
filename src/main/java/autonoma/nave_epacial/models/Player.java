package autonoma.nave_epacial.models;

import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

    private Vector2D position;

    public Player(Vector2D positio, BufferedImage texture) {

        super(positio,texture);

    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(texture, (int) position.getX(), (int) position.getY(), null);



    }
}
