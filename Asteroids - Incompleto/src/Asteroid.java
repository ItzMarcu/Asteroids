import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CLASSE DA IMPLEMENTARE — Asteroid (un asteroide)
 * ═══════════════════════════════════════════════════════════════════
 *
 * Asteroid è una sottoclasse di Entity che rappresenta un asteroide
 * in movimento nell'area di gioco.
 *
 * Gli asteroidi esistono in tre dimensioni:
 *   LARGE  (grande)  → si divide in 2 asteroidi MEDIUM quando colpito
 *   MEDIUM (medio)   → si divide in 2 asteroidi SMALL  quando colpito
 *   SMALL  (piccolo) → viene semplicemente distrutto quando colpito
 *
 * La dimensione è rappresentata da un enum già fornito qui sotto.
 *
 * STATO INTERNO (campi che dovete aggiungere):
 * ┌──────────────┬────────────────┬────────────────────────────────────┐
 * │ Nome         │ Tipo           │ Descrizione                        │
 * ├──────────────┼────────────────┼────────────────────────────────────┤
 * │ size         │ Size           │ La dimensione di questo asteroide  │
 * ├──────────────┼────────────────┼────────────────────────────────────┤
 * │ customShape  │ List<Vector2D> │ Lista di vertici del poligono      │
 * └──────────────┴────────────────┴────────────────────────────────────┘
 *
 * COSTANTI SUGGERITE:
 *   RADIUS_LARGE  = 40    → raggio asteroide grande  (pixel)
 *   RADIUS_MEDIUM = 22    → raggio asteroide medio
 *   RADIUS_SMALL  = 12    → raggio asteroide piccolo
 *   SPEED_LARGE   = 1.2   → velocità massima di riferimento (LARGE)
 *   SPEED_MEDIUM  = 2.0   → velocità massima di riferimento (MEDIUM)
 *   SPEED_SMALL   = 3.0   → velocità massima di riferimento (SMALL)
 *   SCORE_LARGE   = 20    → punti assegnati al giocatore se distrutto
 *   SCORE_MEDIUM  = 50
 *   SCORE_SMALL   = 100
 *
 * RENDERING — cosa fare:
 *   Sovrascrivere getShape() e getColor() (ereditati da Entity).
 *   Non serve scrivere altro: draw() è già gestito da Entity.
 *
 *   getShape(): restituire i vertici di un poligono, da adattare al raggio della dimensione corrente:
 *
 *   getColor(): restituire un colore
 *
 *   getAngle(): gli asteroidi non ruotano visivamente, quindi non è
 *     necessario sovrascrivere questo metodo (il default 0 va bene).
 */
public class Asteroid extends Entity {

    /** Le tre dimensioni possibili di un asteroide. */
    public enum Size { LARGE, MEDIUM, SMALL }

    // Generatore di numeri casuali condiviso da tutti gli asteroidi.
    private static final Random rng = new Random();

    private final Size           size;
    private final List<Vector2D> customShape;
    private final Vector2D       position; 
    private final Vector2D       velocity; 
    private final double         RADIUS_LARGE  = 40; 
    private final double         RADIUS_MEDIUM = 22;
    private final double         RADIUS_SMALL  = 12; 
    private final double         SPEED_LARGE   = 1.2;
    private final double         SPEED_MEDIUM  = 2.0;
    private final double         SPEED_SMALL   = 3.0;
    private final double         SCORE_LARGE   = 20; 
    private final double         SCORE_MEDIUM  = 50;
    private final double         SCORE_SMALL   = 100;


    /*
     * ── COSTRUTTORE ───────────────────────────────────────────────
     *
     * public Asteroid(Vector2D position, Vector2D velocity, Size size)
     *
     * Input:
     *   position → posizione iniziale
     *   velocity → velocità iniziale
     *   size     → dimensione (LARGE, MEDIUM o SMALL)
     *
     * Cosa fare:
     *   Chiamare super(...) passando position, velocity e il raggio
     *   corrispondente alla dimensione (usare le costanti RADIUS_*).
     *   Salvare size nel campo corrispondente.
     *   Assegnare a customShape i vertici del poligono che daranno la forma all'asteroide usando generateRandomShape()
     */

    public Asteroid(Vector2D position, Vector2D velocity, Size size, double radius) {
        super(position, velocity, radius);
        this.position = position; 
        this.velocity = velocity; 
        this.size = size; 
        this.customShape = generateRandomShape(radius);
    }


    /*
     * ── private List<Vector2D> generateRandomShape(double radius) ────────────
     *
     * Metodo che ritorna la forma di un asteroide sotto forma di vertici di un poligono,
     * i vertici devono essere vicini a radius in modo che la hitbox durante il gioco sia realistica
     */

    private List<Vector2D> generateRandomShape(double radius) {
        List<Vector2D> points = new ArrayList<>();
        double step = (2 * Math.PI) / 8; 

        for (int i = 0; i < 8; i++) {
            double angle = (i * step) + (rng.nextDouble() * step);
            double r = (radius * 0.5) + (rng.nextDouble() * radius); 

            double x = r * Math.cos(angle);
            double y = r * Math.sin(angle); 

            points.add(new Vector2D(x, y));
        }

        return points;
    }

    /*
     * ── spawnRandom(int screenWidth, int screenHeight) ────────────
     *
     * Metodo STATICO che crea un asteroide LARGE in una posizione
     * casuale sul bordo dello schermo con velocità casuale.
     * Viene chiamato da GameArea per creare gli asteroidi iniziali.
     *
     * Output: Asteroid  — un nuovo asteroide grande pronto all'uso
     *
     * Algoritmo suggerito:
     *   1. Scegliere casualmente se posizionare l'asteroide su un bordo
     *      orizzontale (sopra/sotto) o verticale (sinistra/destra).
     *   2. Generare una posizione casuale su quel bordo.
     *   3. Generare un angolo casuale: rng.nextDouble() * 2 * Math.PI
     *   4. Generare una velocità casuale intorno a SPEED_LARGE:
     *   5. Calcolare il vettore Vector2D velocità:
     *        vx = Math.cos(angle) * speed
     *        vy = Math.sin(angle) * speed
     *   6. Creare e restituire il nuovo Asteroid.
     */

    public Asteroid spawnRandom(int screenWidth, int screenHeight) {
        int x = (int) (rng.nextDouble() * screenWidth + 1);
        int y = (int) (rng.nextDouble() * screenHeight + 1);
        Vector2D position = new Vector2D(x, y);

        velocity = Asteroid.generateRandomVelocity(getRadius(), SPEED_LARGE);
        
        return new Asteroid(position, velocity, Size.LARGE, RADIUS_LARGE);
    }


    /*
     * ── update(int width, int height) ────────────────────────────
     *
     * Gli asteroidi si muovono in linea retta a velocità costante
     * e fanno wrapping ai bordi dello schermo.
     *
     * Passi:
     *   1. Chiamare move()              → sposta l'asteroide
     *   2. Chiamare wrapAround(w, h)
     *
     * Nota: gli asteroidi non cambiano mai velocità da soli.
     */

    public void update(int width, int height) {
        move(); 
        wrapAround(width, height);
    }


    /*
     * ── split() ──────────────────────────────────────────────────
     *
     * Output: List<Asteroid>  — lista dei frammenti generati
     *                           (lista vuota se l'asteroide è SMALL)
     *
     * Quando un proiettile colpisce questo asteroide, GameArea chiama
     * split() per ottenere i frammenti da aggiungere all'area di gioco.
     *
     * Regole:
     *   LARGE  → restituire 2 nuovi asteroidi MEDIUM
     *   MEDIUM → restituire 2 nuovi asteroidi SMALL
     *   SMALL  → restituire una lista vuota (new ArrayList<>())
     *
     * I frammenti partono dalla stessa posizione del padre (position.copy())
     * e hanno velocità casuali proporzionali alla loro dimensione:
     *
     * Non chiamare destroy() qui: ci pensa GameArea.
     */

    public List<Asteroid> split() {
        Size actualSize = getSize();

        if (actualSize == Size.LARGE) {
            return new ArrayList<>(
                new Asteroid(this.position, Asteroid.generateRandomVelocity(RADIUS_MEDIUM, SPEED_MEDIUM), Size.MEDIUM, RADIUS_MEDIUM), 
                new Asteroid(this.position, Asteroid.generateRandomVelocity(RADIUS_MEDIUM, SPEED_MEDIUM), Size.MEDIUM, RADIUS_MEDIUM)
            ); 
        }

        if (actualSize == Size.MEDIUM) {
            return new ArrayList<>(
                new Asteroid(this.position, Asteroid.generateRandomVelocity(RADIUS_SMALL, SPEED_SMALL), Size.SMALL, RADIUS_SMALL), 
                new Asteroid(this.position, Asteroid.generateRandomVelocity(RADIUS_SMALL, SPEED_SMALL), Size.SMALL, RADIUS_SMALL)
            );
        }

        return new ArrayList<>();
    }


    /*
     * ── getScore() ───────────────────────────────────────────────
     *
     * Output: int  — punteggio assegnato al giocatore quando questo
     *                asteroide viene distrutto
     *
     */

    public int getScore() { 
        switch (getSize()) {
            case Size.LARGE:
                return SCORE_LARGE; 
            case Size.MEDIUM: 
                return SCORE_MEDIUM; 
            case Size.SMALL: 
                return SCORE_SMALL; 
            default:
                return 0;
        }
    }


    /*
     * ── getShape() ───────────────────────────────────────────────
     *
     * Output: List<Vector2D>  — vertici del poligono (vedi spiegazione sopra)
     *
     * Restituire i vertici che danno fomra all' asteroide.
     * Il sistema di coordinate è relativo al centro (0,0).
     */

    public List<Vector2D> getShape() { return this.customShape; }


    /*
     * ── getColor() ───────────────────────────────────────────────
     *
     * Output: Color
     *
     */

    public Color getColor() { return Color.WHITE; }

    /**
     *  GetSize() 
     * 
     *  return type: Size.size
     */

    private Size getSize() { return this.size; }

    /**
     * get\()
     * 
     * return type: double; 
     * 
     * @param radius
     * @return
     */

    private double getRadius() { 
        switch (getSize()) {
            case Size.LARGE:
                return RADIUS_LARGE; 
            case Size.MEDIUM: 
                return RADIUS_MEDIUM; 
            case Size.SMALL: 
                return RADIUS_SMALL; 
            default:
                break;
        }
    }

    /**
     * generateRandomVelocity(double radius) 
     * 
     * return type: Vector2D 
     */

    public static Vector2D  generateRandomVelocity(double radius, double speed) {
        double angle = rng.nextDouble() * 2 * Math.PI; 
        double randomVelocity = rng.nextDouble() * (speed + 1);

        Vector2D velocity = new Vector2D(
            Math.cos(angle) * randomVelocity,
            Math.sin(angle) * randomVelocity
        );

        return velocity; 
    }
}
