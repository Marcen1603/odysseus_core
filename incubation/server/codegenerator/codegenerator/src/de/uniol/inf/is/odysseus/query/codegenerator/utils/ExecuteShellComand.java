package de.uniol.inf.is.odysseus.query.codegenerator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.query.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageEventType;


//TODO mac, unix support?
public class ExecuteShellComand {

	public static void excecuteCommand(String[] commands, boolean sendOkLineToMessageBus){
		
	Process p;

	try {
		p = Runtime.getRuntime().exec(commands);
	
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
			CodegeneratorMessageBus.sendUpdate(new CodegeneratorMessageEvent(-1, errorLine.toString(),UpdateMessageEventType.ERROR));
		}
	
	
		
		if(sendOkLineToMessageBus){
			CodegeneratorMessageBus.sendUpdate(new CodegeneratorMessageEvent(-1, okLine.toString(),UpdateMessageEventType.INFO));
		}
		
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}
	
	
	public static void executeAntScript(String tempDirectory){
	    String[] commands = new String[3];  
	    
      	commands[0] = "cmd";
      	commands[1] = "/c";
    	commands[2] = "ant -f \""+tempDirectory+"\"";
    	
    	excecuteCommand(commands, true);
	}
	
	
}
