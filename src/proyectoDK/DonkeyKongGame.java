package proyectoDK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class DonkeyKongGame extends JFrame implements Runnable, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    private static int BARREL_MOVEMENT = 1;

    // Flags for movementBarrel
    private boolean reachedVerticalLine = false;
    private boolean reachedThirdLine = false;
    private boolean reachedSecondVerticalLine = false;
    private boolean reachedFifthLine = false;
    private boolean reachedSixthVerticalLine = false;
    private boolean reachedSeventhhLine = false;

    private int BARREL_HEIGHT = 20;

    private boolean isRunning;
    private Thread gameThread;

    private BufferedImage buffer;
    private Graphics bufferGraphics;

    private int playerX;
    private int playerY;

    private int barrelX;

    private int barrelY;
    private boolean barrelVisible;

    private enum BarrelState {
        FIRST_LINE,
        SECOND_LINE_VERTICAL,
        THIRD_LINE,
        FOURTH_LINE_VERTICAL,
        FIFTH_LINE,
        SIXTH_LINE,
        SEVENTH_LINE
    }

    private BarrelState barrelState = BarrelState.FIRST_LINE;

    public DonkeyKongGame() {
        setTitle("Donkey Kong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        addKeyListener(this);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.getGraphics();

        playerX = 20;
        playerY = HEIGHT - 60;

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

    private void moveBarrelFirstLine() {
        System.out.println("Moviendo barril por la primer linea");
        int startX = 0;
        int startY = 100;
        int endX = 620; // Inicio de la escalera
        int endY = 120;

        double slope = (double) (endY - startY) / (endX - startX);
        double intercept = startY - slope * startX;

        barrelX++;

        barrelY = (int) (slope * barrelX + intercept) - BARREL_HEIGHT;

        // Verificar si se alcanzó el final de la primera línea
        if (barrelX >= endX) {
            reachedVerticalLine = true;
        }
    }

    private void moveBarrelSecondLineVertical() {
        System.out.println("Moviendo barril por la segunda linea");
        int endYVertical = 180;

        barrelY++;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            reachedThirdLine = true;
        }
    }


    private void moveBarrelThirdLine() {
        System.out.println("Moviendo barril por la tercera linea");
        int startX = 620; // Fin de la escalera
        int startY = 180;
        int endX = 70; // Inicio de la tercera línea
        int endY = 220;

        double slope = (double) (endY - startY) / (endX - startX);
        double intercept = startY - slope * startX;

        barrelX--;

        barrelY = (int) (slope * barrelX + intercept) - BARREL_HEIGHT;
        if (barrelX <= 240) {
            reachedSecondVerticalLine = true;
        }
    }

    private void moveBarrelFourthLineVertical() {
        System.out.println("Moviendo barril por la cuarta linea");
        int endYVertical = 300; // Fin de la segunda escalera

        barrelY++;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.FIFTH_LINE;
        }
    }

    private void moveBarrelFifthLine() {
        System.out.println("Moviendo barril por la quinta linea");
        //(x1: 0, y1: 280, x2: 700, y2: 330);
        int startX = 250; // Fin de la segunda escalera
        int startY = 300;
        int endX = 950; // Inicio de la quinta línea
        int endY = 350;

        double slope = (double) (endY - startY) / (endX - startX);
        double intercept = startY - slope * startX;

        barrelX++;

        barrelY = (int) (slope * barrelX + intercept) - BARREL_HEIGHT;
        if (barrelX >= 640) {
            reachedSixthVerticalLine = true;
        }
    }

    private void moveBarrelSixthLineVertical() {
        System.out.println("Moviendo barril por la sexta linea");
        int endYVertical = 425; // Fin de la tercera escalera

        barrelY++;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.SEVENTH_LINE;
        }
    }

    private void moveBarrelSeventhLine() {
        //(x1: 800, y1: 415, x2: 0, y2: 465);
        System.out.println("Moviendo barril por la septima linea");
        int startX = 650; // Fin de la escalera
        int startY = 425;
        int endX = 0; // Inicio de la tercera línea
        int endY = 465;

        double slope = (double) (endY - startY) / (endX - startX);
        double intercept = startY - slope * startX;

        barrelX--;

        barrelY = (int) (slope * barrelX + intercept) - BARREL_HEIGHT;
        if (barrelX <= 240) {
            reachedSecondVerticalLine = true;
        }
    }



    private void moveBarrel() {
        switch (barrelState) {
            case FIRST_LINE:
                moveBarrelFirstLine();
                if (reachedVerticalLine) {
                    barrelState = BarrelState.SECOND_LINE_VERTICAL;
                }
                break;
            case SECOND_LINE_VERTICAL:
                moveBarrelSecondLineVertical();
                if (reachedThirdLine) {
                    barrelState = BarrelState.THIRD_LINE;
                }
                break;
            case THIRD_LINE:
                moveBarrelThirdLine();
                if (reachedSecondVerticalLine) {
                    barrelState = BarrelState.FOURTH_LINE_VERTICAL;
                }
                break;
            case FOURTH_LINE_VERTICAL:
                moveBarrelFourthLineVertical();
                if (reachedFifthLine) {
                    barrelState = BarrelState.FIFTH_LINE;
                }
                break;
            case FIFTH_LINE:
                moveBarrelFifthLine();
                if (reachedSixthVerticalLine) {
                    barrelState = BarrelState.SIXTH_LINE;
                }
                break;
            case SIXTH_LINE:
                moveBarrelSixthLineVertical();
                if (reachedSeventhhLine) {
                    barrelState = BarrelState.SEVENTH_LINE;
                }
                break;
            case SEVENTH_LINE:
                moveBarrelSeventhLine();
                break;
            default:
                break;
        }
    }

    private void update() {
        // Mueve el barril hacia abajo si está visible
        if (barrelVisible) {
            moveBarrel();
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

        // Dibuja escenario
        bufferGraphics.setColor(Color.orange);
        bufferGraphics.drawLine(0, 100, 700, 120);
        bufferGraphics.drawLine(800, 170, 70, 220);
        bufferGraphics.drawLine(0, 280, 700, 330);
        bufferGraphics.drawLine(800, 415, 0, 465);

        // Dibuja escalera corta 1
        bufferGraphics.setColor(Color.BLUE);
        bufferGraphics.drawLine(630, 120, 630, 180);

        // Dibuja escalera 2
        bufferGraphics.setColor(Color.BLUE);
        bufferGraphics.drawLine(250, 210, 250, 300);

        // Dibuja escalera 3
        bufferGraphics.setColor(Color.BLUE);
        bufferGraphics.drawLine(650, 330, 650, 420);


        // Dibuja el barril si está visible
        if (barrelVisible) {
            bufferGraphics.setColor(Color.RED);
            bufferGraphics.fillRect(barrelX, barrelY, 20, BARREL_HEIGHT);
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
        SwingUtilities.invokeLater(() -> new DonkeyKongGame().setVisible(true));
    }
}
