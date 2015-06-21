package pl.edu.agh.pdptw;

import pl.edu.agh.pdptw.solver.algorithm.LargeNeighbourhoodAlgorithm;
import pl.edu.agh.pdptw.solver.configuration.Configuration;
import pl.edu.agh.pdptw.solver.configuration.ConfigurationReader;
import pl.edu.agh.pdptw.solver.solution.Solution;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {

    public static void main(String[] args) {
        ConfigurationReader reader = new ConfigurationReader();
        Configuration config = null;
        try {
//            config = reader.readConfig("D:\\pdptw-algo-test\\benchmarks\\pdp_100\\lc101.txt");
//            config = reader.readConfig("C:\\benchmarks\\1000\\LC1101.txt");
            config = reader.readConfig("C:\\benchmarks\\pdp_200\\LC1_2_2.txt");
        }
        catch (IOException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Solution solution = new LargeNeighbourhoodAlgorithm().run(config, 2000); // second arg is number of iterations
        solution.printSchedules();
        System.out.println("Cost: " + solution.getCost());
        System.out.println("Number of holons: " + solution.getNumberOfHolons());
    }
}
