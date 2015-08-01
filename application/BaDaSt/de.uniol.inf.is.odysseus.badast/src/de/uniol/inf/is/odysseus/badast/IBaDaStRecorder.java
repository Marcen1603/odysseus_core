package de.uniol.inf.is.odysseus.badast;

import java.util.Properties;

/**
 * BaDaSt recorders act as subscriber for data sources and as publisher for
 * Kafka. <br />
 * <br />
 * Individual recorder implementations are to be designed for certain types of
 * data sources (e.g., a simple file source). They connect to the data source,
 * read the outgoing data streams and publish them to a Kafka server to store
 * them. <br />
 * <br />
 * Concrete implementations have to be annotated with {@link ABaDaStRecorder}
 * and there must be a component definition to make the implementation
 * available.
 * 
 * @author Michael Brand
 *
 * @param <V>
 *            The type of the values to store on the Kafka server. Either String
 *            or byte array.
 */
public interface IBaDaStRecorder<V> extends AutoCloseable {

	/**
	 * Gets the name of the recorder.
	 * 
	 * @return A String identifying the recorder (not the recorder type).
	 */
	public String getName();

	/**
	 * Gets the configuration.
	 * 
	 * @return A key value store holding the configuration, which is set by
	 *         calling {@link #newInstance(Properties)}.
	 */
	public Properties getConfig();

	/**
	 * Starts the recorder: subscribe to the data source, read it and publish it
	 * to a Kafka server.
	 * 
	 * @throws BaDaStException
	 *             if any error occurs.
	 */
	public void start() throws BaDaStException;

	/**
	 * Creates a new instance of the recorder type.
	 * 
	 * @param cfg
	 *            A key value store holding the configuration, which may differ
	 *            for different recorder types.
	 * @return A new recorder of the same type.
	 * @throws BaDaStException
	 *             if any error occurs, e.g., missing key value pairs or if the
	 *             Kafka producer could not be created.
	 */
	public IBaDaStRecorder<V> newInstance(Properties cfg)
			throws BaDaStException;

}