package pl.edu.agh.pdptw.solver.configuration;

public class ActionProxy {
    public int id;
    public int demand;
    public int x;
    public int y;
    public double beginTimeWindow;
    public double endTimeWindow;
    public int serviceTime;
    public int siblingIndex;
    
    public ActionProxy(int id, int x, int y, int demand, int begin, int end, int serviceTime, int siblingIndex)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.beginTimeWindow = begin;
        this.endTimeWindow = end;
        this.serviceTime = serviceTime;
        this.siblingIndex = siblingIndex;
    }
}
