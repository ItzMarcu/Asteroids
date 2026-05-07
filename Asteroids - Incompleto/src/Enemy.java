import java.awt.*; 
import java.util.List; 
import java.util.ArrayList; 
import java.util.Random;
import java.util.Vector;

public class Enemy extends Entity { 
    private Vector2D shape; 
    private double   angle; 
    private int      shootTimer;  
    private Random   rng = new Random(); 

    private static final double ENEMY_VELOCITY = 2.0; 
    private static final double ENEMY_RADIUS = 25.0; 
    private static final int    BULLET_COOLDOWN = 60;

    public Enemy(Vector2D position, Vector2D velocity, double radius) {
        super(position, velocity, radius); 
        this.shootTimer = 0; 
    }

    public Bullet shoot() {
        if (shootTimer > 0)
            return null; 
        
        double bx = getPosition().x + Math.cos(angle) * ENEMY_RADIUS; 
        double by = getPosition().y + Math.sin(angle) * ENEMY_RADIUS; 

        double vx = Math.cos(angle) * Bullet.BULLET_SPEED; 
        double vy = Math.sin(angle) * Bullet.BULLET_SPEED; 

        shootTimer = BULLET_COOLDOWN; 
        return new Bullet(new Vector2D(bx, by), new Vector2D(vx, vy)); 
    }

    public void update(int width, int height) {
        move();
        wrapAround(width, height);

        if (shootTimer > 0) shootTimer--; 
    }

    public List<vector2D> getShape() { 
        return new ArrayList<Vector2D> = new ArrayList<>(
            new Vector2D(0, rng.randInt(20, 25)),
            new Vector2D(0, -rng.randInt(20, 25)), 
            new Vector2D(rng.randInt(20, 25), 0), 
            new Vector2D(-rng.randInt(20, 25), 0)
        ); 
    }

    public Color getColor() { return Color.GREEN; }

    public static Vector2D generateRandomVelocity() {
        double angle = rng.nextDouble() * 2 * Math.PI;
        double randomVelocity = rng.nextDouble() * (ENEMY_VELOCITY + 1);

        Vector2D velocity = new Vector2D(
            Math.cos(angle) * randomVelocity, 
            Math.sin(angle) * randomVelocity
        ); 

        return velocity; 
    }
}