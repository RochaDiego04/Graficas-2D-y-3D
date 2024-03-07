package proyecto1;

import java.awt.*;

public class RelojFondo {
    private static Color colorFondoReloj = new Color(252, 233, 204);
    private static Color colorDetallesReloj = new Color(51, 29, 29);

    public static void dibujarMarcoReloj(Graphics g, int centerX, int centerY) {
        g.setColor(colorDetallesReloj);
        g.fillOval(centerX - 150, centerY - 150, 300, 300);
    }

    public static void dibujarInteriorReloj(Graphics g, int centerX, int centerY) {
        g.setColor(colorFondoReloj);
        g.fillOval(centerX - 140, centerY - 140, 280, 280);
    }

    public static void dibujarLineasInteriorReloj(Graphics g, int centerX, int centerY) {
        g.setColor(colorDetallesReloj);
        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30); // Calcular el ángulo para cada hora (en radianes)
            int x1 = (int) (centerX + 125 * Math.cos(angle)); // Calcular la posición X del inicio de la línea
            int y1 = (int) (centerY + 125 * Math.sin(angle)); // Calcular la posición Y del inicio de la línea
            int x2 = (int) (centerX + 133 * Math.cos(angle)); // Calcular la posición X del final de la línea
            int y2 = (int) (centerY + 133 * Math.sin(angle)); // Calcular la posición Y del final de la línea

            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public static void dibujarLineasDelgadasInteriorReloj(Graphics g, int centerX, int centerY) {
        g.setColor(colorDetallesReloj);
        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6); // Calcular el ángulo para cada minuto (en radianes)
            int x1 = (int) (centerX + 132 * Math.cos(angle)); // Calcular la posición X del inicio de la línea
            int y1 = (int) (centerY + 132 * Math.sin(angle)); // Calcular la posición Y del inicio de la línea
            int x2 = (int) (centerX + 135 * Math.cos(angle)); // Calcular la posición X del final de la línea
            int y2 = (int) (centerY + 135 * Math.sin(angle)); // Calcular la posición Y del final de la línea

            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawLine(x1, y1, x2, y2);
        }
    }
    public static void dibujarHorasReloj(Graphics g, int centerX, int centerY){
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 18f));

        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 90); // Calcular el ángulo para cada hora (en radianes)
            int x = (int) (centerX + 110 * Math.cos(angle)); // Calcular la posición X del texto
            int y = (int) (centerY + 110 * Math.sin(angle)); // Calcular la posición Y del texto
            String hour = Integer.toString(i); // Convertir el número de hora a String
            FontMetrics fm = g.getFontMetrics(); // Obtener las métricas de la fuente
            int width = fm.stringWidth(hour); // Obtener el ancho del texto
            int height = fm.getHeight(); // Obtener la altura del texto
            g.drawString(hour, x - width / 2, y + height / 4); // Dibujar el texto centrado en la posición calculada
        }
    }
}
