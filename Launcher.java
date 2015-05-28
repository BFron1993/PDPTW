
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {

    public static void main(String[] args) {
        ConfigurationReader reader = new ConfigurationReader();
        Configuration config = null;
        try 
        {
            config =reader.readConfig("D:\\pdptw-algo-test\\benchmarks\\pdp_100\\lc101.txt");
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Solution sol = new Solution(config.getBaseLocation(), config.getTimeLimit(), config.getCapacity());
        
        int schduleIndex = sol.getNewSchedule();
        Iterator<Commission> iterator = config.getCommissions().iterator();
        while(iterator.hasNext()) {
            Commission commission = iterator.next();
            InsertProperties properties = sol.countCostOfInsert(commission, schduleIndex);
            if (properties != null) {
                sol.addCommission(properties, commission);
            }
        }
        
        sol.PrintSchedules();
    }
}
