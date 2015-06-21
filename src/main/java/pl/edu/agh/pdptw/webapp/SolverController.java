package pl.edu.agh.pdptw.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.pdptw.webapp.AsyncBean;
import pl.edu.agh.pdptw.webapp.InitConfiguration;

import java.io.UnsupportedEncodingException;

/**
 * Created by Anna on 2015-06-08.
 */

@RestController
public class SolverController {

    private final AsyncBean asyncBean;

    @Autowired
    public SolverController(AsyncBean asyncBean) {
        this.asyncBean = asyncBean;
    }

    @RequestMapping(value="/solve", method = RequestMethod.POST)
    public void solve(@RequestBody InitConfiguration config) throws UnsupportedEncodingException {
        asyncBean.initSolver(config);
    }

    @RequestMapping(value = "/solution", method = RequestMethod.GET)
    public String getSolution() {
        return asyncBean.getCurrentSolution();
    }

}

