package de.uniol.inf.is.odysseus.mep.functions.objectmap;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.collection.ObjectMap;

public class OMGetElementFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = 2632561533834017628L;
	
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
		new SDFDatatype[] { SDFDatatype.OBJECT_MAP}, {SDFDatatype.STRING}};
	
	public OMGetElementFunction() {
		super("getElement", 2, acceptedTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		ObjectMap<IMetaAttribute> om = getInputValue(0);
		String key = getInputValue(1);
		return om.getValue(key);
	}
}
