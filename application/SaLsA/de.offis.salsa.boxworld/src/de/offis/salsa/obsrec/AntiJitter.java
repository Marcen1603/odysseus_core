package de.offis.salsa.obsrec;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;

public class AntiJitter {
	
	private static final double TRESHOLD = 40;
	
	private Sample[] keyframe = null;
	private Measurement last = null;
	
	
	
	public Measurement deJitter(Measurement measurement){
		// TODO changing the incoming measurement is dirty and could produce side effects
		if(last == null){
			last = measurement;	
			return measurement;
		}
		
		if(measurement.getSamples().length != last.getSamples().length)
			return measurement;
		
		if(keyframe == null){
			keyframe = new Sample[measurement.getSamples().length];
			
			for(int i = 0 ; i < measurement.getSamples().length ; i++){
				keyframe[i] = measurement.getSamples()[i];
			}
		} else {
			Sample[] newSamples = measurement.getSamples();
			
			for(int i = 0 ; i < measurement.getSamples().length ; i++){
				Sample old = keyframe[i];
				Sample current = measurement.getSamples()[i];
				
				if(Math.abs(old.getDist1() - current.getDist1()) > TRESHOLD){
					keyframe[i] = current;
				} else {
					newSamples[i] = keyframe[i];
				}
			}
			
			measurement.setSamples(newSamples);
		}
		
		
		
		last = measurement;		
		return measurement;
	}
}
