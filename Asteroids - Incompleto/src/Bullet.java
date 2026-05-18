import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Entity {

   public static final double BULLET_SPEED = 10.0;
   private static final double RAGGIO = 3.0;
   private static final int LIFETIME = 55;
   private Color   color; 
   private int     framesLeft;
   private int     powerupTimer;
   private boolean destroyable; 
  

   public Bullet(Vector2D position, Vector2D velocity){
      super(position,velocity,RAGGIO);
      framesLeft = LIFETIME;
      color = Color.RED;  
      destroyable = true;   
      powerupTimer = 0;     
   }

   @Override
   public void update(int width, int height){
      move();
      framesLeft--;
      powerupTimer--;

      if (framesLeft == 0){
         destroy();
         return;
      }

      if (powerupTimer == 0 && !destroyable)
         removePowerUp();
      
      wrapAround(width , height);
   }

   @Override
   public List<Vector2D> getShape(){
      return new ArrayList<>(List.of(
         new Vector2D(-3, -3),
         new Vector2D(3, -3),
         new Vector2D(3, 3),
         new Vector2D(-3, 3)
      )); 
   }

   @Override
   public Color getColor(){ return this.color; }
   public void setColor(Color color) { this.color = color; }

   public void bulletPowerUp() {
      setColor(Color.ORANGE);
      framesLeft = 165;
      destroyable = false;
      powerupTimer = 300; 
   }

   public void removePowerUp() {
      setColor(Color.RED);
      framesLeft = 55;
      destroyable = true; 
   }

   public boolean isDestroyable() { return destroyable; }

   //serve per i proiettili nemici
   public void setframesLeft(int frames) {
      this.framesLeft = frames;
   }

}
