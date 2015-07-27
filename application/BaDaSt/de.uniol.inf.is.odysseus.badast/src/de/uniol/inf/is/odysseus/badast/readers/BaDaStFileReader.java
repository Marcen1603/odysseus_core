package de.uniol.inf.is.odysseus.badast.readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import de.uniol.inf.is.odysseus.badast.ABaDaStReader;
import de.uniol.inf.is.odysseus.badast.AbstractBaDaStReader;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IBaDaStReader;
import de.uniol.inf.is.odysseus.badast.KafkaProducerFactory;

/**
 * BaDaSt readers act as subscriber for data sources and as publisher for Kafka. <br />
 * <br />
 * Individual BaDaSt reader implementations are to be designed for certain types
 * of data sources (e.g., a simple file source). They connect to the data
 * source, read the outgoing data streams and publish them to a Kafka server to
 * store them. <br />
 * <br />
 * This reader is for file sources and needs {@link #SOURCENAME_CONFIG} and
 * {@link #FILENAME_CONFIG} as entries of the configuration. It publishes the
 * read lines as Strings to Kafka.
 * 
 * @author Michael Brand
 */
@ABaDaStReader(type = BaDaStFileReader.TYPE, parameters = {
		BaDaStFileReader.SOURCENAME_CONFIG, BaDaStFileReader.FILENAME_CONFIG })
public class BaDaStFileReader extends AbstractBaDaStReader<String> {

	/**
	 * The type of the reader.
	 */
	public static final String TYPE = "FileReader";

	/**
	 * The key for configuration, where the source name is set.
	 */
	public static final String SOURCENAME_CONFIG = AbstractBaDaStReader.SOURCENAME_CONFIG;

	/**
	 * The key for configuration, where the file name is set.
	 */
	public static final String FILENAME_CONFIG = "filename";

	/**
	 * True, if the reader should continue reading; false, if {@link #close()}
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
	public void startReading() throws BaDaStException {
		validate();
		this.mContinueReading = true;
		try (BufferedReader reader = new BufferedReader(new FileReader(this
				.getConfig().getProperty(FILENAME_CONFIG)))) {
			String line;
			while ((line = reader.readLine()) != null && this.mContinueReading) {
				this.getProducer().send(
						new ProducerRecord<String, String>(this.getConfig()
								.getProperty(SOURCENAME_CONFIG), line));
			}
		} catch (Exception e) {
			throw new BaDaStException("Could not read from file source!", e);
		}
	}

	@Override
	public IBaDaStReader<String> newInstance() {
		return new BaDaStFileReader();
	}

}