package proyecto1;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import javax.swing.*;

public class Reloj extends JFrame implements Runnable {
    private BufferedImage backgroundBufferImage;
    private BufferedImage manecillasBufferImage;
    private int horas;
    private int minutos;
    private int segundos;

    public Reloj() {
        setTitle("Reloj análogo");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
        setResizable(false);

        backgroundBufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        manecillasBufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics g = backgroundBufferImage.getGraphics();
        dibujarFondo(g);
        g.dispose();

        Thread hilo = new Thread(this);
        hilo.start();
    }

    private void dibujarFondo(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        RelojFondo.dibujarImagenFondo(g, centerX - 353, centerY - 350); // restar mitad de ancho y alto de imagen
        RelojFondo.dibujarMarcoReloj(g, centerX, centerY);
        RelojFondo.dibujarInteriorReloj(g, centerX, centerY);
        RelojFondo.dibujarHorasReloj(g, centerX, centerY);
    }

    public void run() {
        while (true) {
            obtenerHorarioDelSistema();

            Graphics g = manecillasBufferImage.getGraphics();
            g.drawImage(backgroundBufferImage, 0, 0, null);
            dibujarManecillas(g);
            g.dispose();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    repaint();
                }
            });

            try {
                Thread.sleep(1 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        g.drawImage(manecillasBufferImage, 0, 0, null);
    }

    private void obtenerHorarioDelSistema() {
        LocalTime horaActual = LocalTime.now();
        horas = horaActual.getHour();
        minutos = horaActual.getMinute();
        segundos = horaActual.getSecond();
        //System.out.println("horas: " + horas + " minutos: " + minutos + " segundos: " + segundos);
    }

    private void dibujarManecillas(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double anguloHoras = (360.0 / 12) * ((horas % 12) + minutos / 60.0) - 90;
        double anguloMinutos = (360.0 / 60) * (minutos + segundos / 60.0) - 90;
        double anguloSegundos = (360.0 / 60) * (segundos + (double) System.currentTimeMillis() % 1000 / 1000) - 90;

        dibujarManecilla(g, centerX, centerY, anguloHoras, 50, Color.BLACK);
        dibujarManecilla(g, centerX, centerY, anguloMinutos, 90, Color.BLUE);
        dibujarManecilla(g, centerX, centerY, anguloSegundos, 140, Color.RED);
    }

    private void dibujarManecilla(Graphics g, int x, int y, double angulo, int longitud, Color color) {
        int x2 = x + (int) (longitud * Math.cos(Math.toRadians(angulo)));
        int y2 = y + (int) (longitud * Math.sin(Math.toRadians(angulo)));

        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(2)); // ancho de manecillas

        g.drawLine(x, y, x2, y2);
    }

    public static void main(String[] args) {
        new Reloj();
    }
}
