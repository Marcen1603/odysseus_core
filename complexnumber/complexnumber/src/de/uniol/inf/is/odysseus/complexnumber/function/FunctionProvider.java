package de.uniol.inf.is.odysseus.complexnumber.function;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> funcs = new LinkedList<>();
		funcs.add(new ComplexNumberDivisionOperator());
		funcs.add(new ComplexNumberMinusOperator());
		funcs.add(new ComplexNumberMultOperator());
		funcs.add(new ComplexNumberPlusOperator());
		funcs.add(new CreateComplexNumberFunction());
		funcs.add(new ComplexNumberAbsFunction());
		funcs.add(new AsComplexNumberFunction());
		return funcs;
	}

}
