package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Temper1Sensor extends Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(Temper1Sensor.class);
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String, String> activityConditionMap;

	// private String queryForRawValues;

	public Temper1Sensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);

		init();
	}

	private void init() {
		generateQueryForRawValues();
	}

	private void generateQueryForRawValues() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#QNAME " + getRawSourceName() + "_query\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName() + " := TEMPER1ACCESS({SOURCE = '"
				+ getRawSourceName()
				+ "', tempnumber = 0,options=[['tempnumber','0']]})");

		addQueryForRawValues(getRawSourceName(), sBuilder.toString());
	}

	private void generateQueryForActivityInterpreter() {
		for (Entry<String, String> entry : getActivityConditionMap().entrySet()) {
			String activity = entry.getKey();
			String condition = entry.getValue();

			String filteredRawValues = getNameCombination("filteredRawValues",
					activity);
			String filteredRawValuesQuery = createStatementForFilteredRawValues(
					filteredRawValues, condition);
			addQueryForActivityInterpreter(filteredRawValues,
					filteredRawValuesQuery);

			// Activity: hot
			// Configuration:
			String activityConfiguration = getNameCombination(
					"ActivityConfiguration", activity);
			String actConf = createQueryForActivityConfiguration(activity,
					activityConfiguration);

			addQueryForActivityInterpreter(activityConfiguration, actConf);

			// Join with predicate: temp > 24°C
			String activitySourceName = getNameCombination("Activity", activity);
			String actSoNa = createActivitySourceQuery(filteredRawValues,
					activityConfiguration, activitySourceName);

			setActivitySourceName(activity, activitySourceName);

			addQueryForActivityInterpreter(activitySourceName, actSoNa);
		}
	}

	private String createActivitySourceQuery(String filteredRawValues,
			String activityConfiguration, String activitySourceName) {
		StringBuilder actSoNaSB = new StringBuilder();
		actSoNaSB.append("#PARSER PQL\n");
		actSoNaSB.append("#QNAME " + activitySourceName + "_query\n");
		actSoNaSB.append("#RUNQUERY\n");

		// /1:
		actSoNaSB.append("" + activitySourceName + " := PROJECT({ \n");
		actSoNaSB.append("    attributes = ['" + filteredRawValues
				+ ".EntityName', 'ActivityName']},\n");// /
		actSoNaSB.append(" JOIN({\n");

		// /oder 2.
		// sBuilder3.append(""+query3Name+" := JOIN({\n");//

		actSoNaSB.append("                  predicate = '" + filteredRawValues
				+ ".EntityName = " + activityConfiguration + ".EntityName'\n");
		actSoNaSB.append("                },\n");
		actSoNaSB.append("                ELEMENTWINDOW({size = 1}, "
				+ filteredRawValues + "),\n");
		actSoNaSB.append("                ELEMENTWINDOW({size = 1}, "
				+ activityConfiguration + ")\n");
		actSoNaSB.append("              )\n");

		// 1:
		actSoNaSB.append("          )\n");// /

		actSoNaSB.append("\n");
		return actSoNaSB.toString();
	}

	private String createStatementForFilteredRawValues(
			String filteredRawValues, String condition) {
		StringBuilder filteredSB = new StringBuilder();
		filteredSB.append("#PARSER PQL\n");
		filteredSB.append("#QNAME " + filteredRawValues + "_query\n");
		filteredSB.append("#RUNQUERY\n");
		filteredSB.append("" + filteredRawValues + " := SELECT({\n");
		filteredSB.append("                predicate='" + condition + "'\n");
		filteredSB.append("              },\n");
		filteredSB.append("                " + getRawSourceName() + "\n");
		filteredSB.append("              )\n");
		filteredSB.append("\n");
		return filteredSB.toString();
	}

	private String createQueryForActivityConfiguration(String activity,
			String activityConfiguration) {
		StringBuilder actConfSB = new StringBuilder();
		actConfSB.append("#PARSER PQL\n");
		actConfSB.append("#QNAME " + activityConfiguration + "_query\n");
		actConfSB.append("#RUNQUERY\n");
		actConfSB.append("    " + activityConfiguration + " := \n");
		actConfSB.append("	 ACCESS({\n");
		actConfSB.append("      transport = 'activityconfiguration',\n");
		actConfSB.append("      source = '" + activityConfiguration + "',\n");
		actConfSB.append("      datahandler = 'Tuple',\n");
		actConfSB.append("      wrapper = 'GenericPull',\n");
		actConfSB.append("      protocol='none',\n");
		actConfSB.append("      options=[\n");
		actConfSB.append("        ['entity', 'temper'],\n");
		actConfSB.append("        ['activity', '" + activity + "']\n");
		actConfSB.append("      ],\n");
		actConfSB.append("      schema=[\n");
		actConfSB.append("        ['EntityName', 'String'],\n");
		actConfSB.append("        ['ActivityName', 'String']\n");
		actConfSB.append("      ]\n");
		actConfSB.append("      }\n");
		actConfSB.append("    )\n");
		actConfSB.append("\n");
		return actConfSB.toString();
	}

	public void addActivityForCondition(String activity, String condition) {
		try {
			getActivityConditionMap().put(activity, condition);
			addPossibleActivityName(activity);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private LinkedHashMap<String, String> getActivityConditionMap() {
		if (this.activityConditionMap == null) {
			this.activityConditionMap = new LinkedHashMap<String, String>();
		}
		return this.activityConditionMap;
	}

	@Override
	public LinkedHashMap<String, String> getQueryForActivityInterpreterQueries() {
		// delete all
		setQueryForActivityInterpreterQueries(null);

		generateQueryForActivityInterpreter();

		return super.getQueryForActivityInterpreterQueries();
	}
}
