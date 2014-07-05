package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.Game;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(games = {Game.BASKETBALL} ,types = {StatisticType.TEAMSTATISTIC}, name = "mileageteam")
public class MileageTeamSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(String sportsQL) {
		return null;
	}

}
