import javafx.scene.canvas.GraphicsContext;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is a class
 * Created 2021-03-16
 *
 * @author Magnus Silverdal
 */
public class Grafik extends Canvas implements Runnable {
    private int width = 1440;
    private int height = 980;
    int fps = 60;
    private boolean isRunning;

    private BufferStrategy bs;

    private Rectangle bird;
    private double birdVX, birdVY;
    private BufferedImage birdImg;

    private Rectangle man;
    private int manVX, manVY;

    private Rectangle tree;
    private int treeVX, treeVY;

    private Thread thread;

    Pipe[] pipeList = new Pipe[5];

    public Grafik() {
        JFrame frame = new JFrame("A simple painting");
        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);
        isRunning = false;
        for (int i = 0; i <= 4; i++) {
            pipeList[i] = new Pipe();
            pipeList[i].pipe.x = i*1000;
            pipeList[i].pipe.y = ThreadLocalRandom.current().nextInt(300, height + 300-300);
        }

        try {
            birdImg = ImageIO.read(new File("bird.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bird = new Rectangle(300,100,100,100);
        man = new Rectangle(200,200,100,100);
        manVX = 10;
        manVY = 5;
        tree = new Rectangle(200,200,100,100);
        treeVX = 0;
        treeVY = 0;
    }

    public void update () {
        for (int i = 0; i <= 4; i++) {
            pipeList[i].updatePipe();
        }
        man.x += manVX;
        man.y += manVY;
        if (man.x > width - man.width) {
            manVX = -manVX;
        }
        if (man.x < 0) {
            manVX = -manVX;
        }
        if (man.y > height - man.height) {
            manVY = -manVY;
        }
        if (man.y < 0) {
            manVY = -manVY;
        }
        tree.x += treeVX;
        tree.y += treeVY;
        if (man.intersects(tree)) {
            if (man.x < tree.x+tree.width && man.y > tree.y && manVY > 0)  {
                manVX = -manVX;
            } else if (man.x < tree.x+tree.width && man.y < tree.y && manVY > 0)  {
                manVY = -manVY;
            }
        }
        birdVY += 0.5;
        bird.y += birdVY;
        if (bird.y <= 0-bird.height) {
            System.exit(0);
        }
        if (bird.y >= height) {
            System.exit(0);
        }
    }

    public void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        //update();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        drawTree(g, 100,200);
        drawTree(g, tree.x, tree.y);
        drawMan(g, man.x, man.y);
        for (int i = 0; i <= 4; i++) {
            pipeList[i].drawPipe(g);
        }
        g.drawImage(birdImg, bird.x, bird.y, bird.width+100, bird.height+100, null);
        g.dispose();
        bs.show();
    }

    private void drawTree(Graphics g, int x, int y) {
        g.setColor(new Color(0,128,0));
        g.fillOval(x-10,y-10,40,30);
        g.setColor(new Color(200,128,30));
        g.fillRect(7+x,20+y,6,20);
    }

    private void drawMan(Graphics g, int x, int y) {
        g.setColor(new Color(241, 235, 149));
        g.fillOval(x,y,70,70);
        g.setColor(new Color(222, 222, 222));
        g.fillOval(x+35-5-10,y+35-10,10,10);
        g.fillOval(x+35-5+10,y+35-10,10,10);
        g.setColor(new Color(21, 21, 21));
        g.fillOval(x+35-5-5,y+35-7,5,5);
        g.fillOval(x+35-5+10,y+35-7,5,5);
    }

    public static void main(String[] args) {
        Grafik painting = new Grafik();
        painting.start();
    }

    private synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double deltaT = 1000.0/fps;
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long now = System.currentTimeMillis();
            if(now-lastTime > deltaT) {
                update();
                draw();
                lastTime = now;
            }
        }
        stop();
    }

    private class KL implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                treeVX = -5;
            }
            if (keyEvent.getKeyChar() == 'd') {
                treeVX = 5;
            }
            if (keyEvent.getKeyChar() == 'w') {
                treeVY = -5;
                birdVY = -10;
            }
            if (keyEvent.getKeyChar() == 's') {
                treeVY = 5;
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                treeVX = 0;
            }
            if (keyEvent.getKeyChar() == 'd') {
                treeVX = 0;
            }
            if (keyEvent.getKeyChar() == 'w') {
                treeVY = 0;
            }
            if (keyEvent.getKeyChar() == 's') {
                treeVY = 0;
            }
        }
    }

    private class Pipe {
        private Rectangle pipe = new Rectangle(width,500,150,400);
        private int pipeVX = -5;
        private BufferedImage pipeImg;
        {
            try {
                pipeImg = ImageIO.read(new File("pipe.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void updatePipe() {
            pipe.x += pipeVX;
            if (bird.intersects(pipe)) {
                System.exit(0);
            }
            if (pipe.x == -200) {
                pipe.x = width;
                pipe.y = ThreadLocalRandom.current().nextInt(300, height-300);
            }
        }
        void drawPipe(Graphics g) {
            g.drawImage(pipeImg, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
    }
}
