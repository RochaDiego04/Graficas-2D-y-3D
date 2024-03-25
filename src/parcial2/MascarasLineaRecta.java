/* Líneas continuas o descontinuas con algoritmo de Bresenham */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class MascarasLineaRecta extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public MascarasLineaRecta() {
        setSize(400, 400);
        setTitle("Tipos de línea recta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

    public void drawLine(int x0, int x1, int y0, int y1, boolean continuous) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        int counter = 0;
        int mask = 0b11110000000000;
        int maskLength = Integer.toBinaryString(mask).length();

        while (x0 != x1 || y0 != y1) {
            if (continuous || (mask & (1 << counter)) != 0) {
                putPixel(x0, y0, Color.RED);
            }

            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }

            counter = (counter + 1) % maskLength; // 7 is the number of bits in the mask, cut the line
        }
    }

    public static void main(String[] args) {
        MascarasLineaRecta ventana = new MascarasLineaRecta();
        ventana.setVisible(true);
        ventana.drawLine(50, 300, 100, 100, true);
        ventana.drawLine(50, 300, 200, 200, false);
        ventana.drawLine(50, 300, 120, 300, false);
        ventana.drawLine(50, 300, 350, 50, false);
    }

}
