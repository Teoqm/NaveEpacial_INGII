package autonoma.nave_epacial.gui;

import autonoma.nave_epacial.images.Asst;
import com.sun.source.doctree.StartElementTree;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Window  extends javax.swing.JFrame implements Runnable {


    public  static final int    WIDTH = 800;
    public  static final int    HEIGHT = 600;
    private Canvas  canvas;
    private Thread  thread;
    private  boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    private final int FPS = 60;
    //teimepo en nano segundos
    private double TARGETTIME = 100000000/FPS;
    private double delta = 0;
    private int AVERAGEFPS = FPS;

    public Window() {
        setTitle("Nave-Epacial");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        setVisible(true);

        canvas = new Canvas();

        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setFocusable(true);
        add(canvas);
    }

    private void updates() {}

    private void draw() {

        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        //-------SE COMENZA DIBIJAR-------------------

        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        g.drawImage(Asst.player, 0, 0, 60, 60, null);
        //------------
        g.dispose();
        bs.show();
    }

    private void init(){
        Asst.init();
    }

    @Override
    public void run() {

        long now = 0;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;
        int nanoSegundo= 1000000000;

        init();

        while (running) {

            now = System.nanoTime();
            delta += (now - lastTime) / TARGETTIME;
            time += (now - lastTime);
            lastTime = now;

            if (delta >= 1) {
                updates();
                draw();
                delta--;
                frames++;
            }

            if (time >= nanoSegundo) {

                AVERAGEFPS = frames;
                frames = 0;
                time = 0;

            }
        }


        stop();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private void stop() {

        try {
            thread.join();
            running = false;
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
