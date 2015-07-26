package de.uniol.inf.is.odysseus.badast.readers;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.kafka.clients.producer.ProducerRecord;

import de.uniol.inf.is.odysseus.badast.ABaDaStReader;
import de.uniol.inf.is.odysseus.badast.AbstractBaDaStReader;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IBaDaStReader;

// TODO javaDoc
@ABaDaStReader(type = BaDaStFileReader.TYPE)
public class BaDaStFileReader extends AbstractBaDaStReader<String> {

	public static final String TYPE = "FileReader";

	public static final String SOURCENAME_CONFIG = AbstractBaDaStReader.SOURCENAME_CONFIG;

	public static final String FILENAME_CONFIG = "filename";

	private boolean mContinueReading;

	@Override
	public void close() throws Exception {
		this.mContinueReading = false;
	}

	@Override
	public void validate() throws BaDaStException {
		super.validate();
		validate(FILENAME_CONFIG);
	}

	@Override
	public void startReading() throws BaDaStException {
		validate();
		this.mContinueReading = true;
		try (BufferedReader reader = new BufferedReader(new FileReader(this.mCfg.getProperty(FILENAME_CONFIG)))) {
			String line;
			while ((line = reader.readLine()) != null && this.mContinueReading) {
				this.mProducer.send(new ProducerRecord<String, String>(this.mCfg.getProperty(SOURCENAME_CONFIG), line));
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