package parcial2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Pixel extends JFrame {
    private BufferedImage buffer;
    public Graphics graPixel;
    public Pixel() {
        setSize(400, 400);
        setTitle("Prueba de punto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graPixel = buffer.createGraphics();
    }
    public void putPixel(int x, int y, Color c) {
        buffer.setRGB(x, y, c.getRGB());
        this.getGraphics().drawImage(buffer, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public static void main(String[] args) {
        Pixel pixel = new Pixel();
        pixel.setVisible(true);
        pixel.putPixel(50, 50, Color.RED);
    }

}
