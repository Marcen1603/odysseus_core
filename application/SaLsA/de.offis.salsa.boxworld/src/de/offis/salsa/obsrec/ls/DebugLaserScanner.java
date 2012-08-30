package de.offis.salsa.obsrec.ls;

import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.Objectworld;

public class DebugLaserScanner extends SickLaserScanner {

	public DebugLaserScanner(Objectworld world) {
		super(world);
	}
	
	@Override
	public void onMeasurement(Measurement measurement) {
		// call this method everytime the real sensor wnats to send me data
		if(world != null){
			Measurement m = new Measurement();
			
			List<Sample> ss = new ArrayList<Sample>();
			for(int i = 0 ; i <= 70; i+=5){
				// geradegleichung in polarform 1/[(sin(t)-(1/2)cos(t)]
				// .. oder auch nich
				
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
			world.receiveMeasure(m);
		}
	}
	
	public void start() {
		while (true) {
			onMeasurement(null);
			try {
				Thread.sleep((1/25)*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}	
}
