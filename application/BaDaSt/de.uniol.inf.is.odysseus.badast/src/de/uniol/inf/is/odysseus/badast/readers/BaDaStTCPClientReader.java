package de.uniol.inf.is.odysseus.badast.readers;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
 * This reader is for sources, which send data by TCP. It needs
 * {@link #SOURCENAME_CONFIG}, {@link #HOST_CONFIG} and {@link #PORT_CONFIG} as
 * entries of the configuration. {@link SBUFFERSIZE_CONFIG} can optionally be
 * set. The reader publishes the read bytes as byte arrays to Kafka.
 * 
 * @author Michael Brand
 */
@ABaDaStReader(type = BaDaStTCPClientReader.TYPE, parameters = {
		BaDaStTCPClientReader.SOURCENAME_CONFIG,
		BaDaStTCPClientReader.HOST_CONFIG, BaDaStTCPClientReader.PORT_CONFIG,
		BaDaStTCPClientReader.BUFFERSIZE_CONFIG + " (optional)" })
public class BaDaStTCPClientReader extends AbstractBaDaStReader<byte[]> {

	/**
	 * The type of the reader.
	 */
	public static final String TYPE = "TCPClientReader";

	/**
	 * The key for configuration, where the source name is set.
	 */
	public static final String SOURCENAME_CONFIG = AbstractBaDaStReader.SOURCENAME_CONFIG;

	/**
	 * The key for configuration, where the host is set.
	 */
	public static final String HOST_CONFIG = "host";

	/**
	 * The key for configuration, where the port is set.
	 */
	public static final String PORT_CONFIG = "port";

	/**
	 * The key for optional configuration, where the buffer size is set.
	 */
	public static final String BUFFERSIZE_CONFIG = "buffersize";

	/**
	 * The default value for {@link #BUFFERSIZE_CONFIG}.
	 */
	public static final String BUFFERSIZE_DEFAULT = "1024";

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
	public void startReading() throws BaDaStException {
		this.mContinueReading = true;
		final int buffersize = Integer.parseInt(this.getConfig().getProperty(
				BUFFERSIZE_CONFIG, BUFFERSIZE_DEFAULT));
		try (Socket clientSocket = new Socket(this.getConfig().getProperty(
				HOST_CONFIG), Integer.parseInt(this.getConfig().getProperty(
				PORT_CONFIG)));
				InputStream inStream = clientSocket.getInputStream()) {
			while (this.mContinueReading) {
				if (inStream.available() >= buffersize) {
					byte[] readBytes = new byte[inStream.available()];
					inStream.read(readBytes);
					this.getProducer()
							.send(new ProducerRecord<String, byte[]>(
									this.getConfig().getProperty(
											SOURCENAME_CONFIG), readBytes));
				}
			}
		} catch (Exception e) {
			throw new BaDaStException("Could not read from server!", e);
		}
	}

	@Override
	public IBaDaStReader<byte[]> newInstance() {
		return new BaDaStTCPClientReader();
	}

	@Override
	protected KafkaProducer<String, byte[]> createKafkaProducer()
			throws BaDaStException {
		try {
			return KafkaProducerFactory.createKafkaProducerByteArray(getName());
		} catch (IOException e) {
			throw new BaDaStException("Could not create kafka producer!", e);
		}
	}

	@Override
	protected void validate_internal() throws BaDaStException {
		validate(HOST_CONFIG);
		validate(PORT_CONFIG);

		// Check, if the port is an integer
		try {
			Integer.parseInt(this.getConfig().getProperty(PORT_CONFIG));
		} catch (NumberFormatException e) {
			throw new BaDaStException(this.getConfig().getProperty(PORT_CONFIG)
					+ " is not a valid port!", e);
		}

		// Check, if the buffer size is an integer
		try {
			Integer.parseInt(this.getConfig().getProperty(BUFFERSIZE_CONFIG,
					BUFFERSIZE_DEFAULT));
		} catch (NumberFormatException e) {
			throw new BaDaStException(this.getConfig().getProperty(
					BUFFERSIZE_CONFIG, BUFFERSIZE_DEFAULT)
					+ " is not a valid buffer size!", e);
		}
	}

}