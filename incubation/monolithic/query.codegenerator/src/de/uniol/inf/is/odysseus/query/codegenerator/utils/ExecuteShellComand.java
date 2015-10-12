package de.uniol.inf.is.odysseus.query.codegenerator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO mac, unix support?
public class ExecuteShellComand {
	
	
	private static Logger LOG = LoggerFactory.getLogger(ExecuteShellComand.class);
	
	public static void executeAntScript(String tempDirectory){
		
	Process p;

	try {
		p = Runtime.getRuntime().exec("cmd /c ant -f \""+tempDirectory+"\"");
		//p.waitFor();
		
		StringBuilder okLine = new StringBuilder();
		StringBuilder errorLine = new StringBuilder();
		BufferedReader reader = 
			         new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		//no error
		String line = "";			
			    while ((line = reader.readLine())!= null) {
			    	okLine.append(line + "\n");
			    }
		
		//error stream    
		BufferedReader errorReader = 
				         new BufferedReader(new InputStreamReader(p.getErrorStream()));
		
		String errorline = "";			
	    while ((errorline = errorReader.readLine())!= null) {
	    	errorLine.append(errorline + "\n");
	    }
	
		LOG.debug(okLine.toString());
		LOG.error(errorLine.toString());	
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}
	
	
	public static void excecuteCommand(String[] commands){
		
	Process p;

	try {
		p = Runtime.getRuntime().exec(commands);
		//p.waitFor();
		
	
		StringBuilder okLine = new StringBuilder();
		StringBuilder errorLine = new StringBuilder();
		BufferedReader reader = 
			         new BufferedReader(new InputStreamReader(p.getInputStream()));
		

		
		//no error
		String line = "";			
			    while ((line = reader.readLine())!= null) {
			    	okLine.append(line + "\n");
			    }
		
		//error stream    
		BufferedReader errorReader = 
				         new BufferedReader(new InputStreamReader(p.getErrorStream()));
		
		String errorline = "";			
	    while ((errorline = errorReader.readLine())!= null) {
	    	errorLine.append(errorline + "\n");
	    }
		
	
		if(errorLine.length()>0){
			LOG.error(errorLine.toString());	
		}
	
		LOG.debug(okLine.toString());
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}
	
}
