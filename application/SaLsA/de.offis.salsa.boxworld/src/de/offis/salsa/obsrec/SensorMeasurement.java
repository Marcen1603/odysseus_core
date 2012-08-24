package de.offis.salsa.obsrec;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;

public class SensorMeasurement {

	private Measurement measurement;
	
	public SensorMeasurement(SensorMeasurement measurement) {
		this.measurement = measurement.measurement;
	}

	public SensorMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
	
	public Sample[] getData(){
		return measurement.getSamples();
	}
}
