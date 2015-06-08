package pl.edu.agh.pdptw;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Anna on 2015-06-08.
 */
public class AlgorithmLauncher {

    private IAlgorithm algorithm;

    public Solution computeSolution() {
        ConfigurationReader reader = new ConfigurationReader();
        Configuration config = null;
        try {
//            config = reader.readConfig("D:\\pdptw-algo-test\\benchmarks\\pdp_100\\lc101.txt");
//            config = reader.readConfig("C:\\benchmarks\\1000\\LC1101.txt");
            config = reader.readConfig("C:\\benchmarks\\pdp_100\\lc101.txt");
        }
        catch (IOException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        // tutaj wybieranie algosa
        algorithm = new LargeNeighbourhoodAlgorithm();

        Solution solution = algorithm.run(config, 2000); // second arg is number of iterations
        return solution;
    }

    public String getCurrentSolution() {
        return algorithm.getCurrentSolution();
    }
}
