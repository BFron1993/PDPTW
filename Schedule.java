
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Schedule {
    
    private static int index = 1;
    
    private int id;
    private int holonCapacity;
    private int timeLimit;
    private Location locationBase;
    private LinkedList<Interval> intervals = new LinkedList<>();
    
    public Schedule(int holonCapacity, int timeLimit, Location base)
    {
        this.id = index++;
        this.holonCapacity = holonCapacity;
        this.locationBase = base;
        this.timeLimit = timeLimit;
        Interval newInterval = new Interval(timeLimit, timeLimit, timeLimit, IntervalType.GOING_BACK_TO_BASE, 0, 0, this.locationBase);
        this.intervals.add(newInterval);
        this.reCountGoings();
    }
    
    public int getIndex()
    {
        return this.id;
    }
    
    public InsertProperties getBestInsertion(Commission commission)
    {
        double costBefore = countCost();
        
        InsertProperties bestScheduleProperties = null;
        Collections.sort(this.intervals, new IntervalComparer());
        Interval prev = null;
        
        Interval next;
        
        int listLength = intervals.size();
        int nowElementIndex = 1;
        
        while(nowElementIndex <= listLength)
        {
            Iterator<Interval> iterator = this.intervals.iterator();
            int i = 0;
            next = null;
            while(i < nowElementIndex)
            {
                prev = next;
                next = iterator.next();
                i++;
            }
            nowElementIndex ++;
            
            double insertIndex = checkIfInsertPossible(prev, next, commission);
            
            if(insertIndex > 0)
            {
                Interval prev2 = null;
                Interval next2 = null;
                int listLength2 = intervals.size();
                int nowElementIndex2 = 1;
                while(nowElementIndex2 <= listLength2)
                {
                    Iterator<Interval> iterator2 = this.intervals.iterator();
                    int j = 0;
                    next2 = null;
                    while(j < nowElementIndex2)
                    {
                        prev2 = next2;
                        next2 = iterator2.next();
                        j++;
                    }
                    nowElementIndex2 ++;
                 
                    if (prev2 != null && prev2.goingEnd >= insertIndex) {
                        double deliveryIndex = checkIfDeliveryPossible(prev2, next2, commission);
                        if (deliveryIndex >= 0){
                            reCountGoings();
                            if (checkIfCapacityOK()) {
                                double costNow = countCost();
                                double diffrenceCost = costNow - costBefore;
                                if (bestScheduleProperties == null || bestScheduleProperties.cost > diffrenceCost) {
                                    bestScheduleProperties = new InsertProperties();
                                    bestScheduleProperties.cost = diffrenceCost;
                                    bestScheduleProperties.pickupIndex = insertIndex;
                                    bestScheduleProperties.deliveryIndex = deliveryIndex;
                                    bestScheduleProperties.scheduleIndex = this.id;
                                }
                            }
                            removeDelivery(commission.getId());
                        }
                    }
                }
                removePickup(commission.getId());
            }    
            sortIntervals();
            reCountGoings();
        }
        return bestScheduleProperties;
    }
    
    private void reCountGoings()
    {
        Location prev = this.locationBase;
       
        sortIntervals();
        
        for(Interval x : intervals)
        {
            x.begin = x.goingEnd - prev.getDistanceTo(x.location);
            prev = x.location;
        }
    }

    private void sortIntervals() {
        Collections.sort(this.intervals, new IntervalComparer());
    }
    
    private double checkIfInsertPossible(Interval prev, Interval next, Commission commission) {
        double leftBorder = prev == null ? 0 : prev.end;
        Location prevLocation = prev == null ? this.locationBase : prev.location;
        
        double rightBorder = next.goingEnd;
        Location nextLocation = next.location;
        
        Location pickupLocation = commission.getPickupLocation();
        
        double timegoing1 = prevLocation.getDistanceTo(pickupLocation);
        double timegoing2 = pickupLocation.getDistanceTo(nextLocation);
        double serviceTime = commission.getPickupServiceTime();
        
        leftBorder += timegoing1;
        rightBorder -= timegoing2 + serviceTime;
        
        TimeWindow timeWindow = commission.getPickupTimeWindow();
        
        if (leftBorder <= rightBorder) {
            double indexToInsert = getIndexToInsertForPickup(leftBorder, rightBorder, timeWindow.begin, timeWindow.end);
            if (indexToInsert >= 0) 
            {
                Interval intervalToInsert = new Interval(indexToInsert, indexToInsert, indexToInsert + serviceTime, IntervalType.PICKUP, commission.getId(), commission.getPicupDemand(), commission.getPickupLocation());
                this.intervals.add(intervalToInsert);
                sortIntervals();
                return indexToInsert;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    public double countCost() {
        double ret = 0;
        
        for(Interval x : intervals)
        {
            ret += x.goingEnd - x.begin;
        }
        
        return ret;     
    }

    private double getIndexToInsertForPickup(double leftBorder, double rightBorder, double begin, double end) {
        double left = Math.max(leftBorder, begin);
        double right = Math.min(rightBorder, end);
        if (left <= right) 
        {
            return right;
        }
        else
        {
            return -1;
        }
    }

    private double checkIfDeliveryPossible(Interval prev2, Interval next2, Commission commission) {
        double leftBorder = prev2 == null ? 0 : prev2.end;
        Location prevLocation = prev2 == null ? this.locationBase : prev2.location;
        
        double rightBorder = next2.goingEnd;
        Location nextLocation = next2.location;
        
        Location deliveryLocation = commission.getDeliveryLocation();
        
        double timegoing1 = prevLocation.getDistanceTo(deliveryLocation);
        double timegoing2 = deliveryLocation.getDistanceTo(nextLocation);
        double serviceTime = commission.getDeliveryServiceTime();
        
        leftBorder += timegoing1;
        rightBorder -= timegoing2 + serviceTime;
        
        TimeWindow timeWindow = commission.getDeliveryTimeWindow();
        
        if (leftBorder <= rightBorder) {
            double indexToInsert = getIndexToInsertForDelivery(leftBorder, rightBorder, timeWindow.begin, timeWindow.end);
            if (indexToInsert >= 0) 
            {
                Interval intervalToInsert = new Interval(indexToInsert, indexToInsert, indexToInsert + serviceTime, IntervalType.DELIVERY, commission.getId(), commission.getDeliveryDemand(), commission.getDeliveryLocation());
                this.intervals.add(intervalToInsert);
                sortIntervals();
                return indexToInsert;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    private double getIndexToInsertForDelivery(double leftBorder, double rightBorder, double begin, double end) {
        double left = Math.max(leftBorder, begin);
        double right = Math.min(rightBorder, end);
        if (left <= right) 
        {
            return left;
        }
        else
        {
            return -1;
        }
    }

    private void removePickup(int id) {
        Iterator<Interval> iterator = this.intervals.iterator();
        while(iterator.hasNext())
        {
            Interval interval = iterator.next();
            if(interval.type == IntervalType.PICKUP && interval.commissionIndex == id)
            {
                iterator.remove();
                return;
            }
        }
        
        throw new IllegalArgumentException("Nie ma takeigo Id - pickup");
    }

    private void removeDelivery(int id) {
        Iterator<Interval> iterator = this.intervals.iterator();
        while(iterator.hasNext())
        {
            Interval interval = iterator.next();
            if(interval.type == IntervalType.DELIVERY && interval.commissionIndex == id)
            {
                iterator.remove();
                return;
            }
        }
        
        throw new IllegalArgumentException("Nie ma takeigo Id - delivery");
    }

    private boolean checkIfCapacityOK() {
        int capacity = 0;
        for(Interval x : this.intervals)
        {
            capacity += x.demand;
            if (capacity > this.holonCapacity)
            {
                return false;
            }
        }
        return true;
    }

    void addCommissionPrecisively(InsertProperties properties, Commission commission) {
        Interval insertInterval = new Interval(properties.pickupIndex, properties.pickupIndex, properties.pickupIndex + commission.getPickupServiceTime(), IntervalType.PICKUP, commission.getId(), commission.getPicupDemand(), commission.getPickupLocation());
        Interval deliveryInterval = new Interval(properties.deliveryIndex, properties.deliveryIndex, properties.deliveryIndex + commission.getDeliveryServiceTime(), IntervalType.DELIVERY, commission.getId(), commission.getDeliveryDemand(), commission.getDeliveryLocation());
        this.intervals.add(insertInterval);
        this.intervals.add(deliveryInterval);
        sortIntervals();
        reCountGoings();
    }

    void printIntervals() {
        sortIntervals();
        for(Interval x : this.intervals) {
            System.out.format("%5s%20s%20s%20s\n", x.commissionIndex, x.begin, x.end, x.type);
        }
    }
    
    void removeCommission(int commissionId)
    {
        removeDelivery(commissionId);
        removePickup(commissionId);
        sortIntervals();
        reCountGoings();
    }
    
    
}
