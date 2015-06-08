package pl.edu.agh.pdptw;

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

    private double getC2(double lambdaParameter, Commission commission, Location baseLocation, double c1) {
        return lambdaParameter * commission.getPickupDeliveryPathLength(baseLocation) - c1;
    }

    private List<InsertProperties> getCandidatesForInsertionAndTryToRemove(Solution solution, List<Commission> unrouted,
                                                                           int scheduleIndex, Location baseLocation,
                                                                           double lambdaParameter) {
        List<InsertProperties> candidatesForInsertion = new ArrayList<InsertProperties>();
        Double maximalC2 = null;
        Double currentC2;
        InsertProperties bestProperties = null;
        Commission commissionToRemove = null;
        for (Commission commission : unrouted) {
            InsertProperties properties = solution.countCostOfInsert(commission, scheduleIndex);
            if (properties != null) {
                candidatesForInsertion.add(properties);
                currentC2 = getC2(lambdaParameter, commission, baseLocation, properties.cost);
                if(maximalC2 == null || maximalC2 < currentC2) {
                    maximalC2 = currentC2;
                    bestProperties = properties;
                    commissionToRemove = commission;
                }
            }
        }

        if (maximalC2 != null) {
            solution.addCommission(bestProperties, commissionToRemove);
            unrouted.remove(commissionToRemove);
        }

        return candidatesForInsertion;
    }

    public Solution getInitialSolution(Configuration configuration, double lambdaParameter) {
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

            List<InsertProperties> candidatesForInsertion = getCandidatesForInsertionAndTryToRemove(solution, unrouted,
                    scheduleIndex, baseLocation, lambdaParameter);
            while(!candidatesForInsertion.isEmpty()) {
                candidatesForInsertion = getCandidatesForInsertionAndTryToRemove(solution, unrouted,
                        scheduleIndex, baseLocation, lambdaParameter);
            }
        }

        return solution;
    }
    
}
