package proyecto1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class Reloj extends JFrame implements Runnable {
    private BufferedImage backgroundBufferImage;
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
        this.backgroundBufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        /* Hilo */
        hilo = new Thread(this);
        hilo.start();
    }

    public void run() {
        while (true) {
            Graphics g = backgroundBufferImage.createGraphics();
            dibujarReloj(g);
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        g.drawImage(backgroundBufferImage, 0, 0, null);
        dibujarManecillas(g);
    }

    private void dibujarReloj(Graphics g) {
        int centerX = getWidth() / 2;  // Calcula el centro X del círculo
        int centerY = getHeight() / 2; // Calcula el centro Y del círculo

        // Dibujar marco de circulo de fondo
        g.setColor(new Color(53,20,20));
        g.fillOval(centerX - 220, centerY - 220, 440, 440);

        // Dibujar circulo de fondo
        g.setColor(Color.WHITE);
        g.fillOval(centerX - 200, centerY - 200, 400, 400);

        // Dibujar las horas
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));

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

    private Map<String, Integer> obtenerHorarioDelSistema() {
        long tiempoActual = System.currentTimeMillis();
        int segundos = (int) (tiempoActual / 1000 % 60);
        int minutos = (int) ((tiempoActual / (1000 * 60)) % 60);
        int horas = (int) ((tiempoActual / (1000 * 60 * 60)) % 12);

        Map<String, Integer> horario = new HashMap<>();
        horario.put("horas", horas);
        horario.put("minutos", minutos);
        horario.put("segundos", segundos);

        return horario;
    }

    private void dibujarManecillas(Graphics g) {
        int centerX = getWidth() / 2;  // Calcula el centro X del círculo
        int centerY = getHeight() / 2; // Calcula el centro Y del círculo

        Map<String, Integer> horarioActual = obtenerHorarioDelSistema();
        dibujarManecilla(g, centerX, centerY, horarioActual.get("horas") * 30 - 90, 80, Color.BLACK);
        dibujarManecilla(g, centerX, centerY, horarioActual.get("minutos") * 6 - 90, 120, Color.BLUE);
        dibujarManecilla(g, centerX, centerY, horarioActual.get("segundos") * 6 - 90, 140, Color.RED);
    }
    private void dibujarManecilla(Graphics g, int x, int y, double angulo, int longitud, Color color) {
        int x2 = x + (int) (longitud * Math.cos(Math.toRadians(angulo)));
        int y2 = y + (int) (longitud * Math.sin(Math.toRadians(angulo)));
        g.setColor(color);
        g.drawLine(x, y, x2, y2);
    }

    public static void main(String[] args) {
        new Reloj();
    }
}
