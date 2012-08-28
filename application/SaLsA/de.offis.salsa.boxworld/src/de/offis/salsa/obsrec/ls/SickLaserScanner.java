package de.offis.salsa.obsrec.ls;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.obsrec.AntiJitter;
import de.offis.salsa.obsrec.Objectworld;
import de.offis.salsa.obsrec.SensorMeasurement;

public class SickLaserScanner extends AbstractLaserScanner {
	
	protected SickLmsWrapper sick;
	
	private AntiJitter jitter = new AntiJitter();
	
	public SickLaserScanner(Objectworld world, String host, int port) {
		super(world);
		this.sick = new SickLmsWrapper(host, port, this);
	}
	
	public SickLaserScanner(Objectworld world) {
		super(world);
	}

	public void connect() {
		if(sick != null){
			sick.open();
		}
	}
	
	@Override
	public void onMeasurement(Measurement m) {
		if(world != null){
			world.receiveMeasure(new SensorMeasurement(jitter.deJitter(m)));
		}
	}
}
