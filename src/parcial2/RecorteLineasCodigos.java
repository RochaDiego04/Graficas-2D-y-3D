/* Recorte de líneas por código de región, cuadrícula que indica */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RecorteLineasCodigos extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;

    // Definición de códigos de región
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    public RecorteLineasCodigos() {
        setSize(400, 400);
        setTitle("Algoritmo de Recorte de Líneas por Código de Región");
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

    // Función para obtener el código de región de un punto (x, y) con respecto al rectángulo definido por (xLeft, xRight, yTop, yBottom)
    private int computeOutCode(int x, int y, int xLeft, int xRight, int yTop, int yBottom) {
        int code = INSIDE;

        if (x < xLeft)
            code |= LEFT;
        else if (x > xRight)
            code |= RIGHT;

        if (y < yTop)
            code |= TOP;
        else if (y > yBottom)
            code |= BOTTOM;

        return code;
    }

    public void drawBresenhamLine(int x0, int x1, int y0, int y1, int xLeft, int xRight, int yTop, int yBottom) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            int code0 = computeOutCode(x0, y0, xLeft, xRight, yTop, yBottom);
            int code1 = computeOutCode(x1, y1, xLeft, xRight, yTop, yBottom);

            if ((code0 | code1) == 0) { // Ambos puntos están dentro de la ventana
                putPixel(x0, y0, Color.RED);
                if (x0 == x1 && y0 == y1)
                    break;
                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x0 += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y0 += sy;
                }
            } else if ((code0 & code1) != 0) { // Ambos puntos están fuera de la ventana en la misma región
                break;
            } else {
                int codeOut = (code0 != 0) ? code0 : code1;
                int x, y;

                if ((codeOut & TOP) != 0) {
                    x = x0 + (x1 - x0) * (yTop - y0) / (y1 - y0);
                    y = yTop;
                } else if ((codeOut & BOTTOM) != 0) {
                    x = x0 + (x1 - x0) * (yBottom - y0) / (y1 - y0);
                    y = yBottom;
                } else if ((codeOut & RIGHT) != 0) {
                    y = y0 + (y1 - y0) * (xRight - x0) / (x1 - x0);
                    x = xRight;
                } else {
                    y = y0 + (y1 - y0) * (xLeft - x0) / (x1 - x0);
                    x = xLeft;
                }

                if (codeOut == code0) {
                    x0 = x;
                    y0 = y;
                } else {
                    x1 = x;
                    y1 = y;
                }
            }
        }
    }

    public static void main(String[] args) {
        RecorteLineasCodigos ventana = new RecorteLineasCodigos();
        ventana.setVisible(true);

        // Definiendo las coordenadas del rectángulo de recorte
        int xLeft = 100;
        int xRight = 350;
        int yTop = 50;
        int yBottom = 200;

        ventana.drawBresenhamLine(50, 380, 50, 300, xLeft, xRight, yTop, yBottom); // Línea 1 Totalmente visible
        ventana.drawBresenhamLine(150, 180, 100, 130, xLeft, xRight, yTop, yBottom); // Línea 2 Parcialmente Visible
        ventana.drawBresenhamLine(50, 50, 100, 200, xLeft, xRight, yTop, yBottom); // Línea 3 Totalmente invisible

        // Lineas de prueba para el rectangulo
        ventana.drawBresenhamLine(120, 120, 0, 400, xLeft, xRight, yTop, yBottom);
        ventana.drawBresenhamLine(50, 400, 300, 0, xLeft, xRight, yTop, yBottom);
        ventana.drawBresenhamLine(0, 400, 150, 150, xLeft, xRight, yTop, yBottom);
    }

}

