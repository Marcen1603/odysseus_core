package de.uniol.inf.is.odysseus.generator;

import java.util.List;

public interface IDataGenerator {

	IDataGenerator newCleanInstance();
	void init(IProviderRunner runner);
	List<DataTuple> next() throws InterruptedException;
	void close();
	
	boolean isSA();
}
