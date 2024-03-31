package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RecorteCircunferencias extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;

    // Definición de códigos de región
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private static final int xLeft = 100;
    private static final int xRight = 350;
    private static final int yTop = 50;
    private static final int yBottom = 200;

    public RecorteCircunferencias() {
        setSize(400, 400);
        setTitle("Algoritmo de Recorte de Circunferencias por Código de Región");
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
        int code = calculateRegionCode(centerX + x, centerY + y);
        if (code == INSIDE) {
            putPixel(centerX + x, centerY + y, Color.BLUE);
        }
        code = calculateRegionCode(centerX - x, centerY + y);
        if (code == INSIDE) {
            putPixel(centerX - x, centerY + y, Color.BLUE);
        }
        code = calculateRegionCode(centerX + x, centerY - y);
        if (code == INSIDE) {
            putPixel(centerX + x, centerY - y, Color.BLUE);
        }
        code = calculateRegionCode(centerX - x, centerY - y);
        if (code == INSIDE) {
            putPixel(centerX - x, centerY - y, Color.BLUE);
        }
        code = calculateRegionCode(centerX + y, centerY + x);
        if (code == INSIDE) {
            putPixel(centerX + y, centerY + x, Color.BLUE);
        }
        code = calculateRegionCode(centerX - y, centerY + x);
        if (code == INSIDE) {
            putPixel(centerX - y, centerY + x, Color.BLUE);
        }
        code = calculateRegionCode(centerX + y, centerY - x);
        if (code == INSIDE) {
            putPixel(centerX + y, centerY - x, Color.BLUE);
        }
        code = calculateRegionCode(centerX - y, centerY - x);
        if (code == INSIDE) {
            putPixel(centerX - y, centerY - x, Color.BLUE);
        }
    }

    private int calculateRegionCode(int x, int y) {
        int code = INSIDE;
        if (x < xLeft) {
            code |= LEFT;
        } else if (x > xRight) {
            code |= RIGHT;
        }
        if (y < yTop) {
            code |= TOP;
        } else if (y > yBottom) {
            code |= BOTTOM;
        }
        return code;
    }

    public static void main(String[] args) {
        RecorteCircunferencias ventana = new RecorteCircunferencias();
        ventana.setVisible(true);
        ventana.drawBresenhamCircumference(200, 200, 100);
        ventana.drawBresenhamCircumference(100, 100, 50);
        ventana.drawBresenhamCircumference(350, 200, 150);
        ventana.drawBresenhamCircumference(270, 150, 30);
    }
}


