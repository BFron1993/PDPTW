package pl.edu.agh.pdptw;

import java.util.Comparator;

/**
 * Created by Anna on 2015-06-07.
 */
public class ShawRemovalComparator implements Comparator<Commission> {

    private Commission similarCommission;
    private double alfa, beta, gamma;

    public ShawRemovalComparator(Commission commission, double alfa, double beta, double gamma) {
        this.similarCommission = commission;
        this.alfa = alfa;
        this.beta = beta;
        this.gamma = gamma;
    }

    @Override
    public int compare(Commission o1, Commission o2) {
        double o1Result = o1.R(similarCommission, alfa, beta, gamma);
        double o2REsult = o2.R(similarCommission, alfa, beta, gamma);
        if (o1Result > o2REsult) return 1;
        else if (o1Result < o2REsult) return -1;
        return 0;
    }
}
