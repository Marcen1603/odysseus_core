package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.ArrayList;
import java.util.List;

public class RPiGPIOSensor extends Sensor {
	private static final long serialVersionUID = 1L;

	public enum GPIO_SENSOR_STATE {
		ON, OFF
	}

	private String pin;

	public RPiGPIOSensor(String name, String rawSourceName,
			String sourceNamePrefix, String sourceNamePostfix) {
		super(name, rawSourceName, sourceNamePrefix, sourceNamePostfix);

		init();
	}

	private void init() {
		generateQueryForRawValues();
		generateQueryForActivityInterpreter();
	}

	private void generateQueryForActivityInterpreter() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#RUNQUERY\n");

		// ActivityConfiguration:
		String activityConfiguration = "Taster1ActivityConfiguration_"
				+ getSourceNamePostfix();
		sBuilder.append("" + activityConfiguration + " := ACCESS({\n");
		sBuilder.append("    transport = 'activityconfiguration',\n");
		sBuilder.append("    source = '" + activityConfiguration + "',\n");
		sBuilder.append("    datahandler = 'Tuple',\n");
		sBuilder.append("    wrapper = 'GenericPull',\n");
		sBuilder.append("    protocol='none',\n");
		sBuilder.append("    options=[\n");
		sBuilder.append("      ['entity', 'pinNumber'],\n");
		sBuilder.append("      ['activity', 'Tastebetaetigt']\n");
		sBuilder.append("    ],\n");
		sBuilder.append("    schema=[\n");
		sBuilder.append("      ['EntityName', 'String'],\n");
		sBuilder.append("      ['ActivityName', 'String']\n");
		sBuilder.append("    ]\n");
		sBuilder.append("  }\n");
		sBuilder.append(")\n");
		sBuilder.append("\n");

		// sBuilder.append(getRawSourceName()+" := RPIGPIOSOURCE({SOURCE = '"+getRawSourceName()+"', PIN = "+pin+"})");
		// //, PINSTATE = '"+getPinState()+"'
		// rpigpiopin11src := RPIGPIOSOURCE({SOURCE = 'rpigpiopin7src', PIN =
		// 7})
		//
		// Join with predicate: PinState = 1
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append("" + getActivitySourceName() + " := PROJECT({\n");
		sBuilder.append("                          attributes = ['EntityName', 'ActivityName']\n");
		sBuilder.append("                        },SELECT({\n");
		sBuilder.append("                        predicate='PinState = 1'\n");
		sBuilder.append("                      },\n");
		sBuilder.append("                      JOIN({\n");
		sBuilder.append("                          predicate = '"
				+ getRawSourceName() + ".PinNumber = " + activityConfiguration
				+ ".EntityName'\n");
		sBuilder.append("                        },\n");
		sBuilder.append("                        " + getRawSourceName() + ",\n");
		sBuilder.append("                        " + activityConfiguration
				+ "\n");
		sBuilder.append("                      )\n");
		sBuilder.append("                    ))\n");

		/*
		 * #RUNQUERY Taster1ActivityForExport1 := PROJECT({ attributes =
		 * ['EntityName', 'ActivityName'] },SELECT({ predicate='State = 1' },
		 * JOIN({ predicate = 'rpigpiosrc.EntityName =
		 * activityConfiguration.EntityName' }, rpigpiosrc,
		 * activityConfiguration ) ))
		 */

		// Ein Activity-Datenstrom für den export, falls mehr activitäten
		// möglich sind, dann per union vereinen.
		// sBuilder.append("\n");
		// sBuilder.append("#RUNQUERY\n");
		// sBuilder.append(getActivitySourceName()+" := TasterBetaetigtActivityForExport\n");

		setQueryForActivityInterpreter(sBuilder.toString());
	}

	private void generateQueryForRawValues() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName() + " := RPIGPIOSOURCE({SOURCE = '"
				+ getRawSourceName() + "', PIN = " + pin + "})"); // , PINSTATE
																	// =
																	// '"+getPinState()+"'
		// rpigpiopin11src := RPIGPIOSOURCE({SOURCE = 'rpigpiopin7src', PIN =
		// 7})
		setQueryForRawValues(sBuilder.toString());
	}

	@Override
	public String getName() {
		return "RPiGPIOSensor";
	}

	@Override
	public List<Object> possibleValueArea() {
		List<Object> values = new ArrayList<Object>();
		values.add(GPIO_SENSOR_STATE.class);

		return values;
	}

	public void setInputPin(String pin) {
		this.pin = pin;
		generateQueryForRawValues();
	}

}
