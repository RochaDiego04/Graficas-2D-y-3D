package proyecto1;

import java.awt.*;

public class RelojFondo {

    public static void dibujarReloj(int centerX, int centerY, Graphics g) {
        g.drawArc(centerX, centerY, 60, 60, 0, 360);
    }
}
