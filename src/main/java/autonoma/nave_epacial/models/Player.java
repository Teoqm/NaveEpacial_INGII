package autonoma.nave_epacial.models;


import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Player extends MovingObject {

    private Vector2D heading;
    private Vector2D acceleration;
        //constante de aceleracion
    private final double ACC = 0.08;

    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture) {

        super(position, velocity,maxVel,texture);

        heading = new Vector2D(0,1);
        acceleration = new Vector2D();
    }

    @Override
    public void update() {

        if(KeyBoard.RIGHT) {
            angle += Math.PI/20;
        }else if(KeyBoard.LEFT) {
            angle -= Math.PI/20;
        }

        if(KeyBoard.UP) {

            this.acceleration =  heading.scale(ACC) ;
        }

        velocity = velocity.add(acceleration);

        velocity.limit(maxVel);

        heading = heading.setDirection(angle - Math.PI / 2);

        position = position.add(velocity);

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
