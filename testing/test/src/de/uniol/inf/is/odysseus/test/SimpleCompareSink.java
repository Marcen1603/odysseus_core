package de.uniol.inf.is.odysseus.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class SimpleCompareSink extends AbstractSink<Object> implements ICompareSink{

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
		try {
			BufferedReader reader = new BufferedReader(new FileReader(compareFile));
			String line = null;
			while ( (line = reader.readLine()) != null){
				compareInput.add(line);
			}
		} catch (IOException e) {
			throw new OpenFailedException("Cannot read result file "+e.getMessage());
		}
		
	}
	
	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		
		String line = compareInput.remove(0);
		String input = object.toString();
		
		if (!line.equals(input)){
			sinkListener.processingError(line, input);
		}
		
		if (compareInput.isEmpty()){
			sinkListener.processingDone();
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractSink<Object> clone() {
		return new SimpleCompareSink(this);
	}

}
