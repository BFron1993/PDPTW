package pl.edu.agh.pdptw.solver.algorithm;

import pl.edu.agh.pdptw.solver.configuration.Configuration;
import pl.edu.agh.pdptw.solver.solution.Solution;

/**
 * Created by Anna on 2015-05-30.
 */
public interface IAlgorithm {

    public Solution run(Configuration configuration, int numberOfIterations);
    public String getCurrentSolution();
    public int getIteration();
}
