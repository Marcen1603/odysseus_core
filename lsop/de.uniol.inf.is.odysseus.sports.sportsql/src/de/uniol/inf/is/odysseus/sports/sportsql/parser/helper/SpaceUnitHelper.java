package de.uniol.inf.is.odysseus.sports.sportsql.parser.helper;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceUnit;

public class SpaceUnitHelper {
	
	public static int getMillimeters(int spaceValue, SpaceUnit unit) {
		switch(unit) {
		case meters:
			return spaceValue * 1000;
		case centimeters:
			return spaceValue * 10;
		case millimeters:
			return spaceValue;
		default:
			return spaceValue;
		}
	}

}
