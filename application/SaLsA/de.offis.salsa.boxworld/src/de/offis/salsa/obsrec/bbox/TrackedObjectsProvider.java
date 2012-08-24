package de.offis.salsa.obsrec.bbox;

import java.util.List;

import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.SensorMeasurement;

public interface TrackedObjectsProvider {
	List<TrackedObject> getTrackedObjects(SensorMeasurement measurement);
}
