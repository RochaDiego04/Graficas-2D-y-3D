package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RellenoScanLine extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public RellenoScanLine() {
        setSize(400, 400);
        setTitle("Relleno de figuras con ScanLine");
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

    public void fillPolygon(int[] xPoints, int[] yPoints, Color color) {

        // Calcular el punto mínimo en X,Y
        int minY = Arrays.stream(yPoints).min().getAsInt();
        int maxY = Arrays.stream(yPoints).max().getAsInt();

        // Iterar en el eje vertical
        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();
            for (int i = 0; i < xPoints.length; i++) {
                int x1 = xPoints[i];
                int y1 = yPoints[i];
                int x2 = xPoints[(i + 1) % xPoints.length];
                int y2 = yPoints[(i + 1) % yPoints.length];

                if ((y1 <= y && y2 >= y) || (y2 <= y && y1 >= y)) {
                    if (y1 != y2) {
                        int x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                        intersections.add(x);
                    } else if (y == y1 && ((y1 == minY && y2 != minY) || (y1 == maxY && y2 != maxY))) {
                        intersections.add(x1);
                    }
                }
            }

            intersections.sort(Integer::compareTo);
            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);
                for (int x = xStart; x <= xEnd; x++) {
                    putPixel(x, y, color);
                }
            }
        }
    }

    public static void main(String[] args) {
        RellenoScanLine ventana = new RellenoScanLine();
        ventana.setVisible(true);

        // Draw Rectangle
        int[] xPoints = {100, 350, 350, 100};
        int[] yPoints = {50, 50, 200, 200};


        long startTime = System.currentTimeMillis();
        ventana.fillPolygon(xPoints, yPoints, Color.BLUE);
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " milisegundos");



        ventana.drawBresenhamLine(100, 350, 50, 50);
        ventana.drawBresenhamLine(100, 350, 200, 200);
        ventana.drawBresenhamLine(100, 100, 50, 200);
        ventana.drawBresenhamLine(350, 350, 50, 200);

        // Draw Triangle
        int[] x2Points = {200, 100, 300};
        int[] y2Points = {200, 300, 300};
        ventana.fillPolygon(x2Points, y2Points, Color.BLUE);

        ventana.drawBresenhamLine(100, 200, 300, 200);
        ventana.drawBresenhamLine(200, 300, 200, 300);
        ventana.drawBresenhamLine(100, 300, 300, 300);
    }

}

