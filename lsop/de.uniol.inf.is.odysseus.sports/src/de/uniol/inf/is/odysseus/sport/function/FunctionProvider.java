package de.uniol.inf.is.odysseus.sport.function;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.sports.mep.AccessToDCCFunction;
import de.uniol.inf.is.odysseus.sports.mep.GetAllBallSensorIDFunction;

public class FunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new LinkedList<>();
		funcs.add(new AccessToDCCFunction());
		funcs.add(new GetAllBallSensorIDFunction());
		return funcs;
	}

}