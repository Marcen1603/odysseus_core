package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class StringUtils 
{
	public static String namedFormatStr(String format, Map<String, Object> values) {
	    StringBuilder convFormat = new StringBuilder(format);
	    Set<String> keys = values.keySet();
	    ArrayList<String> valueList = new ArrayList<>();
	    int currentPos = 1;
	    for (String key : keys)
	    {
	        String formatKey = "%(" + key + ")";
	        String formatPos = "%" + Integer.toString(currentPos) + "$s";
	        int index = -1;
	        while ((index = convFormat.indexOf(formatKey, index)) != -1) {
	            convFormat.replace(index, index + formatKey.length(), formatPos);
	            index += formatPos.length();
	        }
	        valueList.add(values.get(key).toString());
	        ++currentPos;
	    }
	    return String.format(convFormat.toString(), valueList.toArray());
	}
	
	public static String mapToString(Map<String, String> Map)
	{
		String result = "";
		if (Map != null && !Map.isEmpty())
		{
			for (Entry<String, String> x : Map.entrySet())
			{
				result += String.format("['%s', '%s'],", x.getKey(), x.getValue());
			}		
			result = result.substring(0, result.length()-1);
		}		
		
		return result;
	}
}
