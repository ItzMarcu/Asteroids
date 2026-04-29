import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Bullet extends Entity {

   public static final double BULLET_SPEED = 10.0;
   private static final double RAGGIO = 3.0;
   private static final int LIFETIME = 55;
   private int framesLeft;

   public Bullet(Vector2D position, Vector2D velocity){
     super(position,velocity,RAGGIO);
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
      return new ArrayList<Vector2D>(List.of(
      new Vector2D(-3, -3),
      new Vector2D(3, -3),
      new Vector2D(3, 3),
      new Vector2D(-3, 3)
      )); 
   }


   public Color getColor(){ return Color.RED; }

}
