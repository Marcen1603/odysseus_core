package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQL;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.annotations.SportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.SportsQLParameterHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.GameType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.enums.StatisticType;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

@SportsQL(
		gameTypes = { GameType.SOCCER },
		statisticTypes = { StatisticType.PLAYER },
		name = "mileage",
		parameters = { 
				@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
				@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) 				}
		)
public class MileagePlayerSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(ISession session, SportsQLQuery sportsQL) throws NumberFormatException, MissingDDCEntryException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO(session);
		allOperators.add(soccerGameStreamAO);

		// ----------------------------------------------------
		// First part of the query (Select for questioned time)
		// ----------------------------------------------------

		// 1. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper.getTimeParameter(sportsQL);

		SelectAO timeSelect = OperatorBuildHelper.createTimeMapAndSelect(timeParam,
				soccerGameStreamAO);
		allOperators.add(timeSelect);
		
		// Selection for space
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelectAO = OperatorBuildHelper.createSpaceSelect(
				spaceParam, true, timeSelect);
		allOperators.add(spaceSelectAO);

		// -------------------------------------------------------
		// Second part of the query (Select for questioned entity)
		// -------------------------------------------------------

		SelectAO teamSelect = OperatorBuildHelper.createBothTeamSelectAO(spaceSelectAO);

		// -----------------------
		// Third part of the query
		// -----------------------

		// 2. StateMap
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		// Hint: "__last_n" is part of the StateMap to access historical data.
		// See StateMap documentation.
		SDFExpressionParameter param = OperatorBuildHelper
				.createExpressionParameter(
						"(sqrt((x_meter-__last_1.x_meter)^2 + (y_meter-__last_1.y_meter)^2))/1000",
						"mileage", teamSelect);
		SDFExpressionParameter param2 = OperatorBuildHelper
				.createExpressionParameter("entity_id", teamSelect);
		expressions.add(param);
		expressions.add(param2);
		
		StateMapAO statemapAO = OperatorBuildHelper.createStateMapAO(
				expressions, "entity_id", teamSelect);
		allOperators.add(statemapAO);

		// 3. Aggregate
		AggregateAO sumAggregateAO = OperatorBuildHelper.createAggregateAO(
				"SUM", "entity_id", "mileage", "mileage", null, statemapAO);
		allOperators.add(sumAggregateAO);
		
		List<SDFAttribute> attr = OperatorBuildHelper.createAttributeList("mileage", sumAggregateAO);
		List<SDFAttribute> groupBy = OperatorBuildHelper.createAttributeList("entity_id", sumAggregateAO);
		ChangeDetectAO checkDifference = OperatorBuildHelper.createChangeDetectAO(attr, 0.1, true, groupBy, sumAggregateAO);
		
		return OperatorBuildHelper.finishQuery(checkDifference, allOperators,
				sportsQL.getDisplayName());
	}
}
