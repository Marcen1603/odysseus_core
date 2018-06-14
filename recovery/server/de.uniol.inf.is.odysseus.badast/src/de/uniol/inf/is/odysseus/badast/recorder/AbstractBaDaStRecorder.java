package de.uniol.inf.is.odysseus.badast.recorder;

import java.util.Properties;

import de.uniol.inf.is.odysseus.badast.BaDaStException;

/**
 * BaDaSt recorders act as subscriber for data sources and as publisher for the
 * used publish subscribe system. <br />
 * <br />
 * Individual recorder implementations are to be designed for certain types of
 * data sources (e.g., a simple file source). They connect to the data source,
 * read the outgoing data streams and publish them to the used publish subscribe
 * system to store them. <br />
 * <br />
 * Concrete implementations have to be annotated with {@link ABaDaStRecorder}
 * and there must be a component definition to make the implementation
 * available.
 * 
 * @author Michael Brand
 */
public abstract class AbstractBaDaStRecorder implements IBaDaStRecorder {

	/**
	 * The configuration as a key value store.
	 */
	private Properties cfg;
	
	/**
	 * True, if the recorder should continue reading; false, if {@link #close()}
	 * is called.
	 */
	protected boolean continueReading;

	@Override
	public String getName() {
		if (!getClass().isAnnotationPresent(ABaDaStRecorder.class)) {
			return null;
		}
		StringBuffer out = new StringBuffer(getClass().getAnnotation(ABaDaStRecorder.class).type());
		try {
			validate();
			out.append("_");
			out.append(this.cfg.get(SOURCENAME_CONFIG));
		} catch (BaDaStException e) {
			return null;
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
	 *             if any error occurs, e.g., missing key value pairs.
	 */
	protected void initialize(Properties cfg) throws BaDaStException {
		this.cfg = cfg;
		validate();
	}

	@Override
	public Properties getConfig() {
		return this.cfg;
	}

	/**
	 * Checks, if the recorder is ready to read.
	 * 
	 * @throws BaDaStException
	 *             if any error occurs.
	 */
	protected void validate() throws BaDaStException {
		if (!getClass().isAnnotationPresent(ABaDaStRecorder.class)) {
			throw new BaDaStException(
					getClass().getSimpleName() + " misses the annotation " + ABaDaStRecorder.class.getSimpleName());
		} else if (this.cfg == null) {
			throw new BaDaStException(getClass().getAnnotation(ABaDaStRecorder.class).type() + " is not initialized!");
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
		if (!this.cfg.containsKey(key)) {
			throw new BaDaStException(getClass().getAnnotation(ABaDaStRecorder.class).type()
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
	
	@Override
	public void close() throws Exception {
		this.continueReading = false;
	}

}