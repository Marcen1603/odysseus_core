package de.uniol.inf.is.odysseus.test;

public interface ICompareSinkListener {

	void processingDone();
	void processingError(String line, String input);

}
