package parcial3;

import proyectoDK.Barrel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class CuboTraslacion extends JFrame implements Runnable, KeyListener {
    private boolean isRunning;
    private BufferedImage bufferImage;
    private BufferedImage buffer;
    private BufferedImage bufferEscenario;
    private Graphics graPixel;
    private Graphics gEscenario;
    private int cubeX = 200, cubeY = 200, cubeZ = 20;
    private int MOVE_SPEED = 5;
    private int WIDTH = 700;
    private int HEIGHT = 700;

    public CuboTraslacion() {
        setSize(WIDTH, HEIGHT);
        setTitle("Cubo con movimiento");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);

        isRunning = true;


        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graPixel = buffer.getGraphics();
    }

    public void run() {
        setVisible(true);

        // Buffer for static shapes (background)
        bufferEscenario = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        gEscenario = bufferEscenario.getGraphics();
        drawScene(gEscenario);

        while (isRunning) {
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        // Draw background image on buffer
        graPixel.drawImage(bufferEscenario, 0, 0, null);

        drawCube(graPixel,  new int[]{cubeX, cubeY, cubeZ}, 10, Color.RED); // Punto inicial del cubo, tambien actualiza la posicion en (x,y)

        // Paint the buffer on screen
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    private void putPixel(Graphics g, int x, int y, Color color) {
        bufferImage.setRGB(0, 0, color.getRGB());
        g.drawImage(bufferImage, x, y, this);
    }

    public void drawScene(Graphics gEscenario) {
        int[] xPoints = {0, getWidth(), getWidth(), 0};
        int[] yPoints = {30, 30, getHeight(), getHeight()};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, Color.BLACK);
    }


    /****************** CUBE MOVEMENT *******************/
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            cubeX -= MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            cubeX += MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_UP) {
            cubeY -= MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            cubeY += MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_W) {
            cubeZ -= MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_S) {
            cubeZ += MOVE_SPEED;
        }
    }
    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

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

    public void drawCube(Graphics g, int[] Point, int size, Color color) {
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
        drawEdges(g, projectedVertices, color);
    }

    private int[] project(int[] vertex) {
        int x = vertex[0];
        int y = vertex[1];
        int z = vertex[2];
        int px = x + (int) (0.5 * z);
        int py = y + (int) (0.5 * z);
        return new int[]{px, py};
    }

    private void drawEdges(Graphics g, int[][] vertices, Color color) {
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
            drawBresenhamLine(g, x0, y0, x1, y1, color);
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
                    putPixel(g, x, y, color);
                }
            }
        }
    }

    public static void main(String[] args) {
        CuboTraslacion ventana = new CuboTraslacion();
        Thread windowThread = new Thread(ventana);
        windowThread.start();
    }
}
