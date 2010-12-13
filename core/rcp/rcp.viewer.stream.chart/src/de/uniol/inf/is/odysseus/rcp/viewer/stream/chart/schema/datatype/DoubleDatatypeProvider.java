package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;


public class DoubleDatatypeProvider extends AbstractViewableDatatype<Double>
{

	public DoubleDatatypeProvider() {
		super.addProvidedSDFDatatype(SDFDatatypeFactory.getDatatype("Double"));			
		super.addProvidedClass(Double.class);
		super.addProvidedClass(double.class);		
	}
	
	@Override
	public Double convertToValue(Object value) {
		return (Double) value;
	}

}
