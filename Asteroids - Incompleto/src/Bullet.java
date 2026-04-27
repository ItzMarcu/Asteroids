import java.awt.*;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CLASSE DA IMPLEMENTARE — Bullet (un proiettile)
 * ═══════════════════════════════════════════════════════════════════
 *
 * Bullet è una sottoclasse di Entity che rappresenta un proiettile
 * sparato dalla nave.
 *
 * Il proiettile si muove in linea retta a velocità costante.
 * Non fa wrapping: quando esce dallo schermo viene distrutto.
 * Viene distrutto anche quando scade la sua durata di vita (LIFETIME).
 *
 * STATO INTERNO (campi che dovete aggiungere):
 * ┌────────────┬──────┬──────────────────────────────────────────────┐
 * │ Nome       │ Tipo │ Descrizione                                  │
 * ├────────────┼──────┼──────────────────────────────────────────────┤
 * │ framesLeft │ int  │ Frame di vita rimasti. Inizia a LIFETIME e   │
 * │            │      │ scende di 1 ogni frame. Quando arriva a 0    │
 * │            │      │ il proiettile chiama destroy().              │
 * └────────────┴──────┴──────────────────────────────────────────────┘
 *
 * COSTANTI SUGGERITE:
 *   RAGGIO = 3.0
 *
 *   BULLET_SPEED = 10.0   → velocità in pixel/frame (deve essere public
 *                            perché Ship la usa per calcolare la velocità)
 *   LIFETIME     = 55     → frame di vita del proiettile
 *
 * RENDERING — cosa fare:
 *   Sovrascrivere getShape() e getColor() (ereditati da Entity).
 *   draw() è già gestito da Entity, non serve altro.
 *
 *   getShape(): ritorna la shape del proiettile.
 *
 *   getColor(): restituire colore.
 *
 *   getAngle(): non è necessario sovrascriverlo (il proiettile non ruota).
 */
public class Bullet extends Entity {

    private int framesLeft;
    private Vector2D position; 
    private Vector2D velocity;
    /*
     * ── COSTRUTTORE ───────────────────────────────────────────────
     *
     * public Bullet(Vector2D position, Vector2D velocity)
     *
     * Input:
     *   position → posizione iniziale (la punta della nave al momento dello sparo)
     *   velocity → velocità del proiettile (calcolata da Ship.shoot())
     *
     * Cosa fare:
     *   Chiamare super(...).
     *   Inizializzare framesLeft.
     */

    public Bullet(Vector2D position, Vector2D velocity, double radius) {
        super(position, velocity, radius);
        framesLeft = 55;
    }


    /*
     * ── update(int width, int height) ────────────────────────────
     *
     * Cosa deve fare il metodo:
     *
     * 1. Muovere il proiettile
     *
     * 2. Aggiornare il tempo di vita
     *
     * 3. Controllare se il tempo è finito e distruggerlo

     * 4. wrapAround del proitettile
     *
     */

    public void update(int width, int length) {
        
    }


    /*
     * ── getShape() ───────────────────────────────────────────────
     *
     * Output: List<Vector2D>
     *
     * Restituire i vertici della shape
     */


    /*
     * ── getColor() ───────────────────────────────────────────────
     *
     * Output: Color
     *
     */

}
