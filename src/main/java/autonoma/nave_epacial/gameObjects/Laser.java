package autonoma.nave_epacial.gameObjects;


import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Laser extends MovingObject{

    public Laser(Vector2D position, Vector2D velocity, double maxVel,double angle, BufferedImage texture) {
        super(position, velocity, maxVel, texture);
        this.angle = angle;
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics var1) {

    }
}
