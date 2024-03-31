/* Recorte de líneas por algoritmo explícito 2D */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RecorteLineasExplicito extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;

    public RecorteLineasExplicito() {
        setSize(400, 400);
        setTitle("Algoritmo de Recorte de Líneas Explicito 2D");
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

    public void drawClippedLine(int x0, int y0, int x1, int y1, int xLeft, int xRight, int yTop, int yBottom) {
        boolean accept = false;

        // Verificar si la línea está completamente dentro del área de recorte
        if (x0 >= xLeft && x0 <= xRight && y0 >= yTop && y0 <= yBottom &&
                x1 >= xLeft && x1 <= xRight && y1 >= yTop && y1 <= yBottom) {
            accept = true;
        } else {
            // Calcular las intersecciones de la línea con los bordes del área de recorte
            if (x0 < xLeft) {
                y0 += (int) ((double) (y1 - y0) * (xLeft - x0) / (x1 - x0));
                x0 = xLeft;
            } else if (x0 > xRight) {
                y0 += (int) ((double) (y1 - y0) * (xRight - x0) / (x1 - x0));
                x0 = xRight;
            }

            if (x1 < xLeft) {
                y1 += (int) ((double) (y1 - y0) * (xLeft - x1) / (x1 - x0));
                x1 = xLeft;
            } else if (x1 > xRight) {
                y1 += (int) ((double) (y1 - y0) * (xRight - x1) / (x1 - x0));
                x1 = xRight;
            }

            if (y0 < yTop) {
                x0 += (int) ((double) (x1 - x0) * (yTop - y0) / (y1 - y0));
                y0 = yTop;
            } else if (y0 > yBottom) {
                x0 += (int) ((double) (x1 - x0) * (yBottom - y0) / (y1 - y0));
                y0 = yBottom;
            }

            if (y1 < yTop) {
                x1 += (int) ((double) (x1 - x0) * (yTop - y1) / (y1 - y0));
                y1 = yTop;
            } else if (y1 > yBottom) {
                x1 += (int) ((double) (x1 - x0) * (yBottom - y1) / (y1 - y0));
                y1 = yBottom;
            }

            // Verificar si la línea ahora está completamente dentro del área de recorte
            if (x0 >= xLeft && x0 <= xRight && y0 >= yTop && y0 <= yBottom &&
                    x1 >= xLeft && x1 <= xRight && y1 >= yTop && y1 <= yBottom) {
                accept = true;
            }
        }

        // Si la línea está completamente dentro del área de recorte, dibujarla
        if (accept) {
            drawLineUsingPutPixel(x0, y0, x1, y1);
        }
    }

    // Método auxiliar para dibujar una línea utilizando el método putPixel
    private void drawLineUsingPutPixel(int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
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
        }
    }


    public static void main(String[] args) {
        RecorteLineasExplicito ventana = new RecorteLineasExplicito();
        ventana.setVisible(true);

        // Definiendo las coordenadas del rectángulo de recorte
        int xLeft = 100;
        int xRight = 350;
        int yTop = 50;
        int yBottom = 200;

        ventana.drawClippedLine(50, 300, 380, 50, xLeft, xRight, yTop, yBottom); // Línea 1 Totalmente visible
        ventana.drawClippedLine(150, 130, 180, 100, xLeft, xRight, yTop, yBottom); // Línea 2 Parcialmente Visible
        ventana.drawClippedLine(40, 200, 40, 100, xLeft, xRight, yTop, yBottom); // Línea 3 Totalmente invisible
        ventana.drawClippedLine(100, 0, 100, 400, xLeft, xRight, yTop, yBottom); // Línea 4 Parcialmente Visible
    }

}

