package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPiGPIOSensor extends Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOSensor.class);
	private static final long serialVersionUID = 1L;

	public enum GPIO_SENSOR_STATE {
		HIGH, LOW
	}

	private int pin;
	private HashMap<String, GPIO_SENSOR_STATE> activityPinStateMap;

	public RPiGPIOSensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);

		init();
	}

	private void init() {
		generateQueryForRawValues();
	}

	private void generateQueryForActivityInterpreter() {
		for (Entry<String, GPIO_SENSOR_STATE> entry : getActivityConditionMap()
				.entrySet()) {
			String activity = entry.getKey();
			//GPIO_SENSOR_STATE pinState = entry.getValue();
			
			//FilteredRawValues:
			
			
			String activityConfigName = getNameCombination(
					"ActivityConfiguration", activity);
			String activityConfigQuery = generateActivityConfig(activity, activityConfigName);
			addQueryForActivityInterpreter(activityConfigName,
					activityConfigQuery);

			String activitySourceName = getNameCombination("Activity", activity);
			String activityQuery = generateActivityStream(activitySourceName, activityConfigName);
			setActivitySourceName(activity, activitySourceName);
			addQueryForActivityInterpreter(activitySourceName, activityQuery);
		}
	}

	private String generateActivityStream(String activityStreamName, String activityConfiguration) {
		StringBuilder sBuilder2 = new StringBuilder();
		sBuilder2.append("#PARSER PQL\n");
		sBuilder2.append("#QNAME " + activityStreamName + "_query\n");
		sBuilder2.append("#RUNQUERY\n");
		sBuilder2.append("" + activityStreamName + " := PROJECT({\n");
		sBuilder2
				.append("                          attributes = ['EntityName', 'ActivityName']\n");
		sBuilder2.append("                        },SELECT({\n");
		sBuilder2.append("                        predicate='PinState = 1'\n");
		sBuilder2.append("                      },\n");
		sBuilder2.append("                      JOIN({\n");
		sBuilder2.append("                          predicate = '"
				+ getRawSourceName() + ".PinNumber = " + activityConfiguration
				+ ".EntityName'\n");
		sBuilder2.append("                        },\n");
		sBuilder2.append("                        " + getRawSourceName()
				+ ",\n");
		sBuilder2.append("                        " + activityConfiguration
				+ "\n");
		sBuilder2.append("                      )\n");
		sBuilder2.append("                    ))\n");
		return sBuilder2.toString();
	}

	private String generateActivityConfig(String activity, String activityConfiguration) {
		StringBuilder sBuilder1 = new StringBuilder();
		sBuilder1.append("#PARSER PQL\n");
		sBuilder1.append("#QNAME " + activityConfiguration + "_query\n");
		sBuilder1.append("#RUNQUERY\n");
		sBuilder1.append("" + activityConfiguration + " := ACCESS({\n");
		sBuilder1.append("    transport = 'activityconfiguration',\n");
		sBuilder1.append("    source = '" + activityConfiguration + "',\n");
		sBuilder1.append("    datahandler = 'Tuple',\n");
		sBuilder1.append("    wrapper = 'GenericPull',\n");
		sBuilder1.append("    protocol='none',\n");
		sBuilder1.append("    options=[\n");
		sBuilder1.append("      ['entity', 'pinNumber"+pin+"'],\n");
		sBuilder1.append("      ['activity', '"+activity+"']\n");
		sBuilder1.append("    ],\n");
		sBuilder1.append("    schema=[\n");
		sBuilder1.append("      ['EntityName', 'String'],\n");
		sBuilder1.append("      ['ActivityName', 'String']\n");
		sBuilder1.append("    ]\n");
		sBuilder1.append("  }\n");
		sBuilder1.append(")\n");
		sBuilder1.append("\n");
		return sBuilder1.toString();
	}

	private void generateQueryForRawValues() {

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#QNAME " + getRawSourceName() + "\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName() + " := RPIGPIOSOURCE({SOURCE = '"
				+ getRawSourceName() + "', PIN = " + pin + "})"); 
		
		addQueryForRawValues(getRawSourceName(), sBuilder.toString());
	}

	@Override
	public String getName() {
		return "RPiGPIOSensor";
	}

	public void setInputPin(int pin) {
		this.pin = pin;
	}

	public void addActivityForPinState(String activity,
			GPIO_SENSOR_STATE pinState) {
		try {
			getActivityConditionMap().put(activity, pinState);
			addPossibleActivityName(activity);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private HashMap<String, GPIO_SENSOR_STATE> getActivityConditionMap() {
		if (this.activityPinStateMap == null) {
			this.activityPinStateMap = new HashMap<String, GPIO_SENSOR_STATE>();
		}
		return this.activityPinStateMap;
	}
	
	@Override
	public LinkedHashMap<String, String> getQueryForActivityInterpreterQueries() {
		// delete all
		setQueryForActivityInterpreterQueries(null);

		generateQueryForActivityInterpreter();

		return super.getQueryForActivityInterpreterQueries();
	}
}
