package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
		Set<String> imports = new HashSet<String>();
	
		StringBuilder code = new StringBuilder();
		
		imports.add(OptionMap.class.getName());
		
		code.append("OptionMap "+operatorVariable+"ParameterInfo = new OptionMap();");
		code.append("\n");
	
			for (Map.Entry<String, String> entry : optionMap.entrySet())
			{
			    String value = entry.getValue().replace("\\", "\\\\");
			    code.append(operatorVariable+"ParameterInfo.setOption(\""+entry.getKey()+"\",\""+ value+"\");");
				code.append("\n");
			}
	
		codeFragmentInfo.addCode(code.toString());
		
		codeFragmentInfo.addImports(imports);
		
		return codeFragmentInfo;
		
	}

}
