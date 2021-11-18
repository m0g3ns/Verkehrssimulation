package verkehrssimulation;

import java.util.ArrayList;

/**
 *
 * @author Mogens Meißner
 */
public class Strecke {
    private final ArrayList<Streckenabschnitt> abschnitte = new ArrayList();

    public ArrayList<Streckenabschnitt> getAbschnitte() {
        return abschnitte;
    }
    
    /**
     * 
     * @param s Streckenabschnitt welcher der Strecke hinzugefügt werden soll
     */
    public void addStreckenabschnitt(Streckenabschnitt s) {
        abschnitte.add(s);
    }
    
    /**
     * 
     * @param auto Auto für welches das Tempolimit zurückgegeben werden soll.
     * @return Tempolimit für den Abschnitt in welchem sich das Auto befindet.
     */
    public int getAktuellesTempolimit(Auto auto) {
        for(int i = 0; i < abschnitte.size(); i++) {
            if(abschnitte.get(i).getBeginn() > auto.getPosition()) {
                if(i > 0) return abschnitte.get(i - 1).getTempolimit();
                return abschnitte.get(0).getTempolimit();
            }
        }
        return abschnitte.get(abschnitte.size() - 1).getTempolimit();
    }
}
