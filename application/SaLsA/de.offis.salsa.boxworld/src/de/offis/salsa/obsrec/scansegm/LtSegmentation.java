package de.offis.salsa.obsrec.scansegm;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;

@ScanSegmentation(name = "LtSegmentation")
public class LtSegmentation implements IScanSegmentation {

	@Override
	public MultiLineString segmentScan(Measurement measurement) {
		// TODO Auto-generated method stub
		return new GeometryFactory().createMultiLineString(null);
	}
}
