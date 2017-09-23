/**
 * Ein Objekt dieser Klasse repräsentiert einen Punkt in einer
 * zweidimensionalen Ebene.
 */
public class Punkt {
  
  /**
   * Horizontale Koordinate dieses Punkts.
   */
  private int x;
  
  /**
   * Vertikale Koordinate dieses Punkts.
   */
  private int y;
  
  /**
   * Erzeugt ein Objekt dieser Klasse für die übergebenen
   * Koordinaten.
   * 
   * @param x  horizontale Koordinate
   * @param y  vertikale Koordinate
   */
  public Punkt(int x, int y) {
    
    this.x = x;
    this.y = y;
  }
  
  /**
   * Addiert zu diesem Punkt den übergebenen Punkt. Die Addition
   * erfolgt koordinatenweise.
   * 
   * @param summand  Punkt, der hinzuaddiert wird
   * @return  Summe der Punkte
   */
  public Punkt addiere(Punkt summand) {
    
    return new Punkt(this.x + summand.x, this.y + summand.y);
  }
  
  /**
   * Gibt an, ob dieser und der übergebene Punkt in ihren
   * Koordinaten gleich sind.
   * 
   * @param punkt  Punkt, der mit diesem verglichen wird
   * @return  true genau dann, wenn die Punkte gleich sind
   */
  public boolean istGleich(Punkt punkt) {
    
    return (this.x == punkt.x) && (this.y == punkt.y);
  }
  
  /**
   * Liefert die horizontale Koordinate dieses Punkts.
   *
   * @return horizontale Koordinate
   */
  public int gibX() {
    
    return x;
  }
  
  /**
   * Liefert die vertikale Koordinate dieses Punkts.
   *
   * @return vertikale Koordinate
   */
  public int gibY() {
    
    return y;
  }

}
