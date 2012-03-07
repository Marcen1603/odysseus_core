package de.uniol.inf.is.odysseus.test.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class TestComponentRunner extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(TestComponentRunner.class);
	private final ITestComponent component;
	private Object testResult;
	
	public TestComponentRunner( ITestComponent component ) {
		Preconditions.checkNotNull(component, "Component must not be null!");
		
		this.component = component;
	}
	
	@Override
	public void run() {
		LOG.debug("Start Testcomponent " + component);
		testResult = component.startTesting();
		LOG.debug("End Testcomponent " + component);
	}
	
	public final Object getResult() {
		if(testResult == null ) {
			throw new IllegalStateException("TestComponent " + component + " not finished.");
		}
		return testResult;
	}
	
	public final ITestComponent getTestComponent() {
		return component;
	}
}
