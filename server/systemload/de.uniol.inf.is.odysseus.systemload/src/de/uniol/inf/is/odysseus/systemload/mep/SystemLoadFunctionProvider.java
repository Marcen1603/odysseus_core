package de.uniol.inf.is.odysseus.systemload.mep;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class SystemLoadFunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> functions = Lists.newArrayList();
		
		functions.add(new CPUFreeFunction());
		functions.add(new CPUMaxFunction());
		functions.add(new CPULoadFunction());
		
		functions.add(new MEMFreeFunction());
		functions.add(new MEMMaxFunction());
		functions.add(new MEMLoadFunction());
		
		functions.add(new NETFreeFunction());
		functions.add(new NETMaxFunction());
		functions.add(new NETLoadFunction());
		
		return functions;
	}

}
