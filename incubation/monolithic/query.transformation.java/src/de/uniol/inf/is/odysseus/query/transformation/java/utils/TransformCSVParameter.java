package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.Map;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

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
	
	

public static CodeFragmentInfo getCodeForParameterInfoNeu(Map<String, String> optionMap, String operatorVariable){
		CodeFragmentInfo codeFragmentInfo= new CodeFragmentInfo();
		
		StringTemplate optionMapTemplate = new StringTemplate("java","optionMap");
		optionMapTemplate.getSt().add("operatorVariable", operatorVariable);
		optionMapTemplate.getSt().add("optionMap", optionMap);
		
		codeFragmentInfo.addCode(optionMapTemplate.getSt().render().replace("\\", "\\\\"));
	
		codeFragmentInfo.addImport(OptionMap.class.getName());
		
		return codeFragmentInfo;
	}

}
