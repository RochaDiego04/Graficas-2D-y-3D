/* Midpoint algorithm to draw straight lines*/

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AlgoritmoPuntoMedio extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public AlgoritmoPuntoMedio() {
        setSize(400, 400);
        setTitle("Algoritmo de punto medio");
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

    public void drawMidpointLine(int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int d = 2 * dy - dx;
        int incrE = 2 * dy;
        int incrNE = 2 * (dy - dx);
        int x = x0, y = y0;

        putPixel(x, y, Color.RED);

        while (x < x1) {
            if (d <= 0) {
                d += incrE;
                x++;
            } else {
                d += incrNE;
                x++;
                y++;
            }
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
        AlgoritmoPuntoMedio ventana = new AlgoritmoPuntoMedio();
        ventana.setVisible(true);
        //ventana.drawGrid();
        ventana.drawMidpointLine(50, 200, 200, 300);
    }

}

