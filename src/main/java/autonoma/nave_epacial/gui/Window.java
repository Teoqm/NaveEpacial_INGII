package autonoma.nave_epacial.gui;

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


}
