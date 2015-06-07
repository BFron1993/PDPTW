
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    
    private Map<Integer, Integer> commissionScheduleMap =  new HashMap<Integer, Integer>();
    private Map<Integer, Schedule> indexScheduleMap = new HashMap<Integer, Schedule>();
    private Location base;
    private int timeLimit;
    private int capacity;
            
    public Solution(Location base, int timeLimit, int capacity)
    {
        this.base = base;
        this.timeLimit = timeLimit;
        this.capacity = capacity;
    }

    public Map<Integer, Schedule> getIndexScheduleMap() {
        return indexScheduleMap;
    }

    public int getNumberOfHolons() {
        return indexScheduleMap.size();
    }

    public void checkIfEmptySchedulesAndTryToRemove() {
        List<Integer> schedulesToRemove = new ArrayList<>();
        for (Integer scheduleIndex : indexScheduleMap.keySet()) {
            int sum = 0;
            for (Integer commissionIndex : commissionScheduleMap.keySet()) {
                if (commissionScheduleMap.get(commissionIndex).equals(scheduleIndex)) {
                    sum += 1;
                }
            }
            if (sum == 0) {
//                System.out.println("znalaz³o!");
                schedulesToRemove.add(scheduleIndex);
            }
        }
        for(Integer scheduleId : schedulesToRemove) {
            this.removeSchedule(scheduleId);
        }
    }
    
    public int getNewSchedule()
    {
        Schedule newSchedule = new Schedule(this.capacity, this.timeLimit, this.base);
        this.indexScheduleMap.put(newSchedule.getIndex(), newSchedule);
        return newSchedule.getIndex();
    }
    
    public InsertProperties countCostOfInsert(Commission commission, Integer indexOfSchedule)
    {
        if (indexOfSchedule != null)
        {
            Schedule schedule = this.indexScheduleMap.get(indexOfSchedule);
            return schedule.getBestInsertion(commission);
        }
        else
        {
            InsertProperties bestInsertProperties = null;
            
            for(Schedule x :indexScheduleMap.values())
            {
                InsertProperties tmpProperties = x.getBestInsertion(commission);
                if (bestInsertProperties == null || bestInsertProperties.cost > tmpProperties.cost) 
                {
                    bestInsertProperties = tmpProperties;
                }
            }
            return bestInsertProperties;
        }
    }
    
    public void addCommission(InsertProperties properties, Commission commission)
    {
        this.commissionScheduleMap.put(commission.getId(), properties.scheduleIndex);
        this.indexScheduleMap.get(properties.scheduleIndex).addCommissionPrecisively(properties, commission);
    }
    
    public void printSchedules()
    {
        for(Schedule x : indexScheduleMap.values())
        {
            System.out.println("Holon " + x.getIndex());
            x.printIntervals();
            System.out.println("-----------------------------------------------------------------");
        }
    }

    public double getCost() {
        double sum = 0.0;
        for (Schedule schedule : indexScheduleMap.values()) {
            sum += schedule.countCost();
        }
        return sum;
    }
    
    public void removeCommission(Commission commission)
    {
        int commissionId = commission.getId();
        int scheduleId = this.commissionScheduleMap.get(commissionId);
        Schedule scheduleToRemoveFrom = this.indexScheduleMap.get(scheduleId);
        scheduleToRemoveFrom.removeCommission(commissionId);
        this.commissionScheduleMap.remove(commissionId);
    }
    
    public CommissionViewAndCost tryToAddCommissionBruteForce(Commission commission, int indexOfSchedule)
    {
        Schedule schedule = this.indexScheduleMap.get(indexOfSchedule);
        CommissionViewAndCost ret = schedule.tryToAddBruteForce(commission);
        return ret;
    }

    void addCommission(CommissionViewAndCost best, Commission commission) 
    {
        this.commissionScheduleMap.put(commission.getId(), best.scheduleId);
        this.indexScheduleMap.get(best.scheduleId).addCommissionPrecisively(best, commission);
    }
    
    public List<Commission> removeSchedule(int schduleIndex)
    {
        Schedule schedule = this.indexScheduleMap.get(schduleIndex);
        this.indexScheduleMap.remove(schduleIndex);
        List<Commission> retCommissions = schedule.getCommissions();
        for(Commission x : retCommissions)
        {
            this.commissionScheduleMap.remove(x.getId());
        }
        return retCommissions;
    }
    
}
