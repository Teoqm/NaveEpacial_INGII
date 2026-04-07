package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.states.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ufo extends MovingObject {

    private ArrayList<Vector2D> path;

    private Vector2D currentNode;

    private  int index;

    private  boolean following;

    private  Chronometer fireRate;

    public Ufo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture,
               ArrayList<Vector2D> path,GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);
        this.path = path;
        index = 0;
        following = true;
        fireRate = new Chronometer();
        fireRate.run(Constants.UFO_FIRE_RATE);
    }

    private Vector2D pathFollowing() {
        currentNode = path.get(index);

        double distanceToNode=currentNode.subtract(getCenter()).getMagnitude();

        if (distanceToNode<Constants.NODE_RADIUS) {
            index++;
            if (index>=path.size()) {
                following = false;
            }
        }
        return seekForce(currentNode);
    }

    private  Vector2D seekForce(Vector2D target) {
        Vector2D desiredVelocity = target.subtract(getPosition());
        desiredVelocity = desiredVelocity.normalize().scale(maxVel);
        return desiredVelocity.subtract(velocity);

    }

    @Override
    public void update() {
        Vector2D pathFollowing;
        if (following) {
            pathFollowing=pathFollowing();
        }
        else{
            pathFollowing=new Vector2D();}

        pathFollowing=pathFollowing.scale((double) 1 /Constants.UFO_MASS);

        velocity=velocity.add(pathFollowing);

        velocity=velocity.limit(maxVel);
        position=position.add(velocity);

        if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT
                || position.getX() < 0 || position.getY() < 0) {
            destroy();
        }

        //shoot

        if (!fireRate.isRunning()) {
            Vector2D toPlayer= gameState.getPlayer().getPosition().subtract(getCenter());
            toPlayer = toPlayer.normalize();

            double currentAngle = toPlayer.getAngle();

            currentAngle += Math.random()*Constants.UFO_ANGLE_RANGE - Constants.UFO_ANGLE_RANGE/2;

            if(toPlayer.getX() < 0) {
                currentAngle += currentAngle + Math.PI;
            }

            toPlayer= toPlayer.setDirection(currentAngle);

            Laser laser = new Laser(
                    getCenter().add(toPlayer.scale(width)),
                    toPlayer,
                    Constants.LASER_VEL,
                    currentAngle + Math.PI / 2,
                    Assets.redLaser,
                    gameState
            );
            gameState.getMovingObjects().add(0, laser);
            fireRate.run(Constants.UFO_FIRE_RATE);
        }

        angle+= 0.05;

        collidesWith();
        fireRate.update();
    }

    @Override
    public void destroy(){
        gameState.addScore(Constants.UFO_SCORE, position);
        super.destroy();
     }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width/2, height/2);

        g2d.drawImage(texture, at, null);

    }
}
