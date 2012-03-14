package de.uniol.inf.is.odysseus.test.runner;

/**
 * Interface for Classes with tests, which should be
 * executed during Odysseus Test. The Plugin test.runner
 * executes them, when they are registered over Declarative Services.
 * 
 * @author Timo Michelsen
 *
 */
public interface ITestComponent {
	
	public Object startTesting(String[] args);
	
}
