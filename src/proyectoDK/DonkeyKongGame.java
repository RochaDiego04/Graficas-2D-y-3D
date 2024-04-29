package proyectoDK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Queue;

public class DonkeyKongGame extends JFrame implements Runnable, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    // Flags for movementBarrel
    private boolean reachedVerticalLine = false;
    private boolean reachedThirdLine = false;
    private boolean reachedSecondVerticalLine = false;
    private boolean reachedFifthLine = false;
    private boolean reachedSixthVerticalLine = false;
    private boolean reachedSeventhhLine = false;

    // Pendientes de movimiento de barril
    private double slopeFirstLine;
    private double slopeThirdLine;
    private double slopeFifthLine;
    private double slopeSeventhLine;

    private int BARREL_HEIGHT = 20;

    private boolean isRunning;

    private Image fondo;
    private BufferedImage bufferImage;
    private Image buffer;
    private Graphics graPixel;


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

    private enum PlayerState {
        FIRST_LINE,
        SECOND_LINE_VERTICAL,
        THIRD_LINE,
        FOURTH_LINE_VERTICAL,
        FIFTH_LINE,
        SIXTH_LINE,
        SEVENTH_LINE
    }

    private BarrelState barrelState = BarrelState.FIRST_LINE;
    private PlayerState playerState = PlayerState.FIRST_LINE;

    public DonkeyKongGame() {
        setTitle("Donkey Kong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        addKeyListener(this);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);


        playerX = 20;
        playerY = HEIGHT - 60;

        barrelX = 50;
        barrelY = 100;
        barrelVisible = false;

        isRunning = true;
        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        graPixel = buffer.getGraphics(); // Inicializa graPixel aquí

        calculateSlopes();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void run() {
        while (isRunning) {
            // Actualizacion de movimiento de barril
            logicMoveBarrel();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (fondo == null) {
            fondo = createImage(getWidth(), getHeight());
            Graphics gfondo = fondo.getGraphics();
            gfondo.setClip(0, 0, getWidth(), getHeight());
        }
        update(graphics);
    }

    @Override
    public void update(Graphics graphics) {
        graphics.setClip(0, 0, getWidth(), getHeight());
        buffer = createImage(getWidth(), getHeight());
        graPixel = buffer.getGraphics();
        graPixel.setClip(0, 0, getWidth(), getHeight());
        graphics.drawImage(buffer, 0, 0, this);
    }

    @Override
    public void repaint() {
        // Llena el buffer con el color negro
        graPixel.setColor(Color.BLACK);
        graPixel.fillRect(0, 0, WIDTH, HEIGHT);

        // Dibuja al jugador
        drawRectangle(playerX, playerY, playerX + 20, playerY + 20, Color.white);

        // Dibuja escenario
        drawBresenhamLine(0, 700, 100, 120, Color.orange);
        drawBresenhamLine(800, 70, 170, 220, Color.orange);
        drawBresenhamLine(0, 700, 280, 330, Color.orange);
        drawBresenhamLine(800, 0, 415, 465, Color.orange);

        // Dibuja escalera corta 1
        drawBresenhamLine(630, 630, 120, 180, Color.blue);

        // Dibuja escalera 2
        drawBresenhamLine(250, 250, 210, 300, Color.blue);

        // Dibuja escalera 3
        drawBresenhamLine(650, 650, 330, 420, Color.blue);


        // Dibuja el barril si está visible
        if (barrelVisible) {
            //bufferGraphics.fillRect(barrelX, barrelY, 20, BARREL_HEIGHT);
            drawRectangle(barrelX, barrelY, barrelX + 20, barrelY + 20, Color.red);
        }

        // Dibuja el buffer en la pantalla
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    /****************** MOVIMIENTO DEL BARRIL *******************/

    private void calculateSlopes() {
        // Calcular pendiente para la primera línea
        int startXFirstLine = 0;
        int startYFirstLine = 100;
        int endXFirstLine = 620; // Inicio de la escalera
        int endYFirstLine = 120;
        slopeFirstLine = (double) (endYFirstLine - startYFirstLine) / (endXFirstLine - startXFirstLine);

        // Calcular pendiente para la tercera línea
        int startXThirdLine = 620; // Fin de la escalera
        int startYThirdLine = 180;
        int endXThirdLine = 70; // Inicio de la tercera línea
        int endYThirdLine = 220;
        slopeThirdLine = (double) (endYThirdLine - startYThirdLine) / (endXThirdLine - startXThirdLine);

        // Calcular pendiente para la quinta línea
        int startXFifthLine = 250; // Fin de la segunda escalera
        int startYFifthLine = 300;
        int endXFifthLine = 950; // Inicio de la quinta línea
        int endYFifthLine = 350;
        slopeFifthLine = (double) (endYFifthLine - startYFifthLine) / (endXFifthLine - startXFifthLine);

        // Calcular pendiente para la séptima línea
        int startXSeventhLine = 650; // Fin de la escalera
        int startYSeventhLine = 425;
        int endXSeventhLine = 0; // Inicio de la tercera línea
        int endYSeventhLine = 465;
        slopeSeventhLine = (double) (endYSeventhLine - startYSeventhLine) / (endXSeventhLine - startXSeventhLine);
        System.out.println("1: " + slopeFirstLine + "2: " +slopeThirdLine + "5: " + slopeFifthLine + "7: " + slopeSeventhLine);
    }

    private void moveBarrelFirstLine() {
        //System.out.println("Moviendo barril por la primer linea");
        int startX = 0;
        int startY = 100;
        int endX = 620; // Inicio de la escalera
        int endY = 120;

        double intercept = startY - slopeFirstLine * startX;

        barrelX++;

        barrelY = (int) (slopeFirstLine * barrelX + intercept) - BARREL_HEIGHT;

        // Verificar si se alcanzó el final de la primera línea
        if (barrelX >= endX) {
            reachedVerticalLine = true;
        }
    }

    private void moveBarrelSecondLineVertical() {
        //System.out.println("Moviendo barril por la segunda linea");
        int endYVertical = 180;

        barrelY++;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            reachedThirdLine = true;
        }
    }


    private void moveBarrelThirdLine() {
        //System.out.println("Moviendo barril por la tercera linea");
        int startX = 620; // Fin de la escalera
        int startY = 180;
        int endX = 70; // Inicio de la tercera línea
        int endY = 220;

        double intercept = startY - slopeThirdLine * startX;

        barrelX--;

        barrelY = (int) (slopeThirdLine * barrelX + intercept) - BARREL_HEIGHT;
        if (barrelX <= 240) {
            reachedSecondVerticalLine = true;
        }
    }

    private void moveBarrelFourthLineVertical() {
        //System.out.println("Moviendo barril por la cuarta linea");
        int endYVertical = 300; // Fin de la segunda escalera

        barrelY++;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.FIFTH_LINE;
        }
    }

    private void moveBarrelFifthLine() {
        //System.out.println("Moviendo barril por la quinta linea");
        //(x1: 0, y1: 280, x2: 700, y2: 330);
        int startX = 250; // Fin de la segunda escalera
        int startY = 300;
        int endX = 950; // Inicio de la quinta línea
        int endY = 350;

        double intercept = startY - slopeFifthLine * startX;

        barrelX++;

        barrelY = (int) (slopeFifthLine * barrelX + intercept) - BARREL_HEIGHT;
        if (barrelX >= 640) {
            reachedSixthVerticalLine = true;
        }
    }

    private void moveBarrelSixthLineVertical() {
        //System.out.println("Moviendo barril por la sexta linea");
        int endYVertical = 425; // Fin de la tercera escalera

        barrelY++;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.SEVENTH_LINE;
        }
    }

    private void moveBarrelSeventhLine() {
        //(x1: 800, y1: 415, x2: 0, y2: 465);
        //System.out.println("Moviendo barril por la septima linea");
        int startX = 650; // Fin de la escalera
        int startY = 425;
        int endX = 0; // Inicio de la tercera línea
        int endY = 465;

        double intercept = startY - slopeSeventhLine * startX;

        barrelX--;

        barrelY = (int) (slopeSeventhLine * barrelX + intercept) - BARREL_HEIGHT;
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

    private void resetBarrel() {
        barrelX = 50;
        barrelY = 100;
        barrelVisible = true;

        reachedVerticalLine = false;
        reachedThirdLine = false;
        reachedSecondVerticalLine = false;
        reachedFifthLine = false;
        reachedSixthVerticalLine = false;
        reachedSeventhhLine = false;

        barrelState = BarrelState.FIRST_LINE;
    }


    public void logicMoveBarrel() {
        // Mueve el barril hacia abajo si está visible
        if (barrelVisible) {
            moveBarrel();
            // Comprueba si el barril colisiona con el jugador
            if (barrelX + 20 >= playerX && barrelX <= playerX + 20 && barrelY + 20 >= playerY && barrelY <= playerY + 20) {
                // Colisión detectada
                JOptionPane.showMessageDialog(this, "¡Has sido golpeado por un barril! Juego terminado.");
                System.exit(0);
            }
            // Si el barril llega al suelo o sale de los limites en X, desaparece
            if (barrelY >= HEIGHT || barrelX < 0) {
                barrelVisible = false;
            }
        } else {
            // Genera un nuevo barril un tiempo aleatorio despues de que el primero desaparezca
            if (Math.random() < 0.01) {
                resetBarrel();
            }
        }
    }



    /****************** MOVIMIENTO DEL JUGADOR *******************/

    public void keyPressed(KeyEvent e) {
        double angleInDegrees = Math.toDegrees(Math.atan(slopeSeventhLine));
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && playerX > 0) {
            playerX -= (int) (10 * Math.cos(Math.toRadians(5))); // Mover hacia abajo a la izquierda
            playerY += (int) (10 * Math.sin(Math.toRadians(5))); // Mover hacia abajo a la izquierda
        } else if (keyCode == KeyEvent.VK_RIGHT && playerX < WIDTH - 20) {
            playerX += (int) (10 * Math.cos(Math.toRadians(5))); // Mover hacia arriba a la derecha
            playerY -= (int) (10 * Math.sin(Math.toRadians(5))); // Mover hacia arriba a la derecha
        }
    }

    public void keyReleased(KeyEvent e) {
        // No necesitamos implementar este método, pero debemos proporcionar una implementación vacía para KeyListener
    }

    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar este método, pero debemos proporcionar una implementación vacía para KeyListener
    }

    public static void main(String[] args) {
        DonkeyKongGame donkeyKongGame = new DonkeyKongGame();
        Thread gameThread = new Thread(donkeyKongGame);
        gameThread.start();
    }


    /****************** METODOS DE DIBUJO *******************/
    private void putPixel(int x, int y, Color color) {
        bufferImage.setRGB(0, 0, color.getRGB());
        graPixel.drawImage(bufferImage, x, y, this);
    }
    public void drawBresenhamLine(int x0, int x1, int y0, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        while (x0 != x1 || y0 != y1) {
            putPixel(x0, y0, color);
            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
    public void drawRectangle(int x0, int y0, int x1, int y1, Color color) {
        // If x0 has a greater value, exchange values with x1
        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;
        }
        if (y0 > y1) {
            int temp = y0;
            y0 = y1;
            y1 = temp;
        }

        drawBresenhamLine(x0, x1, y0, y0, color);
        drawBresenhamLine(x0, x1, y1, y1, color);
        drawBresenhamLine(x0, x0, y0, y1, color);
        drawBresenhamLine(x1, x1, y0, y1, color);
    }

    /*public void floodFill(int x, int y, Color targetColor, Color fillColor) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px >= 0 && px < getWidth() && py >= 0 && py < getHeight() &&
                    bufferImage.getRGB(px, py) == targetColor.getRGB()) {
                putPixel(px, py, fillColor);

                // Revisar los vecinos
                queue.add(new Point(px + 1, py));
                queue.add(new Point(px - 1, py));
                queue.add(new Point(px, py + 1));
                queue.add(new Point(px, py - 1));
            }
        }
    }*/
}
