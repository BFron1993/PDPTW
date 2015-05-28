public class Action {
    private int demand;
    private int timeWindowBegin;
    private int timeWindowEnd;
    private Location location;
    private int serviceTime;
    
    public Action(int demand, int timeWindowBegin, int timeWindowEnd, Location location, int serviceTime)
    {
        this.demand = demand;
        this.timeWindowBegin = timeWindowBegin;
        this.timeWindowEnd = timeWindowEnd;
        this.location = location;
        this.serviceTime = serviceTime;
    }
    
    public Action(ActionProxy proxy)
    {
        this.demand = proxy.demand;
        this.timeWindowBegin = proxy.beginTimeWindow;
        this.timeWindowEnd = proxy.endTimeWindow;
        this.location = new Location(proxy.x, proxy.y);
        this.serviceTime = proxy.serviceTime;
    }
    
    public Location getLocation()
    {
        return this.location;
    }
    
    public TimeWindow getTimeWindow()
    {
        return new TimeWindow(this.timeWindowBegin, this.timeWindowEnd);
    }
    
    public int getDemand()
    {
        return this.demand;
    }
    
    public int getServiceTime()
    {
        return this.serviceTime;
    }
}
