package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public abstract class AbstractViewableDatatype<T> implements IViewableDatatype<T> {

	private List<SDFDatatype> datatypes = new ArrayList<SDFDatatype>();
	private List<Class<?>> classes = new ArrayList<Class<?>>();
	
	
	protected void addProvidedSDFDatatype(SDFDatatype datatype){
		this.datatypes.add(datatype);
	}
	
	protected void addProvidedClass(Class<?> classe){
		this.classes.add(classe);
	}
	
	
	@Override
	public List<SDFDatatype> getProvidedSDFDatatypes() {
		return this.datatypes;
	}

	@Override
	public List<Class<?>> getProvidedClasses() {
		return this.classes;
	}

}
