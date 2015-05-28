public class Location {
    private int x;
    private int y;
    
    public Location(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double getDistanceTo(Location location)
    {
        return Math.sqrt(Math.pow(this.x - location.x, 2) + Math.pow(this.y - location.y, 2));
    }
}
