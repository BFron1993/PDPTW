
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {

    public static void main(String[] args) {
        ConfigurationReader reader = new ConfigurationReader();
        Configuration config = null;
        try {
            config = reader.readConfig("C:\\benchmarks\\pdp_100\\lc101.txt");
        } 
        catch (IOException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Solution solution = new LargeNeighbourhoodAlgorithm().run(config);
        solution.printSchedules();
        System.out.println("Cost: " + solution.getCost());
    }
}
