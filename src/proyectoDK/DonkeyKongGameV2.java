package proyectoDK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;

public class DonkeyKongGameV2 extends JFrame implements Runnable, KeyListener {
    // COLORES
    public static final Color SKY1 = new Color(0, 18, 32);
    public static final Color SKY2 = new Color(0, 21, 35);
    public static final Color SKY3 = new Color(1, 24, 38);
    public static final Color SKY4 = new Color(0, 31, 47);

    // DIMENSIONES DE VENTANA
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
    private final int JUMP_SPEED = 10; // Velocidad desalto

    // Barrel
    private List<Barrel> barrels;
    int NUMBER_BARRELS = 4;

    // THREAD AND BUFFER
    private boolean isRunning;
    private BufferedImage bufferImage;
    private Image buffer;
    private Image bufferEscenario;
    private Graphics graPixel;
    private Graphics gEscenario;

    // Definición de códigos de región
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private static final int xLeft = 0;
    private static final int xRight = 800;
    private static final int yTop = 0;
    private static final int yBottom = 500;

    private enum PlayerState {
        FIRST_LINE,
        THIRD_LINE,
        FIFTH_LINE,
        SEVENTH_LINE
    }

    private PlayerState playerState;

    public DonkeyKongGameV2() {
        setTitle("Donkey Kong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        addKeyListener(this);


        barrels = new ArrayList<>();
        createRandomBarrels();
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

        while (isRunning) {
            // Actualizacion de movimiento de barril
            for (Barrel barrel : barrels) {
                barrel.logicMoveBarrel(playerX, playerY);
            }
            // Actualizacion de movimiento de jugador
            logicMovePlayer();
            // Verificar si el jugador alcanza la bandera
            logicPlayerWins();

            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        int[] xPoints = {playerX, playerX + 20, playerX + 20, playerX};
        int[] yPoints = {playerY, playerY, playerY + 20, playerY + 20};
        fillPolygonScanLine(graPixel,xPoints, yPoints, Color.white);
        drawRectangle(graPixel,  playerX, playerY, playerX + 20, playerY + 20, Color.red);

        // Dibuja el barril si está visible
        for (Barrel barrel : barrels) {
            if (barrel.barrelVisible) {
                drawBresenhamCircumference(graPixel, barrel.barrelX + 10, barrel.barrelY + 10, 10, Color.red);
            }
        }

        // Dibuja el buffer en la pantalla
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    /****************** DIBUJOS COMPUESTOS *******************/
    public void dibujarEscenario(Graphics gEscenario) {
        dibujarFondo(gEscenario);
        dibujarBandera(gEscenario);
        dibujarPlataformas(gEscenario);
        dibujarEscaleras(gEscenario);
    }

    public void dibujarFondo(Graphics gEscenario) {
        // CIELO
        int[] xPoints = {0, getWidth(), getWidth(), 0};
        int[] yPoints = {30, 30, 150, 150};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, SKY1);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{150, 150, 190, 190};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, SKY2);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{190, 190, 215, 215};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, SKY3);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{215, 215, 280, 280};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, SKY4);
    }

    public void dibujarBandera(Graphics gEscenario) {
        drawBresenhamLine(gEscenario, 49, 49, 50, 80, Color.white);
        drawRightTriangle(gEscenario, 50, 60, 60, 60, 50,50, Color.white);
        //drawBresenhamCircumference(gEscenario, 50,50,10, Color.red);
    }

    public void dibujarPlataformas(Graphics gEscenario) {
        // plataforma 1
        int yStart = 80;
        int yEnd = 90;
        int startX = 0;
        int endX = 10;
        while (endX <= 700) {
            drawBresenhamLine(gEscenario, startX, endX, yStart, yEnd, Color.orange);
            drawBresenhamLine(gEscenario, endX, endX + 10, yEnd, yStart, Color.orange);

            startX += 20;
            endX += 20;
        }

        drawBresenhamLine(gEscenario, 0, 700, 80, 80, Color.orange); // x0, x1, y0. y1
        drawBresenhamLine(gEscenario, 0, 700, 90, 90, Color.orange); // x0, x1, y0. y1

        // plataforma 3
        int yStart_Platform3 = 170;
        int yEnd_Platform3 = 180;
        int startX_Platform3 = 70; // Empieza desde x = 70
        int endX_Platform3 = 80; // Termina en x = 80, ya que se incrementará en el bucle

        while (endX_Platform3 <= 800) {
            drawBresenhamLine(gEscenario, startX_Platform3, endX_Platform3, yStart_Platform3, yEnd_Platform3, Color.orange);
            drawBresenhamLine(gEscenario, endX_Platform3, endX_Platform3 + 10, yEnd_Platform3, yStart_Platform3, Color.orange);

            startX_Platform3 += 20;
            endX_Platform3 += 20;
        }

        drawBresenhamLine(gEscenario,800, 70, 170, 170, Color.orange);
        drawBresenhamLine(gEscenario,800, 70, 180, 180, Color.orange);

        // plataforma 5
        int yStart_Platform5 = 260;
        int yEnd_Platform5 = 270;
        int startX_Platform5 = 0;
        int endX_Platform5 = 10;

        while (endX_Platform5 <= 700) {
            drawBresenhamLine(gEscenario, startX_Platform5, endX_Platform5, yStart_Platform5, yEnd_Platform5, Color.orange);
            drawBresenhamLine(gEscenario, endX_Platform5, endX_Platform5 + 10, yEnd_Platform5, yStart_Platform5, Color.orange);

            startX_Platform5 += 20;
            endX_Platform5 += 20;
        }

        drawBresenhamLine(gEscenario,0, 700, 260, 260, Color.orange);
        drawBresenhamLine(gEscenario,0, 700, 270, 270, Color.orange);

        // plataforma 7
        int yStart_Platform7 = 460;
        int yEnd_Platform7 = 470;
        int startX_Platform7 = 0;
        int endX_Platform7 = 10;

        while (endX_Platform7 <= 800) {
            drawBresenhamLine(gEscenario, startX_Platform7, endX_Platform7, yStart_Platform7, yEnd_Platform7, Color.orange);
            drawBresenhamLine(gEscenario, endX_Platform7, endX_Platform7 + 10, yEnd_Platform7, yStart_Platform7, Color.orange);

            startX_Platform7 += 20;
            endX_Platform7 += 20;
        }
        drawBresenhamLine(gEscenario,800, 0, 460, 460, Color.orange);
        drawBresenhamLine(gEscenario,800, 0, 470, 470, Color.orange);
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

    /****************** CREACION DE BARRILES *******************/
    private void createRandomBarrels() {
        Random random = new Random();
        for (int i = 0; i < NUMBER_BARRELS; i++) {
            int randomSpeed = random.nextInt(3) + 1; // Generar velocidad aleatoria entre 1 y 3
            Barrel barrel = new Barrel(randomSpeed); // Crear barril con velocidad aleatoria
            barrels.add(barrel); // Agregar barril a la lista
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

    public void logicPlayerWins() {
// Comprueba si el jugador colisiona con la bandera
        if (playerX + 20 >= 49 && playerX <= 60 && playerY + 20 >= 50 && playerY <= 60) {
            // Colisión detectada
            JOptionPane.showMessageDialog(this, "¡Has ganado! ¡Has alcanzado la bandera!");
            System.exit(0);
        }


        drawBresenhamLine(gEscenario, 49, 49, 50, 80, Color.white);
        drawRightTriangle(gEscenario, 50, 60, 60, 60, 50,50, Color.white);
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

    public void drawRightTriangle(Graphics g, int x0, int y0, int x1, int y1, int x2, int y2, Color color) {
        // Calcular el punto de la esquina superior derecha
        int x3 = x0 + (x2 - x0);
        int y3 = y1;

        // Dibujar las tres líneas que forman el triángulo rectángulo
        drawBresenhamLine(g, x0, x1, y0, y1, color);
        drawBresenhamLine(g, x1, x2, y1, y2, color);
        drawBresenhamLine(g, x0, x3, y0, y2, color);
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

    public void drawBresenhamCircumference(Graphics g, int centerX, int centerY, int radius, Color color) {
        int x = 0;
        int y = radius;
        int p = 3 - 2 * radius;

        drawCirclePoints(g, centerX, centerY, x, y, color);

        while (x <= y) {
            x++;
            if (p < 0) {
                p += 4 * x + 6;
            } else {
                y--;
                p += 4 * (x - y) + 10;
            }
            drawCirclePoints(g, centerX, centerY, x, y, color);
        }
    }

    private void drawCirclePoints(Graphics g, int centerX, int centerY, int x, int y, Color color) {
        int code = calculateRegionCode(centerX + x, centerY + y);
        if (code == INSIDE) {
            putPixel(g, centerX + x, centerY + y, color);
        }
        code = calculateRegionCode(centerX - x, centerY + y);
        if (code == INSIDE) {
            putPixel(g, centerX - x, centerY + y, color);
        }
        code = calculateRegionCode(centerX + x, centerY - y);
        if (code == INSIDE) {
            putPixel(g, centerX + x, centerY - y, color);
        }
        code = calculateRegionCode(centerX - x, centerY - y);
        if (code == INSIDE) {
            putPixel(g, centerX - x, centerY - y, color);
        }
        code = calculateRegionCode(centerX + y, centerY + x);
        if (code == INSIDE) {
            putPixel(g, centerX + y, centerY + x, color);
        }
        code = calculateRegionCode(centerX - y, centerY + x);
        if (code == INSIDE) {
            putPixel(g, centerX - y, centerY + x, color);
        }
        code = calculateRegionCode(centerX + y, centerY - x);
        if (code == INSIDE) {
            putPixel(g, centerX + y, centerY - x, color);
        }
        code = calculateRegionCode(centerX - y, centerY - x);
        if (code == INSIDE) {
            putPixel(g, centerX - y, centerY - x, color);
        }
    }

    private int calculateRegionCode(int x, int y) {
        int code = INSIDE;
        if (x < xLeft) {
            code |= LEFT;
        } else if (x > xRight) {
            code |= RIGHT;
        }
        if (y < yTop) {
            code |= TOP;
        } else if (y > yBottom) {
            code |= BOTTOM;
        }
        return code;
    }

    public void drawEllipse(Graphics g, int centerX, int centerY, int radiusMajor, int radiusMinor, Color color) {
        int x = 0;
        int y = radiusMinor;
        int p1 = radiusMinor * radiusMinor - radiusMajor * radiusMajor * radiusMinor + radiusMajor * radiusMajor / 4;
        int dx = 2 * radiusMinor * radiusMinor * x;
        int dy = 2 * radiusMajor * radiusMajor * y;

        drawEllipsePoints(g, centerX, centerY, x, y, color);

        while (dx < dy) {
            x++;
            dx += 2 * radiusMinor * radiusMinor;
            if (p1 < 0) {
                p1 += dx + radiusMinor * radiusMinor;
            } else {
                y--;
                dy -= 2 * radiusMajor * radiusMajor;
                p1 += dx - dy + radiusMinor * radiusMinor;
            }
            drawEllipsePoints(g, centerX, centerY, x, y, color);
        }

        int p2 = (int) (radiusMinor * radiusMinor * (x + 0.5) * (x + 0.5) + radiusMajor * radiusMajor * (y - 1) * (y - 1) - radiusMajor * radiusMajor * radiusMinor * radiusMinor);

        while (y >= 0) {
            y--;
            dy -= 2 * radiusMajor * radiusMajor;
            if (p2 > 0) {
                p2 += radiusMajor * radiusMajor - dy;
            } else {
                x++;
                dx += 2 * radiusMinor * radiusMinor;
                p2 += dx - dy + radiusMajor * radiusMajor;
            }
            drawEllipsePoints(g, centerX, centerY, x, y, color);
        }
    }

    private void drawEllipsePoints(Graphics g, int centerX, int centerY, int x, int y, Color color) {
        putPixel(g, centerX + x, centerY + y, color);
        putPixel(g, centerX - x, centerY + y, color);
        putPixel(g, centerX + x, centerY - y, color);
        putPixel(g, centerX - x, centerY - y, color);
    }

    public void fillPolygonScanLine(Graphics g, int[] xPoints, int[] yPoints, Color color) {

        // Calcular el punto mínimo en X,Y
        int minY = Arrays.stream(yPoints).min().getAsInt();
        int maxY = Arrays.stream(yPoints).max().getAsInt();

        // Iterar en el eje vertical
        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();
            for (int i = 0; i < xPoints.length; i++) {
                int x1 = xPoints[i];
                int y1 = yPoints[i];
                int x2 = xPoints[(i + 1) % xPoints.length];
                int y2 = yPoints[(i + 1) % yPoints.length];

                if ((y1 <= y && y2 >= y) || (y2 <= y && y1 >= y)) {
                    if (y1 != y2) {
                        int x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                        intersections.add(x);
                    } else if (y == y1 && ((y1 == minY && y2 != minY) || (y1 == maxY && y2 != maxY))) {
                        intersections.add(x1);
                    }
                }
            }

            intersections.sort(Integer::compareTo);
            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);
                for (int x = xStart; x <= xEnd; x++) {
                    putPixel(g, x, y, color);
                }
            }
        }
    }

    public void floodFill(Graphics g, int x, int y, Color targetColor, Color fillColor) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px >= 0 && px < getWidth() && py >= 0 && py < getHeight() &&
                    bufferImage.getRGB(px, py) == targetColor.getRGB()) {
                putPixel(g, px, py, fillColor);

                // Revisar los vecinos
                queue.add(new Point(px + 1, py));
                queue.add(new Point(px - 1, py));
                queue.add(new Point(px, py + 1));
                queue.add(new Point(px, py - 1));
            }
        }
    }


}
