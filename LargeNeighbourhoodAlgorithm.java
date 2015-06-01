import cloning.java.com.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import cloning.java.com.rits.

/**
 * Created by Anna on 2015-05-30.
 */
public class LargeNeighbourhoodAlgorithm implements IAlgorithm {

    private Cloner cloner;
    private Random randomGenerator;

    public LargeNeighbourhoodAlgorithm() {
        this.cloner = new Cloner();
        this.randomGenerator = new Random();
    }

    @Override
    public Solution run(Configuration configuration, int numberOfIterations) {

        double lambdaParameter = 1.0;
        double qParameter = 0.3; // percent of removed commissions
        int numberOfCommissionsToRemove = (int)qParameter * configuration.getCommissions().size();
        Solution bestSolution = new SolomonInsertion().getInitialSolution(configuration, lambdaParameter);
        Solution solutionPrim = cloner.deepClone(bestSolution); // deep clone of initial solution
        Solution solutionPrimPrim;
        int i = 0;
        while (i < numberOfIterations) {
            solutionPrimPrim = cloner.deepClone(solutionPrim);

            List<Commission> commissionsToAdd = randomRamoval(solutionPrimPrim, numberOfCommissionsToRemove, configuration.getCommissions());

            // tutaj dodawanie do solution prim prim
            // regret heuristic
            for (Commission commission : commissionsToAdd) {
                InsertProperties properties = solutionPrimPrim.countCostOfInsert(commission, null);

            }

            double primPrimCost = solutionPrimPrim.getCost();
            double primCost = solutionPrim.getCost();
            double bestCost = bestSolution.getCost();
            if (primPrimCost < bestCost) {
                bestSolution = solutionPrimPrim;
            }

            if (primPrimCost < primCost) {
                solutionPrim = solutionPrimPrim;
            }
            i++;

        }
        return bestSolution;
    }

    private List<Commission> randomRamoval(Solution solution, int numberOfCommissionsToRemove, List<Commission> commissions) {
        List<Commission> usedComissions = new ArrayList<Commission>();
        int size = commissions.size();
        for(int i = 0; i < numberOfCommissionsToRemove; i++) {
            Commission commission = commissions.get(randomGenerator.nextInt(size));
            while(usedComissions.contains(commission)) {
                commission = commissions.get(randomGenerator.nextInt(size));
            }
            usedComissions.add(commission);
            solution.removeCommission(commission);
        }
        return usedComissions;
    }
}
