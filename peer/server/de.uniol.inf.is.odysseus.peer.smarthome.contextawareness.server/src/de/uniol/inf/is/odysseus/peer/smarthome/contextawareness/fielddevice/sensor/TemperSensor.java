package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;

public class TemperSensor extends AbstractSensor {
	private static final long serialVersionUID = 1L;

	public TemperSensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	@Override
	public boolean createActivityInterpreterWithCondition(String activityName,
			String condition) {
		TemperSensorActivityInterpreter activityInterpreter = new TemperSensorActivityInterpreter(
				this, activityName, getPrefix(), getPostfix());
		activityInterpreter.setCondition(condition);
		
		return addActivityInterpreter(activityInterpreter);
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

	@Override
	public ArrayList<String> getPossibleAttributes() {
		ArrayList<String> list = new ArrayList<>();
		
		list.add("Temperature (°C) [Long] (Possible values: from -∞ to +∞) example: Temperature > 20");
		
		return list;
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

			String actSoNa = createActivitySourceQuery(getActivitySourceName());
			queries.put(getActivitySourceName(), actSoNa);

			return queries;
		}

		private String createActivitySourceQuery(String activitySourceName) {
			StringBuilder sBuilder2 = new StringBuilder();
			sBuilder2.append("#PARSER PQL\n");
			sBuilder2.append("#QNAME " + activitySourceName + "_query\n");
			sBuilder2.append("#RUNQUERY\n");
			sBuilder2.append(""+activitySourceName+" := RENAME({\n");
			sBuilder2.append("                    aliases = ['EntityName', 'ActivityName']\n");                 
			sBuilder2.append("                  },\n");
			sBuilder2.append("                  MAP({\n");
			sBuilder2.append("                      expressions = ['EntityName','concat(substring(toString(Temperature),0,0),\""+getActivityName()+"\")'] \n");                                                                      
			sBuilder2.append("                    },\n");
			sBuilder2.append("                    SELECT({\n");
			sBuilder2.append("                        predicate='"+condition+"'\n");                                                                                                   
			sBuilder2.append("                      },\n");
			sBuilder2.append("                      "+getRawSourceName()+"\n");
			sBuilder2.append("                    )   \n");                                                            
			sBuilder2.append("                  )\n");
			sBuilder2.append("                )\n");
			sBuilder2.append("\n");
			
			return sBuilder2.toString();
		}

		@Override
		public String getActivityInterpreterDescription() {
			return "Condition: "+getCondition();
		}
	}

	
}
