package de.uniol.inf.is.odysseus.mep.math;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.mep.commons.math.BinomialTestFunction;
import de.uniol.inf.is.odysseus.mep.commons.math.DBSCANFunction;
import de.uniol.inf.is.odysseus.mep.commons.math.DBSCANFunction2;
import de.uniol.inf.is.odysseus.mep.commons.math.DBSCANFunction3;
import de.uniol.inf.is.odysseus.mep.commons.math.DBSCANFunction4;

public class MathFunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		final List<IMepFunction<?>> functions = new ArrayList<>();
		functions.add(new BinomialTestFunction());
		functions.add(new DBSCANFunction());
		functions.add(new DBSCANFunction2());
		functions.add(new DBSCANFunction3());
		functions.add(new DBSCANFunction4());
		return functions;
	}

}
