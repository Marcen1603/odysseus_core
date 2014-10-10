package de.uniol.inf.is.odysseus.generator.process;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.process.util.CSVLogParser;

public class ProcessGenerator extends AbstractDataGenerator{
	
	List<String> csvLines = Lists.newArrayList();
	int lineCounter = 1;
	
	@Override
	public List<DataTuple> next() throws InterruptedException {
		DataTuple tuple = new DataTuple();
		String[] actualLine = csvLines.get(lineCounter-1).split(",");
		lineCounter++;
		// Case id
		tuple.addInteger(actualLine[0]);
		// Activity name
		tuple.addString(actualLine[3]);		
	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
				
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);		
		if((csvLines.size()%lineCounter) == 0){
			lineCounter = 1;
		}
		return list;
	}

	@Override
	public void close() {
		csvLines.clear();
		lineCounter = 1;
		System.out.println("Close Process Generator ...");
	}

	@Override
	protected void process_init() {
		csvLines = CSVLogParser.getCSVLines();
		System.out.println("Init Process Generator ...");
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new ProcessGenerator();
	}

}
