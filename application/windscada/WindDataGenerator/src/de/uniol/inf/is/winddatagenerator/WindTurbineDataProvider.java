package de.uniol.inf.is.winddatagenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class WindTurbineDataProvider extends AbstractDataGenerator {
	
	private final int SLEEP = 200;
	private BufferedReader reader;
	
	private int id;
	private ISingleValueGenerator time;
	private ISingleValueGenerator direction;
	private ISingleValueGenerator rotationalSpeed;
	private ISingleValueGenerator phase;
	private ISingleValueGenerator pitch;
	private ISingleValueGenerator gear;

	public WindTurbineDataProvider(int id2) {
		this.id = id2;
	}

	/*
	 * (non-Javadoc)
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
		// wind_speed
		tuple.addDouble(parts[1]);
		// corrected_score
		tuple.addDouble(parts[4]);
		// wind_direction
		tuple.addDouble(direction.nextValue());
		// rotational_speed
		tuple.addDouble(rotationalSpeed.nextValue());
		// phase_shift
		tuple.addDouble(phase.nextValue());
		// pitch_angle
		tuple.addDouble(pitch.nextValue());
		// gear_angle
		tuple.addDouble(gear.nextValue());
		Thread.sleep(this.SLEEP);
		ArrayList<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

	/*
	 * (non-Javadoc)
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
	 * @see de.uniol.inf.is.odysseus.generator.AbstractDataGenerator#process_init()
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
		direction = new AlternatingGenerator(new NoError(), 0, 1, 0, 359);
		direction.init();
		phase = new ConstantValueGenerator(new NoError(),0);
		phase.init();
		rotationalSpeed = new AlternatingGenerator(new NoError(),0,0.05,-0.3,0.3);
		rotationalSpeed.init();
		pitch = new AlternatingGenerator(new NoError(),0,1,0,90);
		pitch.init();
		gear = new AlternatingGenerator(new NoError(),0,1,0,359);
		gear.init();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.generator.AbstractDataGenerator#newCleanInstance()
	 */
	@Override
	public IDataGenerator newCleanInstance() {
		return new WindTurbineDataProvider(id);
	}

}
