package de.offis.salsa.obsrec.ls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.Objektwelt;
import de.offis.salsa.obsrec.SensorMeasurement;

public class DebugLaserScanner extends SickLaserScanner {

	public DebugLaserScanner(Objektwelt world) {
		super(world);
	}
	
	@Override
	public void onMeasurement(Measurement mm) {
		// call this method everytime the real sensor wnats to send me data
		if(world != null){
			Measurement m = new Measurement();
			
			List<Sample> ss = new ArrayList<>();
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
	
	public void start() {
		while (true) {
			onMeasurement(null);
			try {
				Thread.sleep((1/50)*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}	
}
