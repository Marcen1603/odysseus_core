package de.uniol.inf.is.odysseus.complexnumber.function;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new LinkedList<>();
		funcs.add(new ComplexNumberDivisionOperator());
		funcs.add(new ComplexNumberMinusOperator());
		funcs.add(new ComplexNumberMultOperator());
		funcs.add(new ComplexNumberPlusOperator());
		funcs.add(new CreateComplexNumberFunction());
		funcs.add(new ComplexNumberAbsFunction());
		return funcs;
	}

}
