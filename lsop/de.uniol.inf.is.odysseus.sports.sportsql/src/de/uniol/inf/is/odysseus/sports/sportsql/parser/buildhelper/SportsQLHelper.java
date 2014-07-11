package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.Map;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;

public class SportsQLHelper {

	@SuppressWarnings("unused")
	public static void getTimeParameter(SportsQLQuery query) {
		
		for (Map.Entry<String, String> param : query.getParameters().entrySet()) {
			if (param.getKey().equals("time")) {
				// has time parameter
				String timeValueJson = param.getValue();
				
			}
		}
	}

}
