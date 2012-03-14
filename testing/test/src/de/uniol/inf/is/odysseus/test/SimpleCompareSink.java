package de.uniol.inf.is.odysseus.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

/**
 * Sink, which compares calculated relational tuple with
 * predefined tuple. Used for tests only. Use callbacks to inform
 * listener if one calculated tuple differs or all calculated tuples are correct.  
 * 
 * @author Timo Michelsen
 *
 */
public class SimpleCompareSink extends AbstractSink<Object> implements
		ICompareSink {

	Logger logger = LoggerFactory.getLogger(SimpleCompareSink.class);

	final File compareFile;
	final private ICompareSinkListener sinkListener;
	List<String> compareInput = new LinkedList<String>();

	public SimpleCompareSink(File compareFile, ICompareSinkListener sinkListener) {
		this.compareFile = compareFile;
		this.sinkListener = sinkListener;
	}

	public SimpleCompareSink(SimpleCompareSink simpleCompareSink) {
		this.compareFile = simpleCompareSink.compareFile;
		this.sinkListener = simpleCompareSink.sinkListener;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		synchronized (compareInput) {
			try {
				logger.debug("Reading Compare File " + compareFile);
				BufferedReader reader = new BufferedReader(new FileReader(
						compareFile));
				String line = null;
				while ((line = reader.readLine()) != null) {
					compareInput.add(line.trim());
				}
				logger.debug("Reading Compare File " + compareFile + " done");
			} catch (IOException e) {
				throw new OpenFailedException("Cannot read result file "
						+ e.getMessage());
			}
		}
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		if (!isDone()) {
			synchronized (compareInput) {
				String line = compareInput.remove(0);
				String input = object.toString();

				if (!line.equals(input)) {
					System.err.println(line);
					System.err.println(input);
					System.err.println("Difference at " + line.compareTo(input));
					sinkListener.processingError(line, input);
				}

				if (compareInput.isEmpty()) {
					this.done(port);
					sinkListener.processingDone();
				}

			}
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {

	}

	@Override
	public SimpleCompareSink clone() {
		return new SimpleCompareSink(this);
	}

}
