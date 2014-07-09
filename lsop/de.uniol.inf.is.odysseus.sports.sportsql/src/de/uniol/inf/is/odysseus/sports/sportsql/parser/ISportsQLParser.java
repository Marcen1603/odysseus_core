package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public interface ISportsQLParser {
	public ILogicalQuery parse(SportsQLQuery sportsQL);
}
