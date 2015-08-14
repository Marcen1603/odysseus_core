package de.uniol.inf.is.odysseus.badast;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

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
@SuppressWarnings(value = { "nls" })
public abstract class AbstractBaDaStRecorder<V> implements IBaDaStRecorder<V> {

	/**
	 * The key for configuration, where the recorder type is set.
	 */
	public static final String TYPE_CONFIG = "type";

	/**
	 * The key for configuration, where the recorder name is set.
	 */
	public static final String NAME_CONFIG = "name";

	/**
	 * The key for configuration, where the source name is set.
	 */
	public static final String SOURCENAME_CONFIG = "sourcename";

	/**
	 * The configuration as a key value store.
	 */
	private Properties mCfg;

	/**
	 * The producer for Kafka.
	 */
	private KafkaProducer<String, V> mProducer;

	@Override
	public void close() throws Exception {
		if (this.mProducer != null) {
			this.mProducer.close();
		}
	}

	@Override
	public String getName() {
		if (!getClass().isAnnotationPresent(ABaDaStRecorder.class)) {
			return null;
		}
		StringBuffer out = new StringBuffer(getClass().getAnnotation(
				ABaDaStRecorder.class).type());
		try {
			validate();
			out.append("_");
			out.append(this.mCfg.get(SOURCENAME_CONFIG));
		} catch (BaDaStException e) {
			// Nothing to do
		}
		return out.toString();
	}

	/**
	 * Initializes the recorder. To be called by
	 * {@link #newInstance(Properties)}.
	 * 
	 * @param cfg
	 *            A key value store holding the configuration, which may differ
	 *            for different recorder types.
	 * @throws BaDaStException
	 *             if any error occurs, e.g., missing key value pairs or if the
	 *             Kafka producer could not be created.
	 */
	protected void initialize(Properties cfg) throws BaDaStException {
		this.mCfg = cfg;
		validate();
		this.mProducer = createKafkaProducer();
	}

	/**
	 * Creates the producer for Kafka. Called by {@link #initialize(Properties)}
	 * after {@link #validate()}.
	 * 
	 * @return A {@link KafkaProducer} ready to use.
	 * @throws BaDaStException
	 *             if the producer could not be created.
	 */
	protected abstract KafkaProducer<String, V> createKafkaProducer()
			throws BaDaStException;

	/**
	 * Gets the producer for Kafka.
	 * 
	 * @return The producer set by calling {@link #createKafkaProducer()}, if
	 *         {@link #initialize(Properties)} has been called.
	 */
	protected KafkaProducer<String, V> getProducer() {
		return this.mProducer;
	}

	@Override
	public Properties getConfig() {
		return this.mCfg;
	}

	/**
	 * Checks, if the recorder is ready to read.
	 * 
	 * @throws BaDaStException
	 *             if any error occurs.
	 */
	protected void validate() throws BaDaStException {
		if (!getClass().isAnnotationPresent(ABaDaStRecorder.class)) {
			throw new BaDaStException(getClass().getSimpleName()
					+ " misses the annotation "
					+ ABaDaStRecorder.class.getSimpleName());
		} else if (this.mCfg == null) {
			throw new BaDaStException(getClass().getAnnotation(
					ABaDaStRecorder.class).type()
					+ " is not initialized!");
		}
		validate(SOURCENAME_CONFIG);
		validate_internal();
	}

	/**
	 * Validates a given entry of the configuration.
	 * 
	 * @param key
	 *            The key of the entry.
	 * @throws BaDaStException
	 *             If {@link #getConfig()} does not contain that key.
	 */
	protected void validate(String key) throws BaDaStException {
		if (!this.mCfg.containsKey(key)) {
			throw new BaDaStException(getClass().getAnnotation(
					ABaDaStRecorder.class).type()
					+ " is not properly initialized. " + key + " is missing!");
		}
	}

	/**
	 * Checks, if the recorder is ready. Called by {@link #validate()} after
	 * checking, that a configuration is set. <br />
	 * <br />
	 * May use {@link #validate(String)}.
	 * 
	 * @throws BaDaStException
	 *             if any error occurs, e.g., any configuration entry is
	 *             missing.
	 */
	protected abstract void validate_internal() throws BaDaStException;

}