package de.offis.salsa.obsrec.scansegm;

import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.SensorMeasurement;

public interface IScanSegmentation {
	public List<List<Sample>> segmentScan(SensorMeasurement measurement);
}
