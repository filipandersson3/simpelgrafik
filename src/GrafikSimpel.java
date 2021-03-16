import javax.swing.*;
import java.awt.*;

/**
 * This is a class
 * Created 2021-03-16
 *
 * @author Magnus Silverdal
 */
public class GrafikSimpel extends Canvas {
    public GrafikSimpel() {
        JFrame frame = new JFrame("A simple painting");
        this.setSize(800,600);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(200,200,80,20);
        g.setColor(new Color(255,127,0));
        g.drawOval(200,200,80,20);
        drawTree(g, 100,200);
        drawMan(g,200,300);
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
        GrafikSimpel painting = new GrafikSimpel();
    }

}
