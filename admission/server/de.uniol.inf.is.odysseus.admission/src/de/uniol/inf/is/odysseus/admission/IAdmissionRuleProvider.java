package de.uniol.inf.is.odysseus.admission;

import java.util.Collection;

public interface IAdmissionRuleProvider {

	public Collection<IAdmissionRule<?>> getRules();
	
}
