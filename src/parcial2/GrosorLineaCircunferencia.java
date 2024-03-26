/* Grosor de línea de circunferencia*/

package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GrosorLineaCircunferencia extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public GrosorLineaCircunferencia() {
        setSize(400, 400);
        setTitle("Grosor de linea de circunferencia");
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

    public void drawCircumference(int centerX, int centerY, int radius, int lineWidth) {
        // Calcular el grosor de la línea en píxeles
        int halfWidth = lineWidth / 2;

        // Iterar sobre cada punto en un cuadrado que rodea la circunferencia
        for (int y = centerY - radius - halfWidth; y <= centerY + radius + halfWidth; y++) {
            for (int x = centerX - radius - halfWidth; x <= centerX + radius + halfWidth; x++) {
                // Calcular la distancia del punto actual al centro de la circunferencia
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

                // Verificar si el punto está dentro del grosor de la línea
                if (Math.abs(distance - radius) <= halfWidth) {
                    putPixel(x, y, Color.RED);
                }
            }
        }
    }


    public static void main(String[] args) {
        GrosorLineaCircunferencia ventana = new GrosorLineaCircunferencia();
        ventana.setVisible(true);
        ventana.drawCircumference(200, 200, 100, 10);
    }

}
