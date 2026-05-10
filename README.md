# Asteroids 

Un'implementazione del classico gioco arcade **Asteroids** sviluppata in Java. Il progetto utilizza un'architettura a oggetti per gestire entità dinamiche, collisioni e logica di gioco in tempo reale.

![image](https://github.com/user-attachments/assets/4deba7e5-1de5-490f-bcf1-7f33c71f006a)

## 🛠️ Struttura del Progetto

Il codice è suddiviso in diverse classi, ognuna con una responsabilità specifica:

### Core & Utility
* **`Vector2D.java`**: Gestisce i vettori per posizione e velocità, includendo metodi per la distanza e il ridimensionamento.
* **`Entity.java`**: Classe astratta base che definisce il movimento (`move`), il wrapping dello schermo (`wrapAround`) e il calcolo delle collisioni per tutte le entità.
* **`Main.java`**: Punto di ingresso che configura la finestra `JFrame` e avvia la `GameArea`.

### Entità di Gioco
* **`Ship.java`**: Gestisce la nave del giocatore, inclusa la rotazione, la propulsione con attrito e il cooldown degli spari.
* **`Asteroid.java`**: Implementa asteroidi di diverse dimensioni (`LARGE`, `MEDIUM`, `SMALL`) con forme generate casualmente e logica di scissione (`split`).
* **`Bullet.java`**: Gestisce i proiettili con una velocità costante e una durata limitata (frame di vita).
* **`Enemy.java`**: *(In fase di implementazione)* Classe dedicata a un'entità nemica con logica di puntamento e sparo autonoma.

### Logica di Gioco
* **`GameArea.java`**: Il motore principale che gestisce il game loop (60 FPS), il sistema di punteggio, i livelli, le vite e il rilevamento delle collisioni tra proiettili, asteroidi e nave.

## 🕹️ Comandi
- **Frecce / WASD**: Ruota la nave e attiva la propulsione.
- **Spazio**: Spara proiettili.
- **P**: Mette il gioco in pausa.
- **R**: Riavvia la partita dopo un Game Over.

## 🚧 Note sullo Sviluppo: Classe Enemy.java
La classe `Enemy.java` è attualmente presente come bozza e richiede i seguenti interventi per essere funzionale:
1. **Puntamento**: Affinare il metodo `shoot` per sparare in direzione del giocatore.

## 📈 Meccaniche Implementate
- **Wrapping dello schermo**: Tutte le entità che escono dai bordi riappaiono dal lato opposto.
- **Sistema di scissione**: Gli asteroidi più grandi si dividono in frammenti più piccoli quando colpiti.
- **Invulnerabilità temporanea**: La nave cambia colore (giallo) e non subisce danni per un breve periodo dopo la perdita di una vita.
