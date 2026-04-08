package autonoma.nave_epacial.graphics;

import autonoma.nave_epacial.math.Vector2D;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class Animation {
    private BufferedImage[] frames;
    private int velocity;
    private int index;
    private boolean running;
    private Vector2D position;
    private long time, lasTime;

    public  Animation(BufferedImage[] frames, int velocity, Vector2D position) {
        this.frames = frames;
        this.velocity = velocity;
        this.position = position;
        index = 0;
        this.running = true;
        time  = 0;
        lasTime = System.currentTimeMillis();
    }

    public void update(){
        time += System.currentTimeMillis()-lasTime;
        lasTime = System.currentTimeMillis();

        if(time > velocity){
            time=0;
            index++;
            if(index>=frames.length){
                running = false;
            }
        }
    }


    public boolean isRunning(){
        return running;
    }

    public Vector2D getPosition(){
        return position;
    }

    public BufferedImage getCurrentFrame(){
        return frames[index];
    }

}

