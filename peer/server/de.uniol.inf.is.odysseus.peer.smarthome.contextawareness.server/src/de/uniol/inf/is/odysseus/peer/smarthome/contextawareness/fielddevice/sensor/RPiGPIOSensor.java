package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;

public class RPiGPIOSensor extends AbstractSensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static final long serialVersionUID = 1L;
	private int inputPin;

	public enum GPIO_SENSOR_STATE {
		HIGH, LOW
	}

	public RPiGPIOSensor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);

	}

	public void setInputPin(int _inputPin) {
		this.inputPin = _inputPin;
	}

	public int getInputPin() {
		return this.inputPin;
	}

	public void createActivityInterpreterForPinState(String activityName,
			GPIO_SENSOR_STATE pinState) {
		RPiGPIOActivityInterpreter activityInterpreter = new RPiGPIOActivityInterpreter(
				this, activityName, getPrefix(), getPostfix());
		activityInterpreter.setInputPin(getInputPin());
		activityInterpreter.setPinState(pinState);

		addActivityInterpreter(activityInterpreter);
	}

	@Override
	public Map<String, String> getQueriesForRawValues() {
		HashMap<String, String> rawValueQueries = new HashMap<String, String>();

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#QNAME " + getRawSourceName() + "\n");
		sBuilder.append("#RUNQUERY\n");
		//sBuilder.append(getRawSourceName() + " := RPIGPIOSOURCE({SOURCE = '"
		//		+ getRawSourceName() + "', PIN = " + inputPin + "})");
		
		sBuilder.append(getRawSourceName() + " := ACCESS({\n");
		sBuilder.append("    source='taster"+getInputPin()+"',\n");
		sBuilder.append("    wrapper='GenericPush',\n");
		sBuilder.append("    transport='RPiGPIOPush',\n");
		sBuilder.append("    protocol='none',\n");
		sBuilder.append("    datahandler='Tuple',\n");
		sBuilder.append("    options=[['pin', '" + getInputPin() + "'],['mode', 'in']],\n");
		sBuilder.append("    schema=[['PinNumber', 'String'],['PinState', 'Long']]\n");
		sBuilder.append("  }\n");
		sBuilder.append(")\n");

		rawValueQueries.put(getRawSourceName(), sBuilder.toString());

		LOG.debug("RawValues sourcename:" + getRawSourceName() + " Query:"
				+ sBuilder.toString());

		return rawValueQueries;
	}
	
	@Override
	public boolean createActivityInterpreterWithCondition(String activityName,
			String activityInterpreterCondition) {
		RPiGPIOActivityInterpreter interpreter = new RPiGPIOActivityInterpreter(this, activityName, getPrefix(), getPostfix());
		interpreter.setCondition(activityInterpreterCondition);

		return addActivityInterpreter(interpreter);
	}
	
	@Override
	public ArrayList<String> getPossibleAttributes() {
		ArrayList<String> list = new ArrayList<>();
		
		//list.add("PinNumber [String]");
		list.add("PinState [Long] (Possible values: 0,1) example: PinState = 1");
		
		return list;
	}

	class RPiGPIOActivityInterpreter extends ActivityInterpreter {
		private static final long serialVersionUID = 1L;
		private int inputPin;
		private GPIO_SENSOR_STATE pinState;
		private String condition;

		public RPiGPIOActivityInterpreter(AbstractSensor _sensor, String _activityName,
				String prefix, String postfix) {
			super(_sensor, _activityName, prefix, postfix);

		}

		public void setCondition(String activityInterpreterCondition) {
			this.condition = activityInterpreterCondition;
		}

		/**
		 * return: HashMap<String, String> <ViewName, Query>
		 */
		@Override
		public HashMap<String, String> getActivityInterpreterQueries(
				String activityName) {
			LinkedHashMap<String, String> queries = new LinkedHashMap<String, String>();

			String activityQuery = generateActivityStream(
					getActivitySourceName(), getActivityName());

			queries.put(getActivitySourceName(), activityQuery);

			LOG.debug("getActivityInterpreterQueries:");

			LOG.debug("sourcename:" + getActivitySourceName() + " Query:"
					+ activityQuery);

			return queries;
		}

		private String generateActivityStream(String activityStreamName,
				String activity) {
			String con="";
			if(this.condition!=null && !this.condition.equals("")){
				con = this.condition;
			}else if(getPinState().equals(GPIO_SENSOR_STATE.HIGH)){
				con = "PinState = 1";
			}else if(getPinState().equals(GPIO_SENSOR_STATE.LOW)){
				con = "PinState = 0";
			}else{
				con = "PinState = 1";
			}
			
			StringBuilder sBuilder2 = new StringBuilder();
			sBuilder2.append("#PARSER PQL\n");
			sBuilder2.append("#QNAME " + activityStreamName + "_query\n");
			sBuilder2.append("#RUNQUERY\n");
			sBuilder2.append(""+activityStreamName+" := RENAME({\n");
			sBuilder2.append("                    aliases = ['EntityName', 'ActivityName']\n");                 
			sBuilder2.append("                  },\n");
			sBuilder2.append("                  MAP({\n");
			sBuilder2.append("                      expressions = ['PinNumber','concat(substring(toString(PinState),0,0),\""+activity+"\")'] \n");                                                                      
			sBuilder2.append("                    },\n");
			sBuilder2.append("                    SELECT({\n");
			sBuilder2.append("                        predicate='"+con+"'\n");                                                                                                   
			sBuilder2.append("                      },\n");
			sBuilder2.append("                      "+getRawSourceName()+"\n");
			sBuilder2.append("                    )   \n");                                                            
			sBuilder2.append("                  )\n");
			sBuilder2.append("                )\n");
			sBuilder2.append("\n");
			
			return sBuilder2.toString();
		}

		public int getInputPin() {
			return this.inputPin;
		}

		public void setInputPin(int inputPin) {
			this.inputPin = inputPin;
		}

		public void setPinState(GPIO_SENSOR_STATE pinState) {
			this.pinState = pinState;
		}

		public GPIO_SENSOR_STATE getPinState() {
			return this.pinState;
		}

		@Override
		public String getActivityInterpreterDescription() {
			if(this.condition!=null && !this.condition.equals("")){
				return "Condition: "+this.condition;
			}else{
				return "Pin:" + getInputPin() + " PinState:" + getPinState();				
			}
		}
	}

}
