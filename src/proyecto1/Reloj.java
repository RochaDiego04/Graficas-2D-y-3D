package proyecto1;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class Reloj extends JFrame implements Runnable {
    private BufferedImage backgroundBufferImage;
    private BufferedImage foregroundBufferImage;
    private Clip clockSound;

    private int horas;
    private int minutos;
    private int segundos;
    private long tiempoInicialSegundo;
    private Image backgroundImage;

    private Color colorManecillaSegundos = new Color(101, 44, 22);
    private Color colorManecillaMinutos = new Color(128, 85, 40);
    private Color colorManecillaHoras = new Color(38, 13, 11);

    public Reloj() throws IOException {
        setTitle("Reloj análogo");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
        setResizable(false);

        backgroundBufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        foregroundBufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Cargar imagen de fondo
        File file = new File("src/proyecto1/img/bgImage.jpeg");
        if (file.exists()) {
            backgroundImage = ImageIO.read(file);
        } else {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
        }

        // Cargar sonido de fondo
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/proyecto1/multimedia/tic-tac.wav"));
            clockSound = AudioSystem.getClip();
            clockSound.open(audioInputStream);
            clockSound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

        Thread hilo = new Thread(this);
        hilo.start();
    }

    public void run() {
        // Dibujar fondo de reloj en buffer
        Graphics g = backgroundBufferImage.getGraphics();
        dibujarFondoReloj(g);
        g.dispose();

        while (true) {
            obtenerHorarioDelSistema();

            // Obtener las manecillas almacenadas en el buffer
            g = foregroundBufferImage.getGraphics();

            // Dibujar manecillas en el buffer, encima del fondo del reloj
            g.drawImage(backgroundBufferImage, 0, 0, null);

            // Actualizar manecillas en el buffer
            dibujarManecillas(g);
            g.dispose();
            
            repaint();
            try {
              Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        g.drawImage(foregroundBufferImage, 0, 0, null);
    }

    private void dibujarFondoReloj(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Dibujar la imagen de fondo
        g.drawImage(backgroundImage, 0, -7, 800, 800, null);

        RelojFondo.dibujarMarcoReloj(g, centerX, centerY);
        RelojFondo.dibujarInteriorReloj(g, centerX, centerY);
        RelojFondo.dibujarLineasDelgadasInteriorReloj(g, centerX, centerY);
        RelojFondo.dibujarLineasInteriorReloj(g, centerX, centerY);
        RelojFondo.dibujarHorasReloj(g, centerX, centerY);
    }

    private void obtenerHorarioDelSistema() {
        LocalTime horaActual = LocalTime.now();
        int segundosAnteriores = segundos;
        horas = horaActual.getHour();
        minutos = horaActual.getMinute();
        segundos = horaActual.getSecond();
        if (segundos != segundosAnteriores) {
            tiempoInicialSegundo = System.nanoTime();
        }
        System.out.println("horas: " + horas + " minutos: " + minutos + " segundos: " + segundos);
    }

    private void dibujarManecillas(Graphics g) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double fraccionSegundo = (System.nanoTime() - tiempoInicialSegundo) / 1_000_000_000.0;
        double anguloHoras = (360.0 / 12) * ((horas % 12) + minutos / 60.0) - 90;
        double anguloMinutos = (360.0 / 60) * (minutos + (segundos + fraccionSegundo) / 60.0) - 90;
        double anguloSegundos = (360.0 / 60) * (segundos + fraccionSegundo) - 90;

        dibujarManecilla(g, centerX, centerY, anguloHoras, 35, colorManecillaHoras);
        dibujarManecilla(g, centerX, centerY, anguloMinutos, 60, colorManecillaMinutos);
        dibujarManecilla(g, centerX, centerY, anguloSegundos, 90, colorManecillaSegundos);
      
        // circulo del centro
        g.setColor(colorManecillaSegundos);
        g.drawOval(centerX - 4, centerY - 4, 8, 8);
    }

    private void dibujarManecilla(Graphics g, int x, int y, double angulo, int longitud, Color color) {
        int x2 = x + (int) (longitud * Math.cos(Math.toRadians(angulo)));
        int y2 = y + (int) (longitud * Math.sin(Math.toRadians(angulo)));

        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(4)); // ancho de manecillas
        g.drawLine(x, y, x2, y2);
    }

    public static void main(String[] args) throws IOException {
        new Reloj();
    }
}
