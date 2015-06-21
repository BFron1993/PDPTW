package pl.edu.agh.pdptw.solver.solution.interval;

import pl.edu.agh.pdptw.solver.configuration.Location;

public class Interval
{
    public double begin;
    public double end;
    public double goingEnd;
    public IntervalType type;
    public int commissionIndex;
    public int demand;
    public Location location;
    
    public Interval(double begin, double goingEnd, double end, IntervalType type, int commissionIndex, int demand, Location location)
    {
        this.begin = begin;
        this.end = end;
        this.goingEnd = goingEnd;
        this.type = type;
        this.commissionIndex = commissionIndex;
        this.demand = demand;
        this.location = location;
    }
}
