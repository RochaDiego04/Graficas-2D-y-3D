/* Circunference */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CincunferenciaMejorada extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public CincunferenciaMejorada() {
        setSize(400, 400);
        setTitle("Circunferencia");
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
        double angleIncrement = 0.8 / radius; // Ajustar el incremento del ángulo según el radio
        for (double theta = 0; theta <= 2 * Math.PI; theta += angleIncrement) {
            int x = (int) Math.round(centerX + radius * Math.sin(theta));
            int y = (int) Math.round(centerY + radius * Math.cos(theta));
            putPixel(x, y, Color.RED);
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
        CincunferenciaMejorada ventana = new CincunferenciaMejorada();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawCircumference(200, 200, 70);
    }

}

