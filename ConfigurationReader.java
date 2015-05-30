
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationReader {
    
    public Configuration readConfig(String path) throws IOException
    {
        List<ActionProxy> deliveries = new LinkedList<>();
        List<ActionProxy> pickups = new LinkedList<>();
        int capacity = 0;
        Location base = null;
        int timeLimit = 0;
        List<Commission> commissions = new LinkedList<>();
        BufferedReader bufferedReader = null;
        try 
        {
            bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();
            
            String[] tmpTab = line.split("\\t");
            capacity = Integer.parseInt(tmpTab[1]);
            
            line = bufferedReader.readLine();
            tmpTab = line.split("\\t");
            
            int x = Integer.parseInt(tmpTab[1]);
            int y = Integer.parseInt(tmpTab[2]);
            timeLimit = Integer.parseInt(tmpTab[5]);
            
            base = new Location(x, y);
                   
            line = bufferedReader.readLine();
            while(line != null)
            {
                boolean delivery = true;
                tmpTab = line.split("\\t");
                int siblingIndex = Integer.parseInt(tmpTab[7]);
                if (siblingIndex == 0) {
                    delivery = false;
                    siblingIndex = Integer.parseInt(tmpTab[8]);
                }
                
                int index = Integer.parseInt(tmpTab[0]);
                x = Integer.parseInt(tmpTab[1]);
                y = Integer.parseInt(tmpTab[2]);
                int demand = Integer.parseInt(tmpTab[3]);
                int beginTimeWindow = Integer.parseInt(tmpTab[4]);
                int endTimeWindow = Integer.parseInt(tmpTab[5]);
                int serviceTime = Integer.parseInt(tmpTab[6]);
                
                if (delivery) {
                    deliveries.add(new ActionProxy(index, x, y, demand, beginTimeWindow, endTimeWindow, serviceTime, siblingIndex));
                }
                else
                {
                    pickups.add(new ActionProxy(index, x, y, demand, beginTimeWindow, endTimeWindow, serviceTime, siblingIndex));
                }
                line = bufferedReader.readLine();
            }
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) 
        {
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        
        int index = 1;
        for(ActionProxy pickupProxy : pickups)
        {
            ActionProxy deliveryProxy = FindCorrespondigDelivery(pickupProxy, deliveries);
            
            Location locationPickup = new Location(pickupProxy.x, pickupProxy.y);
            Location locationDelivery = new Location(deliveryProxy.x, deliveryProxy.y);
            
            double serviceTimePickupPlusGoingToDelivery = pickupProxy.serviceTime + locationPickup.getDistanceTo(locationDelivery);
            double lastChanceToPickup = deliveryProxy.endTimeWindow - serviceTimePickupPlusGoingToDelivery;
            if (lastChanceToPickup < pickupProxy.endTimeWindow) 
            {
                pickupProxy.endTimeWindow = lastChanceToPickup - 0.0000000000001;
            }
            
            commissions.add(new Commission(new Action(pickupProxy), new Action(deliveryProxy), index++));  
        }
        
        return new Configuration(commissions, timeLimit, capacity, base);
    }

    private ActionProxy FindCorrespondigDelivery(ActionProxy pickupProxy, List<ActionProxy> deliveries) 
    {
       for(ActionProxy delivery : deliveries)
       {
           if(delivery.id == pickupProxy.siblingIndex)
           {
               return delivery;
           }
       }
       throw new IllegalArgumentException("Błąd!");
    }
}
