package Parcial1;

import java.awt.*;
import javax.swing.*;

public class EspiralArquimides extends JFrame implements Runnable {
    private final int loops = 10;
    private final double a = 4;

    public EspiralArquimides() {
        super("Espiral Arquimides");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void run() {
        // Centro de la ventana
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        Graphics g = getGraphics(); // Obtener el contexto gráfico

        dibujarEspiral(centerX, centerY, g);
    }

    private void dibujarEspiral(int centerX, int centerY, Graphics g) {
        for (double theta = 0; theta < loops * 3 * Math.PI; theta += 0.01) {
            double r = a * theta;
            int x = centerX + (int) (r * Math.cos(theta));
            int y = centerY + (int) (r * Math.sin(theta));
            int x2 = centerX - (int) (r * Math.cos(theta));
            int y2 = centerY - (int) (r * Math.sin(theta));
            g.fillOval(x, y, 5, 5);
            g.fillOval(x2, y2, 5, 5);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        EspiralArquimides espiral = new EspiralArquimides(); // crear instancia
        Thread thread = new Thread(espiral);
        thread.start();
    }
}
