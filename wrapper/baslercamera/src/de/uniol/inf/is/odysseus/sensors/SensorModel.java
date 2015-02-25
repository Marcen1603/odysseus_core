package de.uniol.inf.is.odysseus.sensors;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlRootElement;

import de.uniol.inf.is.odysseus.sensors.utilities.KeyValueFile;
import de.uniol.inf.is.odysseus.sensors.utilities.XmlMarshalHelper;

@XmlRootElement(name = "sensor")
public class SensorModel 
{	
	public final static String loggingQueryName = "LogRaw";
	
	public static String liveViewQueryName(String userName)
	{
		return "liveView_" + userName;
	}
	
	protected String name;
	protected String sourceName;
	protected String type;
	protected String ethernetAddr;
	protected String position;	
	
	public String getEthernetAddr() 	{ return ethernetAddr;	}
	public String getPosition() 		{ return position;		}
	public String getType() 			{ return type;			}
	public String getName() 			{ return name;			}
	public String getSourceName()		{ return sourceName;	}	

	
	public void setType(String type) 					{ this.type = type;					}
	public void setEthernetAddr(String ethernetAddr) 	{ this.ethernetAddr = ethernetAddr;	}
	public void setPosition(String position)			{ this.position = position;			}	
	public void setName(String name) 					{ this.name = name;					}
	public void setSourceName(String sourceName) 		{ this.sourceName = sourceName; 	}
		
	public SensorModel(String name, String sourceName, String type, String ethernetAddr, String position)
	{
		if (ethernetAddr == null) throw new IllegalArgumentException("ethernetAddr == null");
		if (type == null) throw new IllegalArgumentException("type == null");		
		
		this.type = type;
		this.ethernetAddr = ethernetAddr;		
		this.position = position != null ? position : "<no position>";
		this.name = name != null ? name : "<no name>";
		
		this.sourceName = sourceName != null ? sourceName : getUniqueSourceName(type);
	}	
	
	public SensorModel(SensorModel other)
	{		
		this(other.name, other.sourceName, other.type, other.ethernetAddr, other.position);
	}
	
	public SensorModel()
	{
		this(null, null, "LMS1xx", "localhost:2111", null);
	}
	
	public SensorModel(KeyValueFile file)
	{
		this(file.get("Name"), file.get("SourceName"), file.get("Type"), file.get("EthernetAddr"), file.get("Position"));
	}
	
	public void addToMap(Map<String, String> options) 
	{
		options.put("sensorXml", new XmlMarshalHelper<>(SensorModel.class).toXml(this));
		options.put("sensorName",			getName());
		options.put("sensorSourceName",		getSourceName());
		options.put("sensorPosition",		getPosition());
		options.put("sensorEthernetAddr",	getEthernetAddr());
		options.put("sensorType",			getType());		
	}	
	
	@Override public boolean equals(Object other)
	{
		SensorModel otherInfo = (SensorModel) other;
		
		return 	name.equals(otherInfo.name) &&
				position.equals(otherInfo.position) &&
				ethernetAddr.equals(otherInfo.ethernetAddr) &&
				type.equals(otherInfo.type) &&
				sourceName.equals(otherInfo.sourceName);
	}
	
	public boolean isSameSensor(SensorModel other)
	{
		return 	name.equals(other.name) &&
				ethernetAddr.equals(other.ethernetAddr) &&
				type.equals(other.type);
	}
	
	public String getSourceQueryText()
	{
		return null;
	}
			
	public String getLoggingQueryName()
	{
		return getSourceName() + "_" + loggingQueryName;
	}			
	
	public String getLoggingQueryText(Map<String, String> logConfigFile)
	{
		return null;
	}
	
	public String getLiveViewQueryName(String userName)
	{
		return getSourceName() + "_" + liveViewQueryName(userName);
	}		
	
	public String getLiveViewQueryText(String userName, String destinationIP, int destinationPort)
	{
		return null;
	}
	
	public String getLiveViewURL(String destinationIP, int destinationPort)
	{
		return null;
	}
		
	// ************************************************************************
	// Static methods
	
	public static String getUniqueSourceName(String type)
	{
		return type + "_" + Long.toString(Math.round(Math.random() * Integer.MAX_VALUE));
	}			
	
	private static String MapToString(Map<String, String> Map)
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
	
	public static String getAccessQuery(String sourceName, String wrapper, String transport, 
			String protocol, String dataHandler, Map<String, String> optionsMap, Map<String, String> schemaMap)
	{
		String options = MapToString(optionsMap);
		String schema  = MapToString(schemaMap);

		String formatStr =	  "ACCESS({source='%s',\n"
							+ "        wrapper='%s',\n"
							+ "        transport='%s',\n"
							+ "        protocol='%s',\n"
							+ "        dataHandler='%s',\n"
							+ "        schema=[%s],\n"
							+ "        options=[%s]})\n";

		return String.format(formatStr, sourceName, wrapper, transport, protocol, dataHandler, schema, options);
	}


	public static String getSenderQuery(String sinkName, String sinkName2, String wrapper, String transport, 
			String protocol, String dataHandler, String sourceName,	Map<String, String> optionsMap)
	{
		String options = MapToString(optionsMap);
		String formatStr = 	"%s = SENDER({sink='%s',\n"
							+ "			  wrapper='%s',\n"
							+ "           transport='%s',\n"
							+ "			  protocol='%s',\n"
							+ "           dataHandler='%s',\n"
							+ "           options=[%s]},\n"
							+ "           %s)\n";

		return String.format(formatStr, sinkName, sinkName2, wrapper, transport, protocol, dataHandler, options, sourceName);
	}	
}
