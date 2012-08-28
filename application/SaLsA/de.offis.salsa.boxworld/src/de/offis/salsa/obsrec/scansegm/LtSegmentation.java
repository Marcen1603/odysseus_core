package de.offis.salsa.obsrec.scansegm;

import java.util.List;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.SensorMeasurement;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;

@ScanSegmentation(name = "LtSegmentation")
public class LtSegmentation implements IScanSegmentation {

	@Override
	public List<List<Sample>> segmentScan(SensorMeasurement measurement) {
		// TODO Auto-generated method stub
		return null;
	}
}
