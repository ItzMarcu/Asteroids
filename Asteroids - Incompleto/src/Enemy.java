import java.awt.*;
import java.util.ArrayList;
import java.util.List; 
import java.util.Random;

public class Enemy extends Entity { 
    private double   angle; 
    private int      shootTimer;  
    
    private static final Random rng = new Random(); 
    private static final double ENEMY_VELOCITY = 2.0; 
    private static final double ENEMY_RADIUS = 25.0; 
    private static final int    BULLET_COOLDOWN = 120;
    private static final int    ENEMY_SCORE = 75; 

    public Enemy(Vector2D position, Vector2D velocity, double radius) {
        super(position, velocity, radius); 
        //this.angle = -Math.PI / 2; //dffd
        this.shootTimer = 0; 
    }

    
    @Override
    public void update(int width, int height) {
        move();
        wrapAround(width, height);
        if (shootTimer > 0) shootTimer--; 
    }
    public Bullet shoot(Ship ship) {
        if (shootTimer > 0)
            return null; 
        else {
            
            double angle = Math.atan2((ship.getPosition().y - getPosition().y), (ship.getPosition().x - getPosition().x));
            System.out.println(angle);
            //angle = Math.abs(angle);
            //System.out.println("Sparo");
            double bx = getPosition().x + Math.cos(angle) * ENEMY_RADIUS; 
            double by = getPosition().y + Math.sin(angle) * ENEMY_RADIUS;
            //System.out.println(bx);

            double vx = Math.cos(angle) * 3; //+ getVelocity().x;
            double vy = Math.sin(angle) * 3; //+ getVelocity().y;

            shootTimer = BULLET_COOLDOWN; 
            Bullet b = new Bullet(new Vector2D(bx, by), new Vector2D(vx, vy));
            b.setframesLeft(120);
            //System.out.println(b.getVelocity());
            return b;
        }
        
         
    }
    @Override
    public List<Vector2D> getShape() { 
        return new ArrayList<>(
            List.of(
                new Vector2D(0, rng.nextInt(20, 25)),
                new Vector2D(0, -rng.nextInt(20, 25)), 
                new Vector2D(rng.nextInt(20, 25), 0), 
                new Vector2D(-rng.nextInt(20, 25), 0)
            )
        ); 
    }
    @Override
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