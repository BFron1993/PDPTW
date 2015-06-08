package pl.edu.agh.pdptw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;



/**
 * Created by Anna on 2015-06-08.
 */
@Component
public class AsyncBean {
    private final TaskExecutor taskExecutor;
    private AlgorithmLauncher launcher;

    @Autowired
    public AsyncBean(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        this.launcher = new AlgorithmLauncher();
    }

    @PostConstruct
    public void init() {
        taskExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        runSolver();
                    }
                }
        );
    }

    private void runSolver() {
        launcher.computeSolution();
    }

    public String getCurrentSolution() {
        return launcher.getCurrentSolution();
    }
}
