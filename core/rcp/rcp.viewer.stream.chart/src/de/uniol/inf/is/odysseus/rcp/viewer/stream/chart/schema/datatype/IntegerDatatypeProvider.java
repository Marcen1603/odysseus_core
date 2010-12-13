package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;


public class IntegerDatatypeProvider extends AbstractViewableDatatype<Double> {

	public IntegerDatatypeProvider() {
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		super.addProvidedClass(Integer.class);
		super.addProvidedClass(int.class);
	}
	
	@Override
	public Double convertToValue(Object value) {
		Integer valInt = (Integer) value;		
		return valInt.doubleValue();
	}	

}
