package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class PointInTimeDatatypeProvider extends AbstractViewableDatatype<Double> {

	public PointInTimeDatatypeProvider(){
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("PointInTime"));
		super.addProvidedClass(PointInTime.class);
	}
	
	@Override
	public Double convertToValue(Object value) {
		PointInTime time = (PointInTime) value;		
		return new Double(time.getMainPoint());		
	}

}
