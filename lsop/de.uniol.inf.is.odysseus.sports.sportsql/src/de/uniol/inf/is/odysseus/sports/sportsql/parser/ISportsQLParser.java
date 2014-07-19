package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;


/**
 * Interface for SportsQLParser.
 * 
 * @author Thore Stratmann
 *
 */
public interface ISportsQLParser {
	public ILogicalQuery parse(SportsQLQuery sportsQL) throws SportsQLParseException;
}
