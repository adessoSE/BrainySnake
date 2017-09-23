import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Ein Objekt dieser Klasse repr�sentiert die Darstellung des
 * Spielfelds des Schlange-Spiels.
 */
public class Spielfeld extends JPanel {
  
  /**
   * Schlange-Spiel, dessen Spielfeld dargestellt wird.
   */
  private SchlangeSpiel spiel;
  
  /**
   * Erzeugt ein Objekt dieser Klasse f�r das �bergebene Schlange-Spiel.
   * 
   * @param spiel  Spiel, f�r das das Objekt erzeugt wird
   */
  public Spielfeld(SchlangeSpiel spiel) {
    
    this.spiel = spiel;
  }
  
  /**
   * Zeichnet den �bergebenen Punkt in der angegebenen Grafikumgebung.
   * 
   * @param graphics  Grafikumgebung
   * @param punkt  Punkt, der gezeichnet wird
   */
  private void zeichnePunkt(Graphics graphics, Punkt punkt) {
    
    zeichnePunkt(graphics, punkt.gibX(), punkt.gibY());
  }
  
  /**
   * Zeichnet den durch die Koordinaten angegebenen Punkt in der
   * angegebenen Grafikumgebung.
   * 
   * @param graphics  Grafikumgebung
   * @param x  horizontale Koordinate des Punkts
   * @param y  vertikale Koordinate des Punkts
   */
  private void zeichnePunkt(Graphics graphics, int x, int y) {
    
    int left = getInsets().left;
    int top = getInsets().top;
    int width = getSize().width - left - getInsets().right;
    int height = getSize().height - top - getInsets().bottom;
        
    int b = width / (spiel.gibBreite() + 2);
    int h = height / (spiel.gibHoehe() + 2);
    
    int blockgroesse = Math.min(b, h);
    
    graphics.fillRoundRect(x * blockgroesse, y * blockgroesse,
            blockgroesse - 1, blockgroesse - 1, 10, 10);
  }
  
  /**
   * Zeichnet das Spielfeld in die angegebene Grafikumgebung.
   * 
   * @param graphics  Grafikumgebung, in die gezeichnet wird
   */
  public void paintComponent(Graphics graphics) {
    
    super.paintComponent(graphics);
          
    /* Anti-Aliasing einschalten.
     */
    ((Graphics2D) graphics).setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    
    /* Umrandung des Spielfelds schwarz zeichnen.
     */
    graphics.setColor(Color.BLACK);
    
    for (int i = 0; i <= spiel.gibBreite() + 1; i++) {
      this.zeichnePunkt(graphics, i, 0);
      this.zeichnePunkt(graphics, i, spiel.gibHoehe() + 1);
    }
    
    for (int i = 1; i <= spiel.gibHoehe(); i++) {
      this.zeichnePunkt(graphics, 0, i);
      this.zeichnePunkt(graphics, spiel.gibBreite() + 1, i);
    }

    /* Schlange hellgrau zeichnen und den Kopf grau zeichnen
     */
    Color dunkelgruen = new Color((float)0,(float)0.5,(float)0);
    graphics.setColor(dunkelgruen);
    this.zeichnePunkt(graphics, spiel.gibSchlangensegment(0));
    graphics.setColor(Color.GREEN);
    for (int i = 1; i < spiel.gibSchlangenlaenge(); i++) {
      this.zeichnePunkt(graphics, spiel.gibSchlangensegment(i));
    }
    
    /* Futter rot zeichnen.
     */
    graphics.setColor(Color.RED);
    this.zeichnePunkt(graphics, spiel.gibFutterposition());
  }

}
