package autonoma.nave_epacial.gameObjects;


import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Player extends MovingObject {
    private Vector2D heading;
    private Vector2D acceleration;
    private final double ACC = 0.2;
    private final double DELTAANGLE = 0.1;
    private boolean accelerating = false;
    private Chronometer fireRate;


    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVel, texture,  gameState);
        this.heading = new Vector2D((double)0.0F, (double)1.0F);
        this.acceleration = new Vector2D();
        fireRate = new Chronometer();
    }

    public void update() {

        if (KeyBoard.SHOOT && !fireRate.isRunning()) {
            this.gameState.getMovingObjects().add(0, new Laser(this.getCenter().add(this.heading.scale((double)this.width)), this.heading, Constants.LASER_VEL, this.angle, Assets.blueLaser, gameState));
            fireRate.run(Constants.FIRERATE);
        }

        if (KeyBoard.RIGHT) {
            this.angle += 0.1;
        }

        if (KeyBoard.LEFT) {
            this.angle -= 0.1;
        }

        if (KeyBoard.UP) {
            this.acceleration = this.heading.scale(Constants.ACC);
            this.accelerating = true;
        } else {
            if (this.velocity.getMagnitude() != (double)0.0F) {
                this.acceleration = this.velocity.scale((double)-1.0F).normalize().scale(Constants.ACC/2);
            }

            this.accelerating = false;
        }

        this.velocity = this.velocity.add(this.acceleration);
        this.velocity = this.velocity.limit(this.maxVel);
        this.heading = this.heading.setDirection(this.angle - (Math.PI / 2D));
        this.position = this.position.add(this.velocity);
        if (this.position.getX() > Constants.WIDTH) {
            this.position.setX((double)0.0F);
        }

        if (this.position.getY() > Constants.HEIGHT) {
            this.position.setY((double)0.0F);
        }

        if (this.position.getX() < (double)0.0F) {
            this.position.setX(Constants.WIDTH);
        }

        if (this.position.getY() < (double)0.0F) {
            this.position.setY(Constants.HEIGHT);
        }

        fireRate.update();
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

    public Vector2D getCenter() {

        return new Vector2D(this.position.getX() + this.width / 2, this.position.getY() + this.height / 2);
    }
}

