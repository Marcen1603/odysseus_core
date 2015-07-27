package de.uniol.inf.is.odysseus.badast;

import java.util.Properties;

/**
 * BaDaSt readers act as subscriber for data sources and as publisher for Kafka. <br />
 * <br />
 * Individual BaDaSt reader implementations are to be designed for certain types
 * of data sources (e.g., a simple file source). They connect to the data
 * source, read the outgoing data streams and publish them to a Kafka server to
 * store them. <br />
 * <br />
 * Concrete implementations have to be annotated with
 * {@link AbstractBaDaStReader} and there must be a component definition to make
 * the implementation available.
 * 
 * @author Michael Brand
 *
 * @param <V>
 *            The type of the values to store on the Kafka server. Either String
 *            or byte array.
 */
public interface IBaDaStReader<V> extends AutoCloseable {

	/**
	 * Gets the name of the reader.
	 * 
	 * @return A String identifying the reader (not the reader type).
	 */
	public String getName();

	/**
	 * Initializes the reader, so it is ready to read the data stream and to
	 * publish it.
	 * 
	 * @param cfg
	 *            A key value store holding the configuration, which may differ
	 *            for different reader types.
	 * @throws BaDaStException
	 *             if any error occurs, e.g., missing key value pairs or if the
	 *             Kafka producer could not be created.
	 */
	public void initialize(Properties cfg) throws BaDaStException;

	/**
	 * Gets the configuration.
	 * 
	 * @return A key value store holding the configuration, which is set by
	 *         calling {@link #initialize(Properties)}.
	 */
	public Properties getConfig();

	/**
	 * Checks, if the reader is ready to read.
	 * 
	 * @throws BaDaStException
	 *             if any error occurs, e.g., {@link #initialize(Properties)}
	 *             has not been called.
	 */
	public void validate() throws BaDaStException;

	/**
	 * Starts the reading: subscribe to the data source, read it and publish it
	 * to a Kafka server.
	 * 
	 * @throws BaDaStException
	 *             if any error occurs, e.g., {@link #initialize(Properties)}
	 *             has not been called.
	 */
	public void startReading() throws BaDaStException;

	/**
	 * Creates a new instance of the reader type.
	 * 
	 * @return A new reader of the same type, but not initialized.
	 */
	public IBaDaStReader<V> newInstance();

}