package verkehrssimulation;

/**
 *
 * @author Mogens Meißner
 */
public class Streckenabschnitt {
    private final int beginn;
    private final int tempolimit;
    
    /**
     * 
     * @param beginn Beginn des Streckenabschnitts in Meter nach dem Start bei 0 Metern
     * @param tempolimit Tempolimit in Meter / Sekunde für diesen Streckenabschnitt
     */
    public Streckenabschnitt(int beginn, int tempolimit) {
        this.beginn = beginn;
        this.tempolimit = tempolimit;
    }
    
    /**
     * 
     * @return Beginn des Streckenabschnitts in Meter nach dem Start bei 0 Metern
     */    
    public int getBeginn() {
        return this.beginn;
    }
    
    /**
     * 
     * @return Tempolimit in Meter / Sekunde für diesen Streckenabschnitt
     */
    public int getTempolimit() {
        return this.tempolimit;
    }
}
