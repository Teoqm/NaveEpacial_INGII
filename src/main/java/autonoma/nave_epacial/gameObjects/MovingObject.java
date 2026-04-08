package autonoma.nave_epacial.gameObjects;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Sound;
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

    private Sound explosion;

    protected boolean Dead;

    public MovingObject(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, texture);
        this.velocity = velocity;
        this.maxVel = maxVel;
        this.gameState = gameState;
        width = texture.getWidth();
        height = texture.getHeight();
        angle = 0;
        explosion = new Sound(Assets.explosion);
        Dead = false;
    }

    protected void collidesWith() {
        ArrayList<MovingObject> movingObjects = gameState.getMovingObjects();
        for (int i = 0; i < movingObjects.size(); i++) {
            MovingObject m = movingObjects.get(i);
            if (m.equals(this)) continue;

            // Si m es un jugador, solo colisionar si está en ESTA pantalla
            if (m instanceof Player) {
                Player p = (Player) m;
                // Jugador remoto: solo si está en esta pantalla
                if (p.isRemote() && !p.isOnMyScreen()) continue;
                // Jugador local: solo si está en esta pantalla
                if (!p.isRemote() && !p.isOnMyScreen()) continue;
            }

            // Si YO soy un jugador local, solo colisionar si estoy en esta pantalla
            if (this instanceof Player) {
                Player me = (Player) this;
                if (!me.isRemote() && !me.isOnMyScreen()) continue;
            }

            double distance = m.getCenter().subtract(getCenter()).getMagnitude();
            if (distance < m.width / 2 + width / 2
                    && movingObjects.contains(this) && !m.Dead && !Dead) {
                objectCollision(m, this);
            }
        }
    }

    private void objectCollision(MovingObject a, MovingObject b) {
        // Dos jugadores no se destruyen entre sí
        if (a instanceof Player && b instanceof Player) return;
        if (a instanceof Player && ((Player) a).isSpawning()) return;
        if (b instanceof Player && ((Player) b).isSpawning()) return;
        if (a instanceof Meteor && b instanceof Meteor) return;

        gameState.playExplosion(getCenter());
        a.destroy();
        b.destroy();
    }


    protected void destroy(){
        Dead = true;
        if(!(this instanceof Laser))
            explosion.play();
    }

    protected Vector2D getCenter(){
        return new Vector2D(position.getX() + width/2, position.getY() + height/2);
    }

    public boolean isDead() {return Dead;}

    public double getAngle()          { return angle; }
    public void   setAngle(double a)  { this.angle = a; }
}
