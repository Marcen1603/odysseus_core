package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ViewableSDFAttribute<T> implements IViewableAttribute<T>{

	private SDFAttribute attribute;
	
	public ViewableSDFAttribute(SDFAttribute attribute){
		this.attribute = attribute;
	}

	@Override
	public String getName() {
		return attribute.getAttributeName();
	}

	@Override
	public SDFDatatype getSDFDatatype() {		
		return attribute.getDatatype();
	}

	@Override
	public T evaluate(int index, RelationalTuple<? extends IMetaAttribute> tuple) {
		return tuple.getAttribute(index);		
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	
	
	
}
