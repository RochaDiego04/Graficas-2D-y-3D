/* Bresenham algorithm to draw straight lines*/

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AlgoritmoBresenham extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public AlgoritmoBresenham() {
        setSize(400, 400);
        setTitle("Algoritmo Bresenham");
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

    public void drawBresenhamLine(int x0, int x1, int y0, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        while (x0 != x1 || y0 != y1) {
            putPixel(x0, y0, Color.RED);
            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }
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
            }s
        }
    }*/

    public static void main(String[] args) {
        AlgoritmoBresenham ventana = new AlgoritmoBresenham();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawBresenhamLine(50, 380, 50, 300);
    }

}

