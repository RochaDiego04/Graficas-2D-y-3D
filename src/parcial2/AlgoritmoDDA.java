/* DDA algorithm */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class AlgoritmoDDA extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public AlgoritmoDDA() {
        setSize(400, 400);
        setTitle("Algoritmo DDA");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graPixel = buffer.createGraphics();
    }
    public void putPixel(int x, int y, Color c) {
        buffer.setRGB(x, y, c.getRGB());
        this.getGraphics().drawImage(buffer, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public void drawDDALine(int x0, int x1, int y0, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int steps;

        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }

        double xinc = (double) dx / steps;
        double yinc = (double) dy / steps;

        double x = x0;
        double y = y0;

        for (int k = 0; k <= steps; k++) {
            putPixel((int) Math.round(x), (int) Math.round(y), Color.RED);
            x += xinc;
            y += yinc;
        }
    }

    /*public void drawGrid() {
        // Vertical Lines
        for (int x = 0; x <= getWidth(); x += 10) {
            for (int y = 0; y < getHeight(); y++) {
                if (x < getWidth()) {
                    putPixel(x, y, Color.GRAY);
                }
            }
        }

        // Horizontal Lines
        for (int y = 0; y <= getHeight(); y += 10) {
            for (int x = 0; x < getWidth(); x++) {
                if (y < getHeight()) {
                    putPixel(x, y, Color.GRAY);
                }
            }
        }
    }*/

    public static void main(String[] args) {
        AlgoritmoDDA ventana = new AlgoritmoDDA();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawDDALine(50, 380, 50, 300);
    }

}

