
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Solution bestSolution = getInitialSolution(configuration);
        System.out.println(bestSolution.getCost());
        double qParameter = 0.4; // percent of removed commissions
        int numberOfCommissionsToRemove = (int) (qParameter * configuration.getCommissions().size());

        Solution solutionPrim = cloner.deepClone(bestSolution); // deep clone of initial solution
        Solution solutionPrimPrim;
        int i = 0;
        while (i < numberOfIterations) {
            boolean foundCommissionWithNoWayToInsert = false;
            solutionPrimPrim = cloner.deepClone(solutionPrim);

//            List<Commission> commissionsToAdd = randomRemoval(solutionPrimPrim, numberOfCommissionsToRemove, configuration.getCommissions());
            List<Commission> commissionsToAdd = randomRemoval(solutionPrimPrim, numberOfCommissionsToRemove, configuration.getCommissions());
            // tutaj dodawanie do solution prim prim
            // regret heuristic

            while(!commissionsToAdd.isEmpty() && !foundCommissionWithNoWayToInsert) {
                Commission commissionToRemove = null;
                InsertProperties bestInsertProperties = null;
                Double maxCostDifference = null;
                for (Commission commission : commissionsToAdd) {
                    InsertProperties firstBestInsertProperties = null, secondBestInsertProperties = null;
                    for(Integer indexOfSchedule : solutionPrimPrim.getIndexScheduleMap().keySet()) {
                        InsertProperties properties = solutionPrimPrim.countCostOfInsert(commission, indexOfSchedule);
                        if (properties != null) {
                            if (firstBestInsertProperties == null) {
                                firstBestInsertProperties = properties;
                            } else if(secondBestInsertProperties == null) {
                                if(properties.cost < firstBestInsertProperties.cost) {
                                    secondBestInsertProperties = firstBestInsertProperties;
                                    firstBestInsertProperties = properties;
                                } else {
                                    secondBestInsertProperties = properties;
                                }
                            } else {
                                if (properties.cost < firstBestInsertProperties.cost) {
                                    secondBestInsertProperties = firstBestInsertProperties;
                                    firstBestInsertProperties = properties;
                                } else if (properties.cost < secondBestInsertProperties.cost) {
                                    secondBestInsertProperties = properties;
                                }
                            }

                        }
                    }
                    if(secondBestInsertProperties == null && firstBestInsertProperties != null) {
                        commissionToRemove = commission;
                        bestInsertProperties = firstBestInsertProperties;
//                        maxCostDifference = -1.0;
                        break;
                    } else if (secondBestInsertProperties == null && firstBestInsertProperties == null){
                        foundCommissionWithNoWayToInsert = true;
                    } else if (bestInsertProperties == null || (secondBestInsertProperties.cost - firstBestInsertProperties.cost) > maxCostDifference) {
                        commissionToRemove = commission;
                        bestInsertProperties = firstBestInsertProperties;
                        maxCostDifference = secondBestInsertProperties.cost - firstBestInsertProperties.cost;
                    }
                }
                if(!foundCommissionWithNoWayToInsert) {
                    commissionsToAdd.remove(commissionToRemove);
                    solutionPrimPrim.addCommission(bestInsertProperties, commissionToRemove);
                }
            }
            if(foundCommissionWithNoWayToInsert) {
                solutionPrimPrim = cloner.deepClone(solutionPrim);
                continue;
            }


            double primPrimCost = solutionPrimPrim.getCost();
            double primCost = solutionPrim.getCost();
            double bestCost = bestSolution.getCost();
            System.out.println(bestCost);
            if (primPrimCost < bestCost) {
                bestSolution = cloner.deepClone(solutionPrimPrim);
            }

            if (primPrimCost < primCost) {
                solutionPrim = cloner.deepClone(solutionPrimPrim);
            }
            i++;
        }
        return bestSolution;
    }

    private List<Commission> removalOfWorst(Solution solution, int numberOfCommissionsToRemove, List<Commission> commissions) {

        return null;
    }


    private List<Commission> randomRemoval (Solution solution, int numberOfCommissionsToRemove, List<Commission> commissions){
        List<Commission> usedComissions = new ArrayList<Commission>();
        int size = commissions.size();
        for (int i = 0; i < numberOfCommissionsToRemove; i++) {
            Commission commission = commissions.get(randomGenerator.nextInt(size));
            while (usedComissions.contains(commission)) {
                commission = commissions.get(randomGenerator.nextInt(size));
            }
            usedComissions.add(commission);
            solution.removeCommission(commission);
        }
        return usedComissions;
    }


    private Solution getInitialSolution(Configuration configuration) {
        Solution bestSolution = new SolomonInsertion().getInitialSolution(configuration, 0.9);
        double bestCost = bestSolution.getCost();
        for (double i = 0.91; i <= 1.1; i = i + 0.01) {
            Solution solution = new SolomonInsertion().getInitialSolution(configuration, i);
            double cost = solution.getCost();
            if (cost < bestCost) {
                bestSolution = solution;
                bestCost = cost;
            }
        }
        return bestSolution;
    }
}