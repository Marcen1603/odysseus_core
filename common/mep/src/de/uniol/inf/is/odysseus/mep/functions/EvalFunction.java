package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 * Eval function for expressions in String attributes
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class EvalFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -1282864723656994864L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.STRING }};

	public EvalFunction() {
		super("eval", 1, accTypes, SDFDatatype.OBJECT, false);
	}
	
	@Override
	public Object getValue() {
		SDFExpression expression = new SDFExpression("",
				(String) getInputValue(0), null, MEP.getInstance(), null);
		return expression.getValue();
	}

}
