import java.util.List;

/**
 * Created by Anna on 2015-05-30.
 */
public class LargeNeighbourhoodAlgorithm implements IAlgorithm {

    @Override
    public Solution run(Configuration configuration) {
        double lambdaParameter = 1.0;
        Solution solution = new SolomonInsertion().getInitialSolution(configuration, lambdaParameter);
        Solution bestSolution = solution;



        return solution;
    }

}
