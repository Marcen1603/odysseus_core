package de.uniol.inf.is.odysseus.generator.hmm;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class HMMGenerator extends StreamClientHandler {

	private int number = 0;
	private int nextIndex = 0;

	private String[] observations = { "walk", "shop", "clean" };

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// number / time
		tuple.addAttribute(new Long(number));
		// observation
		tuple.addAttribute(observations[nextIndex]);

		number++;
		nextIndex++;
		if (nextIndex == observations.length) {
			nextIndex = 0;
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}
	

	@Override
	public void init() {

	}

	@Override
	public void close() {		

	}
	
	@Override
	public StreamClientHandler clone() {
		return new HMMGenerator();
	}

}
