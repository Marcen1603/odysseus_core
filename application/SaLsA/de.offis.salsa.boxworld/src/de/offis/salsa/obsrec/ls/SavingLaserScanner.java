package de.offis.salsa.obsrec.ls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.Objektwelt;
import de.offis.salsa.obsrec.SensorMeasurement;

public class SavingLaserScanner extends SickLaserScanner {
		String filename;
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		public SavingLaserScanner(Objektwelt world, String host, int port, String filename) {
			super(world, host, port);
			this.filename = filename;
		}
		
		public void onMeasure(){
			// call this method everytime the real sensor wnats to send me data
			if(world != null){
				Measurement m = new Measurement();
				
				List<Sample> ss = new ArrayList<Sample>();
				for(int i = 0 ; i <= 70; i+=5){
					Sample s = new Sample();
					s.setAngle(i);
					s.setDist1(2000);
					ss.add(s);
				}
				
				for(int i = 80 ; i <= 180; i+=5){
					Sample s = new Sample();
					s.setAngle(i);
					s.setDist1(600);
					ss.add(s);
				}
				
				for(int i = 190 ; i <= 270; i+=5){
					Sample s = new Sample();
					s.setAngle(i);
					s.setDist1(900);
					ss.add(s);
				}
				
				for(int i = 280 ; i <= 360; i+=5){
					Sample s = new Sample();
					s.setAngle(i);
					s.setDist1(1200);
					ss.add(s);
				}
				
				m.setSamples(ss.toArray(new Sample[0]));
				SensorMeasurement measurement = new SensorMeasurement(m);
				world.receiveMeasure(measurement);
			}
		}

		public void connect() {
			try {
				fos = new FileOutputStream(filename);
				out = new ObjectOutputStream(fos);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			super.connect();
		}
		
		@Override
		public void onMeasurement(Measurement m) {
			if(world != null){
				
				try {
					out.writeObject(m);
					System.out.println("Object Persisted");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				world.receiveMeasure(new SensorMeasurement(m));
			}
		}
	

}
