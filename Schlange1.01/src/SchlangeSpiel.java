/**
 * Ein Objekt dieser Klasse repräsentiert das Modell des Schlange-Spiels.
 */
public class SchlangeSpiel {
  
  /**
   * Breite des Spielfelds.
   */
  private int breite;
  
  /**
   * Höhe des Spielfelds.
   */
  private int hoehe;
  
  /**
   * Schlange, die sich auf dem Spielfeld bewegt.
   */
  private Schlange schlange;
  
  /**
   * Position des Futters auf dem Spielfeld.
   */
  private Punkt futterposition;
  
  /**
   * Erzeugt ein Objekt dieser Klasse. Das Spielfeld hat die angegebenen
   * Abmessungen. Die Schlange hat anfangs ein Segment und startet auf
   * der Position (breite/2, hoehe/2). Das Futter liegt an einer
   * beliebigen freien Position.
   * 
   * @param breite  Breite des Spielfelds
   * @param hoehe  Höhe des Spielfelds
   */
  public SchlangeSpiel(int breite, int hoehe) {
    
    this.breite = breite;
    this.hoehe = hoehe;
    this.schlange = new Schlange(breite / 2, hoehe / 2);
    this.platziereFutter();
  }
  
  /**
   * Platziert das Futter auf einer beliebigen freien Stelle
   * des Spielfelds.
   */
  private void platziereFutter() {
    
    int x;
    int y;

    /* Bestimme zufällige Position auf dem Spielfeld, an der sich
     * nicht die Schlange befindet.
     */
    do {
      x = 1 + (int) (Math.random() * this.breite);
      y = 1 + (int) (Math.random() * this.hoehe);
    } while (schlange.enthaeltPunkt(new Punkt(x, y)));
    
    this.futterposition = new Punkt(x, y);
  }
  
  /**
   * Bewegt die Schlange um ein Feld in ihrer aktuellen
   * Bewegungsrichtung voran.
   * 
   * @return true genau dann, wenn die Bewegung erfolgreich war.
   *         false genau dann, wenn es zu einer Kollision mit dem
   *         Rand des Spielfelds oder einem Segment der Schlange kam. 
   */
  public boolean geheVoran() {
    
    boolean bewegungErfolgreich = schlange.geheVoran(futterposition);
    
    if (bewegungErfolgreich) {
      
      if (schlange.gibSegment(0).istGleich(futterposition)) {
        
        /* Schlange hat Futter gefressen.
         */
        platziereFutter();
        
      } else if (schlange.gibSegment(0).gibX() == 0
                 || schlange.gibSegment(0).gibY() == 0
                 || schlange.gibSegment(0).gibX() > this.breite
                 || schlange.gibSegment(0).gibY() > this.hoehe) {
        

        /* Schlange ist auf Rand getroffen.
         */
        bewegungErfolgreich = false;
      }
    }
    
    return bewegungErfolgreich;
  }
  
  /**
   * Schlange dieses Spiels ändert ihre Bewegungsrichtung nach rechts.
   */
  public void nachRechts() {
    
    schlange.geheNachRechts();
  }
  
  /**
   * Schlange dieses Spiels ändert ihre Bewegungsrichtung nach links.
   */
  public void nachLinks() {
    
    schlange.geheNachLinks();
  }
  
  /**
   * Liefert die Breite des Spielfelds.
   * @return Breite des Spielfelds
   */
  public int gibBreite() {
    
    return breite;
  }
  
  /**
   * Liefert die Höhe des Spielfelds.
   * @return Höhe des Spielfelds
   */
  public int gibHoehe() {
    
    return hoehe;
  }
  
  /**
   * Länge der Schlange dieses Spiels.
   * @return Länge der Schlange
   */
  public int gibSchlangenlaenge() {
    
    return schlange.gibLaenge();
  }
  
  /**
   * Liefert die Position des angegebenen Segments der Schlange.
   * @param segmentnummer  Index für Segment der Schlange
   * @return Position des angegebenen Segments
   */
  public Punkt gibSchlangensegment(int segmentnummer) {
    
    return schlange.gibSegment(segmentnummer);
  }
  
  /**
   * Liefert die Position des Futters.
   * @return Position des Futters
   */
  public Punkt gibFutterposition() {
    
    return this.futterposition;
  }
  
}
