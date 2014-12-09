package de.uniol.inf.is.odysseus.generator.process;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.process.util.XESLogDataImporter;

public class ProcessGenerator extends AbstractDataGenerator{
	
	List<DataTuple> eventTuples = Lists.newArrayList();
	int elementCounter = 0;
	long caseIdAppendix = 0;
	boolean firstMod = true;
	@Override
	public List<DataTuple> next() throws InterruptedException {
	
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
				
		if(elementCounter%eventTuples.size() == 0 && elementCounter != 0){
			elementCounter = 0;
			caseIdAppendix++;
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		
		if(firstMod){
			String cazeModification = (String) eventTuples.get(elementCounter).getAttribute(0);
			eventTuples.get(elementCounter).setAttribute(0,cazeModification+"-"+caseIdAppendix);
			firstMod = false;
		} else {
			String cazeModification = (String) eventTuples.get(elementCounter).getAttribute(0);
			cazeModification = cazeModification.split("-")[0];
			eventTuples.get(elementCounter).setAttribute(0,cazeModification+"-"+caseIdAppendix);
			
		}
	
		list.add(eventTuples.get(elementCounter));		
		elementCounter++;

		return list;
	}

	@Override
	public void close() {
		eventTuples.clear();
		elementCounter = 0;
		System.out.println("Close Process Generator ...");
	}

	@Override
	protected void process_init() {
		eventTuples = XESLogDataImporter.getEvents();
		System.out.println("Init Process Generator ...");
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new ProcessGenerator();
	}


}
