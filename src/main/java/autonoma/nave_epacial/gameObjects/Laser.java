package autonoma.nave_epacial.gameObjects;


import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Laser extends MovingObject {
    public Laser(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture) {
        super(position, velocity, maxVel, texture);
        this.angle = angle;
        this.velocity = velocity.scale(maxVel);
    }

    public void update() {
        this.position = this.position.add(this.velocity);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        this.at = AffineTransform.getTranslateInstance(this.position.getX() - (double)(this.width / 2), this.position.getY());
        this.at.rotate(this.angle, (double)(this.width / 2), (double)0.0F);
        g2d.drawImage(this.texture, this.at, (ImageObserver)null);
    }
}
