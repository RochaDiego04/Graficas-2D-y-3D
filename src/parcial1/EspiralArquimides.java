package parcial1;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class EspiralArquimides extends JFrame implements Runnable {

    private BufferedImage bufferImage;
    private Thread hilo;
    private final int loops = 10;
    private final double anchuraEspiral = 3;

    public EspiralArquimides() {
        /* Propiedades de la ventana */
        setSize(600, 600);
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
        Graphics g = bufferImage.createGraphics(); // obtener contexto gráfico del contenedor
        dibujarEspiral(getWidth() / 2, getHeight() / 2 + 10, g);
        g.dispose();
    }

    public void paint(Graphics g) {
        /* Se dibuja en el lienzo o contexto gráfico, todo lo que haya en la memoria RAM o buffer*/
        g.drawImage(bufferImage, 0, 0, null);
    }

    private void dibujarEspiral(int centerX, int centerY, Graphics g) {
        g.setColor(Color.BLACK);

        for (double theta = 0; theta < loops * 3 * Math.PI; theta += 0.01) {
            double r = anchuraEspiral * theta;
            int x = centerX + (int) (r * Math.cos(theta));
            int y = centerY + (int) (r * Math.sin(theta));
            int x2 = centerX - (int) (r * Math.cos(theta));
            int y2 = centerY - (int) (r * Math.sin(theta));
            g.fillOval(x, y, 5, 5);
            g.fillOval(x2, y2, 5, 5);

            /* Los cambios de la espiral quedan en la memoria RAM, almacenados en la dirección de memoria del objeto "g"*/
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint(); // llama al metodo paint
        }
    }

    public static void main(String[] args) {
        new EspiralArquimides();
    }
}

