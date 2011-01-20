package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ViewableSDFAttribute implements IViewableAttribute{

	private SDFAttribute attribute;
	private int index;
	
	public ViewableSDFAttribute(SDFAttribute attribute, int index){
		this.attribute = attribute;
		this.index = index;
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
	public Object evaluate(RelationalTuple<? extends IMetaAttribute> tuple) {
		return tuple.getAttribute(this.index);		
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	
	
	
}
