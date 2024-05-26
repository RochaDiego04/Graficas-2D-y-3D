package parcial3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Cubo extends JFrame {

    private BufferedImage buffer;
    public Graphics graPixel;

    public Cubo() {
        setSize(700, 700);
        setTitle("Cubo");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graPixel = buffer.createGraphics();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public void drawBresenhamLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int e2;

        while (true) {
            putPixel(x0, y0, color);

            if (x0 == x1 && y0 == y1) {
                break;
            }

            e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    public void putPixel(int x, int y, Color c) {
        buffer.setRGB(x, y, c.getRGB());
        this.getGraphics().drawImage(buffer, 0, 0, this);
    }

    public void drawCube(int[] Point, int size, Color color) {
        size = size * 20;
        int[][] vertices = {
                {Point[0], Point[1], Point[2]},
                {Point[0] + size, Point[1], Point[2]},
                {Point[0] + size, Point[1] + size, Point[2]},
                {Point[0], Point[1] + size, Point[2]},
                {Point[0], Point[1], Point[2] + size},
                {Point[0] + size, Point[1], Point[2] + size},
                {Point[0] + size, Point[1] + size, Point[2] + size},
                {Point[0], Point[1] + size, Point[2] + size}
        };

        int[][] projectedVertices = new int[8][2];

        for (int i = 0; i < 8; i++) {
            projectedVertices[i] = project(vertices[i]);
        }

        // Draw edges of the cube
        drawEdges(projectedVertices, color);
    }

    private int[] project(int[] vertex) {
        int x = vertex[0];
        int y = vertex[1];
        int z = vertex[2];
        int px = x + (int) (0.5 * z);
        int py = y + (int) (0.5 * z);
        return new int[]{px, py};
    }

    private void drawEdges(int[][] vertices, Color color) {
        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            int x0 = vertices[edge[0]][0];
            int y0 = vertices[edge[0]][1];
            int x1 = vertices[edge[1]][0];
            int y1 = vertices[edge[1]][1];
            drawBresenhamLine(x0, y0, x1, y1, color);  // Usando el método correcto
        }
    }

    public static void main(String[] args) {
        Cubo ventana = new Cubo();
        ventana.setVisible(true);

        int[] Point = {200, 200, 20};
        ventana.drawCube(Point, 10, Color.RED);
    }
}
