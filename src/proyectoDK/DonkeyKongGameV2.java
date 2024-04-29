package proyectoDK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DonkeyKongGameV2 extends JFrame implements Runnable, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    // Platforms
    private ArrayList<int[]> platforms;

    // Ladders
    private ArrayList<int[]> ladders;

    // Player movement
    private int playerX;
    private int playerY;
    private int playerVy = 0; // Velocidad vertical del jugador
    private int playerVx = 0; // Velocidad horizontal del jugador
    private final int MOVE_SPEED = 2; // Velocidad de movimiento
    private final int GRAVITY = 1; // Gravedad
    private final int JUMP_SPEED = 10; // Velocidad de salto

    // Flags for movementBarrel
    private boolean reachedVerticalLine = false;
    private boolean reachedThirdLine = false;
    private boolean reachedSecondVerticalLine = false;
    private boolean reachedFifthLine = false;
    private boolean reachedSixthVerticalLine = false;
    private boolean reachedSeventhhLine = false;
    private int BARREL_HEIGHT = 20;
    private final int BARREL_MOVE_SPEED = 3; // Velocidad de movimiento
    private int barrelX;
    private int barrelY;
    private boolean barrelVisible;

    // THREAD AND BUFFER
    private boolean isRunning;
    private BufferedImage bufferImage;
    private Image buffer;
    private Image bufferEscenario;
    private Graphics graPixel;
    private Graphics gEscenario;



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
        THIRD_LINE,
        FIFTH_LINE,
        SEVENTH_LINE
    }

    private BarrelState barrelState;
    private PlayerState playerState;

    public DonkeyKongGameV2() {
        setTitle("Donkey Kong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        addKeyListener(this);

        barrelState = BarrelState.FIRST_LINE;
        playerState = PlayerState.SEVENTH_LINE;

        ladders = new ArrayList<>();
        ladders.add(new int[]{610, 60, 20, 110}); // Escalera 1
        ladders.add(new int[]{230, 150, 20, 110}); // Escalera 2
        ladders.add(new int[]{630, 240, 20, 220}); // Escalera 3  x, y , width, height

        platforms = new ArrayList<>();
        platforms.add(new int[]{0, 80, 700, 20}); // x, y, width, height
        platforms.add(new int[]{70, 170, 730, 20}); // x, y, width, height
        platforms.add(new int[]{0, 260, 700, 20}); // x, y, width, height
        platforms.add(new int[]{0, 460, 800, 20}); // x, y, width, height

        playerX = 20;
        playerY = HEIGHT - 60;

        barrelX = 50;
        barrelY = 100;
        barrelVisible = false;

        isRunning = true;
        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graPixel = buffer.getGraphics(); // Inicializa graPixel aquí


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void run() {
        // Dibujar buffer con imagen de fondo
        bufferEscenario = createImage(getWidth(), getHeight());
        gEscenario = bufferEscenario.getGraphics();

        dibujarEscenario(gEscenario);
        dibujarEscaleras(gEscenario);

        while (isRunning) {
            // Actualizacion de movimiento de barril
            logicMoveBarrel();
            logicMovePlayer();

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
        // Dibuja buffer del fondo con el escenario
        graPixel.drawImage(bufferEscenario, 0, 0, null);

        // Dibuja al jugador
        drawRectangle(graPixel,  playerX, playerY, playerX + 20, playerY + 20, Color.white);

        // Dibuja el barril si está visible
        if (barrelVisible) {
            //bufferGraphics.fillRect(barrelX, barrelY, 20, BARREL_HEIGHT);
            drawRectangle(graPixel,barrelX, barrelY, barrelX + 20, barrelY + 20, Color.red);
        }

        // Dibuja el buffer en la pantalla
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    /****************** DIBUJOS COMPUESTOS *******************/
    public void dibujarBandera() {

    }

    public void dibujarEscenario(Graphics gEscenario) {
        // Linea 1
        drawBresenhamLine(gEscenario, 0, 700, 80, 80, Color.orange); // x0, x1, y0. y1
        // Linea 3
        drawBresenhamLine(gEscenario,800, 70, 170, 170, Color.orange);
        // Linea 5
        drawBresenhamLine(gEscenario,0, 700, 260, 260, Color.orange);
        // Linea 7
        drawBresenhamLine(gEscenario,800, 0, 460, 460, Color.orange);
    }

    public void dibujarEscaleras(Graphics gEscenario) {
        // Dibuja escalera 1
        //drawBresenhamLine(630, 630, 80, 170, Color.blue);
        drawLine(gEscenario,615, 615, 80, 170, 3, Color.cyan);
        for (int i = 620; i <= 640; i++) {
            drawLine(gEscenario,i, i, 80, 170, false , Color.cyan);
        }
        drawLine(gEscenario,645, 645, 80, 170, 3, Color.cyan);

        // Dibuja escalera 2
        //drawBresenhamLine(250, 250, 170, 260, Color.red);
        drawLine(gEscenario,235, 235, 170, 260, 3, Color.cyan);
        for (int i = 240; i <= 260; i++) {
            drawLine(gEscenario,i, i, 170, 260, false , Color.cyan);
        }
        drawLine(gEscenario,265, 265, 170, 260, 3, Color.cyan);

        // Dibuja escalera 3
        // drawBresenhamLine(650, 650, 260, 460, Color.blue); Linea original
        drawLine(gEscenario,635, 635, 260, 460, 3, Color.cyan);
        for (int i = 640; i <= 660; i++) {
            drawLine(gEscenario,i, i, 260, 460, false , Color.cyan);
        }
        drawLine(gEscenario,665, 665, 260, 460, 3, Color.cyan);
    }

    /****************** MOVIMIENTO DEL BARRIL *******************/
    private void moveBarrelFirstLine() {
        //System.out.println("Moviendo barril por la primer linea");
        int endX = 620; // Inicio de la escalera 1

        barrelX+=BARREL_MOVE_SPEED;

        // Verificar si se alcanzó el final de la primera línea
        if (barrelX >= endX) {
            reachedVerticalLine = true;
        }
    }

    private void moveBarrelSecondLineVertical() {
        //System.out.println("Moviendo barril por la segunda linea");
        int endYVertical = 170;

        barrelY+=BARREL_MOVE_SPEED;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            reachedThirdLine = true;
        }
    }


    private void moveBarrelThirdLine() {
        //System.out.println("Moviendo barril por la tercera linea");
        int endX = 240; // Inicio de la tercera línea

        barrelX-=BARREL_MOVE_SPEED;
        if (barrelX <= endX) {
            reachedSecondVerticalLine = true;
        }
    }

    private void moveBarrelFourthLineVertical() {
        //System.out.println("Moviendo barril por la cuarta linea");
        int endYVertical = 260; // Fin de la segunda escalera

        barrelY+=BARREL_MOVE_SPEED;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.FIFTH_LINE;
        }
    }

    private void moveBarrelFifthLine() {
        //System.out.println("Moviendo barril por la quinta linea");
        //(x1: 0, y1: 280, x2: 700, y2: 330);
        int endX = 640; // Inicio de la quinta línea

        barrelX+=BARREL_MOVE_SPEED;

        if (barrelX >= endX) {
            reachedSixthVerticalLine = true;
        }
    }

    private void moveBarrelSixthLineVertical() {
        //System.out.println("Moviendo barril por la sexta linea");
        int endYVertical = 460; // Fin de la tercera escalera

        barrelY+=BARREL_MOVE_SPEED;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.SEVENTH_LINE;
        }
    }

    private void moveBarrelSeventhLine() {
        //(x1: 800, y1: 415, x2: 0, y2: 465);
        //System.out.println("Moviendo barril por la septima linea");
        int endX = 0; // Inicio de la tercera línea

        barrelX-=BARREL_MOVE_SPEED;

        if (barrelX <= endX) {
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
        barrelY = 60;
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
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && playerX > 0) {
            playerVx = -MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_RIGHT && playerX < WIDTH - 20) {
            playerVx = MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_UP && isOnLadder()) { // Si el jugador está en una escalera
            playerVy = -MOVE_SPEED; // Mueve al jugador hacia arriba
        } else if (keyCode == KeyEvent.VK_UP && (playerY == HEIGHT - 60 || playerY == 260 - 20 || playerY == 170 - 20 || playerY == 80 - 20)) { // Permite saltar si el jugador está en el suelo o en las plataformas
            playerVy = -JUMP_SPEED; // Inicia un salto
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            playerVx = 0; // Detiene el movimiento horizontal cuando se suelta la tecla
        }
    }

    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar este método, pero debemos proporcionar una implementación vacía para KeyListener
    }

    public void logicMovePlayer() {
        // Actualización de la posición vertical del jugador
        playerY += playerVy;
        boolean isColliding = false; // Variable para mantener el estado de colisión

        // Comprobación de colisiones con las plataformas
        if (playerVy > 0) { // Si el jugador está moviéndose hacia abajo
            if (isCollidingWithPlatform(playerX, playerY + 1, 0) ||
                    isCollidingWithPlatform(playerX, playerY + 1, 1) ||
                    isCollidingWithPlatform(playerX, playerY + 1, 2) ||
                    isCollidingWithPlatform(playerX, playerY + 1, 3)) {
                // Detiene el movimiento vertical hacia abajo
                playerVy = 0;
            }
        } else if (playerVy < 0) { // Si el jugador está moviéndose hacia arriba
            if (isCollidingWithPlatform(playerX, playerY, 0) ||
                    isCollidingWithPlatform(playerX, playerY, 1) ||
                    isCollidingWithPlatform(playerX, playerY, 2) ||
                    isCollidingWithPlatform(playerX, playerY, 3)) {
                // Detiene el movimiento vertical hacia arriba
                playerVy = 0;
            }
        }

        switch (playerState) {
            case SEVENTH_LINE:
                if (playerY < HEIGHT - 60 && playerY > 260 - 20) {
                    //System.out.println("Jugador en la primer area");
                    playerVy += GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    playerY = HEIGHT - 60; // Asegura que el jugador no caiga por debajo del suelo
                    playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            case FIFTH_LINE:
                if (playerY < 260 - 20 && playerY > 170 - 20) { // Corregir la condición para aplicar la gravedad en la quinta línea

                    playerVy += GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    //System.out.println("moviendose por quinta linea");
                    playerY = 260 - 20; // Asegura que el jugador no caiga más abajo de la quinta línea
                    playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            case THIRD_LINE:
                if (playerY < 170 - 20 && playerY > 80 - 20) {
                    //System.out.println("Jugador en la tercer area");
                    playerVy += GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    //System.out.println("moviendose por tercera linea");
                    playerY = 170 - 20; // Asegura que el jugador no caiga más abajo de la quinta línea
                    playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            case FIRST_LINE:
                if (playerY < 80 - 20) {
                    playerVy += GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    playerY = 80 - 20; // Asegura que el jugador no caiga más abajo de la séptima línea
                    playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            default:
                break;
        }

        // Actualización de la posición horizontal del jugador
        playerX += playerVx;
        if (playerX < 0) { // Si el jugador está en el borde izquierdo
            playerX = 0; // Asegura que el jugador no se salga por el borde izquierdo
        } else if (playerX > WIDTH - 20) { // Si el jugador está en el borde derecho
            playerX = WIDTH - 20; // Asegura que el jugador no se salga por el borde derecho
        }
    }

    private boolean isOnLadder() {
        for (int[] ladder : ladders) {
            if (playerX >= ladder[0] && playerX <= ladder[0] + ladder[2] && playerY >= ladder[1] && playerY <= ladder[1] + ladder[3]) {
                System.out.println("Esta en escalera");
                return true;
            }
        }
        return false;
    }

    /* COLISIONES */
    private boolean isCollidingWithPlatform(int x, int y, int platformIndex) {
        int platformX = platforms.get(platformIndex)[0];
        int platformY = platforms.get(platformIndex)[1];
        int platformWidth = platforms.get(platformIndex)[2];
        int platformHeight = platforms.get(platformIndex)[3];

        // Check if the player is within the horizontal bounds of the platform
        if (x + 20 >= platformX && x <= platformX + platformWidth) {
            // Check if the player is touching the top of the platform
            if (y + 20 >= platformY && y + 20 <= platformY + 5) {
                //System.out.println("Colliding with platform " + (platformIndex + 1));
                playerState = PlayerState.values()[platformIndex];
                System.out.println(playerState);
                return true; // The player is touching the top of the platform
            }
            // Check if the player is within the bottom of the platform
            else if (y + 20 >= platformY + platformHeight - 5 && y + 20 <= platformY + platformHeight) {
                return false; // The player is touching the bottom of the platform
            }
        }
        return false; // The player is not touching any platform
    }


    public static void main(String[] args) {
        DonkeyKongGameV2 donkeyKongGame = new DonkeyKongGameV2();
        Thread gameThread = new Thread(donkeyKongGame);
        gameThread.start();
    }


    /****************** METODOS DE DIBUJO *******************/
    private void putPixel(Graphics g, int x, int y, Color color) {
        bufferImage.setRGB(0, 0, color.getRGB());
        g.drawImage(bufferImage, x, y, this);
    }
    public void drawBresenhamLine(Graphics g, int x0, int x1, int y0, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        while (x0 != x1 || y0 != y1) {
            putPixel(g, x0, y0, color);
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
    public void drawRectangle(Graphics g, int x0, int y0, int x1, int y1, Color color) {
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

        drawBresenhamLine(g, x0, x1, y0, y0, color);
        drawBresenhamLine(g, x0, x1, y1, y1, color);
        drawBresenhamLine(g, x0, x0, y0, y1, color);
        drawBresenhamLine(g, x1, x1, y0, y1, color);
    }

    public void drawLine(Graphics g, int x0, int x1, int y0, int y1, boolean continuous, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        int counter = 0;
        int mask = 0b110000000000;
        int maskLength = Integer.toBinaryString(mask).length();

        while (x0 != x1 || y0 != y1) {
            if (continuous || (mask & (1 << counter)) != 0) {
                putPixel(g, x0, y0, color);
            }

            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }

            counter = (counter + 1) % maskLength;
        }
    }

    public void drawLine(Graphics g, int x0, int x1, int y0, int y1, int lineWidth, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;
        int x = x0;
        int y = y0;

        while (true) {
            // Draw the current point with the specified line width
            drawPixelWithWidth(g, x, y, lineWidth, color);

            // Check if we have reached the end point
            if (x == x1 && y == y1) break;

            // Calculate the next point
            err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (err2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }
    private void drawPixelWithWidth(Graphics g, int x, int y, int lineWidth, Color color) {
        for (int i = -(lineWidth / 2); i <= lineWidth / 2; i++) {
            putPixel(g, x + i, y, color); // Vertical segments
            putPixel(g, x, y + i, color); // Horizontal segments
        }
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
