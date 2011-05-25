package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class SDFFunctionProvider implements IFunctionProvider{

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> funcs = new ArrayList<IFunction<?>>();
		
		funcs.add(new DolToEur());
		funcs.add(new Now());
		funcs.add(new Distance());
		funcs.add(new Polygon());
		
		return funcs;
	}

	
	
}
