package de.uniol.inf.is.odysseus.core.server.datadictionary;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IDataDictionarySinkListener {

	public void addedSinkDefinition(IDataDictionary sender, String name, ILogicalPlan op, ISession caller);

	public void removedSinkDefinition(IDataDictionary sender, String name, ILogicalPlan op, ISession caller);

}
