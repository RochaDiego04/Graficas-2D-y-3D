package parcial1.visor;

import javax.swing.*;
import java.awt.*;

public class VisorImagenes extends JFrame{
    private JScrollPane panel;
    private Pantalla pantalla;

    public VisorImagenes(String archivo){
        super("Visor Imagen");

        Image img=Toolkit.getDefaultToolkit().getImage(getClass().getResource(archivo));
        pantalla = new Pantalla(img);

        panel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        getContentPane().add(panel);
        panel.setViewportView(pantalla);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        show();
    }

    public static void main(String[] args) {
        // Verifica que se proporcione un archivo como argumento
        if (args.length != 1) {
            System.out.println("Uso: java parcial1.visor.VisorImagenes <archivo>");
            System.exit(1);
        }

        // Obtiene el nombre del archivo de los argumentos de línea de comandos
        String archivo = args[0];

        // Crea una instancia del visor de imágenes
        SwingUtilities.invokeLater(() -> new VisorImagenes(archivo));
    }

}
