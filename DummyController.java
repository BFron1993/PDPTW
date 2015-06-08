package pl.edu.agh.pdptw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Anna on 2015-06-08.
 */
@RestController
public class DummyController {

    private final AsyncBean asyncBean;

    @Autowired
    public DummyController(AsyncBean asyncBean) {
        this.asyncBean = asyncBean;
    }

    @RequestMapping(value = "/solution", method = RequestMethod.GET)
    public String helloWorld() {
        return asyncBean.getCurrentSolution();
    }
}

