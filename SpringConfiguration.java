package pl.edu.agh.pdptw;

import org.springframework.context.annotation.*;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by Anna on 2015-06-08.
 */

@org.springframework.context.annotation.Configuration
public class SpringConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
