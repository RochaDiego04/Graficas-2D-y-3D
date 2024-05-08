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
    public static final Color SKY1 = new Color(0, 10, 20);
    public static final Color SKY2 = new Color(0, 13, 23);
    public static final Color SKY3 = new Color(1, 16, 26);
    public static final Color SKY4 = new Color(0, 23, 35);
    public static final Color SKY5 = new Color(0, 31, 47);

    public static final Color GRASS1 = new Color(61, 107, 73);
    public static final Color GRASS2 = new Color(36, 73, 49);
    public static final Color GRASS3 = new Color(20, 35, 25);
    public static final Color GROUND1 = new Color(17, 25, 20);
    public static final Color GROUND2 = new Color(15, 22, 20);
    public static final Color GROUND3 = new Color(13, 18, 20);
    public static final Color GROUND4 = new Color(10, 15, 17);

    public static final Color PLATFORM1 = new Color(169, 17, 63);
    public static final Color PLATFORM2 = new Color(203, 58, 103);

    public static final Color STAIRS2 = new Color(0, 187, 187);
    public static final Color STAIRS1 = new Color(12, 108, 108);

    public static final Color MOON = new Color(203, 202, 202);
    public static final Color MOONDETAILS = new Color(183, 183, 183);

    public static final Color CHARACTER_FACE = new Color(12, 144, 150);
    public static final Color CHARACTER_LEGS_BEAK = new Color(249, 175, 59);
    public static final Color CHARACTER_CLOTHING = new Color(87, 37, 46);
    public static final Color CHARACTER_CLOTHING2 = new Color(190, 172, 122);

    public static final Color BARRELS1 = new Color(93, 53, 45);
    public static final Color BARRELS2 = new Color(63, 36, 31);
    public static final Color BARRELS3 = new Color(128, 79, 74);

    // WINDOW DIMENSIONS
    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    // PLATFORMS
    private ArrayList<int[]> platforms;

    // LADDERS
    private ArrayList<int[]> ladders;

    private Player player;
    int[] xPoints;
    int[] yPoints;

    // BARRELS
    private List<Barrel> barrels;
    int NUMBER_BARRELS = 6;

    // THREAD AND BUFFER
    private boolean isRunning;
    private BufferedImage bufferImage;
    private BufferedImage buffer;
    private BufferedImage bufferEscenario;
    private Graphics graPixel;
    private Graphics gEscenario;

    // REGION CODES
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private static final int xLeft = 0;
    private static final int xRight = 800;
    private static final int yTop = 0;
    private static final int yBottom = 500;


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

        player = new Player(this);

        ladders = new ArrayList<>();
        ladders.add(new int[]{610, 60, 20, 110}); // Stair 1
        ladders.add(new int[]{230, 150, 20, 110}); // Stair 2
        ladders.add(new int[]{630, 240, 20, 220}); // Stair 3  x, y , width, height

        platforms = new ArrayList<>();
        platforms.add(new int[]{0, 80, 700, 20}); // x, y, width, height
        platforms.add(new int[]{70, 170, 730, 20}); // x, y, width, height
        platforms.add(new int[]{0, 260, 700, 20}); // x, y, width, height
        platforms.add(new int[]{0, 460, 800, 20}); // x, y, width, height

        isRunning = true;
        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // Buffer for non-static shapes
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graPixel = buffer.getGraphics();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void run() {
        // Buffer for static shapes (background)
        bufferEscenario = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        gEscenario = bufferEscenario.getGraphics();
        drawScene(gEscenario);

        while (isRunning) {
            // Update barrels position
            for (Barrel barrel : barrels) {
                barrel.logicMoveBarrel(player.playerX, player.playerY);
            }
            // Update player position
            logicMovePlayer();
            // Verify if player reached the flag
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
        graPixel.setClip(0, 0, getWidth(), getHeight());
        graphics.drawImage(bufferEscenario, 0, 0, this);
    }

    @Override
    public void repaint() {
        // Draw background image on buffer
        graPixel.drawImage(bufferEscenario, 0, 0, null);

        drawCharacter(graPixel);

        // Draw the barrel if visible, on buffer
        for (Barrel barrel : barrels) {
            if (barrel.barrelVisible) {
                drawCircumference(graPixel,barrel.barrelX + 10, barrel.barrelY + 10,8,4,BARRELS2);
                drawCircumference(graPixel,barrel.barrelX + 10, barrel.barrelY + 10,4,2,BARRELS1);
            }
        }

        // Paint the buffer on screen
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
        }
    }

    public void drawCharacter(Graphics graPixel) {
        // FACE
        xPoints = new int[]{player.playerX + 1, player.playerX + 19, player.playerX + 19, player.playerX + 1};
        yPoints = new int[]{player.playerY, player.playerY, player.playerY + 14, player.playerY + 14};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_FACE);

        // BEAK
        xPoints = new int[]{player.playerX + 6, player.playerX + 14, player.playerX + 14, player.playerX + 6};
        yPoints = new int[]{player.playerY + 8, player.playerY + 8, player.playerY + 10, player.playerY + 10};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_LEGS_BEAK);

        // EYES
        drawBresenhamLine(graPixel, player.playerX + 6, player.playerX + 6, player.playerY + 4,  player.playerY + 6, Color.BLACK);
        drawBresenhamLine(graPixel, player.playerX + 7, player.playerX + 7, player.playerY + 4,  player.playerY + 6, Color.BLACK);

        drawBresenhamLine(graPixel, player.playerX + 13, player.playerX + 13, player.playerY + 4,  player.playerY + 6, Color.BLACK);
        drawBresenhamLine(graPixel, player.playerX + 14, player.playerX + 14, player.playerY + 4,  player.playerY + 6, Color.BLACK);

        // CLOTHING
        xPoints = new int[]{player.playerX + 1, player.playerX + 19, player.playerX + 19, player.playerX + 1};
        yPoints = new int[]{player.playerY + 14, player.playerY + 14, player.playerY + 16, player.playerY + 16};
        fillPolygonScanLine(graPixel,xPoints, yPoints, Color.white);

        xPoints = new int[]{player.playerX + 1, player.playerX + 19, player.playerX + 19, player.playerX + 1};
        yPoints = new int[]{player.playerY + 16, player.playerY + 16, player.playerY + 18, player.playerY + 18};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_CLOTHING);

        // WINGS
        xPoints = new int[]{player.playerX, player.playerX + 3, player.playerX + 3, player.playerX};
        yPoints = new int[]{player.playerY + 6, player.playerY + 6, player.playerY + 16, player.playerY + 16};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_CLOTHING);

        xPoints = new int[]{player.playerX + 17, player.playerX + 20, player.playerX + 20, player.playerX + 17};
        yPoints = new int[]{player.playerY + 6, player.playerY + 6, player.playerY + 10, player.playerY + 10};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_CLOTHING);

        xPoints = new int[]{player.playerX, player.playerX + 3, player.playerX + 3, player.playerX};
        yPoints = new int[]{player.playerY + 10, player.playerY + 10, player.playerY + 16, player.playerY + 16};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_CLOTHING2);

        xPoints = new int[]{player.playerX + 17, player.playerX + 20, player.playerX + 20, player.playerX + 17};
        yPoints = new int[]{player.playerY + 10, player.playerY + 10, player.playerY + 16, player.playerY + 16};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_CLOTHING2);

        // LEGS
        xPoints = new int[]{player.playerX + 5, player.playerX + 7, player.playerX + 7, player.playerX + 5};
        yPoints = new int[]{player.playerY + 18, player.playerY + 18, player.playerY + 20, player.playerY + 20};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_LEGS_BEAK);

        xPoints = new int[]{player.playerX + 13, player.playerX + 15, player.playerX + 15, player.playerX + 13};
        yPoints = new int[]{player.playerY + 18, player.playerY + 18, player.playerY + 20, player.playerY + 20};
        fillPolygonScanLine(graPixel,xPoints, yPoints, CHARACTER_LEGS_BEAK);
    }

    /****************** COMPOSITE DRAWINGS *******************/
    public void drawScene(Graphics gEscenario) {
        drawBackground(gEscenario);
        drawFlag(gEscenario);
        drawPlatforms(gEscenario);
        drawStairs(gEscenario);
        drawBarrelGenerator(gEscenario);
    }

    public void drawBackground(Graphics gEscenario) {
        drawSky(gEscenario);
        drawGrass(gEscenario);
        drawMoon(gEscenario);
    }

    public void drawMoon(Graphics gEscenario) {
        drawBresenhamCircumference(gEscenario, 750, 100, 50, MOON);
        floodFill(gEscenario, 750, 100, MOON, MOON);

        drawBresenhamCircumference(gEscenario, 730, 80, 15, MOONDETAILS);
        floodFill(gEscenario, 730, 80, MOONDETAILS, MOONDETAILS);

        drawBresenhamCircumference(gEscenario, 780, 105, 10, MOONDETAILS);
        floodFill(gEscenario, 780, 105, MOONDETAILS, MOONDETAILS);

        drawEllipse(gEscenario, 750, 120, 12, 5, MOONDETAILS);
        floodFill(gEscenario, 750, 120, MOONDETAILS, MOONDETAILS);

        drawBresenhamCircumference(gEscenario, 720, 125, 4, MOONDETAILS);
        floodFill(gEscenario, 720, 125, MOONDETAILS, MOONDETAILS);
    }

    public void drawSky(Graphics gEscenario) {
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

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{280, 280, 345, 345};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, SKY5);

        // STARTS
        int min=0;
        int maxY = 350;
        int maxX = 800;
        for(int i = 0 ; i<200;i++){
            putPixel(gEscenario, (int) (Math.random() * (maxX - min + 1)) + min,(int) (Math.random() * (maxY - min + 1)) + min,Color.white);
        }
    }

    public void drawGrass(Graphics gEscenario) {
        // GRASS
        int[] xPoints = {0, getWidth(), getWidth(), 0};
        int[] yPoints = {345, 345, 350, 350};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, GRASS2);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{350, 350, 355, 355};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, GRASS3);

        // UNDERGROUND
        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{355, 355, 365, 365};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, GROUND1);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{365, 365, 380, 380};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, GROUND2);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{380, 380, 400, 400};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, GROUND3);

        xPoints = new int[]{0, getWidth(), getWidth(), 0};
        yPoints = new int[]{400, 400, 500, 500};
        fillPolygonScanLine(gEscenario,xPoints, yPoints, GROUND4);

        // GRASS DETAILS
        for(int y = 345; y < 350; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GRASS1, 0b111111011111);
        }
        for(int y = 350; y < 355; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GRASS1, 0b111000001100001111);
        }

        for(int y = 350; y < 360; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GRASS2, 0b11111100111110001110000101000011);
        }
        for(int y = 360; y < 365; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GRASS2, 0b1100000011100011100000111000011);
        }

        for(int y = 360; y < 370; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GRASS3, 0b1111110001000011110011110100011);
        }
        for(int y = 370; y < 375; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GRASS3, 0b00010010000100011100000100001001);
        }

        for(int y = 375; y < 385; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GROUND1, 0b010010001001000001000100001001);
        }
        for(int y = 380; y < 395; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GROUND2, 0b000100010000010000010000001);
        }
        for(int y = 400; y < 420; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GROUND3, 0b0000110001000001100001000001);
        }
        for(int y = 400; y < 410; y++) {
            drawLine(gEscenario,0, 800, y, y, false , GROUND3, 0b110000101000001000001000);
        }
    }

    public void drawFlag(Graphics gEscenario) {
        drawBresenhamLine(gEscenario, 49, 49, 50, 80, Color.white);
        drawRightTriangle(gEscenario, 50, 60, 60, 60, 50,50, Color.red);
        floodFill(gEscenario, 55, 56, Color.red, Color.red);
    }

    public void drawPlatforms(Graphics gEscenario) {
        // platform 1
        int yStart = 80;
        int yEnd = 90;
        int startX = 0;
        int endX = 10;

        drawBresenhamLine(gEscenario,0, 700, 81, 81, PLATFORM2); // shine of platforms

        while (endX <= 700) {
            drawBresenhamLine(gEscenario, startX + 2, endX, yStart, yEnd, PLATFORM2); // shine of platforms
            drawBresenhamLine(gEscenario, endX + 2, endX + 10, yEnd, yStart, PLATFORM2); // shine of platforms

            drawBresenhamLine(gEscenario, startX, endX, yStart, yEnd, PLATFORM1);
            drawBresenhamLine(gEscenario, endX, endX + 10, yEnd, yStart, PLATFORM1);

            startX += 20;
            endX += 20;
        }

        drawBresenhamLine(gEscenario, 0, 700, 80, 80, PLATFORM1);
        drawBresenhamLine(gEscenario, 0, 700, 90, 90, PLATFORM1);

        // plataform 3
        int yStart_Platform3 = 170;
        int yEnd_Platform3 = 180;
        int startX_Platform3 = 70;
        int endX_Platform3 = 80;

        drawBresenhamLine(gEscenario,800, 70, 171, 171, PLATFORM2); // shine of platforms

        while (endX_Platform3 <= 800) {
            drawBresenhamLine(gEscenario, startX_Platform3 + 2, endX_Platform3, yStart_Platform3, yEnd_Platform3, PLATFORM2); // shine of platforms
            drawBresenhamLine(gEscenario, endX_Platform3 + 2, endX_Platform3 + 10, yEnd_Platform3, yStart_Platform3, PLATFORM2); // shine of platforms

            drawBresenhamLine(gEscenario, startX_Platform3, endX_Platform3, yStart_Platform3, yEnd_Platform3, PLATFORM1);
            drawBresenhamLine(gEscenario, endX_Platform3, endX_Platform3 + 10, yEnd_Platform3, yStart_Platform3, PLATFORM1);

            startX_Platform3 += 20;
            endX_Platform3 += 20;
        }

        drawBresenhamLine(gEscenario,800, 70, 170, 170, PLATFORM1);
        drawBresenhamLine(gEscenario,800, 70, 180, 180, PLATFORM1);

        // plataforma 5
        int yStart_Platform5 = 260;
        int yEnd_Platform5 = 270;
        int startX_Platform5 = 0;
        int endX_Platform5 = 10;

        drawBresenhamLine(gEscenario,0, 700, 261, 261, PLATFORM2); // shine of platforms

        while (endX_Platform5 <= 700) {
            drawBresenhamLine(gEscenario, startX_Platform5 + 2, endX_Platform5, yStart_Platform5, yEnd_Platform5, PLATFORM2); // shine of platforms
            drawBresenhamLine(gEscenario, endX_Platform5 + 2, endX_Platform5 + 10, yEnd_Platform5, yStart_Platform5, PLATFORM2); // shine of platforms

            drawBresenhamLine(gEscenario, startX_Platform5, endX_Platform5, yStart_Platform5, yEnd_Platform5, PLATFORM1);
            drawBresenhamLine(gEscenario, endX_Platform5, endX_Platform5 + 10, yEnd_Platform5, yStart_Platform5, PLATFORM1);

            startX_Platform5 += 20;
            endX_Platform5 += 20;
        }

        drawBresenhamLine(gEscenario,0, 700, 260, 260, PLATFORM1);
        drawBresenhamLine(gEscenario,0, 700, 270, 270, PLATFORM1);

        // plataforma 7
        int yStart_Platform7 = 460;
        int yEnd_Platform7 = 470;
        int startX_Platform7 = 0;
        int endX_Platform7 = 10;

        drawBresenhamLine(gEscenario,800, 0, 461, 461, PLATFORM2); // shine of platforms

        while (endX_Platform7 <= 800) {
            drawBresenhamLine(gEscenario, startX_Platform7 + 2, endX_Platform7, yStart_Platform7, yEnd_Platform7, PLATFORM2); // shine of platforms
            drawBresenhamLine(gEscenario, endX_Platform7 + 2, endX_Platform7 + 10, yEnd_Platform7, yStart_Platform7, PLATFORM2); // shine of platforms

            drawBresenhamLine(gEscenario, startX_Platform7, endX_Platform7, yStart_Platform7, yEnd_Platform7, PLATFORM1);
            drawBresenhamLine(gEscenario, endX_Platform7, endX_Platform7 + 10, yEnd_Platform7, yStart_Platform7, PLATFORM1);

            startX_Platform7 += 20;
            endX_Platform7 += 20;
        }
        drawBresenhamLine(gEscenario,800, 0, 460, 460, PLATFORM1);
        drawBresenhamLine(gEscenario,800, 0, 470, 470, PLATFORM1);
    }

    public void drawStairs(Graphics gEscenario) {
        // draw stair 1
        // drawBresenhamLine(630, 630, 80, 170, Color.blue);
        drawLine(gEscenario,616, 616, 80, 170, 3, STAIRS2);
        drawLine(gEscenario,615, 615, 80, 170, 3, STAIRS1);
        for (int i = 620; i <= 640; i++) {
            drawLine(gEscenario,i, i, 80, 170, false , STAIRS1, 0b111100000000);
        }
        drawLine(gEscenario,646, 646, 80, 170, 3, STAIRS2);
        drawLine(gEscenario,645, 645, 80, 170, 3, STAIRS1);

        // draw stair 2
        // drawBresenhamLine(250, 250, 170, 260, Color.red);
        drawLine(gEscenario,236, 236, 170, 260, 3, STAIRS2);
        drawLine(gEscenario,235, 235, 170, 260, 3, STAIRS1);
        for (int i = 240; i <= 260; i++) {
            drawLine(gEscenario,i, i, 170, 260, false , STAIRS1, 0b111100000000);
        }
        drawLine(gEscenario,266, 266, 170, 260, 3, STAIRS2);
        drawLine(gEscenario,265, 265, 170, 260, 3, STAIRS1);

        // draw stair 3
        // drawBresenhamLine(650, 650, 260, 460, Color.blue); Linea original
        drawLine(gEscenario,636, 636, 260, 460, 3, STAIRS2);
        drawLine(gEscenario,635, 635, 260, 460, 3, STAIRS1);
        for (int i = 640; i <= 660; i++) {
            drawLine(gEscenario,i, i, 260, 460, false , STAIRS1, 0b111100000000);
        }
        drawLine(gEscenario,666, 666, 260, 460, 3, STAIRS2);
        drawLine(gEscenario,665, 665, 260, 460, 3, STAIRS1);
    }

    public void drawBarrelGenerator(Graphics gEscenario) {
        drawEllipse(gEscenario, 120, 78, 12, 3, BARRELS2);
        floodFill(gEscenario, 120, 78, BARRELS2, BARRELS2);

        drawRectangle(gEscenario, 108, 60, 132, 78, BARRELS2);
        floodFill(gEscenario, 110, 65, BARRELS2, BARRELS2);


        drawEllipse(gEscenario, 120, 60, 12, 3, BARRELS1);
        floodFill(gEscenario, 120, 60, BARRELS1, BARRELS1);

        drawEllipse(gEscenario, 120, 60, 9, 2, Color.BLACK);
        floodFill(gEscenario, 120, 60, Color.BLACK, Color.BLACK);

        drawBresenhamLine(gEscenario, 108, 108, 61, 79, BARRELS3);
        drawBresenhamLine(gEscenario, 109, 109, 62, 80, BARRELS3);
        drawBresenhamLine(gEscenario, 110, 110, 63, 81, BARRELS3);
        drawBresenhamLine(gEscenario, 114, 114, 63, 81, BARRELS3);
        drawBresenhamLine(gEscenario, 115, 115, 63, 81, BARRELS3);
        drawBresenhamLine(gEscenario, 119, 119, 64, 82, BARRELS3);
    }

    /****************** BARREL CREATION *******************/
    private void createRandomBarrels() {
        Random random = new Random();
        // Create two barrels with speed from 3 to 4
        for (int i = 0; i < NUMBER_BARRELS / 2; i++) {
            int randomSpeed = random.nextInt(2) + 3;
            Barrel barrel = new Barrel(randomSpeed, this); // Pass randomSpeed to the constructor
            barrels.add(barrel); // Add barrel to list
        }
        // Create two barrels with speed from 1 to 3
        for (int i = 0; i < NUMBER_BARRELS / 2; i++) {
            int randomSpeed = random.nextInt(3) + 1;
            Barrel barrel = new Barrel(randomSpeed, this);
            barrels.add(barrel);
        }
    }


    /****************** PLAYER MOVEMENT *******************/

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && player.playerX > 0) {
            player.playerVx = -player.MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_RIGHT && player.playerX < WIDTH - 20) {
            player.playerVx = player.MOVE_SPEED;
        } else if (keyCode == KeyEvent.VK_UP && isOnLadder()) { // If player is on ladder
            player.playerVy = -player.MOVE_SPEED; // Move the player on Y axis
        } else if (keyCode == KeyEvent.VK_UP && (player.playerY == 440 || player.playerY == 240 || player.playerY == 150 || player.playerY == 60)) { // Allow jumping if player on any platform
            player.playerVy = -player.JUMP_SPEED;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            player.playerVx = 0; // Stop player movement on X axis if key released
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void logicMovePlayer() {
        // Update vertical position
        player.playerY += player.playerVy;

        //Checking for collisions with platforms
        if (player.playerVy > 0) { // If player moving down
            if (isCollidingWithPlatform(player.playerX, player.playerY + 1, 0) ||
                    isCollidingWithPlatform(player.playerX, player.playerY + 1, 1) ||
                    isCollidingWithPlatform(player.playerX, player.playerY + 1, 2) ||
                    isCollidingWithPlatform(player.playerX, player.playerY + 1, 3)) {
                // Stop vertical movement
                player.playerVy = 0;
            }
        } else if (player.playerVy < 0) { // If player moving up
            if (isCollidingWithPlatform(player.playerX, player.playerY, 0) ||
                    isCollidingWithPlatform(player.playerX, player.playerY, 1) ||
                    isCollidingWithPlatform(player.playerX, player.playerY, 2) ||
                    isCollidingWithPlatform(player.playerX, player.playerY, 3)) {
                // Stop vertical movement
                player.playerVy = 0;
            }
        }

        switch (player.playerState) {
            case SEVENTH_LINE:
                if (player.playerY < HEIGHT - 60 && player.playerY > 260 - 20) {
                    //System.out.println("Jugador en la primer area");
                    player.playerVy += player.GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    player.playerY = HEIGHT - 60; // Asegura que el jugador no caiga por debajo del suelo
                    player.playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            case FIFTH_LINE:
                if (player.playerY < 260 - 20 && player.playerY > 170 - 20) { // Corregir la condición para aplicar la gravedad en la quinta línea

                    player.playerVy += player.GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    //System.out.println("moviendose por quinta linea");
                    player.playerY = 260 - 20; // Asegura que el jugador no caiga más abajo de la quinta línea
                    player.playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            case THIRD_LINE:
                if (player.playerY < 170 - 20 && player.playerY > 80 - 20) {
                    //System.out.println("Jugador en la tercer area");
                    player.playerVy += player.GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    //System.out.println("moviendose por tercera linea");
                    player.playerY = 170 - 20; // Asegura que el jugador no caiga más abajo de la quinta línea
                    player.playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            case FIRST_LINE:
                if (player.playerY < 80 - 20) {
                    player.playerVy += player.GRAVITY; // Aplica la gravedad si el jugador está en el aire
                } else {
                    player.playerY = 80 - 20; // Asegura que el jugador no caiga más abajo de la séptima línea
                    player.playerVy = 0; // Detiene el movimiento hacia abajo
                }
                break;
            default:
                break;
        }

        // Actualización de la posición horizontal del jugador
        player.playerX += player.playerVx;
        if (player.playerX < 0) { // Si el jugador está en el borde izquierdo
            player.playerX = 0; // Asegura que el jugador no se salga por el borde izquierdo
        } else if (player.playerX > WIDTH - 20) { // Si el jugador está en el borde derecho
            player.playerX = WIDTH - 20; // Asegura que el jugador no se salga por el borde derecho
        }
    }

    private boolean isOnLadder() {
        for (int[] ladder : ladders) {
            if (player.playerX >= ladder[0] && player.playerX <= ladder[0] + ladder[2] && player.playerY >= ladder[1] && player.playerY <= ladder[1] + ladder[3]) {
                //System.out.println("Esta en escalera");
                return true;
            }
        }
        return false;
    }

    public void logicPlayerWins() {
        // Checking for collisions with flag
        if (player.playerX + 20 >= 49 && player.playerX <= 60 && player.playerY + 20 >= 50 && player.playerY <= 60) {
            // Colisión detectada
            JOptionPane.showMessageDialog(this, "¡Has ganado! ¡Has alcanzado la bandera!");
            System.exit(0);
        }


        drawBresenhamLine(gEscenario, 49, 49, 50, 80, Color.white);
        drawRightTriangle(gEscenario, 50, 60, 60, 60, 50,50, Color.white);
    }

    /* COLISIONS */
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
                player.playerState = Player.PlayerState.values()[platformIndex];
                System.out.println(player.playerState);
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


    /****************** DRAWING METHODS *******************/
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
        int x3 = x0 + (x2 - x0);

        drawBresenhamLine(g, x0, x1, y0, y1, color);
        drawBresenhamLine(g, x1, x2, y1, y2, color);
        drawBresenhamLine(g, x0, x3, y0, y2, color);
    }

    public void drawLine(Graphics g, int x0, int x1, int y0, int y1, boolean continuous, Color color, int mask) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int err2;

        int counter = 0;
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
            drawPixelWithWidth(g, x, y, lineWidth, color);

            if (x == x1 && y == y1) break;

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
            putPixel(g, x + i, y, color);
            putPixel(g, x, y + i, color);
        }
    }

    public void drawCircumference(Graphics g, int centerX, int centerY, int radius, int lineWidth, Color color) {
        int halfWidth = lineWidth / 2;

        for (int y = centerY - radius - halfWidth; y <= centerY + radius + halfWidth; y++) {
            for (int x = centerX - radius - halfWidth; x <= centerX + radius + halfWidth; x++) {
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

                if (Math.abs(distance - radius) <= halfWidth) {
                    putPixel(g, x, y, color);
                }
            }
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

        int minY = Arrays.stream(yPoints).min().getAsInt();
        int maxY = Arrays.stream(yPoints).max().getAsInt();

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

    public void floodFill(Graphics g, int x, int y, Color fillColor, Color borderColor) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px >= 0 && px < getWidth() && py >= 0 && py < getHeight()) {
                int pixelColor = bufferEscenario.getRGB(px, py);

                if (pixelColor != borderColor.getRGB()) {
                    putPixel(g, px, py, fillColor);

                    queue.add(new Point(px + 1, py));
                    queue.add(new Point(px - 1, py));
                    queue.add(new Point(px, py + 1));
                    queue.add(new Point(px, py - 1));
                }
            }
        }
    }



}
