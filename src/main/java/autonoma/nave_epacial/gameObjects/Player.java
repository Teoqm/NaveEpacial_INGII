package autonoma.nave_epacial.gameObjects;


import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Player extends MovingObject {
    private Vector2D heading = new Vector2D((double)0.0F, (double)1.0F);
    private Vector2D acceleration = new Vector2D();
    //conatnte de aceleracion
    private final double ACC = 0.2;
    private final double DELTAANGLE = 0.1;
    private boolean accelerating = false;

    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture) {
        super(position, velocity, maxVel, texture);
    }

    public void update() {
        if (KeyBoard.RIGHT) {
            this.angle += 0.1;
        }

        if (KeyBoard.LEFT) {
            this.angle -= 0.1;
        }

        if (KeyBoard.UP) {
            this.acceleration = this.heading.scale(0.2);
            this.accelerating = true;
        } else {
            if (this.velocity.getMagnitude() != (double)0.0F) {
                this.acceleration = this.velocity.scale((double)-1.0F).normalize().scale(0.1);
            }

            this.accelerating = false;
        }

        this.velocity = this.velocity.add(this.acceleration);
        this.velocity = this.velocity.limit(this.maxVel);
        this.heading = this.heading.setDirection(this.angle - (Math.PI / 2D));
        this.position = this.position.add(this.velocity);
        if (this.position.getX() > (double)800.0F) {
            this.position.setX((double)0.0F);
        }

        if (this.position.getY() > (double)600.0F) {
            this.position.setY((double)0.0F);
        }

        if (this.position.getX() < (double)0.0F) {
            this.position.setX((double)800.0F);
        }

        if (this.position.getY() < (double)0.0F) {
            this.position.setY((double)600.0F);
        }

    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform at1 = AffineTransform.getTranslateInstance(this.position.getX() + (double)(this.width / 2) + 15, this.position.getY() + (this.height / 2) + 15);
        AffineTransform at2 = AffineTransform.getTranslateInstance(this.position.getX() + 5, this.position.getY() + (this.height / 2) + 15);
        at1.rotate(this.angle, -15, -15);
        at2.rotate(this.angle, (this.width / 2 - 5), -15);
        if (this.accelerating) {
            g2d.drawImage(Assets.speed, at1, (ImageObserver)null);
            g2d.drawImage(Assets.speed, at2, (ImageObserver)null);
        }

        this.at = AffineTransform.getTranslateInstance(this.position.getX(), this.position.getY());
        this.at.rotate(this.angle, (double)(this.width / 2), (double)(this.height / 2));
        g2d.drawImage(Assets.player, this.at, (ImageObserver)null);
    }
}

