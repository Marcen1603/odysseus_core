package de.offis.salsa.obsrec.scansegm;

import java.util.ArrayList;
import java.util.List;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;

@ScanSegmentation(name = "LtSegmentation")
public class LtSegmentation implements IScanSegmentation {

	@Override
	public List<List<Sample>> segmentScan(Measurement measurement) {
		// TODO Auto-generated method stub
		List<List<Sample>> segments = new ArrayList<List<Sample>>();
		return segments;
	}
}
