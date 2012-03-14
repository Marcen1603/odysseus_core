package de.uniol.inf.is.odysseus.test;

/**
 * Listener for {@code SimpleCompareSink} to inform other
 * classes about compare results.
 *  
 * @author Timo Michelsen
 *
 */
public interface ICompareSinkListener {

	void processingDone();
	void processingError(String line, String input);

}
