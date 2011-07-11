package de.uniol.inf.is.odysseus.physicaloperator.sink;

public interface IStreamHandler<T> {

	void start();
	void transfer(T object);
	void done();

}
