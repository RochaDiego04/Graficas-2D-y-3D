/* Circunference using Bresenham algorithm */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CircunferenciaBresenham extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public CircunferenciaBresenham() {
        setSize(400, 400);
        setTitle("Circunferencia de Bresenham");
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

    public void drawBresenhamCircumference(int centerX, int centerY, int radius) {
        int x = 0;
        int y = radius;
        int p = 3 - 2 * radius;

        drawCirclePoints(centerX, centerY, x, y);

        while (x <= y) {
            x++;
            if (p < 0) {
                p += 4 * x + 6;
            } else {
                y--;
                p += 4 * (x - y) + 10;
            }
            drawCirclePoints(centerX, centerY, x, y);
        }
    }
    private void drawCirclePoints(int centerX, int centerY, int x, int y) {
        putPixel(centerX + x, centerY + y, Color.BLUE);
        putPixel(centerX - x, centerY + y, Color.BLUE);
        putPixel(centerX + x, centerY - y, Color.BLUE);
        putPixel(centerX - x, centerY - y, Color.BLUE);
        putPixel(centerX + y, centerY + x, Color.BLUE);
        putPixel(centerX - y, centerY + x, Color.BLUE);
        putPixel(centerX + y, centerY - x, Color.BLUE);
        putPixel(centerX - y, centerY - x, Color.BLUE);
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
        CircunferenciaBresenham ventana = new CircunferenciaBresenham();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawBresenhamCircumference(200, 200, 150);
    }

}

