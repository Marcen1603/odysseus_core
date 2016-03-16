package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

import de.uniol.inf.is.odysseus.recovery.incomingelements.badast.BaDaStException;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badast.IPublisher;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badast.PublisherFactory;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badast.Record;

/**
 * BaDaSt recorders act as subscriber for data sources and as publisher for the
 * used publish subscribe system. <br />
 * <br />
 * This recorder is for sources, which send data by TCP. It needs
 * {@link #SOURCENAME_CONFIG}, {@link #HOST_CONFIG} and {@link #PORT_CONFIG} as
 * entries of the configuration. {@link #BUFFERSIZE_CONFIG} must also be set.
 * The recorder publishes the read bytes as byte arrays to the used publish
 * subscribe system.
 * 
 * @author Michael Brand
 */
@SuppressWarnings(value = { "nls" })
@ABaDaStRecorder(type = "TCPRecorder", parameters = { TCPRecorder.HOST_CONFIG, TCPRecorder.PORT_CONFIG })
public class TCPRecorder extends AbstractBaDaStRecorder {

	/**
	 * The key for configuration, where the host is set.
	 */
	public static final String HOST_CONFIG = "host";

	/**
	 * The key for configuration, where the port is set.
	 */
	public static final String PORT_CONFIG = "port";

	@Override
	public void start() throws BaDaStException {
		this.mContinueReading = true;
		final String topic = this.getConfig().getProperty(SOURCENAME_CONFIG);
		try (Socket clientSocket = new Socket(this.getConfig().getProperty(HOST_CONFIG),
				Integer.parseInt(this.getConfig().getProperty(PORT_CONFIG)));
				InputStream inStream = clientSocket.getInputStream();
				IPublisher<byte[]> publisher = PublisherFactory.createStringByteArrayPublisher(getName())) {
			while (this.mContinueReading) {
				byte[] buffer = new byte[inStream.available()];
				inStream.read(buffer);
				publisher.publish(new Record<>(topic, buffer));
			}
		} catch (Exception e) {
			throw new BaDaStException("Could not read from server!", e);
		}
	}

	@Override
	public IBaDaStRecorder newInstance(Properties cfg) throws BaDaStException {
		TCPRecorder writer = new TCPRecorder();
		writer.initialize(cfg);
		return writer;
	}

	@Override
	protected void validate_internal() throws BaDaStException {
		validate(HOST_CONFIG);
		validate(PORT_CONFIG);

		// Check, if the port is an integer
		try {
			Integer.parseInt(this.getConfig().getProperty(PORT_CONFIG));
		} catch (NumberFormatException e) {
			throw new BaDaStException(this.getConfig().getProperty(PORT_CONFIG) + " is not a valid port!", e);
		}
	}

}