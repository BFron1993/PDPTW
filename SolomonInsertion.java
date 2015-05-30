import java.util.ArrayList;
import java.util.List;

public class SolomonInsertion {

    private Commission findMostExpensiveCommission(List<Commission> commissions, Location baseLocation) {
        int leaderIndex = 0;
        double maxPath = commissions.get(leaderIndex).getPickupDeliveryPathLength(baseLocation);
        double current = maxPath;

        for (int i = 1; i < commissions.size(); i++) {
            current = commissions.get(i).getPickupDeliveryPathLength(baseLocation);
            if (current > maxPath) {
                maxPath = current;
                leaderIndex = i;
            }
        }
        return commissions.get(leaderIndex);
    }

    private List<Commission> createUnroutedList(List<Commission> commissions) {
        List<Commission> unrouted = new ArrayList<Commission>();
        for(Commission commission : commissions) {
            unrouted.add(commission);
        }
        return unrouted;
    }

    private List<InsertProperties> getCandidatesForInsertionAndTryToRemove(Solution solution, List<Commission> unrouted, int scheduleIndex) {
        List<InsertProperties> candidatesForInsertion = new ArrayList<InsertProperties>();
        Double minimalCost = null;
        InsertProperties bestProperties = null;
        Commission commissionToRemove = null;
        for (Commission commission : unrouted) {
            InsertProperties properties = solution.countCostOfInsert(commission, scheduleIndex);
            if (properties != null) {
                candidatesForInsertion.add(properties);
                if(minimalCost == null || minimalCost > properties.cost) {
                    minimalCost = properties.cost;
                    bestProperties = properties;
                    commissionToRemove = commission;
                }
            }
        }

        if (minimalCost != null) {
            solution.addCommission(bestProperties, commissionToRemove);
            unrouted.remove(commissionToRemove);
        }

        return candidatesForInsertion;
    }

    public Solution getInitialSolution(Configuration configuration) {
        int timeLimit = configuration.getTimeLimit();
        int capacity = configuration.getCapacity();
        List<Commission> commissions = configuration.getCommissions();
        Location baseLocation = configuration.getBaseLocation();

        Solution solution = new Solution(baseLocation,
                timeLimit, capacity);

        List<Commission> unrouted = createUnroutedList(commissions);
        Commission leader;
        int scheduleIndex;

        while(!unrouted.isEmpty()) {
            leader = findMostExpensiveCommission(unrouted, baseLocation);
            scheduleIndex = solution.getNewSchedule();
            solution.addCommission(solution.countCostOfInsert(leader, scheduleIndex), leader);
            unrouted.remove(leader);

            List<InsertProperties> candidatesForInsertion = getCandidatesForInsertionAndTryToRemove(solution, unrouted, scheduleIndex);
            while(!candidatesForInsertion.isEmpty()) {
                candidatesForInsertion = getCandidatesForInsertionAndTryToRemove(solution, unrouted, scheduleIndex);
            }
        }

        return solution;
    }
    
}
