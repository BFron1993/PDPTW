package pl.edu.agh.pdptw.solver.algorithm;

import com.rits.cloning.Cloner;
import org.codehaus.jackson.map.ObjectMapper;
import pl.edu.agh.pdptw.solver.configuration.*;
import pl.edu.agh.pdptw.solver.solution.CommissionViewAndCost;
import pl.edu.agh.pdptw.solver.solution.InsertProperties;
import pl.edu.agh.pdptw.solver.solution.Solution;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BruteForce2 implements IAlgorithm {

    private Solution bestHolons = null;
    private int currentIteration = 0;
    private int maxIteration;

    @Override
    public Solution run(Configuration configuration, int numberOfIterations)
    {
        this.maxIteration = numberOfIterations;
        configuration.ShuffleCommissions();
        Solution best = buildSolution(configuration);
        return  best;
    }

    @Override
    public String getCurrentSolution() {
        ObjectMapper mapper = new ObjectMapper();
        Object[] step = {this.currentIteration, this.bestHolons};
        try {
            return mapper.writeValueAsString(step);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getIteration() {
        return this.currentIteration;
    }

    private Solution buildSolution(Configuration configuration) {
        Solution solution = new Solution(configuration.getBaseLocation(), configuration.getTimeLimit(), configuration.getCapacity());
        List<Integer> listOfSchedules = new LinkedList<>();
        List<Commission> commissions = configuration.getCommissions();
        for (Commission commission : commissions) {
            listOfSchedules.add(solution.createNewSchedule());
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
               int newSchedule = solution.createNewSchedule();
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
        this.bestHolons = cloner.deepClone(solution);
        Solution bestDistance = cloner.deepClone(solution);

        boolean stop = false;
        
        List<Integer> listOfOldSchedules = new LinkedList<>();
        
        while(!stop){
            afterFirstBuild(listOfSchedules, solution, listOfOldSchedules);
            if (solution.getNumberOfHolons() < bestHolons.getNumberOfHolons()){
                bestHolons = cloner.deepClone(solution);
                System.out.println(solution.getCost() +  " " + solution.getNumberOfHolons());
            }
            else if (solution.getNumberOfHolons() == bestHolons.getNumberOfHolons())
            {
               if (solution.getCost() < bestHolons.getCost()) {
                   bestHolons = cloner.deepClone(solution);
                   System.out.println(solution.getCost() + " " + solution.getNumberOfHolons());
               }
            }
            if (solution.getCost() < bestDistance.getCost()) {
                bestDistance = cloner.deepClone(solution);
            }
            if (++this.currentIteration == this.maxIteration )
            {
                stop = true;
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
               int newSchedule = solution.createNewSchedule();
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
