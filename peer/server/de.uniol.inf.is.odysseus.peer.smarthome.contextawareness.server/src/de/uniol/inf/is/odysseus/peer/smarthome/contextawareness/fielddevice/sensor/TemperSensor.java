package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;

public class TemperSensor extends AbstractSensor {
	private static final long serialVersionUID = 1L;

	public TemperSensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	public void createActivityInterpreterWithCondition(String activityName,
			String condition) {
		TemperSensorActivityInterpreter activityInterpreter = new TemperSensorActivityInterpreter(
				this, activityName, getPrefix(), getPostfix());
		activityInterpreter.setCondition(condition);

		addActivityInterpreter(activityInterpreter);
	}

	@Override
	public Map<String, String> getQueriesForRawValues() {
		HashMap<String, String> rawValueQueries = new HashMap<String, String>();

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#QNAME " + getRawSourceName() + "_query\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName() + " := TEMPER1ACCESS({SOURCE = '"+getRawSourceName()+"', TEMPNUMBER = 0})");

		rawValueQueries.put(getRawSourceName(), sBuilder.toString());

		return rawValueQueries;
	}

	class TemperSensorActivityInterpreter extends ActivityInterpreter {
		private static final long serialVersionUID = 1L;
		private String condition;

		TemperSensorActivityInterpreter(AbstractSensor sensor, String activityName,
				String prefix, String postfix) {
			super(sensor, activityName, prefix, postfix);

		}

		public void setCondition(String _condition) {
			this.condition = _condition;
		}

		private String getCondition() {
			return this.condition;
		}

		@Override
		public HashMap<String, String> getActivityInterpreterQueries(
				String activityName) {
			LinkedHashMap<String, String> queries = new LinkedHashMap<String, String>();

			String filteredRawValues = getNameCombination("filteredRawValues",
					activityName);
			String filteredRawValuesQuery = createStatementForFilteredRawValues(
					filteredRawValues, getCondition());
			queries.put(filteredRawValues, filteredRawValuesQuery);

			// Activity: hot
			// Configuration:
			String activityConfiguration = getNameCombination(
					"ActivityConfiguration", activityName);
			String actConf = createQueryForActivityConfiguration(activityName,
					activityConfiguration);
			queries.put(activityConfiguration, actConf);

			// Join with predicate: temp > 24°C

			String actSoNa = createActivitySourceQuery(filteredRawValues,
					activityConfiguration, getActivitySourceName());
			queries.put(getActivitySourceName(), actSoNa);

			//for(Entry<String, String> entry : queries.entrySet()){
			//	System.out.println("SourceName:"+entry.getKey()+" Query:"+entry.getValue());	
			//}
			
			
			return queries;
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

			actSoNaSB.append("                  predicate = '"
					+ filteredRawValues + ".EntityName = "
					+ activityConfiguration + ".EntityName'\n");
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
			filteredSB
					.append("                predicate='" + condition + "'\n");
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
			actConfSB.append("      source = '" + activityConfiguration
					+ "',\n");
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

		@Override
		public String getActivityInterpreterDescription() {
			return "Condition: "+getCondition();
		}
	}
}
