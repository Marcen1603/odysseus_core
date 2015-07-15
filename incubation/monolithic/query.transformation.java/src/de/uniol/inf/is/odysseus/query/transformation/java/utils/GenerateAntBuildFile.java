package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.util.List;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;

public class GenerateAntBuildFile {
	
	
	public String getCodeForAntBuild(TransformationParameter parameter, List<String> copyJars){
		
	
		StringBuilder code = new StringBuilder();
		code.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		code.append("\n");
		code.append("<project default=\"jar\" name=\"Create Runnable Jar for Odysseus\">");
		code.append("\n");
		
		
		code.append("<target name=\"clean\">");
		code.append("\n");
		code.append("<delete dir=\"build\"/>");
		code.append("\n");
		code.append("</target>");
		code.append("\n");
		code.append("\n");
		
		code.append("<target name=\"compile\">");
		code.append("\n");
		code.append("<mkdir dir=\"build/classes\"/>");
		code.append("\n");
		code.append(" <javac srcdir=\"./src\" destdir=\"./build/classes\">");   
		code.append("\n");
		code.append("<classpath>");
		code.append("\n");
	
		for(String jar : copyJars){
			code.append("<pathelement path=\"lib/"+jar+"\"/>");
    		code.append("\n");
	 	}
		
		code.append("</classpath>");
		code.append("\n");
		code.append("</javac>");
		code.append("\n");
		code.append("</target>");
		code.append("\n");
		
	
		
		code.append("<target name=\"jar\" depends=\"compile\">");
		code.append("\n");
		code.append("<mkdir dir=\"target\"/>");
		code.append("\n");
		code.append("<jar destfile=\"target/OdysseusQuery.jar\" filesetmanifest=\"mergewithoutmain\" basedir=\"build/classes\">");
		code.append("\n");
		code.append("<manifest>");
		code.append("\n");
		code.append("<attribute name=\"Main-Class\" value=\"main.Main\"/>");
		code.append("\n");
			code.append("<attribute name=\"Class-Path\" value=\".\"/>");
			code.append("\n");
				code.append("</manifest>");
				code.append("\n");
				code.append("<fileset dir=\"./build\"/>");
				code.append("<fileset dir=\"lib\"/>");
			 	for(String jar : copyJars){
            		code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\"lib/"+jar+"\"/>");
            		code.append("\n");
			 	}
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\"lib/slf4j-log4j12-1.6.4.jar\"/>");
            	code.append("\n");         
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\"lib/slf4j-api-1.6.4.jar\"/>");
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\"lib/log4j-1.2.16.jar\"/>");
            	code.append("\n");
        	code.append("</jar>");
        	code.append("\n");
    	code.append("</target>");
    	code.append("\n");
	code.append("</project>");
	code.append("\n");
	
	return code.toString();

		
		
	}

}
