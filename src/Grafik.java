import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * This is a class
 * Created 2021-03-16
 *
 * @author Magnus Silverdal
 */
public class Grafik extends Canvas implements Runnable {
    private int width = 800;
    private int height = 600;
    int fps = 30;
    private boolean isRunning;

    private BufferStrategy bs;
    //private BufferedImage image;

    private int manX, manY;
    private int manVX, manVY;

    private int treeX, treeY;
    private int treeVX, treeVY;

    private Thread thread;

    public Grafik() {
        JFrame frame = new JFrame("A simple painting");
        this.setSize(800,600);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);
        isRunning = false;

        manX = 300;
        manY = 150;
        manVX = 10;
        manVY = 0;
        treeX = 200;
        treeY = 200;
        treeVX = 0;
        treeVY = 0;
    }

    public void update () {
        manX += manVX;
        if (manX > width) {
            manVX = -manVX;
        }
        if (manX < 0) {
            manVX = -manVX;
        }
        treeX += treeVX;
        treeY += treeVY;

    }

    public void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        update();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        drawTree(g, 100,200);
        drawTree(g, treeX, treeY);
        drawMan(g, manX, manY);
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

    private class ML implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class MML implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}
