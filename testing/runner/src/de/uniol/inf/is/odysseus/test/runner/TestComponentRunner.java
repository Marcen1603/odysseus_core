package de.uniol.inf.is.odysseus.test.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * TestComponent for running tests in nexmark.
 * Nexmark usually uses files for repeating the same data.
 * 
 * @author Timo Michelsen
 *
 */
public class TestComponentRunner extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(TestComponentRunner.class);
	private final ITestComponent component;
	private final String[] args;
	private Object testResult;
	
	public TestComponentRunner( ITestComponent component, String[] args ) {
		Preconditions.checkNotNull(component, "Component must not be null!");
		Preconditions.checkNotNull(args, "Args must not be null");
		
		setName("TestComponentRunner:" + component);
		this.component = component;
		this.args = args;
	}
	
	@Override
	public void run() {
		LOG.debug("Start Testcomponent '" + component + "'. Arguments = '" + argsToString(args) + "'");
		testResult = component.startTesting(args);
		LOG.debug("End Testcomponent '" + component + "'.");
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
	
	private static String argsToString( String[] args ) {
	    StringBuilder sb = new StringBuilder();
	    for( String str: args ) {
	        sb.append(str).append(" ");
	    }
	    return sb.toString();
	}
}
