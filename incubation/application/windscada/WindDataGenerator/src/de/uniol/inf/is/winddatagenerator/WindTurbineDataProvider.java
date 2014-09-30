package de.uniol.inf.is.winddatagenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.IDataGenerator;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.TimeGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;
import de.uniol.inf.is.winddatagenerator.ConsoleInputReader.State;

public class WindTurbineDataProvider extends AbstractDataGenerator implements
		Observer {

	private final int SLEEP = 200;
	private BufferedReader reader;

	private int id;
	private ISingleValueGenerator time;
	private ISingleValueGenerator direction;
	private ISingleValueGenerator rotationalSpeed;
	private ISingleValueGenerator phase;
	private ISingleValueGenerator pitch;
	private ISingleValueGenerator gear;

	private ISingleValueGenerator zeroGenerator;
	private State state;

	public WindTurbineDataProvider(int id2) {
		this.id = id2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.generator.IDataGenerator#next()
	 */
	@Override
	public List<DataTuple> next() throws InterruptedException {
		String line;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String[] parts = CSVParser.parseCSV(line, ',', true);
		DataTuple tuple = new DataTuple();
		// wka_id
		tuple.addInteger(id);
		// timestamp
		tuple.addLong(Calendar.getInstance().getTimeInMillis());
		switch (this.state) {
		case ON:
			// wind_speed
			tuple.addDouble(parts[1]);
			// corrected_score
			tuple.addDouble(parts[4]);
			// wind_direction
			tuple.addDouble(direction.nextValue());
			// rotational_speed
			tuple.addDouble(Double.valueOf(parts[4]) / 100);
			// phase_shift
			tuple.addDouble(phase.nextValue());
			// pitch_angle
			tuple.addDouble(pitch.nextValue());
			// gear_angle
			tuple.addDouble(gear.nextValue());
			break;
		case OFF:
			// wind_speed
			tuple.addDouble(0);
			// corrected_score
			tuple.addDouble(0);
			// wind_direction
			tuple.addDouble(0);
			// rotational_speed
			tuple.addDouble(0);
			// phase_shift
			tuple.addDouble(0);
			// pitch_angle
			tuple.addDouble(0);
			// gear_angle
			tuple.addDouble(0);
			break;
		}
		Thread.sleep(this.SLEEP);
		ArrayList<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.generator.IDataGenerator#close()
	 */
	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.generator.AbstractDataGenerator#process_init()
	 */
	@Override
	protected void process_init() {
		Bundle bundle = Platform.getBundle("de.uniol.inf.is.WindDataGenerator");
		URL url = bundle.getResource("/data/2005/" + id + ".csv");
		File file;
		try {
			file = new File(FileLocator.toFileURL(url).getPath());
			FileReader fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		time = new TimeGenerator(new NoError());
		time.init();
		direction = new AlternatingGenerator(new NoError(), 0, 0.2, 0, 359);
		direction.init();
		phase = new AlternatingGenerator(new NoError(), 0, 10, -90, 90);
		phase.init();
		rotationalSpeed = new AlternatingGenerator(new NoError(), 0, 0.005, 0,
				0.3);
		rotationalSpeed.init();
		pitch = new AlternatingGenerator(new NoError(), 0, 1, 0, 90);
		pitch.init();
		gear = new AlternatingGenerator(new NoError(), 2, 0.2, 0, 359);
		gear.init();
		zeroGenerator = new ConstantValueGenerator(new NoError(), 0);
		zeroGenerator.init();
		this.state = ConsoleInputReader.getInstance().getState();
		ConsoleInputReader.getInstance().addObserver(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.generator.AbstractDataGenerator#newCleanInstance
	 * ()
	 */
	@Override
	public IDataGenerator newCleanInstance() {
		return new WindTurbineDataProvider(id);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		switch (ConsoleInputReader.getInstance().getState()) {
		case ON:
			this.state = State.ON;
			break;
		case OFF:
			this.state = State.OFF;
			break;

		}
	}

}
