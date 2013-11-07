package de.uniol.inf.is.odysseus.generator.heatmap;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;

public class HeatmapDataProvider extends AbstractDataGenerator {

	int counter;
	static int WAITING_TIME_MILLIS = 1000;
	
	@Override
	public void process_init() {
		counter = 0;
		System.out.println("Heatmap Daten-Generator gestartet.");
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		List<DataTuple> tuples = new ArrayList<DataTuple>();
		tuples.add(createSpatialTuple());
		waitSomeTime();
		System.out.println("Tupel erstellt.");
		return tuples;
	}

	@Override
	public HeatmapDataProvider newCleanInstance() {
		return new HeatmapDataProvider();
	}
	
	private DataTuple createSpatialTuple() {

		DataTuple tuple = new DataTuple();

		if (counter % 2 == 0) {
			tuple.addAttribute(51.00); // lat
			tuple.addAttribute(8.00); // lng
			tuple.addAttribute(5);   // value
		} else {
			tuple.addAttribute(48.00);  // lat
			tuple.addAttribute(10.00); // lng
			tuple.addAttribute(25);   // value
		}
		counter++;
		// TODO: Timestamps
		return tuple;
	}
	
	private void waitSomeTime() {
		try {
			Thread.sleep(WAITING_TIME_MILLIS);
		} catch (InterruptedException ex) {}
	}

}
