/* Grosor de línea*/

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GrosorLineas extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public GrosorLineas() {
        setSize(400, 400);
        setTitle("Grosor de linea");
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

    public void drawLine(int x0, int x1, int y0, int y1, int lineWidth) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;
        int x = x0;
        int y = y0;

        while (true) {
            // Draw the current point with the specified line width
            drawPixelWithWidth(x, y, lineWidth);

            // Check if we have reached the end point
            if (x == x1 && y == y1) break;

            // Calculate the next point
            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (err2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }


    private void drawPixelWithWidth(int x, int y, int lineWidth) {
        for (int i = -(lineWidth / 2); i <= lineWidth / 2; i++) {
            putPixel(x + i, y, Color.RED); // Vertical segments
            putPixel(x, y + i, Color.RED); // Horizontal segments
        }
    }


    public static void main(String[] args) {
        GrosorLineas ventana = new GrosorLineas();
        ventana.setVisible(true);
        ventana.drawLine(50, 100, 100, 300, 2); // línea con pendiente menor que 1
        ventana.drawLine(100, 300, 50, 100, 4); // línea con pendiente mayor que 1
        ventana.drawLine(100, 300, 150, 270, 1);
        ventana.drawLine(50, 350, 350, 350, 5);
    }

}
