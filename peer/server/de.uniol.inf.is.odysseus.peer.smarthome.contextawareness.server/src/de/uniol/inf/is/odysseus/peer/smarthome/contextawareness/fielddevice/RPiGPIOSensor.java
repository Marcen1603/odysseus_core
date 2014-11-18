package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartHomeServerPlugIn;

public class RPiGPIOSensor extends Sensor {
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
		
        /*
		sBuilder.append(getRawSourceName() + " := ACCESS({\n");
		sBuilder.append("    source='"+getRawSourceName()+"',\n");
		sBuilder.append("    wrapper='GenericPull',\n");
		sBuilder.append("    transport='RPiGPIO',\n");
		sBuilder.append("    protocol='none',\n");
		sBuilder.append("    datahandler='Tuple',\n");
		sBuilder.append("    options=[['pin', '" + inputPin + "'],['mode', 'in']],\n");
		sBuilder.append("    schema=[['PinNumber', 'String'],['PinState', 'Long']]\n");                                     
		sBuilder.append("  }\n");                            
		sBuilder.append(")\n");
*/

		rawValueQueries.put(getRawSourceName(), sBuilder.toString());

		LOG.debug("RawValues sourcename:" + getRawSourceName() + " Query:"
				+ sBuilder.toString());

		return rawValueQueries;
	}

	class RPiGPIOActivityInterpreter extends ActivityInterpreter {
		private static final long serialVersionUID = 1L;
		private int inputPin;
		private GPIO_SENSOR_STATE pinState;

		public RPiGPIOActivityInterpreter(Sensor _sensor, String _activityName,
				String prefix, String postfix) {
			super(_sensor, _activityName, prefix, postfix);

		}

		/**
		 * return: HashMap<String, String> <ViewName, Query>
		 */
		@Override
		public HashMap<String, String> getActivityInterpreterQueries(
				String activityName) {
			LinkedHashMap<String, String> queries = new LinkedHashMap<String, String>();

			// TODO: FilteredRawValues:

			String activityConfigName = getNameCombination(
					"ActivityConfiguration", getActivityName());
			String activityConfigQuery = generateActivityConfig(
					getActivityName(), activityConfigName);
			queries.put(activityConfigName, activityConfigQuery);

			String activityQuery = generateActivityStream(
					getActivitySourceName(), activityConfigName);

			queries.put(getActivitySourceName(), activityQuery);

			LOG.debug("getActivityInterpreterQueries:");
			LOG.debug("sourcename:" + activityConfigName + " Query:"
					+ activityConfigQuery);
			LOG.debug("sourcename:" + getActivitySourceName() + " Query:"
					+ activityQuery);

			return queries;
		}

		private String generateActivityStream(String activityStreamName,
				String activityConfiguration) {
			StringBuilder sBuilder2 = new StringBuilder();
			sBuilder2.append("#PARSER PQL\n");
			sBuilder2.append("#QNAME " + activityStreamName + "_query\n");
			sBuilder2.append("#RUNQUERY\n");
			sBuilder2.append("" + activityStreamName + " := PROJECT({\n");
			sBuilder2
					.append("                          attributes = ['EntityName', 'ActivityName']\n");
			sBuilder2.append("                        },SELECT({\n");
			sBuilder2
					.append("                        predicate='PinState = 1'\n");
			sBuilder2.append("                      },\n");
			sBuilder2.append("                      JOIN({\n");
			sBuilder2.append("                          predicate = '"
					+ getRawSourceName() + ".PinNumber = "
					+ activityConfiguration + ".EntityName'\n");
			sBuilder2.append("                        },\n");
			sBuilder2
					.append("                        ELEMENTWINDOW({size = 1}, "
							+ getRawSourceName() + "),\n");
			sBuilder2
					.append("                        ELEMENTWINDOW({size = 1}, "
							+ activityConfiguration + ")\n");
			sBuilder2.append("                      )\n");
			sBuilder2.append("                    ))\n");
			return sBuilder2.toString();
		}

		private String generateActivityConfig(String activity,
				String activityConfiguration) {
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
			sBuilder1.append("      ['entity', 'pinNumber" + getInputPin()
					+ "'],\n");
			sBuilder1.append("      ['activity', '" + activity + "']\n");
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
			return "Pin:" + getInputPin() + " PinState:" + getPinState();
		}
	}
}
