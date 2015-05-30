import java.util.List;

/**
 * Created by Anna on 2015-05-30.
 */
public class LargeNeighbourhoodAlgorithm implements IAlgorithm {

    @Override
    public Solution run(Configuration configuration) {
        Solution solution = new SolomonInsertion().getInitialSolution(configuration);


//        int scheduleId = solution.getNewSchedule();
//
//        Iterator<Commission> iterator = configuration.getCommissions().iterator();
//        while (iterator.hasNext()) {
//            Commission commission = iterator.next();
////            System.out.println(commission.getId());
//            InsertProperties properties = solution.countCostOfInsert(commission, scheduleId);
//            if (properties != null) {
//                solution.addCommission(properties, commission);
//            }
//        }


        return solution;
    }

}
