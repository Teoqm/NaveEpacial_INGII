package autonoma.nave_epacial.gameObjects;

public class Chronometer {
    private double delta, lastTime;
    private long time;
    private boolean running;

    public Chronometer() {
        this.delta = 0;
        this.lastTime = System.currentTimeMillis();
        this.running = false;
    }

    public void run (long time)
    {
        this.running = true;
        this.time = time;
    }

    public void update () {
        if  (this.running)
            this.delta += System.currentTimeMillis() - this.lastTime;

        if (this.delta >=this.time) {
            this.running = false;
            this.delta = 0;
        }
        this.lastTime = System.currentTimeMillis();
    }

    public boolean isRunning ()
    {
        return this.running;
    }
}
