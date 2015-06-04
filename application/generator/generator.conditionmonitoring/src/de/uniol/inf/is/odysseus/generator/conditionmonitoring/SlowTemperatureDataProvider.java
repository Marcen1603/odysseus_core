package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.noise.JitterNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;

public class SlowTemperatureDataProvider extends AbstractDataGenerator {

	private IncreaseGenerator increateGenerator;
	private IncreaseGenerator decreaseGenerator;
	
	private IncreaseGenerator currentGenerator;
	
	private double maxValue = 40;
	private double minValue = 0;

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(500);
		
		double value = currentGenerator.nextValue();

		if (value >= maxValue) {
			// Restart
			increateGenerator.init();
			currentGenerator = decreaseGenerator;
		} else if (value <= minValue) {
			decreaseGenerator.init();
			currentGenerator = increateGenerator;
		}
		
		// Create a tuple for that data
		DataTuple tuple = new DataTuple();
		tuple.addDouble(value);

		List<DataTuple> dataTuplesList = new ArrayList<DataTuple>();
		dataTuplesList.add(tuple);
		return dataTuplesList;
	}

	@Override
	public void close() {
	}

	@Override
	protected void process_init() {
		increateGenerator = new IncreaseGenerator(new RandomErrorModel(new JitterNoise(0.5)), minValue, 0.25);
		decreaseGenerator = new IncreaseGenerator(new RandomErrorModel(new JitterNoise(0.5)), maxValue, -0.25);
		currentGenerator = increateGenerator;
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new SlowTemperatureDataProvider();
	}

}