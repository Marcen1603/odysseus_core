package de.uniol.inf.is.odysseus.test.runner;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Activator implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
	
	private static final List<TestComponentRunner> runners = Lists.newArrayList(); 

	@Override
	public void start(BundleContext ctx) throws Exception {

	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		LOG.debug("Test results");
		for( TestComponentRunner runner : runners ) {
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
		runners.add(runner);
		
		LOG.debug("Start TestComponent" + component);
		runner.start();
	}
}
