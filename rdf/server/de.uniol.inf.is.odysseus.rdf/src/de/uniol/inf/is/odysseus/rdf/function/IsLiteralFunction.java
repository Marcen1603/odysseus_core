package de.uniol.inf.is.odysseus.rdf.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringInputFunction;
import de.uniol.inf.is.odysseus.rdf.datamodel.INode;

public class IsLiteralFunction extends AbstractUnaryStringInputFunction<Boolean>{

	
	private static final long serialVersionUID = -8165539454827148642L;

	public IsLiteralFunction() {
		super("isLiteral", SDFDatatype.BOOLEAN);
	}

	@Override
	public Boolean getValue() {
		Object input = getInputValue(0);
		if (input instanceof INode){
			return ((INode)input).isLiteral();
		}
		return false;
	}

}
