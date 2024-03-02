package proyecto1;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Reloj extends JFrame implements Runnable {
    private BufferedImage bufferImage;
    private Thread hilo;

    public Reloj() {
        /* Propiedades de la ventana */
        setTitle("Reloj análogo");
        setSize(800, 800);
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        /* Crear bufferImage con las dimensiones de la ventana */
        this.bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        /* Hilo */
        hilo = new Thread(this);
        hilo.start();
    }

    public void run() {
        Graphics g = bufferImage.createGraphics();
        dibujarReloj(g);
        g.dispose();
    }

    public void paint(Graphics g) {
        g.drawImage(bufferImage, 0, 0, null);
    }

    private void dibujarReloj(Graphics g) {
        int centerX = getWidth() / 2;  // Calcula el centro X del círculo
        int centerY = getHeight() / 2; // Calcula el centro Y del círculo

        g.setColor(new Color(53,20,20));
        g.fillOval(centerX - 220, centerY - 220, 440, 440); // borde circulo fondo

        g.setColor(Color.WHITE);
        g.fillOval(centerX - 200, centerY - 200, 400, 400); // circulo fondo

        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));

        // Dibujar las horas
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 90); // Calcular el ángulo para cada hora (en radianes)
            int x = (int) (centerX + 160 * Math.cos(angle)); // Calcular la posición X del texto
            int y = (int) (centerY + 160 * Math.sin(angle)); // Calcular la posición Y del texto
            String hour = Integer.toString(i); // Convertir el número de hora a String
            FontMetrics fm = g.getFontMetrics(); // Obtener las métricas de la fuente
            int width = fm.stringWidth(hour); // Obtener el ancho del texto
            int height = fm.getHeight(); // Obtener la altura del texto
            g.drawString(hour, x - width / 2, y + height / 4); // Dibujar el texto centrado en la posición calculada
        }
    }

    public static void main(String[] args) {
        new Reloj();
    }
}
