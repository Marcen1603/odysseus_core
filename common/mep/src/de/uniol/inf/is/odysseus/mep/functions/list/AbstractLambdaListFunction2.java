package de.uniol.inf.is.odysseus.mep.functions.list;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

abstract public class AbstractLambdaListFunction2 extends AbstractLambdaListFunction {

	private static final long serialVersionUID = -8269748747594828192L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists() , 	{ SDFDatatype.STRING } };


	public AbstractLambdaListFunction2(String name) {
		super(name, 2, accTypes);
	}

}
