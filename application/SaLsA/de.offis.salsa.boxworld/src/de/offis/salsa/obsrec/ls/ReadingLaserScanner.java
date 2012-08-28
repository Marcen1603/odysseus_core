package de.offis.salsa.obsrec.ls;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.obsrec.Objectworld;
import de.offis.salsa.obsrec.SensorMeasurement;

public class ReadingLaserScanner extends SickLaserScanner {

	private String filename;
	FileInputStream fis = null;
	ObjectInputStream in = null;
	
	public ReadingLaserScanner(Objectworld world, String filename) {
		super(world);
		this.filename = filename;
	}
	
	public void connect() {
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	};
	
	public void start() {
		Measurement m = null;
		try {
			while ((m = (Measurement) in.readObject()) != null) {
				onMeasurement(m);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

	@Override
	public void onMeasurement(Measurement m) {
		if(world != null){
			world.receiveMeasure(new SensorMeasurement(m));
		}
	}
}
