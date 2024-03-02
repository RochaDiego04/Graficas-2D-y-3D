package proyecto1;

import java.awt.*;

public class RelojFondo {
    public static void dibujarMarcoReloj(Graphics g, int centerX, int centerY) {
        g.setColor(new Color(53,20,20));
        g.fillOval(centerX - 220, centerY - 220, 440, 440);
    }

    public static void dibujarInteriorReloj(Graphics g, int centerX, int centerY) {
        g.setColor(Color.WHITE);
        g.fillOval(centerX - 200, centerY - 200, 400, 400);
    }

    public static void dibujarHorasReloj(Graphics g, int centerX, int centerY){
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));

        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 90); // Calcular el ángulo para cada hora (en radianes)
            int x = (int) (centerX + 160 * Math.cos(angle)); // Calcular la posición X del texto
            int y = (int) (centerY + 160 * Math.sin(angle)); // Calcular la posición Y del texto
            String hour = Integer.toString(i); // Convertir el número de hora a String
            FontMetrics fm = g.getFontMetrics(); // Obtener las métricas de la fuente
            int width = fm.stringWidth(hour); // Obtener el ancho del texto
            int height = fm.getHeight(); // Obtener la altura del texto
            g.drawString(hour, x - width / 2, y + height / 4); // Dibujar el texto centrado en la posición calculada
        }
    }
}
