import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Entity {

   public static final double BULLET_SPEED = 10.0;
   private static final double RAGGIO = 3.0;
   private static final int LIFETIME = 55;
   private int framesLeft;
  

   public Bullet(Vector2D position, Vector2D velocity){
      super(position,velocity,RAGGIO);
      framesLeft = LIFETIME;        
   }

   public Bullet(Vector2D position, Vector2D velocity, double radius){
      super(position, velocity, radius);
      framesLeft = LIFETIME;
   }

   @Override
   public void update(int width, int height){
      move();
      framesLeft --;
      
      if (framesLeft == 0){
         destroy();
         return;
      }
      
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
   public Color getColor(){ return Color.RED; }

   //serve per i proiettili nemici
   public void setframesLeft(int frames) {
      this.framesLeft = frames;
   }

}
