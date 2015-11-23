package de.uniol.inf.is.odysseus.server.opcua.access;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.server.opcua.func.ErrorFunction;
import de.uniol.inf.is.odysseus.server.opcua.func.QualityFunction;
import de.uniol.inf.is.odysseus.server.opcua.func.StatusFunctions.ToErrorStrFunction;
import de.uniol.inf.is.odysseus.server.opcua.func.StatusFunctions.ToErrorValFunction;
import de.uniol.inf.is.odysseus.server.opcua.func.TimestampFunction;
import de.uniol.inf.is.odysseus.server.opcua.func.ToOPCValueFunction;
import de.uniol.inf.is.odysseus.server.opcua.func.ValueFunction;

public class OPCUAFunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new LinkedList<IFunction<?>>();
		funcs.add(new ToOPCValueFunction());
		funcs.add(new ErrorFunction());
		funcs.add(new QualityFunction());
		funcs.add(new TimestampFunction());
		funcs.add(new ValueFunction<Object>());
		funcs.add(new ToErrorValFunction());
		funcs.add(new ToErrorStrFunction());
		return funcs;
	}
}