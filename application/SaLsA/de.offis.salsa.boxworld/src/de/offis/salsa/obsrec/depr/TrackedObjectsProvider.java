package de.offis.salsa.obsrec.depr;

import java.util.List;

import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.SensorMeasurement;
@Deprecated
public interface TrackedObjectsProvider {
	List<TrackedObject> getTrackedObjects(SensorMeasurement measurement);
}
