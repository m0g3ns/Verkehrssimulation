package verkehrssimulation;

/**
 *
 * @author Mogens Meißner
 */
public class PIRegler {    
    private final double KC;
    private final double TI;
    private final Beschleunigungsregler aRegler = new Beschleunigungsregler();
    
    private double output;
    private double errorSum = 0.0;
    
    public PIRegler(double KC, double TI) {
        this.KC = KC;
        this.TI = TI;
    }
    
    public double calcOutput(double currentSpeed, double commandSpeed) {
        double error = commandSpeed - currentSpeed;
        errorSum += error;
        output = KC * error + KC / TI * errorSum;
        return output;
    }
}
