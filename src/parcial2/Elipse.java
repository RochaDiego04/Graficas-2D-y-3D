package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Elipse extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public Elipse() {
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

    public void drawEllipse(int centerX, int centerY, int radiusMajor, int radiusMinor) {
        int x = 0;
        int y = radiusMinor;
        int p1 = radiusMinor * radiusMinor - radiusMajor * radiusMajor * radiusMinor + radiusMajor * radiusMajor / 4;
        int dx = 2 * radiusMinor * radiusMinor * x;
        int dy = 2 * radiusMajor * radiusMajor * y;

        drawEllipsePoints(centerX, centerY, x, y);

        while (dx < dy) {
            x++;
            dx += 2 * radiusMinor * radiusMinor;
            if (p1 < 0) {
                p1 += dx + radiusMinor * radiusMinor;
            } else {
                y--;
                dy -= 2 * radiusMajor * radiusMajor;
                p1 += dx - dy + radiusMinor * radiusMinor;
            }
            drawEllipsePoints(centerX, centerY, x, y);
        }

        int p2 = (int) (radiusMinor * radiusMinor * (x + 0.5) * (x + 0.5) + radiusMajor * radiusMajor * (y - 1) * (y - 1) - radiusMajor * radiusMajor * radiusMinor * radiusMinor);

        while (y >= 0) {
            y--;
            dy -= 2 * radiusMajor * radiusMajor;
            if (p2 > 0) {
                p2 += radiusMajor * radiusMajor - dy;
            } else {
                x++;
                dx += 2 * radiusMinor * radiusMinor;
                p2 += dx - dy + radiusMajor * radiusMajor;
            }
            drawEllipsePoints(centerX, centerY, x, y);
        }
    }

    private void drawEllipsePoints(int centerX, int centerY, int x, int y) {
        putPixel(centerX + x, centerY + y, Color.BLUE);
        putPixel(centerX - x, centerY + y, Color.BLUE);
        putPixel(centerX + x, centerY - y, Color.BLUE);
        putPixel(centerX - x, centerY - y, Color.BLUE);
    }

    public static void main(String[] args) {
        Elipse ventana = new Elipse();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawEllipse(200, 200, 150, 100);
    }

}

