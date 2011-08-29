package de.uniol.inf.is.odysseus.ac;

import java.util.List;

public interface IAdmissionReaction {

	public IPossibleExecution react( List<IPossibleExecution> possibilities );
	
}
