package de.uniol.inf.is.odysseus.wrapper.iec.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class IECStringHelper {

	public static void appendStringElement(StringBuilder builder,
			String elementName, String elementValue) {
		if (elementValue != null){
			builder.append(" " + elementName + "=\"").append(elementValue + "\"");			
		}

	}

	public static void appendDateElement(StringBuilder builder,
			String elementName, Date elementValue) {
		if (elementValue != null){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			builder.append(" " + elementName + "=\"").append(df.format(elementValue) + "\"");			
		}
	}

	public static void appendIntElement(StringBuilder builder,
			String elementName, Integer elementValue) {
		if (elementValue != null){
			builder.append(" " + elementName + "=\"").append(elementValue + "\"");			
		}
	}

	public static void appendDoubleElement(StringBuilder builder,
			String elementName, Double elementValue) {
		if (elementValue != null){
			builder.append(" " + elementName + "=\"").append(elementValue + "\"");			
		}
	}

}
