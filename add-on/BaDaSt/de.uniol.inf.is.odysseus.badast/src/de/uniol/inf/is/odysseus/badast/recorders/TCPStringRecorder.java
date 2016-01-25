package de.uniol.inf.is.odysseus.badast.recorders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Properties;

import de.uniol.inf.is.odysseus.badast.IPublisher;
import de.uniol.inf.is.odysseus.badast.PublisherFactory;
import de.uniol.inf.is.odysseus.badast.Record;
import de.uniol.inf.is.odysseus.core.server.recovery.badast.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.core.server.recovery.badast.BaDaStException;
import de.uniol.inf.is.odysseus.core.server.recovery.badast.IBaDaStRecorder;

// TODO javaDoc
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