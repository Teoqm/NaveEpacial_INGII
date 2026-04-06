package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

    protected void collidesWith(){
        ArrayList<MovingObject> movingObjects= gameState.getMovingObjects();

        for(int i=0; i<movingObjects.size(); i++){

            MovingObject m = movingObjects.get(i);

            if(m.equals(this)){
                continue;
            }
            double distance=m.getCenter().subtract(getCenter()).getMagnitude();

            if (distance < m.width/2+ width/2 && movingObjects.contains(this)){
                objectCollision(m, this);
            }
        }
    }

    private void objectCollision(MovingObject a, MovingObject b){
        if (!(a instanceof Meteor && b instanceof Meteor)){
            gameState.playExplosion(getCenter());
            a.destroy();
            b.destroy();
        }
    }

    protected void destroy(){
        gameState.getMovingObjects().remove(this);
    }

    protected Vector2D getCenter() {

        return new Vector2D(this.position.getX() + this.width / 2, this.position.getY() + this.height / 2);
    }
}
