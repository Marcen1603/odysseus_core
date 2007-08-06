package weasim;

import org.w3c.dom.Element;

public class TimePeriodFactory {
	private static TimePeriodFactory instance = new TimePeriodFactory();

	private TimePeriodFactory() {
	}
	
	public TimePeriod createTimePeriod(Element element) {
		if (!element.getNodeName().equals("time_period")) {
			throw new IllegalArgumentException("No time_period element");
		}
		String type = element.getAttribute("type");
		if (type.equals("periodical")) {
		
			return new PeriodicalTimePeriod(Long.parseLong(element.getTextContent()));
		}
		
		throw new IllegalArgumentException("Invalid type of time_period");
	}
	
	public static TimePeriodFactory getInstance(){
		return instance;
	}
}
