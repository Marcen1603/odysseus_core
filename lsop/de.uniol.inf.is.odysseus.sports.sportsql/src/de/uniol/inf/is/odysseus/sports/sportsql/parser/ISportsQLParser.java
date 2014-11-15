package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;


/**
 * Interface for SportsQLParser.
 * 
 * @author Thore Stratmann
 *
 */
public interface ISportsQLParser {
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL) throws SportsQLParseException, NumberFormatException, MissingDDCEntryException;
}
