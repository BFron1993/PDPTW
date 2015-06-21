package pl.edu.agh.pdptw.webapp;

import pl.edu.agh.pdptw.solver.algorithm.BruteForce2;
import pl.edu.agh.pdptw.solver.configuration.ConfigurationReader;
import pl.edu.agh.pdptw.solver.algorithm.LargeNeighbourhoodAlgorithm;
import pl.edu.agh.pdptw.Launcher;
import pl.edu.agh.pdptw.solver.algorithm.IAlgorithm;
import pl.edu.agh.pdptw.solver.configuration.Configuration;
import pl.edu.agh.pdptw.solver.solution.Solution;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Anna on 2015-06-08.
 */

public class AlgorithmLauncher extends Thread {

    private IAlgorithm algorithm;
    private InitConfiguration config;

    public synchronized Solution computeSolution() {
        ConfigurationReader reader = new ConfigurationReader();
        Configuration config = null;
        try {
            config = reader.readConfig(this.config.getConfig());
        }
        catch (IOException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (this.config.getAlgorithm()) {
            case 0:
                algorithm = new LargeNeighbourhoodAlgorithm();
                break;
            case 1:
                algorithm = new BruteForce2();
                break;
            default:
                algorithm = new LargeNeighbourhoodAlgorithm();
        }

        Solution solution = algorithm.run(config, this.config.getNumberOfIterations()); // second arg is number of iterations
        return solution;
    }

    public String getCurrentSolution() {
        if (algorithm != null) {
            return algorithm.getCurrentSolution();
        } else return null;
    }

    @Override
    public void run() {
        computeSolution();
    }

    public void setConfig(InitConfiguration config) {
        this.config = config;
    }
}
