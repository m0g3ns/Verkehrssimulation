package verkehrssimulation;

/**
 *
 * @author Mogens Meißner
 */
public class Auto {    
    private final Beschleunigungsregler aRegler = new Beschleunigungsregler();
    private final PIRegler piRegler = new PIRegler(Verkehrssimulation.KCv, Verkehrssimulation.TIv);
    
    private final int index;        
    
    private int tempolimit;
    private double v = 0;
    private double position = 0;    
    private double breakGoal = 0;
    
    /**
     * Konstruktor, wenn die Position des Autos für die Simulation irrelevant ist.
     */
    public Auto() {
       this.index = -1;
    }
    
    /**
     * 
     * @param position Position in Metern auf der Strecke
     * @param index Index des Autos in der LinkedList Verkehrssimulation.autos
     */
    public Auto(double position, int index) {
        this.index = index;
        this.position = position;        
    }
    
    /**
     * Das Auto soll sich bewegen
     */
    public void fahren() {
        this.updateTempolimit();
        Auto autoDavor = this.getCarInFront();        
        if(autoDavor == null ||
                (   autoDavor.getPosition() - this.position - Verkehrssimulation.AUTOLAENGE > 3.6 * this.v / 2  ||
                    autoDavor.getPosition() - this.position - Verkehrssimulation.AUTOLAENGE > 3.6 * this.tempolimit / 2)) {
            //Das Auto davor ist weit genug weg und kann ignoriert werden
            this.geschwindigkeitRegulieren(this.tempolimit);
        } else if(autoDavor.getPosition() - this.position < Verkehrssimulation.AUTOLAENGE) {
            //Das Auto ist auf das vordere Auto aufgefahren
            System.out.println("Auto mit Index " + this.index + " hat einen Unfall bei " + this.position + " gebaut!");
            Verkehrssimulation.unfall = true;
        } else {
            //Das Auto ist zu nah an dem vorderen Auto oder der Abstand ist nicht groß genug für das aktuelle Tempolimit
            Verkehrssimulation.breakTooClose++;
            this.geschwindigkeitRegulieren(this.v / 3);
        }
        this.position += this.v * Verkehrssimulation.dt;
    }
    
    /**
     * 
     * @param zielV Zielgeschwindigkeit auf welche Beschleunigt werden soll.
     */
    public void geschwindigkeitRegulieren(double zielV) {        
        //Neue Geschwindigkeit von PI-Regler abfragen
        double v = this.piRegler.calcOutput(this.v, zielV);
        //Beschleunigung mit Geschwindigkeit von PI Regler ausrechnen
        double a = (v - this.v) / Verkehrssimulation.dt;
        //ggf Beschleunigung nach ISO-Vorschriften regulieren
        a = this.aRegler.getRegulatedMax(a, v);
        //Geschwindigkeit an Beschleunigung anpassen
        this.v += a * Verkehrssimulation.dt;
    }
    
    public void setTempolimit(int tempolimit) {
        this.tempolimit = tempolimit;
    }
    
    /**
     * Tempolimit für die aktuelle Position des Autos abfragen
     */
    public void updateTempolimit() {
        this.tempolimit = Verkehrssimulation.strecke.getAktuellesTempolimit(this);
    }
    
    /**
     * 
     * @return Auto welches sich vor diesem befindet (null, wenn es nicht relevant ist)
     */
    public Auto getCarInFront() {
        if(this.index > 0) return Verkehrssimulation.autos.get(this.index - 1);
        return null;
    }
    
    public int getTempolimit() {
        return this.tempolimit;
    }
    
    public double getPosition() {
        return this.position;
    }
    
    public double getVelocity() {
        return this.v;
    }
}
