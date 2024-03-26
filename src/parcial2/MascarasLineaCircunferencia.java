/* Líneas continuas o descontinuas con algoritmo de Bresenham */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class MascarasLineaCircunferencia extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public MascarasLineaCircunferencia() {
        setSize(400, 400);
        setTitle("Tipos de línea recta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

    public void drawBresenhamCircumference(int centerX, int centerY, int radius, boolean isContinuous) {
        int x = 0;
        int y = radius;
        int p = 3 - 2 * radius;

        int counter = 0;
        int mask = 0b1111000011110000; // Máscara inicial
        int maskLength = Integer.toBinaryString(mask).length();

        while (x <= y) {
            if (isContinuous || (mask & (1 << counter)) != 0) {
                drawCirclePoints(centerX, centerY, x, y);
            }

            x++;
            if (p < 0) {
                p += 4 * x + 6;
            } else {
                y--;
                p += 4 * (x - y) + 10;
            }

            // Ajustar la secuencia de interespaciado para el espacio adicional entre octantes
            if (x == y + 1) {
                counter = 0; // Reiniciar el contador para el próximo octante
            } else {
                counter = (counter + 1) % maskLength;
            }
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


    public static void main(String[] args) {
        MascarasLineaCircunferencia ventana = new MascarasLineaCircunferencia();
        ventana.setVisible(true);
        ventana.drawBresenhamCircumference(200, 200, 150, false);
    }

}
