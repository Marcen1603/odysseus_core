package de.uniol.inf.is.odysseus.condition.conditionql.parser;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IConditionQLParser  {
	public ILogicalQuery parse(ISession session, ConditionQLQuery conditionQL);

}
