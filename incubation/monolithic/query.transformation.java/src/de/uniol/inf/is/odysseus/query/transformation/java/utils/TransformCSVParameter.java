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
	
	

public static CodeFragmentInfo getCodeForParameterInfoNeu(Map<String, String> map, String operatorVariable){
		CodeFragmentInfo codeFragmentInfo= new CodeFragmentInfo();
		Set<String> imports = new HashSet<String>();
	
	
		StringBuilder code = new StringBuilder();
		
		imports.add(OptionMap.class.getName());
		
		code.append("OptionMap "+operatorVariable+"ParameterInfo = new OptionMap();");
		code.append("\n");
		
		if(map.containsKey("FILENAME")){
			code.append(operatorVariable+"ParameterInfo.setOption(\"filename\","+ map.get("FILENAME").replace("'", "\"").replace("\\", "\\\\")+");");
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
		
		codeFragmentInfo.addCode(code.toString());
		
		codeFragmentInfo.addImports(imports);
		
		return codeFragmentInfo;
		
	}
	

	
	public static Set<String> getImportsForParameterInfo(){
		Set<String> importList = new HashSet<String>();
		importList.add("de.uniol.inf.is.odysseus.core.collection.OptionMap");
		
		return importList;
		
	}
	


}
