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

    public Solution getInitialSolution(Configuration configuration) {
        int timeLimit = configuration.getTimeLimit();
        int capacity = configuration.getCapacity();
        List<Commission> commissions = configuration.getCommissions();
        Location baseLocation = configuration.getBaseLocation();

        Solution solution = new Solution(baseLocation,
                timeLimit, capacity);

        Commission leader = findMostExpensiveCommission(commissions, baseLocation);
        System.out.println(leader.getId());

        return solution;
    }
    
}
