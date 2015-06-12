package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.noise.JitterNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;

public class MillingCutterPowerConsumptionDataProvider extends AbstractDataGenerator {

	private ISingleValueGenerator standbyGenerator;
	private ISingleValueGenerator firstSaddlePointGenerator;
	private ISingleValueGenerator secondSaddlePointGenerator;
	private ISingleValueGenerator thirdSaddlePointGenerator;

	private double standbyConsumption = 20;
	private double firstSaddlePoint = 100;
	private double secondSaddlePoint = 300;
	private double thirdSaddlePoint = 50;

	private int counter;
	private int phase;
	private int[] phases;

	private int standbyMinLength = 50;
	private int standbyMaxLength = 100;
	// Counts, how many pieces the milling cutter produced
	private int productionCouter;

	@Override
	public List<DataTuple> next() throws InterruptedException {
		Thread.sleep(50);
		double value = 0;
		if (counter >= phases[phase]) {
			phase = (phase + 1) % phases.length;
			counter = 0;

			// The standby phase has a random length
			phases[0] = (int) (standbyMinLength + (Math.random() * (standbyMaxLength - standbyMinLength)));
			if (phase == 0) {
				// Produce the next piece
				productionCouter++;
			}
		}

		switch (phase) {
		case 0:
			value = standbyGenerator.nextValue();
			break;
		case 1:
			value = firstSaddlePointGenerator.nextValue();
			break;
		case 2:
			value = secondSaddlePointGenerator.nextValue();
			break;
		case 3:
			value = thirdSaddlePointGenerator.nextValue();
			break;
		}

		// Some wrong build pieces
		if (productionCouter == 20) {
			// Error: Too high energy consumption during processing
			if (value >= standbyConsumption + 3) {
				// We are not in standby
				value += 10;
			}
		} else if (productionCouter == 25) {
			// We have some peaks which should not be there
			double random = Math.random();
			if (phase > 0 && random < 0.05) {
				value += 40;
			}
		} else if (productionCouter == 30) {
			// The machine leaves out the third production part
			if (phase == 3) {
				phase = 0;
				productionCouter++;
			}
		}
	
		if (phase == 2 && counter >= 12 && counter <= 15) {
			value = 20;
		}

		counter++;

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
		phases = new int[4];
		// Cutter is off
		phases[0] = 70;
		// Cutter is on
		phases[1] = 25;
		phases[2] = 25;
		phases[3] = 25;

		// Low standby consumption
		standbyGenerator = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(1.0)), standbyConsumption);

		firstSaddlePointGenerator = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(3.0)),
				firstSaddlePoint);
		secondSaddlePointGenerator = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(3.0)),
				secondSaddlePoint);
		thirdSaddlePointGenerator = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(3.0)),
				thirdSaddlePoint);
	}

	@Override
	public IDataGenerator newCleanInstance() {
		return new MillingCutterPowerConsumptionDataProvider();
	}
}
