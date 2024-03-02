import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DiagramaPastel extends JFrame {

    private ArrayList<Double> porcentajes;

    public DiagramaPastel(ArrayList<Double> porcentajes) {
        this.porcentajes = porcentajes;

        /* Propiedades de la ventana */
        setTitle("Diagrama pastel");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    @Override
    public void paint(Graphics g) {
        // Definir el ángulo inicial y el ángulo total
        double anguloInicio = 0;
        double anguloTotal = 0;
        for (double porcentaje : porcentajes) {
            anguloTotal += porcentaje;
        }

        // Dibujar el diagrama de pastel
        int x = getWidth() / 2; // Coordenada x del centro del diagrama
        int y = getHeight() / 2; // Coordenada y del centro del diagrama
        int radio = 200; // Radio del diagrama
        for (int i = 0; i < porcentajes.size(); i++) {
            double porcentaje = porcentajes.get(i);
            double angulo = 360 * (porcentaje / anguloTotal);
            g.setColor(Color.getHSBColor((float) Math.random(), 1.0f, 1.0f)); // Color aleatorio
            g.fillArc(x - radio, y - radio, 2 * radio, 2 * radio, (int) anguloInicio, (int) angulo);

            // Calcular la posición para la etiqueta de porcentaje
            double anguloCentral = Math.toRadians(anguloInicio + angulo / 2);
            int etiquetaX = (int) (x + radio * Math.cos(anguloCentral));
            int etiquetaY = (int) (y - radio * Math.sin(anguloCentral));
            String etiqueta = String.format("%.2f%%", porcentaje);
            g.setColor(Color.BLACK);
            g.drawString(etiqueta, etiquetaX, etiquetaY);

            anguloInicio += angulo;
        }
    }

    public static ArrayList<Double> convertirAPorcentajes(String[] args) {
        ArrayList<Double> porcentajes = new ArrayList<>();
        double total = 0;
        for (String arg : args) {
            try {
                double valor = Double.parseDouble(arg);
                total += valor;
                porcentajes.add(valor);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Los argumentos deben ser números válidos.");
            }
        }
        // Convertir los valores a porcentajes
        for (int i = 0; i < porcentajes.size(); i++) {
            porcentajes.set(i, porcentajes.get(i) / total * 100);
        }
        return porcentajes;
    }

    public static void main(String[] args) {
        // Verificar que se hayan pasado los argumentos adecuados
        if (args.length < 1) {
            System.out.println("Uso: java DiagramaPastel <valor1_porcentaje> <valor2_porcentaje> <etc>");
            return;
        }

        // Convertir los argumentos a porcentajes
        ArrayList<Double> porcentajes;
        try {
            porcentajes = convertirAPorcentajes(args);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Crear y mostrar el diagrama de pastel
        SwingUtilities.invokeLater(() -> new DiagramaPastel(porcentajes));
    }
}
