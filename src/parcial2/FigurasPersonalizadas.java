package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FigurasPersonalizadas extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public FigurasPersonalizadas() {
        setSize(500, 500);
        setTitle("Figuras Personalizadas");
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

    public void drawRectangle(int x0, int y0, int x1, int y1) {
        // If x0 has a greater value, exchange values with x1
        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;
        }
        if (y0 > y1) {
            int temp = y0;
            y0 = y1;
            y1 = temp;
        }

        drawBresenhamLine(x0, x1, y0, y0);
        drawBresenhamLine(x0, x1, y1, y1);
        drawBresenhamLine(x0, x0, y0, y1);
        drawBresenhamLine(x1, x1, y0, y1);
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

    public void drawDotsLine(int x0, int x1, int y0, int y1, int gapSize) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        int drawnPixels = 0;

        while (x0 != x1 || y0 != y1) {
            if (drawnPixels >= gapSize) {
                putPixel(x0, y0, Color.BLUE);
                drawnPixels = 0;
            } else {
                drawnPixels++;
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
        }
    }



    public static void main(String[] args) {
        FigurasPersonalizadas ventana = new FigurasPersonalizadas();
        ventana.setVisible(true);

        ventana.drawBresenhamLine(70, 140, 70, 140);

        ventana.drawBresenhamLine(170, 240, 100, 100);

        ventana.drawBresenhamLine(350, 270, 70, 150);

        ventana.drawBresenhamLine(450, 370, 100, 100);

        ventana.drawBresenhamCircumference(100, 370, 8);
        ventana.drawBresenhamCircumference(100, 370, 20);
        ventana.drawBresenhamCircumference(100, 370, 30);
        ventana.drawBresenhamCircumference(100, 370, 40);
        ventana.drawDotsLine(52, 145, 320, 415, 10);

        ventana.drawRectangle(180, 340, 315, 400);
        ventana.drawRectangle(305, 390, 190, 350);

        ventana.drawEllipse(400, 370, 75, 25);
        ventana.drawEllipse(400, 370, 60, 15);
        ventana.drawEllipse(400, 370, 45, 8);
        ventana.drawDotsLine(330, 360, 340, 370, 6);
        ventana.drawDotsLine(440, 470, 370, 400, 6);
    }

}

