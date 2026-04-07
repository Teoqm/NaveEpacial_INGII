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

    private boolean spawning,visible;

    private Chronometer spawnTime, flickerTime;



    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVel, texture,  gameState);
        this.heading = new Vector2D(0, 1);
        this.acceleration = new Vector2D();
        fireRate = new Chronometer();
    }

    public void update() {


        if(!spawnTime.isRunning()){
            spawning = false;
            visible = true;
        }

        if(spawning){

            if(! flickerTime.isRunning()){

                flickerTime.run(Constants.FLICKER_TIME);
                visible = !visible;
            }

        }


        if (KeyBoard.SHOOT && !fireRate.isRunning() && !spawning) {
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
        spawnTime.update();
        flickerTime.update();
        collidesWith();
    }

    @Override
    public void destroy() {
        spawning = true;
        spawnTime.run(Constants.SPAWNING_TIME);
        resetValues();
        gameState.subtractLife();
    }

    @Override
    public void draw(Graphics g) {

        if(!visible)
            return;

        Graphics2D g2d = (Graphics2D)g;

        AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() + width/2 + 5,
                position.getY() + height/2 + 10);

        AffineTransform at2 = AffineTransform.getTranslateInstance(position.getX() + 5, position.getY() + height/2 + 10);

        at1.rotate(angle, -5, -10);
        at2.rotate(angle, width/2 -5, -10);

        if(accelerating)
        {
            g2d.drawImage(Assets.speed, at1, null);
            g2d.drawImage(Assets.speed, at2, null);
        }



        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        at.rotate(angle, width/2, height/2);

        g2d.drawImage(texture, at, null);

    }

    public boolean isSpawning() {return spawning;}


}

