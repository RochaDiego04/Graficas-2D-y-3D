package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class RellenoInundacion extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public RellenoInundacion() {
        setSize(400, 400);
        setTitle("Relleno de figuras con FloodFill");
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

    public void floodFill(int x, int y, Color targetColor, Color fillColor) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px >= 0 && px < getWidth() && py >= 0 && py < getHeight() &&
                    buffer.getRGB(px, py) == targetColor.getRGB()) {
                putPixel(px, py, fillColor);

                // Revisar los vecinos
                queue.add(new Point(px + 1, py));
                queue.add(new Point(px - 1, py));
                queue.add(new Point(px, py + 1));
                queue.add(new Point(px, py - 1));
            }
        }
    }


    public static void main(String[] args) {
        RellenoInundacion ventana = new RellenoInundacion();
        ventana.setVisible(true);

        ventana.drawBresenhamLine(100, 350, 50, 50);
        ventana.drawBresenhamLine(100, 350, 200, 200);
        ventana.drawBresenhamLine(100, 100, 50, 200);
        ventana.drawBresenhamLine(350, 350, 50, 200);

        ventana.floodFill(225, 125, Color.BLACK, Color.BLUE); // Para rellenar el rectángulo
    }

}

