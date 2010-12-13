package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public interface IViewableDatatype<T> {
	
	public T convertToValue(Object value);
	public List<SDFDatatype> getProvidedSDFDatatypes();
	public List<Class<?>> getProvidedClasses();
	

}
