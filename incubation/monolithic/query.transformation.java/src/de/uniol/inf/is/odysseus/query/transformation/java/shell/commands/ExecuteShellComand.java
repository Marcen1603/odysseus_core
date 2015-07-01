package de.uniol.inf.is.odysseus.query.transformation.java.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteShellComand {
	
	public static void compileJavaProgram(String tempDirectory){
		
	Process p;

	try {
		p = Runtime.getRuntime().exec("javac "+tempDirectory+"\\TestFile.java");
		p.waitFor();
		
		
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
		
		System.out.println(okLine);		    
		System.out.println(errorLine);		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	
	
	


}
