package de.uniol.inf.is.odysseus.badast.recorders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import de.uniol.inf.is.odysseus.badast.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.AbstractBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.KafkaProducerFactory;

/**
 * BaDaSt recorders act as subscriber for data sources and as publisher for
 * Kafka. <br />
 * <br />
 * This recorder is for file sources and needs {@link #SOURCENAME_CONFIG} and
 * {@link #FILENAME_CONFIG} as entries of the configuration. It publishes the
 * read lines as Strings to Kafka.
 * 
 * @author Michael Brand
 */
@SuppressWarnings(value = { "nls" })
@ABaDaStRecorder(type = FileRecorder.TYPE, parameters = {
		FileRecorder.SOURCENAME_CONFIG, FileRecorder.FILENAME_CONFIG })
public class FileRecorder extends AbstractBaDaStRecorder<String> {

	/**
	 * The type of the recorder.
	 */
	public static final String TYPE = "FileRecorder";

	/**
	 * The key for configuration, where the source name is set.
	 */
	public static final String SOURCENAME_CONFIG = AbstractBaDaStRecorder.SOURCENAME_CONFIG;

	/**
	 * The key for configuration, where the file name is set.
	 */
	public static final String FILENAME_CONFIG = "filename";

	/**
	 * True, if the recorder should continue reading; false, if {@link #close()}
	 * is called.
	 */
	private boolean mContinueReading;

	@Override
	public void close() throws Exception {
		this.mContinueReading = false;
	}

	@Override
	protected KafkaProducer<String, String> createKafkaProducer()
			throws BaDaStException {
		try {
			return KafkaProducerFactory.createKafkaProducerString(getName());
		} catch (IOException e) {
			throw new BaDaStException("Could not create kafka producer!", e);
		}
	}

	@Override
	public void validate_internal() throws BaDaStException {
		validate(FILENAME_CONFIG);
	}

	@Override
	public void start() throws BaDaStException {
		validate();
		this.mContinueReading = true;
		try (BufferedReader reader = new BufferedReader(new FileReader(this
				.getConfig().getProperty(FILENAME_CONFIG)))) {
			String line;
			while ((line = reader.readLine()) != null && this.mContinueReading) {
				String out = line + "\n";
				this.getProducer().send(
						new ProducerRecord<String, String>(this.getConfig()
								.getProperty(SOURCENAME_CONFIG), out));
			}
		} catch (Exception e) {
			throw new BaDaStException("Could not read from file source!", e);
		}
	}

	@Override
	public IBaDaStRecorder<String> newInstance(Properties cfg)
			throws BaDaStException {
		FileRecorder writer = new FileRecorder();
		writer.initialize(cfg);
		return writer;
	}

}