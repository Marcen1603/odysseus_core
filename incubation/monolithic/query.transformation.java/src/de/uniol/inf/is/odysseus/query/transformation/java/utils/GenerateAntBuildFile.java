package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;

public class GenerateAntBuildFile {
	
	
	public String getCodeForAntBuild(TransformationParameter parameter){
		
	
		StringBuilder code = new StringBuilder();
		code.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		code.append("\n");
		code.append("<project default=\"create_run_jar\" name=\"Create Runnable Jar for Project TestProgrammOutOdysseus2\">");
		code.append("\n");
		code.append("<target name=\"create_run_jar\">");
		code.append("\n");
		code.append("<jar destfile=\""+parameter.getDestinationDirectory().replace("\\", "//")+"/OdysseusQuery.jar\" filesetmanifest=\"mergewithoutmain\">");
		code.append("\n");
		code.append("<manifest>");
		code.append("\n");
		code.append("<attribute name=\"Main-Class\" value=\"main.Main\"/>");
		code.append("\n");
			code.append("<attribute name=\"Class-Path\" value=\".\"/>");
			code.append("\n");
				code.append("</manifest>");
				code.append("\n");
				code.append("<fileset dir=\""+parameter.getTempDirectory().replace("\\", "//")+"/bin\"/>");
				code.append("<fileset dir=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib\"/>");
				code.append("\n");
				code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/OdysseusCore.jar\"/>");
				code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/OdysseusServer.jar\"/>");
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/OdysseusRelational.jar\"/>");
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/slf4j-log4j12-1.6.4.jar\"/>");
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/slf4j-api-1.6.4.jar\"/>");
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/log4j-1.2.16.jar\"/>");
            	code.append("\n");
            	code.append("<zipfileset excludes=\"META-INF/*.SF\" src=\""+parameter.getTempDirectory().replace("\\", "//")+"/lib/OdysseusLogger.jar\"/>");
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
