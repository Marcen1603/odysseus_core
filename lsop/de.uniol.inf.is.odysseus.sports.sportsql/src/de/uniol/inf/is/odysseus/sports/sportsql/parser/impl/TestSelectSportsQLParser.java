package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLParseException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;

/**
 * This is a very easy plan to test if the translation works. Always selects for entity-id 8.
 * @author Tobias
 *
 */
@SportsQL(
		gameTypes = { GameType.SOCCER }, 
		statisticTypes = {StatisticType.GLOBAL}, 
		name = "testSelect",
		parameters = { }
		)
public class TestSelectSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL)
			throws SportsQLParseException {
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		StreamAO access = OperatorBuildHelper.createGameStreamAO();
		SelectAO testSelect = OperatorBuildHelper.createEntityIDSelect(8, access);
		allOperators.add(access);
		allOperators.add(testSelect);
		return OperatorBuildHelper.finishQuery(testSelect, allOperators, sportsQL.getName());
	}

}
