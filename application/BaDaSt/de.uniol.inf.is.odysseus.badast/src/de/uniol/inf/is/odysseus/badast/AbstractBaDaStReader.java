package de.uniol.inf.is.odysseus.badast;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

// TODO javaDoc
public abstract class AbstractBaDaStReader<V> implements IBaDaStReader<V> {

	public static final String SOURCENAME_CONFIG = "sourcename";

	protected Properties mCfg;

	protected KafkaProducer<String, V> mProducer;

	@Override
	public void close() throws Exception {
		if (this.mProducer != null) {
			this.mProducer.close();
		}
	}

	@Override
	public String getName() {
		if (!getClass().isAnnotationPresent(ABaDaStReader.class)) {
			return null;
		}
		StringBuffer out = new StringBuffer(getClass().getAnnotation(ABaDaStReader.class).type());
		try {
			validate();
			out.append("_");
			out.append(this.mCfg.get(SOURCENAME_CONFIG));
		} catch (BaDaStException e) {
			// Nothing to do
		}
		return out.toString();
	}

	@Override
	public void initialize(Properties cfg) throws BaDaStException {
		this.mCfg = cfg;
		validate();
		try {
			this.mProducer = KafkaProducerFactory.createKafkaProducer();
		} catch (IOException e) {
			throw new BaDaStException("Could not create kafka producer!", e);
		}
	}

	@Override
	public void validate() throws BaDaStException {
		if (!getClass().isAnnotationPresent(ABaDaStReader.class)) {
			throw new BaDaStException(
					getClass().getSimpleName() + " misses the annotation " + ABaDaStReader.class.getSimpleName());
		} else if (this.mCfg == null) {
			throw new BaDaStException(getClass().getAnnotation(ABaDaStReader.class).type() + " is not initialized!");
		}
		validate(SOURCENAME_CONFIG);
	}

	protected void validate(String key) throws BaDaStException {
		if (!this.mCfg.containsKey(key)) {
			throw new BaDaStException(getClass().getAnnotation(ABaDaStReader.class).type()
					+ " is not properly initialized. " + key + " is missing!");
		}
	}

}