package parcial3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Cilindro extends JFrame implements Runnable, KeyListener {
    private boolean isRunning;
    private BufferedImage bufferImage;
    private BufferedImage buffer;
    private BufferedImage bufferEscenario;
    private Graphics graPixel;
    private Graphics gEscenario;
    private double translateX = 0, translateY = 0, translateZ = 0;
    private double angleX = 0, angleY = 0, angleZ = 0;
    private int MOVE_SPEED = 1;
    private double scaleFactor = 20.0;
    private final int WIDTH = 700;
    private final int HEIGHT = 700;

    public Cilindro() {
        setSize(WIDTH, HEIGHT);
        setTitle("Cilindro (Reloj de Arena)");
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
        // Clear the buffer
        graPixel.drawImage(bufferEscenario, 0, 0, null);

        // Draw the surface
        drawSurface(graPixel);

        // Paint the buffer on screen
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    /****************** MESH MOVEMENT *******************/
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                translateX -= MOVE_SPEED;
                break;
            case KeyEvent.VK_RIGHT:
                translateX += MOVE_SPEED;
                break;
            case KeyEvent.VK_UP:
                translateY -= MOVE_SPEED;
                break;
            case KeyEvent.VK_DOWN:
                translateY += MOVE_SPEED;
                break;
            case KeyEvent.VK_W:
                translateZ -= MOVE_SPEED;
                break;
            case KeyEvent.VK_S:
                translateZ += MOVE_SPEED;
                break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_ADD:
                scaleFactor += 0.1;
                break;
            case KeyEvent.VK_MINUS:
            case KeyEvent.VK_SUBTRACT:
                scaleFactor = Math.max(0.1, scaleFactor - 0.1);
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

    public void drawSurface(Graphics g) {
        int rows = 20;
        int cols = 20;
        double tStart = 0;
        double tEnd = 6;
        double rhoStart = 0;
        double rhoEnd = 2 * Math.PI;


        double sStep = (tEnd - tStart) / rows;
        double tStep = (rhoEnd - rhoStart) / cols;

        // Draw horizontal curves
        for (int i = 0; i <= rows; i++) {
            double s = tStart + i * sStep;
            drawCurve(g, s, rhoStart, rhoEnd, tStep, true);
        }

        // Draw vertical curves
        for (int j = 0; j <= cols; j++) {
            double t = rhoStart + j * tStep;
            drawCurve(g, t, tStart, tEnd, sStep, false);
        }
    }

    public void drawCurve(Graphics g, double fixedParam, double varStart, double varEnd, double varStep, boolean isHorizontal) {
        double prevX = isHorizontal ? f1(fixedParam, varStart) : f1(varStart, fixedParam);
        double prevY = isHorizontal ? f2(fixedParam, varStart) : f2(varStart, fixedParam);
        double prevZ = isHorizontal ? f3(fixedParam, varStart) : f3(varStart, fixedParam);

        for (double var = varStart + varStep; var <= varEnd; var += varStep) {
            double x = isHorizontal ? f1(fixedParam, var) : f1(var, fixedParam);
            double y = isHorizontal ? f2(fixedParam, var) : f2(var, fixedParam);
            double z = isHorizontal ? f3(fixedParam, var) : f3(var, fixedParam);

            double[] prevRotatedX = rotateX(new double[]{prevX, prevY, prevZ}, angleX);
            double[] prevRotatedXY = rotateY(prevRotatedX, angleY);
            double[] prevRotatedXYZ = rotateZ(prevRotatedXY, angleZ);
            int[] prevProjected = project(prevRotatedXYZ);

            double[] rotatedX = rotateX(new double[]{x, y, z}, angleX);
            double[] rotatedXY = rotateY(rotatedX, angleY);
            double[] rotatedXYZ = rotateZ(rotatedXY, angleZ);
            int[] projected = project(rotatedXYZ);

            drawBresenhamLine(g, prevProjected[0], prevProjected[1], projected[0], projected[1]);

            prevX = x;
            prevY = y;
            prevZ = z;
        }
    }

    private double f1(double t, double rho) {
        // parametric function for x
        return (2 + Math.cos(t)) * Math.cos(rho);
    }

    private double f2(double t, double rho) {
        // parametric function for y
        return (2 + Math.cos(t)) * Math.sin(rho);
    }

    private double f3(double t, double rho) {
        // parametric function for z (deformation)
        return t - 3;
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

    private int[] project(double[] vertex) {
        double factor = (500 / (500 + vertex[2] + translateZ)) * scaleFactor; // Apply scale factor and z translation
        int x = (int) ((vertex[0] + translateX) * factor) + WIDTH / 2; // Translate to center
        int y = (int) ((vertex[1] + translateY) * factor) + HEIGHT / 2; // Translate to center
        return new int[]{x, y};
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

    // Bresenham line with color that depends on the height of the line
    public void drawBresenhamLine(Graphics gra, int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int e2;

        while (true) {
            // Interpolate between red and purple based on the y-coordinate
            float t = Math.max(0, Math.min(1, (float)y0 / HEIGHT)); // Clamp t to the range [0, 1]
            Color color = Color.getHSBColor(t * 0.90f, 1f, 1f);

            putPixel(gra, x0, y0, color);

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
        Cilindro curva = new Cilindro();
        Thread mainThread = new Thread(curva);
        mainThread.start();
    }
}
