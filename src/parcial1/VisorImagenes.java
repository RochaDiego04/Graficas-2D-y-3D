package parcial1;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class VisorImagenes extends JFrame {

    private JScrollPane panel;

    public VisorImagenes(String archivo) {
        super("Visor de imágenes");
        Image img = Toolkit.getDefaultToolkit().getImage(archivo);
        Pantalla pantalla = new Pantalla(img);
        panel = new JScrollPane(pantalla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        new VisorImagenes(args[0]);
    }
}

class Pantalla extends JPanel {
    private Image imagen;

    public Pantalla(Image img) {
        this.imagen = img;
        Dimension tam = new Dimension(imagen.getWidth(this), imagen.getHeight(this));
        setPreferredSize(tam);
        setMinimumSize(tam);
        setMaximumSize(tam);
        setSize(tam);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, this);
    }
}
