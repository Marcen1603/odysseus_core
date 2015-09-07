package de.uniol.inf.is.odysseus.badast.recorders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

import de.uniol.inf.is.odysseus.badast.ABaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.AbstractBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IBaDaStRecorder;
import de.uniol.inf.is.odysseus.badast.IPublisher;
import de.uniol.inf.is.odysseus.badast.PublisherFactory;
import de.uniol.inf.is.odysseus.badast.Record;

/**
 * BaDaSt recorders act as subscriber for data sources and as publisher for the
 * used publish subscribe system. <br />
 * <br />
 * This recorder is for file sources and needs {@link #SOURCENAME_CONFIG} and
 * {@link #FILENAME_CONFIG} as entries of the configuration. It publishes the
 * read lines as Strings to the used publish subscribe system.
 * 
 * @author Michael Brand
 */
@SuppressWarnings(value = { "nls" })
@ABaDaStRecorder(type = FileRecorder.TYPE, parameters = { AbstractBaDaStRecorder.SOURCENAME_CONFIG,
		FileRecorder.FILENAME_CONFIG })
public class FileRecorder extends AbstractBaDaStRecorder {

	/**
	 * The type of the recorder.
	 */
	public static final String TYPE = "FileRecorder";

	/**
	 * The key for configuration, where the file name is set.
	 */
	public static final String FILENAME_CONFIG = "filename";

	@Override
	protected void validate_internal() throws BaDaStException {
		validate(FILENAME_CONFIG);
	}

	@Override
	public void start() throws BaDaStException {
		validate();
		this.mContinueReading = true;
		String topic = this.getConfig().getProperty(SOURCENAME_CONFIG);
		try (BufferedReader reader = new BufferedReader(new FileReader(this.getConfig().getProperty(FILENAME_CONFIG)));
				IPublisher<String> publisher = PublisherFactory.createStringStringPublisher(getName())) {
			String line;
			while ((line = reader.readLine()) != null && this.mContinueReading) {
				String out = line + "\n";
				publisher.publish(new Record<>(topic, out));
			}
		} catch (Exception e) {
			throw new BaDaStException("Could not read from file source!", e);
		}
	}

	@Override
	public IBaDaStRecorder newInstance(Properties cfg) throws BaDaStException {
		FileRecorder writer = new FileRecorder();
		writer.initialize(cfg);
		return writer;
	}

}