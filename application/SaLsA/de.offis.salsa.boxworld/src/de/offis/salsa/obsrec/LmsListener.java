package de.offis.salsa.obsrec;

import de.offis.salsa.lms.model.Measurement;

public interface LmsListener {
	void onMeasurement(Measurement m);
}
