package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Meteor extends MovingObject {

    private Size size;

    public Meteor(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState, Size size) {
        super(position, velocity, maxVel, texture, gameState);
        this.size = size;
        this.velocity=velocity.scale(maxVel);
    }

    @Override
    public void update() {
        position=position.add(velocity);

        if (this.position.getX() > Constants.WIDTH) {
            this.position.setX((double)-width);
        }

        if (this.position.getY() > Constants.HEIGHT) {
            this.position.setY((double)-height);
        }

        if (this.position.getX() < (double)-width) {
            this.position.setX(Constants.WIDTH);
        }

        if (this.position.getY() < (double)-height) {
            this.position.setY(Constants.HEIGHT);
        }

        angle += Constants.DELTAANGLE/2;
    }

    @Override
    public void destroy() {
        gameState.divideMeteor(this);
        gameState.addScore(Constants.METEOR_SCORE);
        super.destroy();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        this.at = AffineTransform.getTranslateInstance(this.position.getX(), this.position.getY());
        this.at.rotate(this.angle, (double)(this.width / 2), (double)(this.height / 2));
        g2d.drawImage(texture, this.at, (ImageObserver)null);
    }

    public Size getSize() {
        return this.size;
    }
}
