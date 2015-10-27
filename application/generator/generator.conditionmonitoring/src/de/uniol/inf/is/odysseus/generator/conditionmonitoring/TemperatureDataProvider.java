package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.noise.JitterNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;

public class TemperatureDataProvider extends AbstractDataGenerator {

	private ConstantValueGenerator constantLowValue;
	private ConstantValueGenerator constantHighValue;

	private boolean isLow = true;

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(100);
		double random = Math.random();

		// Switch between two normal areas, in average every 20 tuples
		if (random < 0.05)
			isLow = !isLow;

		double value = 0;
		if (isLow) {
			value = constantLowValue.nextValue();
		} else {
			value = constantHighValue.nextValue();
		}

		// Add a factor which makes this tuple an outlier (in average every 50
		// tuples)
		random = Math.random();
		if (random < 0.025)
			value += 8;

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
		constantLowValue = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(2.5)), 0.0);
		constantHighValue = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(0.5)), 20.0);
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new TemperatureDataProvider();
	}

}