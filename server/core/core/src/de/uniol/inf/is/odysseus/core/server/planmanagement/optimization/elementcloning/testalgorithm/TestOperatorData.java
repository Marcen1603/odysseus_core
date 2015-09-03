package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning.testalgorithm;

import java.util.HashMap;
import java.util.Map;

public class TestOperatorData 
{
	enum State
	{
		UNINITIALIZED,
		MOD_PATH,
		NON_MOD_PATH
	}
	
	public Map<Integer, State> states = new HashMap<>();
}
