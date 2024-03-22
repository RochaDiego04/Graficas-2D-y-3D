/* Circunference using 8 quarter algorithm*/

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CircunferenciaSimetriaOchoLados extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public CircunferenciaSimetriaOchoLados() {
        setSize(400, 400);
        setTitle("Circunferencia Simetría de 8 lados");
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

    public void drawCircumference(int centerX, int centerY, int radius) {
        for (int x = 0; x <= radius; x++) {
            int y = (int) Math.round(Math.sqrt(radius * radius - x * x));
            putPixel(centerX + x, centerY + y, Color.RED);
            putPixel(centerX - x, centerY + y, Color.RED);
            putPixel(centerX + x, centerY - y, Color.RED);
            putPixel(centerX - x, centerY - y, Color.RED);
            putPixel(centerX + y, centerY + x, Color.RED);
            putPixel(centerX - y, centerY + x, Color.RED);
            putPixel(centerX + y, centerY - x, Color.RED);
            putPixel(centerX - y, centerY - x, Color.RED);
        }
    }


    /*public void drawGrid() {
        // Vertical Lines
        for (int x = 0; x <= getWidth(); x += 10) {
            for (int y = 0; y < getHeight(); y++) {
                if (x < getWidth()) {
                    putPixel(x, y, Color.GRAY);
                }
            }
        }

        // Horizontal Lines
        for (int y = 0; y <= getHeight(); y += 10) {
            for (int x = 0; x < getWidth(); x++) {
                if (y < getHeight()) {
                    putPixel(x, y, Color.GRAY);
                }
            }s
        }
    }*/

    public static void main(String[] args) {
        CircunferenciaSimetriaOchoLados ventana = new CircunferenciaSimetriaOchoLados();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawCircumference(200, 200, 20);
    }

}

