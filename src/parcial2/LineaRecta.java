/* straight line with formula y = mx + b */

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class LineaRecta extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public LineaRecta() {
        setSize(400, 400);
        setTitle("Linea recta");
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

    public void drawLine(int x0, int x1, int y0, int y1) {
        double m = (double) (y1 - y0) / (x1 - x0); // Slope of the line
        double b = y0 - (m * x0); // y interception
        for (int x = x0; x <= x1; x++) {
            int y = (int) round(m * x + b);
            putPixel(x, round(y), Color.RED    );
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
            }
        }
    }*/

    public static void main(String[] args) {
        LineaRecta ventana = new LineaRecta();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawLine(50, 100, 20, 399);
    }

}
