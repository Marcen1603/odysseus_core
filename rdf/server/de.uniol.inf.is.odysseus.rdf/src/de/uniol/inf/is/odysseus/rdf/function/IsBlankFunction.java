package de.uniol.inf.is.odysseus.rdf.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringInputFunction;
import de.uniol.inf.is.odysseus.rdf.datamodel.INode;

public class IsBlankFunction extends AbstractUnaryStringInputFunction<Boolean>{

	
	private static final long serialVersionUID = -8165539454827148642L;

	public IsBlankFunction() {
		super("isBlank", SDFDatatype.BOOLEAN);
	}

	@Override
	public Boolean getValue() {
		Object input = getInputValue(0);
		if (input instanceof INode){
			return ((INode)input).isBlankNode();
		}
		return false;
	}

}
