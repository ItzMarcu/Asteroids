import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CLASSE DA IMPLEMENTARE — Ship (la nave del giocatore)
 * ═══════════════════════════════════════════════════════════════════
 *
 * Ship è una sottoclasse di Entity che rappresenta la nave controllata
 * dal giocatore.
 *
 * STATO INTERNO (campi che dovete aggiungere):
 * ┌─────────────────┬────────────┬────────────────────────────────────────┐
 * │ Nome suggerito  │ Tipo       │ Descrizione                            │
 * ├─────────────────┼────────────┼────────────────────────────────────────┤
 * │ angle           │ double     │ Direzione in cui punta la nave,        │
 * │                 │            │ in radianti. 0 = destra, -PI/2 = su.  │
 * │ thrusting       │ boolean    │ true se il motore è acceso.            │
 * │ turningLeft     │ boolean    │ true se il tasto sinistra è premuto.   │
 * │ turningRight    │ boolean    │ true se il tasto destra è premuto.     │
 * │ shootTimer      │ int        │ Countdown al prossimo sparo permesso.  │
 * │                 │            │ La nave può sparare solo quando è 0.   │
 * └─────────────────┴────────────┴────────────────────────────────────────┘
 *
 * COSTANTI SUGGERITE (aggiungetele come campi static final):
 *   RAGGIO = 15
 *   ROTATION_SPEED = 0.07   → radianti aggiunti/sottratti per frame
 *   THRUST_POWER   = 0.25   → incremento di velocità per frame
 *   FRICTION       = 0.98   → fattore di attrito applicato ogni frame
 *   MAX_SPEED      = 6.0    → velocità massima (pixel/frame)
 *   BULLET_COOLDOWN = 15    → frame minimi tra uno sparo e l'altro
 *
 * RENDERING — cosa fare:
 *   Sovrascrivere getShape() e getColor() (ereditati da Entity).
 *   Il metodo draw() viene chiamato automaticamente e usa queste due
 *   informazioni per disegnare la nave: voi non scrivete mai codice grafico.
 *
 *   getShape(): restituire i vertici di un triangolo che punta a destra:
 *     { 15, 0,   -10, -8,   -10, 8 }
 *     Il sistema di coordinate è relativo al centro della nave (0,0).
 *     Entity ruoterà automaticamente il triangolo dell'angolo corrente.
 *
 *   getColor(): restituire Color.WHITE.
 *
 *   getAngle(): sovrascrivere questo metodo per restituire il campo angle,
 *     così Entity sa di quanto ruotare la forma.
 *
 */
public class Ship extends Entity {
    private double    angle;
    private boolean   thrusting;
    private boolean   turningLeft;
    private boolean   turningRight;
    private int       shootTimer;
    private Color     color;
    private static final int RADIUS = 15;
    private static final double ROTATION_SPEED = 0.07;
    private static final double THRUST_POWER = 0.25;
    private static final double FRICTION = 0.5;
    private static final double MAX_SPEED = 6.0;
    private static final int BULLET_COOLDOWN = 15;
    
    /*
     * ── COSTRUTTORE ───────────────────────────────────────────────
     *
     * public Ship(int screenWidth, int screenHeight)
     *
     * Input:
     *   screenWidth, screenHeight → dimensioni dell'area di gioco
     *
     * Cosa fare:
     *   Chiamare super(...) passando:
     *     - posizione iniziale: centro dello schermo
     *     - velocità iniziale: ferma
     *     - raggio:
     *   Inizializzare angle = -Math.PI / 2  (punta verso l'alto)
     *   Inizializzare shootTimer = 0
     */
    public Ship(int screenWidth, int screenHeight) {
        super( new Vector2D(screenWidth/2,screenHeight/2), new Vector2D(0, 0), RADIUS); //ricontrollare
        this.angle = -Math.PI / 2;
        this.shootTimer = 0;
        this.color = Color.WHITE;
    }
    

    /*
     * ── METODI DI INPUT - NON MODIFICARE ───────────────────────────
     *
     * Questi tre metodi vengono chiamati da GameArea ogni volta che
     * il giocatore preme o rilascia un tasto. Semplicemente assegnano
     * il valore al campo corrispondente.
     *
     * public void setTurningLeft(boolean v)   → turningLeft  = v
     * public void setTurningRight(boolean v)  → turningRight = v
     * public void setThrusting(boolean v)     → thrusting    = v
     */
    public void setTurningLeft(boolean v)  { turningLeft  = v; }
    public void setTurningRight(boolean v) { turningRight = v; }
    public void setThrusting(boolean v)    { thrusting    = v; }

    @Override
    public void update(int width, int height) {
        //1
        if (turningLeft) 
            angle -= ROTATION_SPEED;
        
        if (turningRight)
            angle += ROTATION_SPEED;
        
        //2 + 3 + 4
        if (thrusting)
            getVelocity().add(new Vector2D(Math.cos(angle) * THRUST_POWER, Math.sin(angle) * THRUST_POWER));
        else 
            getVelocity().scale(FRICTION);
        
        //4 DA CAPIRE SE USARE QUESTO
        if (getVelocity().length() > MAX_SPEED)
            getVelocity().scale(FRICTION); // MAX_SPEED = 6 (4)
        //5
        move();

        //6
        wrapAround(width, height);

        //7
        if (shootTimer > 0) {
            shootTimer -= 1;
        }
            
    }

    /*
     * ── update(int width, int height) ────────────────────────────
     *
     * Chiamato ~60 volte al secondo da GameArea. Aggiorna la nave.
     *
     * Passi da implementare nell'ordine:
     *
     *  1. ROTAZIONE
     *     if turningLeft  → angle -= ROTATION_SPEED
     *     if turningRight → angle += ROTATION_SPEED
     *
     *  2. PROPULSIONE
     *     if thrusting → aggiungere alla velocità un vettore nella
     *     direzione angle con modulo THRUST_POWER:
     *       velocity.x += Math.cos(angle) * THRUST_POWER
     *       velocity.y += Math.sin(angle) * THRUST_POWER
     *
     *  3. ATTRITO
     *     velocity.scale(FRICTION)
     *     Questo fa rallentare la nave da sola quando non si accelera.
     *
     *  4. LIMITE DI VELOCITÀ
     *     Se velocity.length() > MAX_SPEED, riscalare il vettore
     *     mantenendo la direzione ma portando la lunghezza a MAX_SPEED.
     *
     *  5. MOVIMENTO
     *
     *  6. WRAPPING
     *     Chiamare wrapAround(width, height)
     *     Se la nave esce da un bordo, riappare dal lato opposto.
     *
     *  7. COOLDOWN SPARO
     */

    public Bullet shoot(){
        if (shootTimer > 0) {
            return null;
        }
        else {
            double bx = getPosition().x + Math.cos(angle) * RADIUS;
            double by = getPosition().y + Math.sin(angle) * RADIUS;

            double vx = Math.cos(angle) * Bullet.BULLET_SPEED + getVelocity().x;
            double vy = Math.sin(angle) * Bullet.BULLET_SPEED + getVelocity().y;
             
            shootTimer = BULLET_COOLDOWN;

            Bullet bullet = new Bullet(new Vector2D(bx, by), new Vector2D(vx, vy));
            return bullet;
        }
    }

    /*
     * ── shoot() ──────────────────────────────────────────────────
     *
     * Output: Bullet  (il nuovo proiettile) oppure null (se in cooldown)
     *
     * Comportamento:
     *   Se shootTimer > 0 → restituire null (non si può ancora sparare)
     *   Altrimenti:
     *     Calcolare la posizione iniziale del proiettile (la punta della nave):
     *       bx = position.x + Math.cos(angle) * radius
     *       by = position.y + Math.sin(angle) * radius
     *     Calcolare la velocità del proiettile (direzione angle + velocità nave):
     *       vx = Math.cos(angle) * Bullet.BULLET_SPEED + velocity.x
     *       vy = Math.sin(angle) * Bullet.BULLET_SPEED + velocity.y
     *     Resettare shootTimer = BULLET_COOLDOWN
     *     Restituire il proiettile appena creato.
     *
     * Questo metodo viene chiamato da GameArea quando il giocatore
     * preme la barra spaziatrice.
     */
    @Override
    public List<Vector2D> getShape(){
        List<Vector2D> p = new ArrayList<>();
        p.add(new Vector2D(15, 0));
        p.add(new Vector2D(-10, -8));
        p.add(new Vector2D(-10, 8));
        return p;
    }

    /*
     * ── getShape() ───────────────────────────────────────────────
     *
     * Output: List<Vector2D>  — vertici del triangolo della nave
     *
     * Sono tre vertici (coppie x,y) relativi al centro (0,0):
     *   (15, 0)    → punta della nave (destra)
     *   (-10, -8)  → angolo superiore sinistro
     *   (-10,  8)  → angolo inferiore sinistro
     * Entity ruoterà automaticamente questi punti di `angle` radianti.
     */
    @Override
    public Color getColor() { return this.color; }

    /*
     * ── getColor() ───────────────────────────────────────────────
     *
     * Output: Color
     *
     * Restituire Color
     */

    @Override
    public double getAngle() { return angle; }

    /*
     * ── getAngle() ───────────────────────────────────────────────
     *
     * Output: double  — l'angolo corrente della nave in radianti
     *
     * Sovrascrivere il metodo ereditato da Entity e restituire
     * il campo angle. Questo permette a draw() di ruotare
     * correttamente la forma.
     */

    /**
     * 
     * setColor(Color color)
     * 
     */

    public void setColor(Color color) { this.color = color; }

}
