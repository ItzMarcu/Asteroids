import java.awt.*; 
import java.util.List; 
import java.util.ArrayList; 
import java.util.Random;

public class Enemy extends Entity { 
    private Vector2D shape; 
    private double   angle; 
    private int      shootTimer;  
    
    private static final Random rng = new Random(); 
    private static final double ENEMY_VELOCITY = 2.0; 
    private static final double ENEMY_RADIUS = 25.0; 
    private static final int    BULLET_COOLDOWN = 60;
    private static final int    ENEMY_SCORE = 75; 

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

    public List<Vector2D> getShape() { 
        return new ArrayList<Vector2D>(
            List.of(
                new Vector2D(0, rng.nextInt(20, 25)),
                new Vector2D(0, -rng.nextInt(20, 25)), 
                new Vector2D(rng.nextInt(20, 25), 0), 
                new Vector2D(-rng.nextInt(20, 25), 0)
            )
        ); 
    }

    public Color getColor() { return Color.GREEN; }

    public int getScore() { return ENEMY_SCORE; }

    public static Vector2D generateRandomVelocity() {
        double angle = rng.nextDouble() * 2 * Math.PI;
        double randomVelocity = rng.nextDouble() * (ENEMY_VELOCITY + 1);

        Vector2D velocity = new Vector2D(
            Math.cos(angle) * randomVelocity, 
            Math.sin(angle) * randomVelocity
        ); 

        return velocity; 
    }

    public static Enemy spawnRandom(int screenWidth, int screenHeight) {
        int x = rng.nextInt(0, screenWidth);
        int y = rng.nextInt(0, screenHeight); 
        
        Vector2D randomPosition = new Vector2D(x, y); 

        return new Enemy(randomPosition, Enemy.generateRandomVelocity(), ENEMY_RADIUS); 
    }
}