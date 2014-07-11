package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;

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

		for (Map.Entry<String, String> param : query.getParameters().entrySet()) {
			if (param.getKey().equals(TIME_PARAMTER)) {
				// has time parameter
				String timeValueJson = param.getValue();
				ObjectMapper mapper = new ObjectMapper();
				try {
					SportsQLTimeParameter timeParameter = mapper.readValue(
							timeValueJson, SportsQLTimeParameter.class);
					return timeParameter;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// There was no time parameter: We want to get the whole time
		return new SportsQLTimeParameter(0, 0, SportsQLTimeParameter.TIME_PARAMETER_ALWAYS);
	}

	public static SportsQLSpaceParameter getSpaceParameter(SportsQLQuery query) {

		for (Map.Entry<String, String> param : query.getParameters().entrySet()) {
			if (param.getKey().equals(SPACE_PARAMTER)) {
				// has space parameter
				String spaceValueJson = param.getValue();
				ObjectMapper mapper = new ObjectMapper();
				try {
					SportsQLSpaceParameter spaceParameter = mapper.readValue(
							spaceValueJson, SportsQLSpaceParameter.class);
					return spaceParameter;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// There was no space parameter: We want to get the whole space
		return new SportsQLSpaceParameter(0, 0, 0, 0, SportsQLSpaceParameter.SPACE_PARAMETER_ALL);
	}

}
