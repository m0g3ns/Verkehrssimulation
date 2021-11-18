package verkehrssimulation;

/**
 *
 * @author Mogens Meißner
 */
public class Beschleunigungsregler {
    /**
     * Parameter der ISO 22179
     * 
     * Weitere Informationen unter:
     * https://diglib.tugraz.at/download.php?id=576a780005f9b&location=browse
     * (2.2.2)
     */
    private final double vHighMin = 20.0; //In Meter / Sekunde
    private final double aMaxHighMin = 2.0;
    private final double aMinHighMin = -3.5;
    private final double vLowMax = 0.0;
    private final double aMaxLowMax = 4.0;
    private final double aMinLowMax = -5.0;
    
    public double getRegulatedMax(double a, double v) {        
        if(a >= 0) {
            return Math.min(a, this.getAMax(v));
        }
        return Math.max(a, this.getAMin(v));
    }
    
    /**
     * 
     * @param auto Auto für das die maximale Beschleunigung nach ISO 22179 berechnet werden soll
     * @return maximale Beschleunigung nach ISO 22179
     */
    public double getAMax(Auto auto) {
        return this.getAMax(auto.getVelocity());
    }
    
    public double getAMax(double v) {   
        if(v > vHighMin) return aMaxHighMin;
        if(v <= vLowMax) return aMaxLowMax;
        double aMax = (aMaxHighMin - aMaxLowMax) / (vHighMin - vLowMax) * (v - vLowMax) + aMaxLowMax;
        return aMax;
    }
    
    /**
     * 
     * @param auto Auto für das die maximale Entschleunigung nach ISO 22179 berechnet werden soll
     * @return maximale Entschleunigung nach ISO 22179
     */
    public double getAMin(Auto auto) {
        return this.getAMin(auto.getVelocity());
    }
    
    public double getAMin(double v) {
        if(v > vHighMin) return aMinHighMin;
        if(v <= vLowMax) return aMinLowMax;
        double aMin = (aMinHighMin - aMinLowMax) / (vHighMin - vLowMax) * (v - vLowMax) + aMinLowMax;
        return aMin;
    }    
}
