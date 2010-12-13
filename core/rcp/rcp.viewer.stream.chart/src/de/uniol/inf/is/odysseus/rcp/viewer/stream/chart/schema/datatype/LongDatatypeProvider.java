package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;


public class LongDatatypeProvider extends AbstractViewableDatatype<Double> {	
	
	public LongDatatypeProvider() {
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("Long"));
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("StartTimestamp"));
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("EndTimestamp"));
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("Timestamp"));		
		super.addProvidedClass(Long.class);
		super.addProvidedClass(long.class);
	}
	
	@Override
	public Double convertToValue(Object value) {
		Long longValue = (Long) value;
		return new Double(longValue.doubleValue());
	}

}
