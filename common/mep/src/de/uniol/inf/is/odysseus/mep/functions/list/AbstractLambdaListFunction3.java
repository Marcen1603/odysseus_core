package de.uniol.inf.is.odysseus.mep.functions.list;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

abstract public class AbstractLambdaListFunction3 extends AbstractLambdaListFunction {

	private static final long serialVersionUID = -8269748747594828192L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists() , { SDFDatatype.TUPLE },
			{ SDFDatatype.STRING } };

	public AbstractLambdaListFunction3(String name) {
		super(name, 3, accTypes);
	}

}
