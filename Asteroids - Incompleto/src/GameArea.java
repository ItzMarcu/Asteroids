import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.swing.*;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  CLASSE DA IMPLEMENTARE — GameArea (l'area di gioco)
 * ═══════════════════════════════════════════════════════════════════
 *
 * GameArea è il contenitore principale del gioco. Si occupa di:
 *   - tenere traccia di tutte le entità presenti (nave, asteroidi, proiettili)
 *   - fare avanzare il gioco frame per frame (game loop)
 *   - gestire i tasti premuti dal giocatore
 *   - controllare le collisioni tra entità
 *   - tenere il punteggio e le vite
 *   - disegnare tutto sullo schermo
 *
 * ── Come funziona il game loop ────────────────────────────────────
 *
 * Il Timer Swing scatta circa 60 volte al secondo e chiama
 * actionPerformed(). Ogni "tick" deve:
 *   1. Aggiornare tutte le entità (spostarle, ecc.)
 *   2. Controllare le collisioni
 *   3. Verificare se il livello è finito (lista asteroidi vuota)
 *   4. Ridisegnare lo schermo (repaint())
 *
 * ── Come funziona il rendering ────────────────────────────────────
 *
 * paintComponent() è già implementato qui sotto: chiama draw() su ogni
 * entità e disegna l'HUD (punteggio, vite, livello).
 * Voi non dovete preoccuparvi del disegno: basta che le liste
 * asteroids, bullets e il campo ship siano aggiornati correttamente.
 *
 * ── Come funziona l'input ─────────────────────────────────────────
 *
 * keyPressed() e keyReleased() sono già implementati: leggono i tasti
 * freccia (o WASD) per muovere la nave e la barra spaziatrice per sparare.
 * Voi non dovete modificare questi metodi.
 *
 * ═══════════════════════════════════════════════════════════════════
 *  STATO INTERNO — campi che dovete dichiarare
 * ═══════════════════════════════════════════════════════════════════
 *
 * ┌────────────────────┬──────────────────┬──────────────────────────────────────┐
 * │ Nome               │ Tipo             │ Descrizione                          │
 * ├────────────────────┼──────────────────┼──────────────────────────────────────┤
 * │ ship               │ Ship             │ La nave del giocatore                │
 * │ asteroids          │ List<Asteroid>   │ Tutti gli asteroidi presenti         │
 * │ bullets            │ List<Bullet>     │ Tutti i proiettili in volo           │
 * │ score              │ int              │ Punteggio corrente                   │
 * │ lives              │ int              │ Vite rimaste                         │
 * │ level              │ int              │ Livello corrente                     │
 * │ gameOver           │ boolean          │ true se la partita è finita          │
 * │ paused             │ boolean          │ true se il gioco è in pausa          │
 * │ timer              │ Timer            │ Il Timer Swing che pilota il loop    │
 * └────────────────────┴──────────────────┴──────────────────────────────────────┘
 *
 * COSTANTI SUGGERITE:
 *   WIDTH           = 800   → larghezza dell'area (public static final)
 *   HEIGHT          = 600   → altezza dell'area   (public static final)
 *   FPS             = 60    → frame al secondo
 *   INITIAL_LIVES   = 3     → vite iniziali
 *   ASTEROIDS_START = 4     → asteroidi al primo livello
 */
public class GameArea extends JPanel implements ActionListener, KeyListener {
    private Ship           ship;
    private List<Asteroid> asteroids;
    private List<Bullet>   bullets;

    private int     score;
    private int     lives;
    private int     level;
    private boolean gameOver;
    private boolean paused;

    private final int     WIDTH = 800;
    private final int     HEIGHT = 600;
    private final int     FPS = 60;
    private final int     INITIAL_LIVES = 3;
    private int     ASTEROIDS_START = 4;

    private final Timer timer;
    /*
     * ── COSTRUTTORE ───────────────────────────────────────────────
     *
     * public GameArea()
     *
     * Cosa fare:
     *   - setPreferredSize(new Dimension(WIDTH, HEIGHT))
     *   - setBackground(Color.BLACK)
     *   - setFocusable(true)
     *   - addKeyListener(this)
     *   - Inizializzare le liste asteroids e bullets come ArrayList vuoti
     *   - Creare il Timer: new Timer(1000 / FPS, this)
     *     (il secondo argomento "this" fa sì che il timer chiami
     *      actionPerformed() su questo oggetto)
     *   - Chiamare startGame()
     */

    public GameArea() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        asteroids = new ArrayList<Asteroid>();
        bullets = new ArrayList<Bullet>();
        
        timer = new Timer(1000 / FPS, this);
        startGame(); 
    }
    
    /*
     * ── void startGame() ───────────────────────────────────────────────
     *
     * Metodo privato. Resetta tutto e inizia una nuova partita.
     *
     * Cosa fare:
     *   - Azzerare score, impostare:
     *   - lives = INITIAL_LIVES,
     *   - level = 1
     *   - gameOver = false,
     *   - paused = false
     *   - Creare una nuova nave
     *   - Svuotare le liste bullets e asteroids
     *   - Chiamare spawnAsteroids(ASTEROIDS_START)
     *   - Avviare il timer: timer.start()
     */

    private void startGame() {
        score =    0;
        lives =    INITIAL_LIVES;
        level =    1;
        gameOver = false;
        paused =   false;

        ship = new Ship(WIDTH, HEIGHT);
        bullets.clear(); // .clear() -> svuota una lista
        asteroids.clear();

        spawnAsteroids(ASTEROIDS_START);
        timer.start();
    }


    /*
     * ── void spawnAsteroids(int count) ─────────────────────────────────
     *
     * Input:  count → quanti asteroidi grandi aggiungere
     *
     * Aggiunge count asteroidi LARGE all'area di gioco.
     * Gli asteroidi non devono apparire troppo vicino alla nave!
     *
     * Algoritmo:
     *   Ripetere count volte:
     *     1. Creare un asteroide casuale
     *     2. Se la distanza tra la sua posizione e quella della nave
     *        è < 150 pixel, scartarlo e ricrearne uno.
     *     3. Aggiungere l'asteroide alla lista asteroids.
     */

    public void spawnAsteroids(int count) {
        while (asteroids.size() - 1 < count) { 
            Asteroid randomAsteroid = Asteroid.spawnRandom(WIDTH, HEIGHT); 
            if (Vector2D.distance(randomAsteroid.getPosition(), ship.getPosition()) < 150) 
                randomAsteroid = Asteroid.spawnRandom(WIDTH, HEIGHT); 
            else 
                asteroids.add(randomAsteroid); 
        }
    }


    /*
     * ── void actionPerformed(ActionEvent e) ───────────────────────────
     *
     * Chiamato dal Timer ~60 volte al secondo. È il cuore del game loop.
     *
     * Cosa fare:
     *   Se !paused && !gameOver:
     *     1. Chiamare updateEntities()
     *     2. Chiamare checkCollisions()
     *     3. Se asteroids è vuota → livello finito, aumenta livello e asteroidi per quel livello:
     *   Sempre (anche se paused o gameOver):
     *     4. Chiamare repaint()  → ridisegna lo schermo
     */

    public void actionPerformed(ActionEvent e) {
        if (!paused && !gameOver) {
            updateEntities();
            checkCollisions(); 
        }

        if (asteroids.isEmpty()) {
            level++;
            ASTEROIDS_START += 2;
        }

        repaint();
    }


    /*
     * ── void updateEntities() ─────────────────────────────────────────
     *
     * Aggiorna tutte le entità per un frame e rimuove quelle distrutte.
     *
     * Passi:
     *   1. ship.update(WIDTH, HEIGHT)
     *   2. Update ogni asteroide nella lista
     *   3. Update ogni proiettile nella lista
     *   4. Rimuovere dalla lista asteroids non alive
     *   5. Rimuovere dalla lista bullets non alive
     */

    public void updateEntities() {
        ship.update(WIDTH, HEIGHT);

        Iterator<Asteroid> asteroidsIterator = asteroids.iterator(); 
        List<Asteroid> asteroidsToRemove = new ArrayList<>(); 
        
        while (asteroidsIterator.hasNext()) {
            Asteroid actual = asteroidsIterator.next(); 
            if (!actual.isAlive()) 
                asteroidsToRemove.add(actual);
            else
                actual.update(WIDTH, HEIGHT);
        }

        Iterator<Bullet> bulletsIterator = bullets.iterator(); 
        List<Bullet> bulletsToRemove = new ArrayList<>();

        while (bulletsIterator.hasNext()) { 
            Bullet actual = bulletsIterator.next(); 
            if (!actual.isAlive()) 
                bulletsToRemove.add(actual);
            else
                actual.update(WIDTH, HEIGHT);
        }

        asteroids.removeAll(asteroidsToRemove);
        bullets.removeAll(bulletsToRemove);
    }


    /*
     * ── void checkCollisions() ────────────────────────────────────────
     *
     * Rileva e gestisce tutte le collisioni del frame corrente.
     *
     * ⚠ ATTENZIONE — ConcurrentModificationException ⚠
     * Non modificate una lista mentre la state scorrendo con for-each!
     * Raccogliete i nuovi frammenti in una lista temporanea separata
     * e aggiungetela ad asteroids DOPO aver finito tutti i cicli:
     *
     /*
     * --- A) Proiettile colpisce Asteroide ---
     * Controllare i proiettili e gli asteroidi ancora attivi.
     * Se un proiettile tocca un asteroide:
     * - il proiettile viene distrutto
     * - il punteggio aumenta in base al valore dell’asteroide
     * - l’asteroide si divide nei suoi pezzi
     * - l’asteroide originale viene distrutto
     * Un proiettile può colpire al massimo un asteroide per ogni aggiornamento.
     *
     * --- B) Nave colpisce Asteroide ---
     * Controllare se la nave tocca uno degli asteroidi attivi.
     * Se succede:
     * - il giocatore perde una vita
     * - se le vite arrivano a zero il gioco termina e il timer si ferma
     * - altrimenti la nave viene ricreata (respawn)
     * In ogni aggiornamento si può perdere al massimo una vita.
     */
    public void checkCollisions(){
        Iterator<Bullet> bullettsIterator = bullets.iterator(); 
        List<Asteroid> newAsteroids = new ArrayList<>(); 
        
        while (bullettsIterator.hasNext()) { 
            Bullet b = bullettsIterator.next(); 
            Iterator<Asteroid> asteroidsIterator = asteroids.iterator();

            while (asteroidsIterator.hasNext()) {
                Asteroid a = asteroidsIterator.next(); 

                if (!a.isAlive()) continue;  
                
                if (b.collidesWith(a)) { 
                    a.destroy(); 
                    b.destroy(); 
                    
                    List<Asteroid> buffer = a.split(); 
                    if (!buffer.isEmpty())
                        newAsteroids.addAll(buffer);

                    break; 
                }

            }    
        }

        for (Asteroid a : asteroids) {
            if (ship.collidesWith(a)) {
                lives--; 
                if (lives <= 0) {
                    gameOver = true; 
                    return; 
                }
                
                break; 
            }
        }
        
        asteroids.addAll(newAsteroids);
        newAsteroids.clear();
        
        updateEntities();
    }

    // ═══════════════════════════════════════════════════════════════
    // RENDERING — già implementato, non modificare
    // ═══════════════════════════════════════════════════════════════

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            drawGameOver(g);
            return;
        }

        ship.draw(g);
        for (Asteroid a : asteroids) a.draw(g);
        for (Bullet   b : bullets)   b.draw(g);

        drawHUD(g);
        if (paused) drawPaused(g);
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString("SCORE: " + score,  10,          20);
        g.drawString("LIVES: " + lives,  10,          42);
        g.drawString("LEVEL: " + level,  WIDTH - 110, 20);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Monospaced", Font.BOLD, 48));
        g.drawString("GAME OVER", WIDTH / 2 - 140, HEIGHT / 2 - 20);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 20));
        g.drawString("Score: " + score,          WIDTH / 2 - 70,  HEIGHT / 2 + 20);
        g.drawString("Premi R per ricominciare", WIDTH / 2 - 160, HEIGHT / 2 + 50);
    }

    private void drawPaused(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        g.drawString("PAUSA", WIDTH / 2 - 60, HEIGHT / 2);
    }

    // ═══════════════════════════════════════════════════════════════
    // INPUT TASTIERA — già implementato, non modificare
    // ═══════════════════════════════════════════════════════════════

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: ship.setTurningLeft(true);  break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: ship.setTurningRight(true); break;
            case KeyEvent.VK_UP:    case KeyEvent.VK_W: ship.setThrusting(true);    break;
            case KeyEvent.VK_SPACE:
                if (!gameOver && !paused) {
                    Bullet b = ship.shoot();
                    if (b != null) bullets.add(b);
                }
                break;
            case KeyEvent.VK_P: paused = !paused; break;
            case KeyEvent.VK_R: startGame();       break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: ship.setTurningLeft(false);  break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: ship.setTurningRight(false); break;
            case KeyEvent.VK_UP:    case KeyEvent.VK_W: ship.setThrusting(false);    break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}
