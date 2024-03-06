package parcial1;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventHandler extends JFrame implements KeyListener {
    private JLabel label;
    public KeyEventHandler() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creamos el JLabel con el texto deseado
        label = new JLabel("Presiona cualquier tecla");
        add(label);

        setVisible(true);
        addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Tecla presionada: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Tecla soltada: " + e.getKeyChar());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Tecla escrita: " + e.getKeyChar());
    }

    public static void main(String[] args) {
        new KeyEventHandler();
    }
}
