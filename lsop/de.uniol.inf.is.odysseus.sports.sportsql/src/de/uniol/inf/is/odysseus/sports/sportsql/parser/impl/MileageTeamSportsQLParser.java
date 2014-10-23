package de.uniol.inf.is.odysseus.sports.sportsql.parser.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
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

/**
 * This query calculated the mileage of a whole team.
 * 
 * @author Tobias Brandt
 * @since 24.07.2014
 *
 */
@SportsQL(gameTypes = { GameType.SOCCER }, statisticTypes = { StatisticType.TEAM }, name = "mileage", parameters = {
		@SportsQLParameter(name = "time", parameterClass = SportsQLTimeParameter.class, mandatory = false),
		@SportsQLParameter(name = "space", parameterClass = SportsQLSpaceParameter.class, mandatory = false) })
public class MileageTeamSportsQLParser implements ISportsQLParser {

	@Override
	public ILogicalQuery parse(SportsQLQuery sportsQL) throws NumberFormatException, MissingDDCEntryException {

		// We need this list to initialize all operators
		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();

		StreamAO soccerGameStreamAO = OperatorBuildHelper.createGameStreamAO();

		// ---------------------------------------
		// First part (Select for questioned time)
		// ---------------------------------------

		// 1. MAP
		MapAO firstMap = OperatorBuildHelper.createTimeMap(soccerGameStreamAO);
		allOperators.add(firstMap);

		// 2. Correct selection for the time
		SportsQLTimeParameter timeParam = SportsQLParameterHelper
				.getTimeParameter(sportsQL);

		SelectAO timeSelect = OperatorBuildHelper.createTimeSelect(timeParam,
				firstMap);
		allOperators.add(timeSelect);

		// -----------------------------------------
		// Second part (Select for questioned space)
		// -----------------------------------------

		// 1. Map for space select, but with meters
		SportsQLSpaceParameter spaceParam = SportsQLParameterHelper
				.getSpaceParameter(sportsQL);
		SelectAO spaceSelectAO = OperatorBuildHelper.createSpaceSelect(
				spaceParam, true, timeSelect);
		allOperators.add(spaceSelectAO);

		// -----------------------------------------
		// Third part (Select for questioned team)
		// -----------------------------------------

		
		// 3. Select for teams
		SelectAO teamSelect = OperatorBuildHelper.createBothTeamSelectAO(spaceSelectAO);
		
		
		// -----------------------------------------
		// Fourth part (Calculate distance)
		// -----------------------------------------

		// 3. StateMapAO for mileage calculation
		List<SDFExpressionParameter> expressions = new ArrayList<SDFExpressionParameter>();
		SDFExpressionParameter param2 = OperatorBuildHelper
				.createExpressionParameter("entity_id", teamSelect);
		SDFExpressionParameter param3 = OperatorBuildHelper
				.createExpressionParameter("team_id", teamSelect);
		SDFExpressionParameter param4 = OperatorBuildHelper
				.createExpressionParameter(
						"((sqrt((x_meter-__last_1.x_meter)^2 + (y_meter-__last_1.y_meter)^2))/1000)",
						"mileage", teamSelect);

		expressions.add(param2);
		expressions.add(param3);
		expressions.add(param4);
		
		StateMapAO mileageStateMap = OperatorBuildHelper.createStateMapAO(
				expressions, "entity_id", teamSelect);
		allOperators.add(mileageStateMap);
		
		// 3. Aggregate for the sum
		
		AggregateAO sumAggregate = OperatorBuildHelper.createAggregateAO(
				"SUM", "team_id", "mileage", "mileage", null, mileageStateMap);
		allOperators.add(sumAggregate);
		
		List<SDFAttribute> attr = OperatorBuildHelper.createAttributeList("mileage", sumAggregate);
		List<SDFAttribute> groupBy = OperatorBuildHelper.createAttributeList("team_id", sumAggregate);
		ChangeDetectAO checkDifference = OperatorBuildHelper.createChangeDetectAO(attr, 0.5, true, groupBy, sumAggregate);
		
		return OperatorBuildHelper.finishQuery(checkDifference, allOperators, sportsQL.getName());
	}


}
