package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.ArrayList;
import java.util.List;

public class RPiGPIOSensor extends Sensor {
	private static final long serialVersionUID = 1L;

	public enum Character {
	    ON, OFF
	  }

	private String pin;
	
	public RPiGPIOSensor(String name, String rawSourceName)  {
		super(name, rawSourceName);
		
		init();
	}
	
	private void init(){
		generateQuery();
		
	}

	private void generateQuery() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName()+" := RPIGPIOSOURCE({SOURCE = '"+getRawSourceName()+"', PIN = "+pin+"})"); //, PINSTATE = '"+getPinState()+"'
		//rpigpiopin11src := RPIGPIOSOURCE({SOURCE = 'rpigpiopin7src', PIN = 7})
		setQueryForRawValues(sBuilder.toString());
	}

	

	@Override
	public String getName() {
		return "RPiGPIOSensor";
	}

	@Override
	public List<Object> possibleValueArea() {
		List<Object> values = new ArrayList<Object>();
		values.add(Character.class);
		
		return values;
	}
	
	public void setPin(String pin){
		this.pin = pin;
		generateQuery();
	}

}
