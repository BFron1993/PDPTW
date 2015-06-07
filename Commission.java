class Commission {
    
    private Action pickup;
    private Action delivery;
    private int id;
    
    public Commission(Action pickup, Action delivery, int id)
    {
        this.id = id;
        this.pickup = pickup;
        this.delivery = delivery;
    }
    
    public Location getPickupLocation()
    {
        return this.pickup.getLocation();
    }
    
    public Location getDeliveryLocation()
    {
        return this.delivery.getLocation();
    }
    
    public TimeWindow getPickupTimeWindow()
    {
        return this.pickup.getTimeWindow();
    }
    
    public int getPickupDemand()
    {
        return this.pickup.getDemand();
    }
    
    public int getPickupServiceTime()
    {
        return this.pickup.getServiceTime();
    }
    
    public int getId()
    {
        return this.id;
    }

    double getDeliveryServiceTime() {
        return this.delivery.getServiceTime();
    }

    TimeWindow getDeliveryTimeWindow() {
        return this.delivery.getTimeWindow();
    }
    
    int getDeliveryDemand()
    {
        return this.delivery.getDemand();
    }

    double getPickupDeliveryPathLength(Location location) {
        return location.getDistanceTo(this.getPickupLocation())
                + this.getPickupLocation().getDistanceTo(this.getDeliveryLocation())
                + this.getDeliveryLocation().getDistanceTo(location);
    }

    public double R(Commission anotherCommission, double alfa, double beta, double gamma) {
        return alfa * (this.pickup.getLocation().getDistanceTo(anotherCommission.pickup.getLocation()) +
                this.delivery.getLocation().getDistanceTo(anotherCommission.delivery.getLocation())) +
                beta * (Math.abs(this.pickup.getServiceTime() - anotherCommission.pickup.getServiceTime()) +
                Math.abs(this.delivery.getServiceTime() - anotherCommission.delivery.getServiceTime())) +
                gamma * (Math.abs(this.getPickupDemand() - anotherCommission.getPickupDemand()));
    }

}
