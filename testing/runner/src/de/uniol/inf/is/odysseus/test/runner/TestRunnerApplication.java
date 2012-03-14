package de.uniol.inf.is.odysseus.test.runner;

import java.util.List;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Application for running all TestComponents. Recieves all registered {@code TestComponents}
 * over Declarative Services and executes them.
 * 
 * @author Timo Michelsen
 *
 */
public class TestRunnerApplication implements IApplication {

    private static final List<TestComponentRunner> RUNNERS = Lists.newArrayList(); 
    private static final Logger LOG = LoggerFactory.getLogger(TestRunnerPlugIn.class);

    @Override
    public Object start(IApplicationContext context) throws Exception {
        return null;
    }

    @Override
    public void stop() {
        LOG.debug("Test results");
        for( TestComponentRunner runner : RUNNERS ) {
            String result;
            try {
                result = runner.getResult() != null ? runner.getResult().toString() : "null";
            } catch( IllegalStateException ignore ) {
                result = "Not finished";
            }
            
            LOG.debug(runner.getTestComponent() + " : " + result);
        }
    }

    public void startTestComponent(ITestComponent component) {
        TestComponentRunner runner = new TestComponentRunner(component);
        RUNNERS.add(runner);
        
        LOG.debug("Start TestComponent" + component);
        runner.start();
    }
}
