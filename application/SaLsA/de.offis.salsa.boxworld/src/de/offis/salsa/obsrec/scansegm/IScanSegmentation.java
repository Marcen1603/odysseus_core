package de.offis.salsa.obsrec.scansegm;

import com.vividsolutions.jts.geom.MultiLineString;

import de.offis.salsa.lms.model.Measurement;

public interface IScanSegmentation {
	public MultiLineString segmentScan(Measurement measurement);
}
