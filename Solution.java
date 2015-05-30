
import java.util.HashMap;
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
}
