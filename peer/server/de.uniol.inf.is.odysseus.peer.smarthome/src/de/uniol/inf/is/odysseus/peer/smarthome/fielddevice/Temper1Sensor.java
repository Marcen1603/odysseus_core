package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.ArrayList;
import java.util.List;

public class Temper1Sensor extends Sensor {

	private static final long serialVersionUID = 1L;
	private String queryForRawValues;

	
	public Temper1Sensor(String name, String rawSourceName) {
		super(name, rawSourceName);
		
		init();
	}
	
	private void init(){
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName()+" := TEMPER1ACCESS({SOURCE = '"+getRawSourceName()+"', tempnumber = 0,options=[['tempnumber','0']]})");
		setQueryForRawValues(sBuilder.toString());
	}

	@Override
	public List<Object> possibleValueArea() {
		List<Object> values = new ArrayList<Object>();
		values.add(Float.class);
		
		return values;
	}

	public String getQueryForRawValues() {
		return this.queryForRawValues;
	}

	public void setQueryForRawValues(String queryForRawValues) {
		this.queryForRawValues = queryForRawValues;
	}
}
