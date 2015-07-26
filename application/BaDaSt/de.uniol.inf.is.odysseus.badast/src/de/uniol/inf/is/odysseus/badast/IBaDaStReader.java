package de.uniol.inf.is.odysseus.badast;

import java.util.Properties;

// TODO javaDoc
public interface IBaDaStReader<V> extends AutoCloseable {

	public String getName();

	public void initialize(Properties cfg) throws BaDaStException;

	public void validate() throws BaDaStException;

	public void startReading() throws BaDaStException;
	
	public IBaDaStReader<V> newInstance();

}