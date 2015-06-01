import java.util.List;

/**
 * Created by Anna on 2015-05-30.
 */
public class LargeNeighbourhoodAlgorithm implements IAlgorithm {

    @Override
    public Solution run(Configuration configuration) {
        Solution bestSolution = new SolomonInsertion().getInitialSolution(configuration, 0.9);
        double bestCost = bestSolution.getCost();
        //double bestLambda = 0.0;
        //System.out.println("Lambda: " + 0.0 + " Cost: " + bestCost);
        for(double i = 0.91; i <= 1.1 ; i=i+0.01)
        {
            Solution solution = new SolomonInsertion().getInitialSolution(configuration, i);
            double cost = solution.getCost();
            if (cost < bestCost) {
                bestSolution = solution;
                bestCost = cost;
                //bestLambda = i;
            }
            //System.out.println("Lambda: " + i + " Cost: " + cost);
        }
        //System.out.println("The best lambda is:" + bestLambda);
        return bestSolution;
    }

}
