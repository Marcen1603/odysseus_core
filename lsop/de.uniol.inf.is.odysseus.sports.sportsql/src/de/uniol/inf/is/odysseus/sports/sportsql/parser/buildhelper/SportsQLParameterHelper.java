package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.Map;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.ISportsQLParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLEvaluationParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLIntegerParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceUnit;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter;

/**
 * Class with static methods which help you to get the parameters from the
 * SportsQLQuery
 * 
 * @author Tobias Brandt
 * @since 11.07.2014
 *
 */


public class SportsQLParameterHelper {

	public static final String TIME_PARAMTER = "time";
	public static final String SPACE_PARAMTER = "space";

	public static SportsQLTimeParameter getTimeParameter(SportsQLQuery query) {

		for (Map.Entry<String, ISportsQLParameter> param : query.getParameters().entrySet()) {
			if (param.getKey().equals(TIME_PARAMTER)) {				
				return (SportsQLTimeParameter) param.getValue();
			}
		}

		// There was no time parameter: We want to get the whole time
		return new SportsQLTimeParameter(null, null, SportsQLTimeParameter.TIME_PARAMTER_GAME, null);
	}

	public static SportsQLSpaceParameter getSpaceParameter(SportsQLQuery query) {

		for (Map.Entry<String, ISportsQLParameter> param : query.getParameters().entrySet()) {
			if (param.getKey().equals(SPACE_PARAMTER)) {				
				return (SportsQLSpaceParameter) param.getValue();
			}
		}
		
		// There was no space parameter: We want to get the whole space
		return new SportsQLSpaceParameter(0, 0, 0, 0, SportsQLSpaceParameter.SpaceType.field, SpaceUnit.millimeters);
	}
	
	public static SportsQLIntegerParameter getIntegerParameter(SportsQLQuery query, String paramName) {
		for (Map.Entry<String, ISportsQLParameter> param : query.getParameters().entrySet()) {
			if (param.getKey().equals(paramName)) {	
				return (SportsQLIntegerParameter) param.getValue();
			}
		}
		return null;
	}
	
	public static SportsQLEvaluationParameter getEvaluationParameter(SportsQLQuery query){
		

		for (Map.Entry<String, ISportsQLParameter> param : query.getParameters().entrySet()) {
			if (param.getKey().equals("evaluation")) {				
				
				return (SportsQLEvaluationParameter)param.getValue();
			}
		}
		return null;
		
	}
	

}
