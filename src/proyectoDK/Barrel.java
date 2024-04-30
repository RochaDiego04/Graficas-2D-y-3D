package proyectoDK;

import javax.swing.*;

public class Barrel {
    public int barrelX;
    public int barrelY;
    public boolean barrelVisible;
    public BarrelState barrelState;
    private int BARREL_HEIGHT = 20;
    private int BARREL_MOVE_SPEED; // Velocidad de movimiento
    public boolean reachedVerticalLine = false;
    public boolean reachedThirdLine = false;
    public boolean reachedSecondVerticalLine = false;
    public boolean reachedFifthLine = false;
    public boolean reachedSixthVerticalLine = false;
    public boolean reachedSeventhhLine = false;

    public enum BarrelState {
        FIRST_LINE,
        SECOND_LINE_VERTICAL,
        THIRD_LINE,
        FOURTH_LINE_VERTICAL,
        FIFTH_LINE,
        SIXTH_LINE,
        SEVENTH_LINE
    }

    public Barrel(int BARREL_MOVE_SPEED) {
        this.barrelVisible = false;
        this.barrelState = BarrelState.FIRST_LINE;
        this.BARREL_MOVE_SPEED = BARREL_MOVE_SPEED;
    }

    public void moveBarrel() {
        switch (barrelState) {
            case FIRST_LINE:
                moveBarrelFirstLine();
                if (reachedVerticalLine) {
                    barrelState = BarrelState.SECOND_LINE_VERTICAL;
                }
                break;
            case SECOND_LINE_VERTICAL:
                moveBarrelSecondLineVertical();
                if (reachedThirdLine) {
                    barrelState = BarrelState.THIRD_LINE;
                }
                break;
            case THIRD_LINE:
                moveBarrelThirdLine();
                if (reachedSecondVerticalLine) {
                    barrelState = BarrelState.FOURTH_LINE_VERTICAL;
                }
                break;
            case FOURTH_LINE_VERTICAL:
                moveBarrelFourthLineVertical();
                if (reachedFifthLine) {
                    barrelState = BarrelState.FIFTH_LINE;
                }
                break;
            case FIFTH_LINE:
                moveBarrelFifthLine();
                if (reachedSixthVerticalLine) {
                    barrelState = BarrelState.SIXTH_LINE;
                }
                break;
            case SIXTH_LINE:
                moveBarrelSixthLineVertical();
                if (reachedSeventhhLine) {
                    barrelState = BarrelState.SEVENTH_LINE;
                }
                break;
            case SEVENTH_LINE:
                moveBarrelSeventhLine();
                break;
            default:
                break;
        }
    }

    private void resetBarrel() {
        barrelX = 120;
        barrelY = 60;
        barrelVisible = true;

        reachedVerticalLine = false;
        reachedThirdLine = false;
        reachedSecondVerticalLine = false;
        reachedFifthLine = false;
        reachedSixthVerticalLine = false;
        reachedSeventhhLine = false;

        barrelState = BarrelState.FIRST_LINE;
    }



    private void moveBarrelFirstLine() {
        //System.out.println("Moviendo barril por la primer linea");
        int endX = 620; // Inicio de la escalera 1

        barrelX+=BARREL_MOVE_SPEED;

        // Verificar si se alcanzó el final de la primera línea
        if (barrelX >= endX) {
            reachedVerticalLine = true;
        }
    }

    private void moveBarrelSecondLineVertical() {
        //System.out.println("Moviendo barril por la segunda linea");
        int endYVertical = 170;

        barrelY+=BARREL_MOVE_SPEED;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            reachedThirdLine = true;
        }
    }


    private void moveBarrelThirdLine() {
        //System.out.println("Moviendo barril por la tercera linea");
        int endX = 240; // Inicio de la tercera línea

        barrelX-=BARREL_MOVE_SPEED;
        if (barrelX <= endX) {
            reachedSecondVerticalLine = true;
        }
    }

    private void moveBarrelFourthLineVertical() {
        //System.out.println("Moviendo barril por la cuarta linea");
        int endYVertical = 260; // Fin de la segunda escalera

        barrelY+=BARREL_MOVE_SPEED;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.FIFTH_LINE;
        }
    }

    private void moveBarrelFifthLine() {
        //System.out.println("Moviendo barril por la quinta linea");
        //(x1: 0, y1: 280, x2: 700, y2: 330);
        int endX = 640; // Inicio de la quinta línea

        barrelX+=BARREL_MOVE_SPEED;

        if (barrelX >= endX) {
            reachedSixthVerticalLine = true;
        }
    }

    private void moveBarrelSixthLineVertical() {
        //System.out.println("Moviendo barril por la sexta linea");
        int endYVertical = 460; // Fin de la tercera escalera

        barrelY+=BARREL_MOVE_SPEED;

        barrelY = Math.min(barrelY, endYVertical - BARREL_HEIGHT);

        if (barrelY >= endYVertical - BARREL_HEIGHT) {
            barrelState = BarrelState.SEVENTH_LINE;
        }
    }

    private void moveBarrelSeventhLine() {
        //(x1: 800, y1: 415, x2: 0, y2: 465);
        //System.out.println("Moviendo barril por la septima linea");
        int endX = 0; // Inicio de la tercera línea

        barrelX-=BARREL_MOVE_SPEED;

        if (barrelX <= endX) {
            reachedSecondVerticalLine = true;
        }
    }

   public void logicMoveBarrel(int playerX, int playerY) {
        // Mueve el barril hacia abajo si está visible
        if (barrelVisible) {
            moveBarrel();
            // Comprueba si el barril colisiona con el jugador
            if (barrelX + 20 >= playerX && barrelX <= playerX + 20 && barrelY + 20 >= playerY && barrelY <= playerY + 20) {
                // Colisión detectada
                System.out.println("Colision");
                System.exit(0);
            }
            // Si el barril llega al suelo o sale de los limites en X, desaparece
            if (barrelY >= 500 || barrelX < 0) {
                barrelVisible = false;
            }
        } else {
            // Genera un nuevo barril un tiempo aleatorio despues de que el primero desaparezca
            if (Math.random() < 0.01) {
                resetBarrel();
            }
        }
    }
}
