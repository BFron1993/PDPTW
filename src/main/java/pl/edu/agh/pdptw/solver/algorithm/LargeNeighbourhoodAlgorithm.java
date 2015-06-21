package pl.edu.agh.pdptw.solver.algorithm;

import com.rits.cloning.Cloner;
import org.codehaus.jackson.map.ObjectMapper;
import pl.edu.agh.pdptw.solver.algorithm.solomoninsertion.ShawRemovalComparator;
import pl.edu.agh.pdptw.solver.algorithm.solomoninsertion.SolomonInsertion;
import pl.edu.agh.pdptw.solver.configuration.Commission;
import pl.edu.agh.pdptw.solver.configuration.Configuration;
import pl.edu.agh.pdptw.solver.solution.InsertProperties;
import pl.edu.agh.pdptw.solver.solution.Solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Anna on 2015-05-30.
 */

public class LargeNeighbourhoodAlgorithm implements IAlgorithm {

    private Cloner cloner;
    private Random randomGenerator;
    private Solution bestSolution;
    private int iteration;

    public LargeNeighbourhoodAlgorithm() {
        this.cloner = new Cloner();
        this.randomGenerator = new Random();
        this.bestSolution = null;
        this.iteration = 0;
    }

    @Override
    public Solution run(Configuration configuration, int numberOfIterations) {
        bestSolution = getInitialSolution(configuration);
        double initialCost = bestSolution.getCost();
        System.out.println(initialCost);
        double qParameter = 0.4; // percent of removed commissions
        int numberOfCommissionsToRemove = (int) (qParameter * configuration.getCommissions().size());

        Solution solutionPrim = cloner.deepClone(bestSolution); // deep clone of initial solution
        Solution solutionPrimPrim;
        iteration = 0;
        while (iteration < numberOfIterations) {
            boolean foundCommissionWithNoWayToInsert = false;
            solutionPrimPrim = cloner.deepClone(solutionPrim);

            int choice = randomGenerator.nextInt(3);
            List<Commission> commissionsToAdd;
            switch (choice) {
                case 0: {
                    commissionsToAdd = randomRemoval(solutionPrimPrim, numberOfCommissionsToRemove, configuration.getCommissions());
                    break;
                }
                case 1: {
                    commissionsToAdd = scheduleRemoval(solutionPrimPrim);
                    break;
                }
                default: commissionsToAdd = shawRemoval(solutionPrimPrim, numberOfCommissionsToRemove, configuration.getCommissions());
            }

            // tutaj dodawanie do solution prim prim
            // regret heuristic

            while(!commissionsToAdd.isEmpty() && !foundCommissionWithNoWayToInsert) {
                Commission commissionToRemove = null;
                InsertProperties bestInsertProperties = null;
                Double maxCostDifference = null;
                boolean foundCommissionWithOneWayToInsert = false;
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
                        if(!foundCommissionWithOneWayToInsert ||
                                (bestInsertProperties != null && (firstBestInsertProperties.cost > bestInsertProperties.cost))) {
                            foundCommissionWithOneWayToInsert = true;
                            commissionToRemove = commission;
                            bestInsertProperties = firstBestInsertProperties;
                        }
                    } else if (!foundCommissionWithOneWayToInsert) {
                        if (secondBestInsertProperties == null && firstBestInsertProperties == null){
                            foundCommissionWithNoWayToInsert = true;
                        } else if (bestInsertProperties == null || (secondBestInsertProperties.cost - firstBestInsertProperties.cost) > maxCostDifference) {
                            commissionToRemove = commission;
                            bestInsertProperties = firstBestInsertProperties;
                            maxCostDifference = secondBestInsertProperties.cost - firstBestInsertProperties.cost;
                        }
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
            solutionPrimPrim.checkIfEmptySchedulesAndTryToRemove();

            double primPrimCost = solutionPrimPrim.getCost();
            double primCost = solutionPrim.getCost();
            double bestCost = bestSolution.getCost();
            System.out.println(bestCost + " " + bestSolution.getNumberOfHolons());
            if (solutionPrimPrim.getNumberOfHolons() < bestSolution.getNumberOfHolons() ||
                    (solutionPrimPrim.getNumberOfHolons() == bestSolution.getNumberOfHolons() && primPrimCost < bestCost)) {
                bestSolution = cloner.deepClone(solutionPrimPrim);
            }

            if (solutionPrimPrim.getNumberOfHolons() < solutionPrim.getNumberOfHolons() ||
                    (solutionPrimPrim.getNumberOfHolons() == solutionPrim.getNumberOfHolons() && primPrimCost < primCost)) {
                solutionPrim = cloner.deepClone(solutionPrimPrim);
            }
            iteration++;
        }
        return bestSolution;
    }

    @Override
    public String getCurrentSolution() {
        ObjectMapper mapper = new ObjectMapper();
        Object[] step = {this.iteration, this.bestSolution};
        try {
            return mapper.writeValueAsString(step);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    private Commission getRandomCommissionNotUsed(List<Commission> commissions, List<Commission> usedCommissions) {
        int size = commissions.size();
        Commission commission = commissions.get(randomGenerator.nextInt(size));
        while (usedCommissions.contains(commission)) {
            commission = commissions.get(randomGenerator.nextInt(size));
        }
        return commission;
    }

    private List<Commission> shawRemoval(Solution solution, int numberOfCommissionsToRemove, List<Commission> commissions) {
        List<Commission> commissionsToRemove = new ArrayList<Commission>();
        Commission commission = getRandomCommissionNotUsed(commissions, commissionsToRemove);
        commissionsToRemove.add(commission);
        solution.removeCommission(commission);
        while (commissions.size() < numberOfCommissionsToRemove) {
            commission = commissionsToRemove.get(randomGenerator.nextInt(commissionsToRemove.size()));
            List<Commission> commissionsNotToRemove = new ArrayList<Commission>();
            // creating array containing all requests from commissions not in commissions to remove
            for(Commission commissionToCheck : commissions) {
                if (!commissionsToRemove.contains(commissionToCheck)) {
                    commissionsNotToRemove.add(commissionToCheck);
                }
            }
            Collections.sort(commissionsNotToRemove, new ShawRemovalComparator(commission, 1.0, 1.0, 1.0));
            commission = commissionsNotToRemove.get(randomGenerator.nextInt(commissionsNotToRemove.size()));
            commissionsToRemove.add(commission);
            solution.removeCommission(commission);
        }

        return commissionsToRemove;
    }


    private List<Commission> randomRemoval (Solution solution, int numberOfCommissionsToRemove, List<Commission> commissions){
        List<Commission> usedComissions = new ArrayList<Commission>();
        int size = commissions.size();
        for (int i = 0; i < numberOfCommissionsToRemove; i++) {
            Commission commission = getRandomCommissionNotUsed(commissions, usedComissions);
            usedComissions.add(commission);
            solution.removeCommission(commission);
        }
        return usedComissions;
    }

    private List<Commission> scheduleRemoval(Solution solution) {
        Object[] scheduleIds = solution.getIndexScheduleMap().keySet().toArray();
        int randomId = (int) scheduleIds[randomGenerator.nextInt(scheduleIds.length)];
        List<Commission> usedCommissions = solution.removeSchedule(randomId);
        return usedCommissions;
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