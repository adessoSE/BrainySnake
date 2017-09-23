import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Ein Objekt dieser Klasse "taktet" das Schlange-Spiel.
 * In einem festen zeitlichen Raster wird die Schlange
 * bewegt und es werden registrierte Listener informiert,
 * darauf zu reagieren.
 */
public class Spieltaktung implements Runnable {
  
  /**
   * Pause zwischen zwei Bewegungen der Schlange.
   */
  private static final int PAUSE_IN_MS = 500;
  
  /**
   * Bezeichnung eines Ereignisses: Schlange hat sich vorwärts bewegt.
   */
  public static final String VORWAERTS = "vorwaerts";
  
  /**
   * Bezeichnung eines Ereignisses: Schlange ist kollidiert.
   */
  public static final String KOLLISION = "kollision";
  
  /**
   * Schlange-Spiel, dass durch dieses Objekt getaktet wird.
   */
  private SchlangeSpiel spiel;
  
  /**
   * Unterstützt die Benachrichtigung registrierter Objekte.
   */
  private PropertyChangeSupport propertyChangeSupport;
  
  /**
   * Erzeugt ein Objekt dieser Klasse für das übergebene Spiel.
   * 
   * @param spiel  Spiel, das durch dieses Objekt getaktet wird.
   */
  public Spieltaktung(SchlangeSpiel spiel) {
    
    this.spiel = spiel;
    
    /* für Benachrichtigung
     */
    propertyChangeSupport = new PropertyChangeSupport(this);
  }
  
  /**
   * Bewegt die Schlange, wobei zwischen zwei Bewegungen eine
   * bestimmte Zeit pausiert wird. Registrierte Listener werden
   * benachrichtigt, wenn die Schlange sich vorwärts bewegt hat
   * oder kollidiert ist.
   */
  public void run() {
    
    while (true) {
      
      try {
        Thread.sleep(PAUSE_IN_MS);
      } catch (InterruptedException e) {
        throw new InternalError();
      }
      
      if (spiel.geheVoran()) {
        propertyChangeSupport.firePropertyChange("vorwaerts", 0, 1);
      } else {
        propertyChangeSupport.firePropertyChange("kollision", 0, 1);
      }
    }
  }
  
  /**
   * Registriert den angegebenen Listener.
   *
   * @param l  Listener, der registriert werden soll
   */
  public void addPropertyChangeListener(PropertyChangeListener l) {
        
    propertyChangeSupport.addPropertyChangeListener(l);
  }    
    
  /**
   * Entfernt den angegebenen Listener.
   *
   * @param l  Listener, der aus der Registrierung entfernt wird
   */
  public void removePropertyChangeListener(PropertyChangeListener l) {

    propertyChangeSupport.removePropertyChangeListener(l);
  }
  
}
