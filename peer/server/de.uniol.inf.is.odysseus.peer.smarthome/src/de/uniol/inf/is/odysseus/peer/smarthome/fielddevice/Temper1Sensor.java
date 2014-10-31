package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.ArrayList;
import java.util.List;

public class Temper1Sensor extends Sensor {

	private static final long serialVersionUID = 1L;

	// private String queryForRawValues;

	public Temper1Sensor(String name, String sourceName, String sourceNamePrefix, String sourceNamePostfix) {
		super(name, sourceName, sourceNamePrefix, sourceNamePostfix);

		init();
	}

	private void init() {
		generateQueryForRawValues();
		generateQueryForActivityInterpreter();
	}

	private void generateQueryForActivityInterpreter() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("\n");
		
		//auf den rohdaten: temperatur > 24
		String filteredRawValues = getSourceNamePrefix()+"_"+getName()+"_filteredRawValues_"+getSourceNamePostfix();
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(""+filteredRawValues+" := SELECT({\n");
		sBuilder.append("                predicate='Temperature > 24'\n");
		sBuilder.append("              },\n");
		sBuilder.append("                "+getRawSourceName()+"\n");
		sBuilder.append("              )\n");
		sBuilder.append("\n");
		
		//Activity: hot
		//Configuration:
		String activityConfiguration = getSourceNamePrefix()+"_"+getName()+"_ActivityConfiguration_"+getSourceNamePostfix();
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append("    "+activityConfiguration+" := \n");
		sBuilder.append("	 ACCESS({\n");
		sBuilder.append("      transport = 'activityconfiguration',\n");
		sBuilder.append("      source = '"+activityConfiguration+"',\n");
		sBuilder.append("      datahandler = 'Tuple',\n");
		sBuilder.append("      wrapper = 'GenericPull',\n");
		sBuilder.append("      protocol='none',\n");
    	sBuilder.append("      options=[\n");
    	sBuilder.append("        ['entity', 'temper'],\n");
    	sBuilder.append("        ['activity', 'hot']\n");
    	sBuilder.append("      ],\n");
    	sBuilder.append("      schema=[\n");
    	sBuilder.append("        ['EntityName', 'String'],\n");
    	sBuilder.append("        ['ActivityName', 'String']\n");
    	sBuilder.append("      ]\n");
    	sBuilder.append("      }\n");
    	sBuilder.append("    )\n");
		sBuilder.append("\n");
		
		
		//Join with predicate: temp > 24°C
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(""+getActivitySourceName()+" := JOIN({\n");
        sBuilder.append("                  predicate = '"+filteredRawValues+".EntityName = "+activityConfiguration+".EntityName'\n");
        sBuilder.append("                },\n");
        sBuilder.append("                ELEMENTWINDOW({size = 1}, "+filteredRawValues+"),\n");
        sBuilder.append("                ELEMENTWINDOW({size = 1}, "+activityConfiguration+")\n");
        sBuilder.append("              )\n");
        sBuilder.append("\n");
        
        /*
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(""+getActivitySourceName()+" := PROJECT({\n");
		sBuilder.append("                  attributes = ['EntityName', 'ActivityName']\n");
		sBuilder.append("                },\n");
		sBuilder.append("              JOIN({\n");
        sBuilder.append("                  predicate = '"+filteredRawValues+".EntityName = "+activityConfiguration+".EntityName'\n");
        sBuilder.append("                },\n");
        sBuilder.append("                "+filteredRawValues+",\n");
        sBuilder.append("                "+activityConfiguration+"\n");
        sBuilder.append("              )\n");
        sBuilder.append("            )\n");
        sBuilder.append("\n");
		*/
        
        
		setQueryForActivityInterpreter(sBuilder.toString());
	}

	private void generateQueryForRawValues() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("#PARSER PQL\n");
		sBuilder.append("#RUNQUERY\n");
		sBuilder.append(getRawSourceName() + " := TEMPER1ACCESS({SOURCE = '"
				+ getRawSourceName()
				+ "', tempnumber = 0,options=[['tempnumber','0']]})");
		setQueryForRawValues(sBuilder.toString());
	}

	@Override
	public List<Object> possibleValueArea() {
		List<Object> values = new ArrayList<Object>();
		values.add(Float.class);

		return values;
	}

	/*
	 * public String getQueryForRawValues() { return this.queryForRawValues; }
	 * 
	 * public void setQueryForRawValues(String queryForRawValues) {
	 * this.queryForRawValues = queryForRawValues; }
	 */
	
}
