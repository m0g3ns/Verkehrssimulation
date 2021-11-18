package verkehrssimulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;


/**
 *
 * @author Mogens Meißner
 */
public class Verkehrssimulation {
    public static final LinkedList<Auto> autos = new LinkedList();
    //Anzahl der Autos in der Simulation
    public static final int N = 500;    
    //Länge eines Autos für die Abstandsregelung
    public static final double AUTOLAENGE = 4.0;
    //Abstand der Fahrzeugvorderpunkte am Anfang der Simulation
    public static final double ABSTAND = 5;
    //Vergangene Zeit in Sekunden
    public static double t = 0.0;
    //Zeitinkrement der Simulation in Sekunden
    public static double dt = 0.001;
    //Strecke der aktuellen Simulation
    public static Strecke strecke = new Strecke();
    public static final int STRECKENENDE = 10000;
    //Unterbrechen der Simulation, wenn ein Unfall passiert ist.
    public static boolean unfall = false;
    //Dateien zum Speicheren der Logs
    public static Writer positionFile;
    public static Writer velocityFile;
    public static Writer distanceToCarInFrontFile;
    
    public static double KCv = 0.0001;
    public static double TIv = 0.9;
    
    public static int breakTooClose = 0;
    
    /**
     * @param args Keine Argumente notwendig
     */
    public static void main(String[] args) {
        //Logs löschen
        File dir = new File("positions/data/");
        for(File file: dir.listFiles()) {
            if (!file.isDirectory()) file.delete();
        }
        dir = new File("positions/images/");
        for(File file: dir.listFiles()) {
            if (!file.isDirectory()) file.delete();
        }
        //Dateien zum Speichern der Logs erstellen
        try {
            positionFile = new FileWriter("distance.dat");
            velocityFile = new FileWriter("velocity.dat");
            distanceToCarInFrontFile = new FileWriter("distanceToCarInFrontFile.dat");
        } catch(IOException e) {
            System.out.println("Fehler beim Erstellen der Datei!");
            return;
        }
        //Streckenabschnitte der Simulationsstrecke hinzufügen
        strecke.addStreckenabschnitt(new Streckenabschnitt(0, 14));
        strecke.addStreckenabschnitt(new Streckenabschnitt(1000, 28));
        strecke.addStreckenabschnitt(new Streckenabschnitt(5000, 8));
        strecke.addStreckenabschnitt(new Streckenabschnitt(8000, 28));
        
        
        
        writeTempolimit();
        
        //Autos erstellen und entsprechend positionieren
        for(int i = 0; i < N; i++) {
            autos.add(new Auto(i * -ABSTAND, i));
        }
        
        //Counter um Häufigkeit der Logs zu verändern
        int c = 0;
        //Simulation bis das letzte Auto bei 10km angelangt ist oder ein Unfall passiert ist
        while(autos.get(N - 1).getPosition() <= STRECKENENDE && !unfall) {
            //Jedes Auto soll fahren
            for(Auto auto : autos) {
                auto.fahren();
            }
            
            //Logs schreiben
            if(c % 20 == 0) {
                //logDistances();
                logVelocity(autos.get(N - 1));
            }
            if(c % 1000 == 0) {
                logPositions();
            }
            //Zeit und Counter hochzählen
            t += dt;
            c++;
        }
        System.out.println(breakTooClose);
        //Log-Dateien schließen
        try {
            distanceToCarInFrontFile.close();
            positionFile.close();
            velocityFile.close();
        } catch(IOException e) {
            System.out.println("Fehler beim Schließen der Datei!");
        }        
    }   
    
    /**
     * Distanzen zwischen den einzelnen Autos in einer Datei schreiben
     */        
    public static void logDistances() {
        double distance[] = new double[N];
        StringBuilder sb = new StringBuilder(String.valueOf(t));
        for(int i = 0; i < N - 1; i++) {
            distance[i] = autos.get(i + 1).getPosition() - autos.get(i).getPosition();
            sb.append("\t").append(distance[i]);
        }
        sb.append("\n");
        try {
            positionFile.write(sb.toString());
        } catch(IOException e) {
            System.out.println("Fehler beim Schreiben der Datei!");
        }        
    }
    
    /**
     * 
     * @param auto Geschwindigkeit eines gewissen Autos loggen
     */
    public static void logVelocity(Auto auto) {
        try {
            distanceToCarInFrontFile.write(auto.getPosition() + "\t" + (auto.getCarInFront().getPosition() - auto.getPosition()) + "\t" + auto.getTempolimit() + "\n");
            velocityFile.write(auto.getPosition() + "\t" + auto.getVelocity() + "\t" + auto.getTempolimit() + "\n");
        } catch(IOException e) {
            System.out.println("Fehler beim Schreiben der Datei!");
        }        
    }
    
    public static int fileNumber = 0;
    
    public static void logPositions() {
        try {
            Writer w = new FileWriter("positions/data/positions" + (fileNumber++) + ".dat");
            for(Auto auto : autos) {
                w.write(auto.getPosition() + "\t" + auto.getVelocity() + "\n");
            }
            w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        
    public static void writeTempolimit() {
        try {
            FileWriter w = new FileWriter("positions/tempolimit.dat");
            Streckenabschnitt previousAbschnitt = null;
            for(Streckenabschnitt s : strecke.getAbschnitte()) {                            
                if(previousAbschnitt != null) w.write(s.getBeginn() + "\t" + previousAbschnitt.getTempolimit() + "\n");
                w.write(s.getBeginn() + "\t" + s.getTempolimit() + "\n");                   
                previousAbschnitt = s;
            }
            w.write(STRECKENENDE + "\t" + previousAbschnitt.getTempolimit() + "\n");
            w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
