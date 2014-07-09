package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

@SportsQL(gameTypes = {GameType.BASKETBALL} ,statisticTypes  = {StatisticType.TEAMSTATISTIC}, name = "mileageteam")
public class MileageTeamSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(String sportsQL) {
		return null;
	}

}
