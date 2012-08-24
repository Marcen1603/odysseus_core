package de.offis.salsa.obsrec.datasegm;

import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.SensorMeasurement;

public interface ILmsDataSegmenter {
	public List<List<Sample>> segmentScan(SensorMeasurement measurement);
}
