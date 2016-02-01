package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
 * This recorder is for sources, which send data by TCP in a string format
 * (e.g., nexmark). It needs {@link #SOURCENAME_CONFIG}, {@link #HOST_CONFIG}
 * and {@link #PORT_CONFIG} as entries of the configuration.
 * {@link #BUFFERSIZE_CONFIG} can be ignored. The recorder publishes the read
 * string lines as strings plus "\n" to the used publish subscribe system.
 * 
 * @author Michael Brand
 */
@SuppressWarnings(value = { "nls" })
@ABaDaStRecorder(type = "TCPStringRecorder", parameters = { TCPRecorder.HOST_CONFIG, TCPRecorder.PORT_CONFIG })
public class TCPStringRecorder extends TCPRecorder {

	@Override
	public void start() throws BaDaStException {
		this.mContinueReading = true;
		final String topic = this.getConfig().getProperty(SOURCENAME_CONFIG);
		try (Socket clientSocket = new Socket(this.getConfig().getProperty(HOST_CONFIG),
				Integer.parseInt(this.getConfig().getProperty(PORT_CONFIG)));
				InputStreamReader inStreamReader = new InputStreamReader(clientSocket.getInputStream());
				BufferedReader inReader = new BufferedReader(inStreamReader);
				IPublisher<String> publisher = PublisherFactory.createStringStringPublisher(getName())) {
			String line;
			while ((line = inReader.readLine()) != null && this.mContinueReading) {
				String out = line + "\n";
				publisher.publish(new Record<>(topic, out));
			}
		} catch (Exception e) {
			throw new BaDaStException("Could not read from server!", e);
		}
	}

	@Override
	public IBaDaStRecorder newInstance(Properties cfg) throws BaDaStException {
		TCPStringRecorder writer = new TCPStringRecorder();
		writer.initialize(cfg);
		return writer;
	}

	@Override
	protected void validate_internal() throws BaDaStException {
		validate_withoutBufferSize();
	}

}