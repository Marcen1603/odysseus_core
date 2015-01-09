package de.uniol.inf.is.odysseus.s100;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.s100.functions.*;

/**
 * Functions that registers S100 functions in ODYSSEUS.
 * 
 * @author Henrik Surm
 */
public class S100FunctionProvider implements IFunctionProvider 
{
	private static final Logger LOG = LoggerFactory.getLogger(S100FunctionProvider.class);
	
	public S100FunctionProvider() 
	{
	}
	
	@Override
	public List<IFunction<?>> getFunctions() 
	{
		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		functions.add(new NewGMPoint());
		functions.add(new ToGMPoint());
		functions.add(new GetGMPointCoordinate());
		functions.add(new SetGMPointCoordinate());
		S100FunctionProvider.LOG.info(String.format("Register functions: %s", functions));
		return functions;
	}
}
