package de.offis.salsa.obsrec.scansegm;

import java.util.List;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.lms.model.Sample;

public interface IScanSegmentation {
	public List<List<Sample>> segmentScan(Measurement measurement);
}
