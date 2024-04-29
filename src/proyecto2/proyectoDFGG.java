package proyecto2;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.Graphics;

public class proyectoDFGG extends JFrame implements Runnable {

    private Image imgPixel;
    private Graphics grafPixel;
    int width = 1250;
    int height = 1000;

    BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    int rotx = 0;
    int roty = 0;
    int contador = 0;
    int contador2 = 0;
    int ciclos = 0;

    Thread thr = new Thread(this);
    int traslaX = 0;
    int traslaX3 = 0;
    int traslaY = 0;

    Point punto1 = new Point();
    Point punto2 = new Point();
    Point micirc = new Point();

    public proyectoDFGG() {

        this.setSize(width, height);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Proyecto DFGG");
        imgPixel = createImage(1, 1);
        grafPixel = imgPixel.getGraphics();
        thr.start();

    }

    public static void main(String[] args) {
        new proyectoDFGG();
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
            repaint();
        }

    }

    public void actualiza() {

        montanIzq();
        montanDer();
        nubes();
        sol(rotx, roty);
        estrellas();
        cielo();
        pajarito(traslaX, 0, "GRIS");
        piso();
        this.getGraphics().drawImage(buffer, 1, 1, this);

    }

    public void paint(Graphics g) {

        traslaX += 10;
        rotx += 5;
        roty += (rotx > 450) ? 1 : -1;
        actualiza();
        buffer.getGraphics().fillRect(0, 0, width, height);

    }

    public void linea(int x1, int y1, int x2, int y2, int grueso, String colorLinea) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double m = (double) dy / dx;
        int steps = 0;

        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }

        double xinc = (double) dx / steps;
        double yinc = (double) dy / steps;
        double x = x1;
        double y = y1;

        colorsHalsey((int) Math.round(x), (int) Math.round(y), colorLinea);
        for (short k = 1; k <= steps; k++) {
            x = x + xinc;
            y = y + yinc;
            colorsHalsey((int) Math.round(x), (int) Math.round(y), colorLinea);
            if (m < 1) {
                boolean dirArriba = true;
                int ySube = 0;
                int yBaja = 0;
                for (short yVertical = 0; yVertical < grueso; yVertical++) {
                    if (dirArriba) {
                        colorsHalsey((int) Math.round(x), (int) Math.round(y) + ySube, colorLinea);
                        ySube++;
                        dirArriba = false;
                    } else {

                        colorsHalsey((int) Math.round(x), (int) Math.round(y) - yBaja, colorLinea);
                        yBaja++;
                        dirArriba = true;
                    }

                }
            }
        }
    }

    public void linea(int x0, int y0, int x1, int y1, int[] mascara) {

        int jelp = 0;
        int dx = x1 - x0;
        int dy = y1 - y0;
        int steps = 0;
        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }
        double xinc = (double) dx / steps;
        double yinc = (double) dy / steps;
        double x = x0;
        double y = y0;
        colorsHalsey((int) Math.round(x), (int) Math.round(y), "MONTANIA");
        for (short k = 1; k <= steps; k++) {
            x = x + xinc;
            y = y + yinc;
            if (jelp >= mascara.length)
                jelp = 0;
            int pintNoPint = mascara[jelp];
            if (pintNoPint == 1) {
                colorsHalsey((int) Math.round(x), (int) Math.round(y), "MONTANIA");
                jelp++;
            } else {
                jelp++;
                continue;
            }
        }
    }

    public void circulo(int x0, int y0, int radio) {

        int x = 0;
        int y = radio;

        colorsHalsey(x0 + x, y0 + y);
        colorsHalsey(x0 + x, y0 - y);
        colorsHalsey(x0 - x, y0 + y);
        colorsHalsey(x0 - x, y0 - y);
        colorsHalsey(x0 + y, y0 + x);
        colorsHalsey(x0 + y, y0 - x);
        colorsHalsey(x0 - y, y0 + x);
        colorsHalsey(x0 - y, y0 - x);

        double help = 1 - radio;

        while (x < y) {
            x++;
            if (help < 0) {

                help = help + 2 * x + 6;
            } else {

                y = y - 1;
                help = help + (2 * (x - y)) + 1;
            }
            colorsHalsey(x0 + x, y0 + y);
            colorsHalsey(x0 + x, y0 - y);
            colorsHalsey(x0 - x, y0 + y);
            colorsHalsey(x0 - x, y0 - y);
            colorsHalsey(x0 + y, y0 + x);
            colorsHalsey(x0 + y, y0 - x);
            colorsHalsey(x0 - y, y0 + x);
            colorsHalsey(x0 - y, y0 - x);

        }

    }

    public void puntoRom(int cx, int cy, double radio) {

        double help = 5d / 4d - radio;
        int y, mascara = 0, x = 0, dy = -2 * (int) radio, dx = 1000;
        radio = Math.abs(radio);
        if (radio % 1 == 0)
            help = 1 - radio;
        y = (int) radio;
        while (x <= y) {
            if (mascara < 1) {
                colorsHalsey(cx + x, cy + y, "BLANCO");
                colorsHalsey(cx - x, cy + y, "BLANCO");
                colorsHalsey(cx + x, cy - y, "BLANCO");
                colorsHalsey(cx - x, cy - y, "BLANCO");
                colorsHalsey(cx + y, cy + x, "BLANCO");
                colorsHalsey(cx - y, cy + x, "BLANCO");
                colorsHalsey(cx + y, cy - x, "BLANCO");
                colorsHalsey(cx - y, cy - x, "BLANCO");
            }
            if (help >= 0) {
                dy += 2;
                help += dy;
                y--;
            }

            dx += 2;
            help += dx;
            x++;

            mascara = (mascara < 7) ? mascara + 1 : 0;
        }

    }

    public Point movimiento(Point puntoActual, int movimientoEnX, int movimientoEnY) {

        Point movimientoP = new Point();

        movimientoP.x = (int) puntoActual.x + movimientoEnX;
        movimientoP.y = (int) puntoActual.y + movimientoEnY;

        return movimientoP;

    }

    public void drawOval(int x0, int y0, int R1, int R2) {

        int a2 = R1 * R1;
        int b2 = R2 * R2;
        int fa2 = 4 * a2, fb2 = 4 * b2;
        int x, y, diferencia;

        for (x = 0, y = R2, diferencia = 2 * b2 + a2 * (1 - 2 * R2); b2 * x <= a2 * y; x++) {
            colorsHalsey(x0 + x, y0 + y);
            colorsHalsey(x0 - x, y0 + y);
            colorsHalsey(x0 - x, y0 - y);
            colorsHalsey(x0 + x, y0 - y);

            if (diferencia >= 0) {
                diferencia += fa2 * (1 - y);
                y--;

            }

            diferencia += b2 * ((4 * x) + 6);
        }

        for (x = R1, y = 0, diferencia = 2 * a2 + b2 * (1 - 2 * R1); a2 * y <= b2 * x; y++) {
            colorsHalsey(x0 + x, y0 + y);
            colorsHalsey(x0 - x, y0 + y);
            colorsHalsey(x0 - x, y0 - y);
            colorsHalsey(x0 + x, y0 - y);

            if (diferencia >= 0) {
                diferencia += fb2 * (1 - x);
                x--;

            }

            diferencia += a2 * ((4 * y) + 6);
        }

    }

    public void floodFill(int x, int y, String colorFlood) {

        BufferedImage image = buffer;
        Queue<Point> colaPuntos = new LinkedList<Point>();
        int color1 = image.getRGB(x, y);
        colaPuntos.add(new Point(x, y));
        while (colaPuntos.size() > 0) {
            Point help = colaPuntos.remove();
            if (help.x > 0 && help.x <= buffer.getWidth() && help.y > 0 && help.y <= buffer.getHeight()) {
                if (image.getRGB(help.x, help.y) == color1) {
                    x = help.x;
                    y = help.y;
                    colorsHalsey(x, y, colorFlood);
                    colaPuntos.add(new Point(x - 1, y));
                    colaPuntos.add(new Point(x + 1, y));
                    colaPuntos.add(new Point(x, y - 1));
                    colaPuntos.add(new Point(x, y + 1));
                }
            }
        }
    }

    public void colorsHalsey(int x, int y, String colorPixel) {
        if (x > buffer.getWidth() - 10) {

        } else {
            switch (colorPixel) {
                case "MONTANIA":
                    grafPixel.setColor(new Color(102, 51, 0));
                    break;
                case "GRIS":
                    grafPixel.setColor(new Color(152, 152, 152));
                    break;
                case "AZUL":
                    grafPixel.setColor(new Color(36, 113, 163));
                    break;
                case "BLANCO":
                    grafPixel.setColor(new Color(254, 254, 254));
                    break;
                case "SOL":
                    grafPixel.setColor(new Color(255, 175, 0));
                    break;
                case "LUNA":
                    grafPixel.setColor(new Color(255, 255, 204));
                    break;
                case "PISO":
                    grafPixel.setColor(new Color(255, 255, 204));
                    break;
                case "AMARILLO":
                    grafPixel.setColor(new Color(255, 255, 0));
                    break;

                case "CIELO0":
                    grafPixel.setColor(new Color(0, 0, 51));
                    break;
                case "CIELO1":
                    grafPixel.setColor(new Color(0, 0, 76));
                    break;
                case "CIELO2":
                    grafPixel.setColor(new Color(0, 0, 102));
                    break;
                case "CIELO3":
                    grafPixel.setColor(new Color(0, 26, 127));
                    break;
                case "CIELO4":
                    grafPixel.setColor(new Color(0, 51, 153));
                    break;
                case "CIELO5":
                    grafPixel.setColor(new Color(0, 76, 178));
                    break;
                case "CIELO6":
                    grafPixel.setColor(new Color(0, 102, 204));
                    break;
                case "CIELO7":
                    grafPixel.setColor(new Color(0, 127, 229));
                    break;
                case "CIELO8":
                    grafPixel.setColor(new Color(0, 153, 255));
                    break;
                case "CIELO9":
                    grafPixel.setColor(new Color(0, 182, 255));
                    break;
                case "CIELO10":
                    grafPixel.setColor(new Color(51, 204, 255));
                    break;

            }
            buffer.setRGB(x, y, grafPixel.getColor().getRGB());
        }
    }

    public void colorsHalsey(int x, int y) {

        buffer.setRGB(x, y, grafPixel.getColor().getRGB());
    }

    public void dibujaCuadros(int x1, int y1, int x2, int y2) {
        linea(x1, y1, x2, y1, 0, "MONTANIA");
        linea(x1, y1, x1, y2, 0, "CIELO1");
        linea(x2, y1, x2, y2, 0, "CIELO1");
        linea(x1, y2, x2, y2, 0, "MONTANIA");
    }

    public void montanIzq() {

        // linea arriba
        linea(7, 32, 105, 200, 0, "MONTANIA");
        // linea izquierda
        linea(7, 32, 7, 200, 0, "MONTANIA");
        // linea derecha
        linea(7, 32, 105, 200, 0, "MONTANIA");
        // linea abajo
        linea(7, 200, 105, 200, 0, "MONTANIA");
        floodFill(33, 199, "MONTANIA");

        // linea arriba
        linea(7, 200, 105, 200, 0, "MONTANIA");
        // linea izquierda
        linea(7, 200, 7, 400, 0, "MONTANIA");
        // linea derecha
        linea(105, 200, 125, 400, 0, "MONTANIA");
        // linea abajo
        linea(7, 400, 125, 400, 0, "MONTANIA");
        floodFill(33, 399, "MONTANIA");

        // linea arriba
        linea(7, 400, 125, 400, 0, "MONTANIA");
        // linea izquierda
        linea(7, 400, 7, 600, 0, "MONTANIA");
        // linea derecha
        linea(125, 400, 125, 600, 0, "MONTANIA");
        // linea abajo
        linea(7, 600, 125, 600, 0, "MONTANIA");
        floodFill(33, 599, "MONTANIA");

        // linea arriba
        linea(7, 600, 125, 600, 0, "MONTANIA");
        // linea izquierda
        linea(7, 600, 7, 800, 0, "MONTANIA");
        // linea derecha
        linea(125, 600, 115, 800, 0, "MONTANIA");
        // linea abajo
        linea(7, 800, 115, 800, 0, "MONTANIA");
        floodFill(33, 799, "MONTANIA");

        // linea arriba
        linea(7, 800, 115, 800, 0, "MONTANIA");
        // linea izquierda
        linea(7, 800, 7, 997, 0, "MONTANIA");
        // linea derecha
        linea(115, 800, 130, 997, 0, "MONTANIA");
        // linea abajo
        linea(7, 997, 130, 997, 0, "MONTANIA");
        floodFill(33, 996, "MONTANIA");

    }

    public void montanDer() {

        // linea arriba
        linea(1240, 400, 1150, 600, 0, "MONTANIA");
        // linea izquierda
        linea(1240, 400, 1150, 600, 0, "MONTANIA");
        // linea derecha
        linea(1240, 400, 1240, 600, 0, "MONTANIA");
        // linea abajo
        linea(1150, 600, 1240, 600, 0, "MONTANIA");
        floodFill(1239, 599, "MONTANIA");

        // linea arriba
        linea(1150, 600, 1240, 600, 0, "MONTANIA");
        // linea izquierda
        linea(1150, 600, 1195, 800, 0, "MONTANIA");
        // linea derecha
        linea(1240, 600, 1240, 800, 0, "MONTANIA");
        // linea abajo
        linea(1195, 800, 1240, 800, 0, "MONTANIA");
        floodFill(1239, 799, "MONTANIA");

        // linea arriba
        linea(1195, 800, 1240, 800, 0, "MONTANIA");
        // linea izquierda
        linea(1195, 800, 1100, 997, 0, "MONTANIA");
        // linea derecha
        linea(1240, 800, 1240, 997, 0, "MONTANIA");
        // linea abajo
        linea(1100, 997, 1240, 997, 0, "MONTANIA");
        floodFill(1239, 996, "MONTANIA");

    }

    public void nubes() {

        if (contador < 220) {

            circulo(10, 10, 3);
            floodFill(10, 10, "BLANCO");

            circulo(100, 100, 35);
            circulo(115, 80, 35);
            circulo(140, 115, 35);
            circulo(150, 90, 35);
            circulo(160, 105, 35);

            circulo(415, 200, 35);
            circulo(430, 180, 35);
            circulo(455, 215, 35);
            circulo(465, 190, 35);
            circulo(475, 205, 35);

            circulo(1015, 130, 35);
            circulo(1030, 110, 35);
            circulo(1055, 145, 35);
            circulo(1065, 120, 35);
            circulo(1075, 135, 35);

            circulo(715, 400, 35);
            circulo(730, 380, 35);
            circulo(755, 415, 35);
            circulo(765, 390, 35);
            circulo(775, 405, 35);
        }
    }

    public void sol(int xTras, int yTras) {

        String elcolor = "";
        if (contador <= 220)
            elcolor = "SOL";
        else
            elcolor = "LUNA";
        micirc.x = 50;
        micirc.y = 176;
        micirc = movimiento(micirc, xTras, yTras);
        if (micirc.x > buffer.getWidth() - 100) {
            rotx = 0;
            roty = 0;
            return;
        }

        circulo(micirc.x, micirc.y, 55);
        floodFill(104 + xTras, 176 + yTras, elcolor);

    }

    public void estrellas() {

        if (contador >= 221) {

            if (!((contador % 10) == 0)) {

                int estrx = 110;
                int estry = 75;

                for (short i = 0; i < 10; i++) {
                    if (i < 7) {
                        for (short j = 0; j < 14; j++) {

                            puntoRom(estrx, estry, 75);

                            estrx += 80;
                        }
                    } else {
                        for (short k = 0; k < 13; k++) {

                            puntoRom(estrx, estry, 75);

                            estrx += 80;
                        }
                    }
                    estrx = 201;
                    estry += 40;
                }
            } else {

                int estrx = 130;
                int estry = 75;

                for (short i = 0; i < 10; i++) {
                    if (i < 7) {
                        for (short j = 0; j < 13; j++) {

                            puntoRom(estrx, estry, 75);

                            estrx += 80;
                        }
                    } else {
                        for (short k = 0; k < 12; k++) {

                            puntoRom(estrx, estry, 75);

                            estrx += 80;
                        }
                    }
                    estrx = 221;
                    estry += 40;
                }
            }
        }
    }

    public void cielo() {

        contador++;
        // System.out.println("CIELO "+contador);
        dibujaCuadros(6, 29, (buffer.getWidth() - 10), (buffer.getHeight() / 2));
        {
            if (contador <= 7) {
                // System.out.println("CIELO 1");
                floodFill(8, 31, "CIELO1");
            }
            if (contador >= 7 & contador <= 14) {
                // System.out.println("CIELO 2");
                floodFill(8, 31, "CIELO2");
            }
            if (contador >= 14 & contador <= 21) {
                // System.out.println("CIELO 3");
                floodFill(8, 31, "CIELO3");
            }
            if (contador >= 21 & contador <= 28) {
                // System.out.println("CIELO 4");
                floodFill(8, 31, "CIELO4");
            }
            if (contador >= 28 & contador <= 35) {
                // System.out.println("CIELO 5");
                floodFill(8, 31, "CIELO5");
            }
            if (contador >= 35 & contador <= 42) {
                // System.out.println("CIELO 6");
                floodFill(8, 31, "CIELO6");
            }
            if (contador >= 42 & contador <= 49) {
                // System.out.println("CIELO 7");
                floodFill(8, 31, "CIELO7");
            }
            if (contador >= 49 & contador <= 56) {
                // System.out.println("CIELO 8");
                floodFill(8, 31, "CIELO8");
            }
            if (contador >= 56 & contador <= 63) {
                // System.out.println("CIELO 9");
                floodFill(8, 31, "CIELO9");
            }
            if (contador >= 63 & contador <= 158) {
                // System.out.println("CIELO 10, principal");
                floodFill(8, 31, "CIELO10");
            }
            if (contador >= 158 & contador <= 165) {
                // System.out.println("CIELO 9");
                floodFill(8, 31, "CIELO9");
            }
            if (contador >= 165 & contador <= 172) {
                // System.out.println("CIELO 8");
                floodFill(8, 31, "CIELO8");
            }
            if (contador >= 172 & contador <= 179) {
                // System.out.println("CIELO 7");
                floodFill(8, 31, "CIELO7");
            }
            if (contador >= 179 & contador <= 186) {
                // System.out.println("CIELO 6");
                floodFill(8, 31, "CIELO6");
            }
            if (contador >= 186 & contador <= 193) {
                // System.out.println("CIELO 5");
                floodFill(8, 31, "CIELO5");
            }
            if (contador >= 193 & contador <= 200) {
                // System.out.println("CIELO 4");
                floodFill(8, 31, "CIELO4");
            }
            if (contador >= 200 & contador <= 207) {
                // System.out.println("CIELO 3");
                floodFill(8, 31, "CIELO3");
            }
            if (contador >= 207 & contador <= 214) {
                // System.out.println("CIELO 2");
                floodFill(8, 31, "CIELO2");
            }
            if (contador >= 214 & contador <= 221) {
                // System.out.println("CIELO 1");
                floodFill(8, 31, "CIELO1");
            }
        }
        if (contador >= 221) {
            // System.out.println("CIELO 0, noche");
            floodFill(8, 31, "CIELO0");
        }
        if (contador >= 442) {
            ciclos++;
            contador = 0;
            if (ciclos == 6)
                System.out.println("Saludos Profe :D");
        }

    }

    public void pajarito(int xTras, int yTras, String color) {

        contador2++;

        // linea arriba
        punto1.x = 7;
        punto1.y = 250;
        punto1 = movimiento(punto1, xTras, yTras);
        punto2.x = 16;
        punto2.y = 285;
        punto2 = movimiento(punto2, xTras, yTras);
        if (punto2.x > buffer.getWidth() - 10) {

            traslaX = 0;
            return;

        } else {
            if (contador2 > 14) {
                linea(punto1.x, punto1.y, punto2.x, punto2.y, 3, color);
            }
        }

        // linea derecha
        punto1.x = 16;
        punto1.y = 285;
        punto1 = movimiento(punto1, xTras, yTras);
        punto2.x = 50;
        punto2.y = 290;
        punto2 = movimiento(punto2, xTras, yTras);
        if (contador2 > 14) {
            linea(punto1.x, punto1.y, punto2.x, punto2.y, 3, color);
        }

        // linea izquierda
        punto1.x = 7;
        punto1.y = 250;
        punto1 = movimiento(punto1, xTras, yTras);
        punto2.x = 7;
        punto2.y = 290;
        punto2 = movimiento(punto2, xTras, yTras);
        if (contador2 > 14) {
            linea(punto1.x, punto1.y, punto2.x, punto2.y, 3, color);
        }

        // linea abajo
        punto1.x = 7;
        punto1.y = 290;
        punto1 = movimiento(punto1, xTras, yTras);
        punto2.x = 50;
        punto2.y = 290;
        punto2 = movimiento(punto2, xTras, yTras);
        if (contador2 > 14) {
            linea(punto1.x, punto1.y, punto2.x, punto2.y, 3, color);
        }

        if (contador2 > 14) {
            floodFill(8 + xTras, 289 + yTras, "GRIS");
        }

        if (contador2 > 118) {
            contador2 = 0;
        }

    }

    public void piso() {

        drawOval(850, 650, 100, 28);
        floodFill(949, 650, "AZUL");

        drawOval(850, 650, 110, 38);
        floodFill(959, 650, "BLANCO");

        int jelpy = 530;

        for (short i = 0; i < 30; i++) {

            if (i % 2 == 0) {
                linea(245, jelpy, 245, jelpy + 20, 0, "AMARILLO");
            }

            jelpy += 15;
        }

        dibujaCuadros(5, 501, (buffer.getWidth() - 10), (buffer.getHeight() - 1));
        floodFill(620, 998, "GRIS");

        dibujaCuadros(145, 521, 345, (buffer.getHeight() - 21));
        floodFill(147, 986, "PISO");

    }

}