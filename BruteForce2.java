package pl.edu.agh.pdptw;

import com.rits.cloning.Cloner;
import pl.edu.agh.pdptw.IAlgorithm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BruteForce2 implements IAlgorithm {

    @Override
    public Solution run(Configuration configuration, int numberOfIterations) 
    {
        final int TIMES = 0;
        configuration.ShuffleCommissions();
        Solution best = buildSolution(configuration);
        for (int i = 0; i < TIMES; i++) {
            configuration.ShuffleCommissions();
            Solution result = buildSolution(configuration);
            if (result.getCost() < best.getCost()) {
                best = result;
            }
            //System.out.println(i + " " + result.getCost());
        }
        return  best;
    }

    @Override
    public String getCurrentSolution() {
        return null;
    }

    private Solution buildSolution(Configuration configuration) {
        Solution solution = new Solution(configuration.getBaseLocation(), configuration.getTimeLimit(), configuration.getCapacity());
        List<Integer> listOfSchedules = new LinkedList<>();
        List<Commission> commissions = configuration.getCommissions();
        for (Commission commission : commissions) {
            listOfSchedules.add(solution.getNewSchedule());
            CommissionViewAndCost best = null;
            for (int i : listOfSchedules) {
                CommissionViewAndCost result = solution.tryToAddCommissionBruteForce(commission, i);
                if (best == null) 
                {
                    best = result;
                }
                else if (result != null && result.cost < best.cost) 
                {
                     best = result;
                }
            }
            if (best == null) 
            {
               int newSchedule = solution.getNewSchedule();
               InsertProperties properties = solution.countCostOfInsert(commission, newSchedule);
               solution.addCommission(properties, commission);
               listOfSchedules.add(newSchedule);
            }
            else
            {
                solution.addCommission(best, commission);
            }
        }
        
        System.out.println(solution.getCost() +  " " + solution.getNumberOfHolons());
        Cloner cloner=new Cloner();
        Solution bestHolons = cloner.deepClone(solution);
        Solution bestDistance = cloner.deepClone(solution);
        
        int maxIterationWithNoBetterSolution =  solution.getNumberOfHolons();
        int iterationsWithNoBetterSolution = 0;
        boolean stop = false;
        
        List<Integer> listOfOldSchedules = new LinkedList<>();
        
        while(!stop){
            afterFirstBuild(listOfSchedules, solution, listOfOldSchedules);
            if (solution.getNumberOfHolons() < bestHolons.getNumberOfHolons()){
                bestHolons = cloner.deepClone(solution);
                iterationsWithNoBetterSolution = 0;
                System.out.println(solution.getCost() +  " " + solution.getNumberOfHolons());
            }
            else if (solution.getNumberOfHolons() == bestHolons.getNumberOfHolons())
            {
               if (solution.getCost() < bestHolons.getCost()) {
                    bestHolons = cloner.deepClone(solution);
                    iterationsWithNoBetterSolution = iterationsWithNoBetterSolution < 0 ? iterationsWithNoBetterSolution : 0;
                    System.out.println(solution.getCost() +  " " + solution.getNumberOfHolons());
               }
               else if (++iterationsWithNoBetterSolution == maxIterationWithNoBetterSolution) {
                stop = true;
               }
            }
            else if (++iterationsWithNoBetterSolution == maxIterationWithNoBetterSolution) {
                stop = true;
            }
            if (solution.getCost() < bestDistance.getCost()) {
                bestDistance = cloner.deepClone(solution);
            }
        }    
        
        System.out.println(bestDistance.getCost() +  " " + bestDistance.getNumberOfHolons());
        
        return bestHolons;
    }

    private void afterFirstBuild(List<Integer> listOfSchedules, Solution solution, List<Integer> listOfOldSchedules) {
        List<Commission> commissions;
        if (listOfOldSchedules.isEmpty()) 
        {
            Cloner cloner=new Cloner();
            listOfOldSchedules = cloner.deepClone(listOfSchedules);
            Collections.shuffle(listOfOldSchedules);
        }
            int x = listOfOldSchedules.remove(0);
            
            commissions = solution.removeSchedule(x);
            Collections.shuffle(commissions);
            listOfSchedules.remove((Integer) x);
            for (Commission commission : commissions) {
            CommissionViewAndCost best = null;
            for (int i : listOfSchedules) {
                CommissionViewAndCost result = solution.tryToAddCommissionBruteForce(commission, i);
                if (best == null) 
                {
                    best = result;
                }
                else if (result != null && result.cost < best.cost) 
                {
                     best = result;
                }
            }
            if (best == null) 
            {
               int newSchedule = solution.getNewSchedule();
               InsertProperties properties = solution.countCostOfInsert(commission, newSchedule);
               solution.addCommission(properties, commission);
               listOfSchedules.add(newSchedule);
            }
            else
            {
                solution.addCommission(best, commission);
            }
        }
    }
}
