package parcial3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class CuboRotacionAutomatica extends JFrame implements Runnable, KeyListener {
    private boolean isRunning;
    private BufferedImage bufferImage;
    private BufferedImage buffer;
    private BufferedImage bufferEscenario;
    private Graphics graPixel;
    private Graphics gEscenario;
    private double angleX = 0, angleY = 0, angleZ = 0; // Angles of rotation around each axis
    private int cubeX = 200, cubeY = 200, cubeZ = 200; // Position of the cube
    private int MOVE_SPEED = 5;
    private final int WIDTH = 700;
    private final int HEIGHT = 700;
    private final int size = 50; // Size of the cube
    private double[][] vertices;

    public CuboRotacionAutomatica() {
        setSize(WIDTH, HEIGHT);
        setTitle("Cubo Perspectiva con movimiento automatico");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);

        isRunning = true;

        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graPixel = buffer.getGraphics();

        initializeVertices();
    }

    public void run() {
        setVisible(true);

        // Buffer for static shapes (background)
        bufferEscenario = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        gEscenario = bufferEscenario.getGraphics();
        drawScene(gEscenario);

        while (isRunning) {
            // Increment the rotation angles for continuous rotation
            angleX += Math.toRadians(1);
            angleY += Math.toRadians(1);
            angleZ += Math.toRadians(1);

            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeVertices() {
        vertices = new double[][]{
                {-size, -size, -size}, {size, -size, -size}, {size, size, -size}, {-size, size, -size},
                {-size, -size, size}, {size, -size, size}, {size, size, size}, {-size, size, size}
        };
    }

    @Override
    public void update(Graphics graphics) {
        graphics.setClip(0, 0, getWidth(), getHeight());
        graPixel.setClip(0, 0, getWidth(), getHeight());
        graphics.drawImage(bufferEscenario, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    @Override
    public void repaint() {
        // Clear the buffer
        graPixel.drawImage(bufferEscenario, 0, 0, null);

        // Draw the rotated and translated cube
        drawCube(graPixel);

        // Paint the buffer on screen
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    /****************** CUBE MOVEMENT *******************/
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                cubeX -= MOVE_SPEED;
                break;
            case KeyEvent.VK_RIGHT:
                cubeX += MOVE_SPEED;
                break;
            case KeyEvent.VK_UP:
                cubeY -= MOVE_SPEED;
                break;
            case KeyEvent.VK_DOWN:
                cubeY += MOVE_SPEED;
                break;
            case KeyEvent.VK_W:
                cubeZ -= MOVE_SPEED;
                break;
            case KeyEvent.VK_S:
                cubeZ += MOVE_SPEED;
                break;
            case KeyEvent.VK_Q:
                angleX -= Math.toRadians(5);
                break;
            case KeyEvent.VK_E:
                angleX += Math.toRadians(5);
                break;
            case KeyEvent.VK_A:
                angleY -= Math.toRadians(5);
                break;
            case KeyEvent.VK_D:
                angleY += Math.toRadians(5);
                break;
            case KeyEvent.VK_Z:
                angleZ -= Math.toRadians(5);
                break;
            case KeyEvent.VK_C:
                angleZ += Math.toRadians(5);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /****************** GRAPHIC PAINT METHODS *******************/

    private void putPixel(Graphics g, int x, int y, Color color) {
        bufferImage.setRGB(0, 0, color.getRGB());
        g.drawImage(bufferImage, x, y, this);
    }

    public void drawScene(Graphics gEscenario) {
        int[] xPoints = {0, getWidth(), getWidth(), 0};
        int[] yPoints = {30, 30, getHeight(), getHeight()};
        fillPolygonScanLine(gEscenario, xPoints, yPoints, Color.BLACK);
    }

    public void drawCube(Graphics g) {
        double[][] rotatedVertices = new double[8][3];

        for (int i = 0; i < vertices.length; i++) {
            double[] rotatedX = rotateX(vertices[i], angleX);
            double[] rotatedXY = rotateY(rotatedX, angleY);
            double[] rotatedXYZ = rotateZ(rotatedXY, angleZ);
            rotatedVertices[i] = translate(rotatedXYZ, cubeX, cubeY, cubeZ);
        }

        int[][] projectedVertices = new int[8][2];
        for (int i = 0; i < rotatedVertices.length; i++) {
            projectedVertices[i] = project(rotatedVertices[i]);
        }

        drawEdges(g, projectedVertices);
    }

    private double[] rotateX(double[] vertex, double angle) {
        double[] rotated = new double[3];
        rotated[0] = vertex[0];
        rotated[1] = vertex[1] * Math.cos(angle) - vertex[2] * Math.sin(angle);
        rotated[2] = vertex[1] * Math.sin(angle) + vertex[2] * Math.cos(angle);
        return rotated;
    }

    private double[] rotateY(double[] vertex, double angle) {
        double[] rotated = new double[3];
        rotated[0] = vertex[0] * Math.cos(angle) + vertex[2] * Math.sin(angle);
        rotated[1] = vertex[1];
        rotated[2] = -vertex[0] * Math.sin(angle) + vertex[2] * Math.cos(angle);
        return rotated;
    }

    private double[] rotateZ(double[] vertex, double angle) {
        double[] rotated = new double[3];
        rotated[0] = vertex[0] * Math.cos(angle) - vertex[1] * Math.sin(angle);
        rotated[1] = vertex[0] * Math.sin(angle) + vertex[1] * Math.cos(angle);
        rotated[2] = vertex[2];
        return rotated;
    }

    private double[] translate(double[] vertex, int dx, int dy, int dz) {
        double[] translated = new double[3];
        translated[0] = vertex[0] + dx;
        translated[1] = vertex[1] + dy;
        translated[2] = vertex[2] + dz;
        return translated;
    }

    private int[] project(double[] vertex) {
        double factor = 500 / (500 + vertex[2]); // Adjust factor based on z-depth
        int x = (int) (vertex[0] * factor) + WIDTH / 2; // Translate to center
        int y = (int) (vertex[1] * factor) + HEIGHT / 2; // Translate to center
        return new int[]{x, y};
    }

    private void drawEdges(Graphics g, int[][] vertices) {
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
            drawBresenhamLine(g, x0, y0, x1, y1, Color.RED);
        }
    }

    public void drawBresenhamLine(Graphics g, int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int e2;

        while (true) {
            putPixel(g, x0, y0, color);

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

    public void fillPolygonScanLine(Graphics g, int[] xPoints, int[] yPoints, Color color) {
        int minY = Arrays.stream(yPoints).min().getAsInt();
        int maxY = Arrays.stream(yPoints).max().getAsInt();

        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();
            for (int i = 0; i < xPoints.length; i++) {
                int x1 = xPoints[i];
                int y1 = yPoints[i];
                int x2 = xPoints[(i + 1) % xPoints.length];
                int y2 = yPoints[(i + 1) % xPoints.length];

                if ((y1 < y && y2 >= y) || (y2 < y && y1 >= y)) {
                    int x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                    intersections.add(x);
                }
            }
            Collections.sort(intersections);
            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);
                for (int x = xStart; x <= xEnd; x++) {
                    putPixel(g, x, y, color);
                }
            }
        }
    }

    public static void main(String[] args) {
        CuboRotacionAutomatica cubo = new CuboRotacionAutomatica();
        Thread mainThread = new Thread(cubo);
        mainThread.start();
    }
}
