import java.awt.*;

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
    private int screenWidth;
    private int screenHeight;
    
    private double  angle;
    private boolean thrusting;
    private boolean turningLeft;
    private boolean turningRight;
    private int     shootTimer;
    
    private static final int    raggio = 15;
    private static final double rotation_speed = 0.07;
    private static final double thrust_power = 0.25;
    private static final double friction = 0.98;
    private static final double max_speed = 6.0;
    private static final int    bullet_cooldown = 15;

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
        super(new Vector2D(0, 0), new Vector2D(0, 0), raggio);
        
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
        angle = -Math.PI / 2.0;
        shootTimer = 0;
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


    /*
     * ── getColor() ───────────────────────────────────────────────
     *
     * Output: Color
     *
     * Restituire Color
     */


    /*
     * ── getAngle() ───────────────────────────────────────────────
     *
     * Output: double  — l'angolo corrente della nave in radianti
     *
     * Sovrascrivere il metodo ereditato da Entity e restituire
     * il campo angle. Questo permette a draw() di ruotare
     * correttamente la forma.
     */

}
