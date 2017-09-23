
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Florian
 */
public class UserEinst {

    private static int gibgroesse(String name) {
        int groesse = 15;
        boolean b = false;
        while (!b) {
            Object reuckgabe = JOptionPane.showInputDialog
                    (null, "Gib die " + name + " des Feldes ein 15 < x < 50" );
            String antwort = (String) reuckgabe;
            b = antwort.matches("[0-9]*");
            if (b) {
                groesse = Integer.parseInt(antwort);
                if(groesse < 15) {
                    groesse = 15;
                } else if(groesse > 50) {
                    groesse = 50;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Du musst eine Zahl eingeben");
            }
        }
        return groesse;
    }

    protected static int[] frageAb() {
        int b = gibgroesse("breite");
        int h = gibgroesse("hoehe");
       JOptionPane.showMessageDialog(null, "Benutze die linke und rechte Pfeiltaste um zu steuern.");
       return new int[]{b,h};

       }


//    public static void main(String[] args) {
//        int[] a = frageAb();
//        System.out.println(a[0]);
//        System.out.println(a[1]);
//    }
}
