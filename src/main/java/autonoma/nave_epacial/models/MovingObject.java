package autonoma.nave_epacial.models;

import autonoma.nave_epacial.math.Vector2D;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class MovingObject extends GameObject {

    protected Vector2D velocity;
    protected AffineTransform at;
    protected double angle;
    protected double maxVel;

    public MovingObject(Vector2D position,Vector2D velocity ,double maxVel,BufferedImage texture) {
        super (position,texture);
        this.velocity = velocity;
        this.maxVel = maxVel;
        angle = 0;
    }

}
