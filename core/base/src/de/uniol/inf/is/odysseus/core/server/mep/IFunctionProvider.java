package de.uniol.inf.is.odysseus.core.server.mep;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;

public interface IFunctionProvider {

	public List<IFunction<?>> getFunctions();
}
