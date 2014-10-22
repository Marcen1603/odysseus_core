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
		case microseconds:
			return timeValue.divide(new BigInteger("60000000")).intValue();
		case nanoseconds:
			return timeValue.divide(new BigInteger("60000000000")).intValue();
		case picoseconds:
			return timeValue.divide(new BigInteger("60000000000000")).intValue();
		default:
			return timeValue.intValue();
		}
	}
	
	public static long getBTUtoMinutesFactor(TimeUnit unit) {
		switch(unit) {
		case minutes:
			return 1;
		case seconds:
			return 60;
		case milliseconds:
			return 60000;
		case microseconds:
			return 60000000;
		case nanoseconds:
			return new Long("6000000000");
		case picoseconds:
			return new Long("60000000000000");
		default:
			return 1;
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
		case microseconds:
			return timeValue.divide(new BigInteger("1000000")).intValue();
		case nanoseconds:
			return timeValue.divide(new BigInteger("1000000000")).intValue();
		case picoseconds:
			return timeValue.divide(new BigInteger("1000000000000")).intValue();
		default:
			return timeValue.longValue();
		}
	}
	
	public static double getBTUtoSecondsFactor(TimeUnit unit) {
		switch(unit) {
		case minutes:
			return 1/60;
		case seconds:
			return 1;
		case milliseconds:
			return 1000;
		case microseconds:
			return 1000000;
		case nanoseconds:
			return 1000000000;
		case picoseconds:
			return new Long("1000000000000");
		default:
			return 1;
		}
	}
	
	public static long getMilliseconds(BigInteger timeValue, TimeUnit unit) {
		switch(unit) {
		case minutes:
			return timeValue.multiply(new BigInteger("60000")).longValue();
		case seconds:
			return timeValue.multiply(new BigInteger("1000")).longValue();
		case milliseconds:
			return timeValue.longValue();		
		case microseconds:
			return timeValue.divide(new BigInteger("1000")).intValue();
		case nanoseconds:
			return timeValue.divide(new BigInteger("1000000")).intValue();
		case picoseconds:
			return timeValue.divide(new BigInteger("1000000000")).intValue();
		default:
			return timeValue.longValue();
		}
	}
	
	public static double getBTUtoMillisecondsFactor(TimeUnit unit) {
		switch(unit) {
		case minutes:
			return 1/60000;
		case seconds:
			return 1/1000;
		case milliseconds:
			return 1;
		case microseconds:
			return 1000;
		case nanoseconds:
			return 1000000;
		case picoseconds:
			return 1000000000;
		default:
			return 1;
		}
	}
	
	public static long getMicroseconds(BigInteger timeValue, TimeUnit unit) {
		switch(unit) {
		case minutes:
			return timeValue.multiply(new BigInteger("60000000")).longValue();
		case seconds:
			return timeValue.multiply(new BigInteger("1000000")).longValue();
		case milliseconds:
			return timeValue.multiply(new BigInteger("1000")).intValue();
		case microseconds:
			return timeValue.longValue();
		case nanoseconds:
			return timeValue.divide(new BigInteger("1000")).intValue();
		case picoseconds:
			return timeValue.divide(new BigInteger("1000000")).intValue();
		default:
			return timeValue.longValue();
		}
	}
	
	public static BigInteger getNanoseconds(BigInteger timeValue, TimeUnit unit) {
		switch(unit) {
		case minutes:
			return timeValue.multiply(new BigInteger("60000000000"));
		case seconds:
			return timeValue.multiply(new BigInteger("1000000000"));
		case milliseconds:
			return timeValue.multiply(new BigInteger("1000000"));
		case microseconds:
			return timeValue.multiply(new BigInteger("1000"));
		case nanoseconds:
			return timeValue;
		case picoseconds:
			return timeValue.divide(new BigInteger("1000"));
		default:
			return timeValue;
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
		case microseconds:
			return timeValue.multiply(new BigInteger("1000000"));
		case nanoseconds:
			return timeValue.multiply(new BigInteger("1000"));
		case picoseconds:
			return timeValue;
		default:
			return timeValue;
		}
	}

}
