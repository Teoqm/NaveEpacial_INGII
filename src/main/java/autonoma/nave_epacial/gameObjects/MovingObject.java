package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class MovingObject extends GameObject {
    protected Vector2D velocity;
    protected AffineTransform at;
    protected double angle;
    protected double maxVel;
    protected int width;
    protected int height;
    protected GameState gameState;

    public MovingObject(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, texture);
        this.velocity = velocity;
        this.maxVel = maxVel;
        this.gameState = gameState;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.angle = (double)0.0F;
    }
}
