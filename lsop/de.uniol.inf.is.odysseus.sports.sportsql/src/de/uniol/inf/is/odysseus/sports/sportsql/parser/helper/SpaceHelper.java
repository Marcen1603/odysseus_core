package de.uniol.inf.is.odysseus.sports.sportsql.parser.helper;

import java.awt.Point;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.model.Space;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLSpaceParameter.SpaceType;

public class SpaceHelper {
	
	public static Space getSpace(SpaceType spaceType) {
		
		switch(spaceType) {
		case all:
			return new Space(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE), 
					new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
		case field:
			return new Space(new Point(OperatorBuildHelper.LOWERLEFT_X, OperatorBuildHelper.LOWERLEFT_Y), 
					new Point(OperatorBuildHelper.LOWERRIGHT_X, OperatorBuildHelper.UPPERLEFT_Y));
		case left_half:
			return new Space(new Point(OperatorBuildHelper.LOWERLEFT_X, OperatorBuildHelper.LOWERLEFT_Y), 
					new Point(OperatorBuildHelper.LOWERRIGHT_X, 0));
		case right_half:
			return new Space(new Point(OperatorBuildHelper.LOWERLEFT_X, 0), 
					new Point(OperatorBuildHelper.LOWERRIGHT_X, OperatorBuildHelper.UPPERLEFT_Y));
		case left_third:
			return new Space(new Point(OperatorBuildHelper.LOWERLEFT_X, OperatorBuildHelper.LOWERLEFT_Y), 
					new Point(OperatorBuildHelper.LOWERRIGHT_X, OperatorBuildHelper.THIRD_LEFT_RIGHT_Y));
		case middle_third:
			return new Space(new Point(OperatorBuildHelper.LOWERLEFT_X, OperatorBuildHelper.THIRD_LEFT_RIGHT_Y), 
					new Point(OperatorBuildHelper.LOWERRIGHT_X, OperatorBuildHelper.THIRD_MIDDLE_RIGHT_Y));
		case right_third:
			return new Space(new Point(OperatorBuildHelper.LOWERLEFT_X, OperatorBuildHelper.THIRD_MIDDLE_RIGHT_Y), 
					new Point(OperatorBuildHelper.LOWERRIGHT_X, OperatorBuildHelper.THIRD_RIGHT_RIGHT_Y));
		default:
			break;
		}
		
		return null;
	}

}
