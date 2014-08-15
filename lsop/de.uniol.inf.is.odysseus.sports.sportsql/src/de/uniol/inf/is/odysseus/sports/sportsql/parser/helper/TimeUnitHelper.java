package de.uniol.inf.is.odysseus.sports.sportsql.parser.helper;

import java.math.BigInteger;

import de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter.SportsQLTimeParameter.TimeUnit;

public class TimeUnitHelper {
	
	public static int getMinutes(BigInteger timeValue, TimeUnit unit) {
		switch(unit) {
		case minutes:
			return timeValue.intValue();
		case seconds:
			return timeValue.divide(new BigInteger("60")).intValue();
		case milliseconds:
			return timeValue.divide(new BigInteger("60000")).intValue();
		case picoseconds:
			return timeValue.divide(new BigInteger("60000000000000")).intValue();
		default:
			return timeValue.intValue();
		}
	}
	
	public static long getSeconds(BigInteger timeValue, TimeUnit unit) {
		switch(unit) {
		case minutes:
			return timeValue.multiply(new BigInteger("60")).longValue();
		case seconds:
			return timeValue.longValue();
		case milliseconds:
			return timeValue.divide(new BigInteger("1000")).intValue();
		case picoseconds:
			return timeValue.divide(new BigInteger("1000000000000")).intValue();
		default:
			return timeValue.longValue();
		}
	}
	
	public static BigInteger getPicoseconds(BigInteger timeValue, TimeUnit unit) {
		switch(unit) {
		case minutes:
			return timeValue.multiply(new BigInteger("60000000000000"));
		case seconds:
			return timeValue.multiply(new BigInteger("1000000000000"));
		case milliseconds:
			return timeValue.multiply(new BigInteger("1000000000"));
		case picoseconds:
			return timeValue;
		default:
			return timeValue;
		}
	}

}
