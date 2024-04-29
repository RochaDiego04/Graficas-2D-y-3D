package proyectoDK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Galaga extends JFrame implements Runnable, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BARREL_MOVEMENT = 2;
    private static final int STAIR_HEIGHT = 10; // Altura de la escalera

    private boolean isRunning;
    private Thread gameThread;

    private BufferedImage buffer;
    private Graphics bufferGraphics;

    private int playerX;
    private int playerY;

    private int barrelX;
    private int barrelY;
    private boolean barrelVisible;
    private boolean onStairs; // Indica si el barril está en las escaleras
    private int stairX; // Posición X de la escalera

    public Galaga() {
        setTitle("Donkey Kong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        addKeyListener(this);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();

        playerX = WIDTH / 2;
        playerY = HEIGHT - 50;

        barrelX = 50;
        barrelY = 100;
        barrelVisible = false;

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        while (isRunning) {
            update();
            render();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*private void moveBarrel() {
        // Movimiento del barril en forma de zigzag
        if (barrelX < playerX && barrelY < playerY) {
            barrelX += BARREL_MOVEMENT;
            barrelY += BARREL_MOVEMENT;
        } else if (barrelX > playerX && barrelY < playerY) {
            // Baja hacia abajo y a la izquierda
            barrelX -= BARREL_MOVEMENT;
            barrelY += BARREL_MOVEMENT;
        } else {
            // Desciende en línea recta hacia abajo
            barrelY += BARREL_MOVEMENT;
        }
    }*/




    private void update() {
        // Mueve el barril hacia abajo si está visible
        if (barrelVisible) {
            //moveBarrel(); // Movimiento del barril
            // Comprueba si el barril colisiona con el jugador
            if (barrelX + 20 >= playerX && barrelX <= playerX + 20 && barrelY + 20 >= playerY && barrelY <= playerY + 20) {
                // Colisión detectada
                JOptionPane.showMessageDialog(this, "¡Has sido golpeado por un barril! Juego terminado.");
                System.exit(0);
            }
            // Si el barril llega al suelo, desaparece
            if (barrelY >= HEIGHT) {
                barrelVisible = false;
            }
        } else {
            // Genera un nuevo barril después de un cierto intervalo de tiempo
            if (Math.random() < 0.01) {
                barrelX = 50;
                barrelY = 50;
                barrelVisible = true;
                onStairs = false; // Reinicia el estado de las escaleras
            }
        }
    }

    private void render() {
        // Dibuja todos los elementos en el buffer
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        // Dibuja al jugador
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(playerX, playerY, 20, 20);

        // Dibuja escalera 1
        bufferGraphics.setColor(Color.BLUE);
        bufferGraphics.drawLine(550, 200, 550, 250);


        // Dibuja el barril si está visible
        if (barrelVisible) {
            bufferGraphics.setColor(Color.RED);
            bufferGraphics.fillRect(barrelX, barrelY, 20, 20);
        }

        // Dibuja el buffer en la pantalla
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && playerX > 0) {
            playerX -= 10;
        } else if (keyCode == KeyEvent.VK_RIGHT && playerX < WIDTH - 20) {
            playerX += 10;
        }
    }

    public void keyReleased(KeyEvent e) {
        // No necesitamos implementar este método, pero debemos proporcionar una implementación vacía para KeyListener
    }

    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar este método, pero debemos proporcionar una implementación vacía para KeyListener
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Galaga().setVisible(true));
    }
}
