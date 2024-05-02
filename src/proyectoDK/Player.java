package proyectoDK;

public class Player {
    public int playerX;
    public int playerY;
    public int playerVy; // Velocidad vertical del jugador
    public int playerVx; // Velocidad horizontal del jugador
    public int MOVE_SPEED; // Velocidad de movimiento
    public int GRAVITY; // Gravedad
    public int JUMP_SPEED; // Velocidad de salto

    public DonkeyKongGameV2 ventana;
    public PlayerState playerState;

    public enum PlayerState {
        FIRST_LINE,
        THIRD_LINE,
        FIFTH_LINE,
        SEVENTH_LINE
    }

    public Player(DonkeyKongGameV2 ventana) {
        this.playerState = PlayerState.SEVENTH_LINE;
        this.ventana = ventana;
        this.playerY = 500 - 60;
        this.playerX = 20;
        this.playerVy = 0;
        this.playerVx = 0;
        this.MOVE_SPEED = 2;
        this.GRAVITY = 1;
        this.JUMP_SPEED = 11;
    }

}
