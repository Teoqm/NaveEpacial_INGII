package autonoma.nave_epacial.gui;

import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.input.KeyBoard;
import autonoma.nave_epacial.states.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Window extends JFrame implements Runnable{

    public static final int WIDTH = 800, HEIGHT = 600;
    private Canvas canvas;
    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    private final int FPS = 60;

    //teimpo en nano segindo
    private double TARGETTIME = 1000000000/FPS;
    private double delta = 0;
    private int AVERAGEFPS = FPS;

    private GameState gameState;
    private KeyBoard keyBoard;

    public Window()
    {
        setTitle("Space Ship Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        canvas = new Canvas();
        keyBoard = new KeyBoard();

        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
        canvas.addKeyListener(keyBoard);
    }




    private void update(){
        keyBoard.update();
        gameState.update();
    }

    private void draw(){
        bs = canvas.getBufferStrategy();

        if(bs == null)
        {
            canvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        //-----------COMIENZA A DIBIJAR------------

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, WIDTH, HEIGHT);

        gameState.draw(g);

        g.drawString(""+AVERAGEFPS, 10, 20);

        //---------------------
        g.dispose();
        bs.show();
    }

    private void init()
    {
        Assets.init();
        gameState = new GameState();
    }


    @Override
    public void run() {

        long now = 0;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;

        init();

        while(running)
        {
            now = System.nanoTime();
            delta += (now - lastTime)/TARGETTIME;
            time += (now - lastTime);
            lastTime = now;



            if(delta >= 1)
            {
                update();
                draw();
                delta --;
                frames ++;
            }
            if(time >= 1000000000)
            {
                AVERAGEFPS = frames;
                frames = 0;
                time = 0;

            }


        }

        stop();
    }

    public void start(){

        thread = new Thread(this);
        thread.start();
        running = true;


    }
    private void stop(){
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
