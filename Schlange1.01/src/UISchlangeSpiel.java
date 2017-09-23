import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Ein Objekt dieser Klasse repr�sentiert die Oberfl�che f�r
 * das Schlange-Spiel.
 */
public class UISchlangeSpiel extends JFrame
        implements PropertyChangeListener {
  
  /**
   * Horizontale Gr��e des Anwendungsfensters.
   */
  private static final int FENSTERGROESSE_X = 600;
  
  /**
   * Vertikale Gr��e des Anwendungsfensters.
   */
  private static final int FENSTERGROESSE_Y = 600;
  
  /**
   * Modell des Schlange-Spiels.
   */
  private SchlangeSpiel spiel;
  
  /**
   * Darstellung des Spielfelds.
   */
  private Spielfeld spielfeld;
  
  /**
   * Erzeugt die Oberfl�che f�r das Schlange-Spiel f�r das
   * �bergebene Modell des Spiels.
   * 
   * @param spiel  Modell des Spiels
   */
  public UISchlangeSpiel(SchlangeSpiel spiel) {
    
    super("Schlange");
    
    this.spiel = spiel;
    
    /* Erzeugt die Komponenten dieses Frame.
     */
    erzeugeKomponenten();
    
    /* Anwendung beenden beim Schlie�en dieses Frame.
     */
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  /**
   * Erzeugt die Komponenten dieses Frame.
   */
  private void erzeugeKomponenten() {
    
    Container container = this.getContentPane();
    container.setLayout(new GridLayout(1, 1));
    
    this.setFocusable(true);
    
    /* Behandlung der Tastaturereignisse zur Steuerung der
     * Bewegungsrichtung.
     */
    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
          spiel.nachRechts();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
          spiel.nachLinks();
        }
      }
    });
    
    /*
     * Objekt zur Darstellung des Spielfelds erzeugen und dem
     * Container diesen Frame hinzuf�gen.
     * Das Spielfeld erh�lt keinen Fokus, da die Tastaturereignisse
     * von diesem Frame selbst behandelt werden.
     */
    spielfeld = new Spielfeld(spiel);
    spielfeld.setFocusable(false);
    container.add(spielfeld);
  }
  
  /**
   * Aktualisiert die Darstellung.
   *
   * @param propertyChangeEvent  Ereignis, von dem abh�ngig die
   *        Darstellung aktualisiert wird
   */
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    
    if (Spieltaktung.KOLLISION.equals(
            propertyChangeEvent.getPropertyName())) {
      
      JOptionPane.showMessageDialog(this, "Leider verloren");
      System.exit(0);
      
    } else if (Spieltaktung.VORWAERTS.equals(
            propertyChangeEvent.getPropertyName())) {
      
      spielfeld.repaint();
    }
  }
  
  /**
   * Start der Anwendung.
   *
   * @param args  wird nicht verwendet
   */
  public static void main(String[] args) {

      // My Config
      // Frage nach der Felderanzahl
      int[] a = UserEinst.frageAb();
      // ende


      
    SchlangeSpiel spiel = new SchlangeSpiel(a[0], a[1]);
    Spieltaktung taktung = new Spieltaktung(spiel);
    UISchlangeSpiel fenster = new UISchlangeSpiel(spiel);
    
    taktung.addPropertyChangeListener(fenster);
    
    //fenster.setSize(FENSTERGROESSE_X, FENSTERGROESSE_Y);
    fenster.setResizable(false);
    fenster.setVisible(true);
    // My Config
    // Bildschirmgröße anpassen

    /* Bildschirmgröße abfragen in Pixel */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    /* Versuch auf 16 zu 9 zu optimieren */
    int h = (screen.height / 9) * 5;
    int b = (screen.width / 16) * 5;
    
    fenster.setSize(b,h);
    // ende
    
    new Thread(taktung).start();
  }
  
}
