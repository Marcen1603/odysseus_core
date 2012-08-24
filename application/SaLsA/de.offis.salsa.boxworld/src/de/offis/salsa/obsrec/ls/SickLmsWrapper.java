package de.offis.salsa.obsrec.ls;

import de.offis.salsa.lms.SickConnectionImpl;
import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.obsrec.AntiJitter;
import de.offis.salsa.obsrec.LmsListener;

public class SickLmsWrapper extends SickConnectionImpl {

	private LmsListener listener;
	
	private AntiJitter jitter = new AntiJitter();
	
	public SickLmsWrapper(String host, int port, LmsListener listener) {
		super(host, port);
		this.listener = listener;
	}

	@Override
	public void onMeasurement(Measurement measurement, long timestamp) {
		if(listener != null){
			listener.onMeasurement(measurement);
		}
	}
}
