package pl.edu.agh.pdptw.webapp;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * Created by Anna on 2015-06-08.
 */

@Component
public class AsyncBean {
    private AlgorithmLauncher launcher;
    boolean firstUse;

    public AsyncBean() {
        this.launcher = new AlgorithmLauncher();
        this.firstUse = true;
    }

    @PostConstruct
    public void init() {
    }

    public void initSolver(InitConfiguration config) throws UnsupportedEncodingException {
        if (!firstUse) {
            launcher.interrupt();
            launcher = new AlgorithmLauncher();
        } else {
            firstUse = false;
        }
        launcher.setConfig(config);
        launcher.start();
    }

    public String getCurrentSolution() {
        return launcher != null? launcher.getCurrentSolution(): null;
    }
}
