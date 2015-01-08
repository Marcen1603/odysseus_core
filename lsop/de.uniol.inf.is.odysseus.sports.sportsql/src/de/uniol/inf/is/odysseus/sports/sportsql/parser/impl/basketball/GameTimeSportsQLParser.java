package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl.basketball;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLBooleanParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLLongParameter;

/*
 * only created java class no OSGI bind!
 * 
 */

@SportsQL(gameTypes = { GameType.BASKETBALL }, statisticTypes = { StatisticType.GLOBAL }, name = "gameTime", parameters = {
		@SportsQLParameter(name = "startts", parameterClass = SportsQLLongParameter.class, mandatory = false),
		@SportsQLParameter(name = "endts", parameterClass = SportsQLLongParameter.class, mandatory = false),
		@SportsQLParameter(name = "isSelectDataBetweenTimestamps", parameterClass = SportsQLBooleanParameter.class, mandatory = false),
		@SportsQLParameter(name = "addMilliseconds", parameterClass = SportsQLBooleanParameter.class, mandatory = false), })
public class GameTimeSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL)
			throws SportsQLParseException, NumberFormatException,
			MissingDDCEntryException {
		// TODO Auto-generated method stub
		return null;
	}

}
