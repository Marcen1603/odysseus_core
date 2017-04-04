package de.uniol.inf.is.odysseus.parser.novel.cql;

import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImplConstants;

public class PQLBuilder 
{
	private final StringBuilder builder = new StringBuilder();
	
	public String buildTimebasedWindowOperator(String windowSize, String windowUnit, String advanceSize, String advanceUnit, String input)
	{
		builder.setLength(0);
		builder.append("TIMEWINDOW({size=[");
		builder.append(windowSize);
		builder.append(",");
		builder.append("'");
		builder.append(windowUnit);
		builder.append("'");
		builder.append("],advance=[");
		builder.append(advanceSize);
		builder.append(",'");
		builder.append(advanceUnit);
		builder.append("']},");
		builder.append(input);
		builder.append(")");

		return builder.toString();//"TIMEWINDOW({size = ["+windowSize+",'"+windowUnit+"'],advance = ["+advanceSize+",'"+advanceUnit+"']},"+input+")";
	}
	
	private StringBuilder buildWindowOperator()
	{

		builder.setLength(0);
		return null;
	}

	public static void main(String[] args) 
	{
	
		for(int i = 0; i < PQLParserImplConstants.tokenImage.length; i++)
		{
			System.out.println("token="+PQLParserImplConstants.tokenImage[i]+", index="+i);
		}
		
	}
	
}
