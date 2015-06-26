package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.Map;

public class TransformCSVParameter {
	
	
	/*
    OptionMap csvOptions = new OptionMap();
    csvOptions.setOption("delimiter", ",");
    csvOptions.setOption("filename", "F:\\Dropbox\\Dropbox\\Studium\\Masterarbeit\\Odysseus\\Data\\soccergame_10_20.csv");
    csvOptions.setOption("basetimeunit", "MICROSECONDS");
    csvOptions.setOption("dumpeachline", "1000000");
    csvOptions.setOption("measureeachline", "100000");
    csvOptions.setOption("lastline", "49576078");
    
    */
	
	public static String getCodeForParameterInfo(Map<String, String> map){
		
		StringBuilder code = new StringBuilder();
		code.append("OptionMap csvOptions = new OptionMap();");
		code.append("\n");
		
		if(map.containsKey("FILENAME")){
			code.append("csvOptions.setOption(\"filename\","+ map.get("FILENAME").replace("'", "\"").replace("\\", "\\\\")+");");
			code.append("\n");
		}
		
		/*
		if(map.containsKey("OPTIONS")){
			code.append("csvOptions.setOption(\"options\","+ map.get("OPTIONS")+");");
			code.append("\n");
		}
		
		if(map.containsKey("SCHEMA")){
			code.append("csvOptions.setOption(\"schema\","+ map.get("SCHEMA")+");");
			code.append("\n");
		}
		
		if(map.containsKey("SOURCE")){
			code.append("csvOptions.setOption(\"source\","+ map.get("SOURCE")+");");
			code.append("\n");
		}
		*/
		
		return code.toString();
		
	}
	


}
