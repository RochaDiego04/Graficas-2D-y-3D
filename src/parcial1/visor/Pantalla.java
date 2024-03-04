package parcial1.visor;

import javax.swing.JPanel;
import java.awt.*;

public class Pantalla extends JPanel {

    private Image imagen;

    public Pantalla(Image imagen) {
        this.imagen = imagen;
    }
    
    public void paint(Graphics g){
        super.paint(g);
        Dimension tam = new Dimension(imagen.getWidth(this),
    imagen.getHeight(this));
        setPreferredSize(tam);
        setMinimumSize(tam);
        setMaximumSize(tam);
        setSize(tam);
        update(g);

    }

    public void update(Graphics g){
        g.drawImage(imagen, 0, 0, this);
    } 
    
}
