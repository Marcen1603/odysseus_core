package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
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
	
	

public static CodeFragmentInfo getCodeForParameterInfoNeu(ILogicalOperator operator, String operatorVariable){
		CodeFragmentInfo codeFragmentInfo= new CodeFragmentInfo();
		Set<String> imports = new HashSet<String>();
	
		StringBuilder code = new StringBuilder();
		
		imports.add(OptionMap.class.getName());
		
		code.append("OptionMap "+operatorVariable+"ParameterInfo = new OptionMap();");
		code.append("\n");
		
	
		if(operator instanceof CSVFileSource)
		{
			CSVFileSource sss = (CSVFileSource) operator;
			for (Map.Entry<String, String> entry : sss.getOptionsMap().entrySet())
			{
			    String value = entry.getValue().replace("\\", "\\\\");
			    code.append(operatorVariable+"ParameterInfo.setOption(\""+entry.getKey()+"\",\""+ value+"\");");
				code.append("\n");
			}
		}
		
		if(operator instanceof CSVFileSink){
			CSVFileSink sss = (CSVFileSink) operator;
			for (Map.Entry<String, String> entry : sss.getOptionsMap().entrySet())
			{
			    String value = entry.getValue().replace("\\", "\\\\");
			    code.append(operatorVariable+"ParameterInfo.setOption(\""+entry.getKey()+"\",\""+ value+"\");");
				code.append("\n");
			}
		}
		
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
