
/**
 *
 */
public class Schlange {

     private String richtung;
     private int laenge = 1;
     private Punkt koerper[] = new Punkt[1];


     public Schlange (int x, int y){
         this.koerper[0] = new Punkt(x, y);
         this.richtung = "rechts";

     }
     
     public void geheNachLinks(){
         if(this.richtung.equals("rechts")){
             this.richtung = "unten";
         }else if (this.richtung.equals("unten")){
             this.richtung = "links";
         }else if(this.richtung.equals("links")){
             this.richtung = "oben";
         }else if(this.richtung.equals("oben")){
             this.richtung = "rechts";
         }
         
     }
         
     public void geheNachRechts(){
         if(this.richtung.equals("rechts")){
             this.richtung = "oben";
         }else if (this.richtung.equals("unten")){
             this.richtung = "rechts";
         }else if(this.richtung.equals("links")){
             this.richtung = "unten";
         }else if(this.richtung.equals("oben")){
             this.richtung = "links";
         }
         
     }

     private Punkt naechsterPunkt(){
         int x = koerper[0].gibX();
         int y = koerper[0].gibY();

         if(this.richtung.equals("rechts")){
             x = x + 1;
         }else if (this.richtung.equals("unten")){
             y = y - 1;
         }else if(this.richtung.equals("links")){
             x = x - 1;
         }else if(this.richtung.equals("oben")){
             y = y + 1;
         }

         return(new Punkt(x, y));
     }
     private void wachse(Punkt futter) {
         int index = 1;
          Punkt body[] = new Punkt[this.laenge + 1];
          body[0] = futter;
          while (laenge != index - 1) {
          body[index] = this.koerper[index - 1];
          index = index + 1;
          }
          this.koerper = body;
         }
     

     private void bewege(Punkt ziel){
         for(int i = this.laenge - 1; i >= 1; i = i - 1){
             this.koerper[i] = this.koerper[i - 1];
         }
         this.koerper[0] = ziel;
     }

     public boolean geheVoran(Punkt futter) {
         if(this.eatitself()){
             return false;
         }else if(this.enthaeltPunkt(futter)){
             return false;
         }else if(this.naechsterPunkt().istGleich(futter)){
             this.wachse(futter);
             this.laenge = this.laenge + 1;
             return true;
         }else{
             this.bewege(this.naechsterPunkt());
             return true;
         }
     }

     public boolean enthaeltPunkt(Punkt punkt){
         boolean enthaelt = false;
         for(int i = 0; i < this.laenge && enthaelt == false ; i = i + 1){
             enthaelt = this.koerper[i].istGleich(punkt);
         }
         return enthaelt;
     }

     public boolean eatitself() {
         boolean enthaelt = false;
         for(int i = 1; i < this.laenge - 1 && enthaelt == false ; i = i + 1){
             enthaelt = this.naechsterPunkt().istGleich(this.koerper[i]);
         }
         return enthaelt;

     }




     public int gibLaenge(){
         return this.laenge;
     }

     public Punkt gibSegment(int position){
         return this.koerper[position];
     }


}
