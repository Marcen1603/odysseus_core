package de.uniol.inf.is.odysseus.test.physicaloperator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.test.TupleTestActivator;

public class TupleCompareSinkPO extends AbstractSink<Tuple<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(TupleCompareSinkPO.class);

	private String compareFile = null;
	//final private ICompareSinkListener sinkListener;
	List<Tuple<?>> expectedResults = new LinkedList<Tuple<?>>();	
	TupleDataHandler tupleDataHandler = null;
	String qry;

	public TupleCompareSinkPO() {
	}
	
	public TupleCompareSinkPO(String compareFile) {
		this.compareFile = compareFile;
	}

	public TupleCompareSinkPO(TupleCompareSinkPO tupleCompareSinkPO) {
		compareFile = tupleCompareSinkPO.getCompareFile();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		SDFSchema s = getOutputSchema();
		this.tupleDataHandler = new TupleDataHandler(s);
		
		synchronized (expectedResults) {
			try {
				LOG.debug("Reading Compare File " + compareFile);
				
//				URL fileUrl = TupleTestActivator.context.getBundle().getResource(compareFile);
//				File f = new File(FileLocator.toFileURL(fileUrl).getPath());
				File f = new File(TupleTestActivator.bundlePath + File.separator + compareFile);
				this.qry = f.getName();
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String line = null;
				while ((line = reader.readLine()) != null) {
					this.expectedResults.add(this.tupleDataHandler.readData(line.trim().split(";")));
				}
				LOG.debug("Reading Compare File " + compareFile + " done");
			} catch (IOException e) {
				throw new OpenFailedException("Cannot read result file "
						+ e.getMessage());
			}
		}
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port, boolean isReadOnly) {
		if (!isDone()) {
			synchronized (expectedResults) {
				Tuple<?> expected = expectedResults.remove(0);

				if (!expected.equals(tuple)) {
					System.err.println(expected);
					System.err.println(tuple);
					System.err.println("Difference at " + expected.compareTo(tuple));
					//sinkListener.processingError(expected.toString(), tuple.toString());
				}

				if (expectedResults.isEmpty()) {
					this.done(port);
					System.out.println("Finished testing of Query: " + qry);
					//sinkListener.processingDone();
				}

			}
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {

	}

	@Override
	public AbstractSink<Tuple<?>> clone() {
		return new TupleCompareSinkPO(this);
	}
	
	public String getCompareFile() {
		return compareFile;
	}

}
