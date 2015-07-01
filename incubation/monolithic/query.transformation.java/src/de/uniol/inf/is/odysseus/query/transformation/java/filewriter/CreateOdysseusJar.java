package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;


public class CreateOdysseusJar {
	
	/*
	 * cmd command 
	 * jar cvf MyJar.jar *.properties lib/*.jar -C bin . 
	 * 
	 */
	public  void createOdysseusJar(TransformationParameter parameter){
		 Process p;
		try {
			
			String[] volumen = parameter.getOdysseusPath().split(":"); 
			
			p = Runtime.getRuntime().exec("cmd /c "+parameter.getOdysseusPath()+"\\incubation\\monolithic\\query.transformation.java\\Scripte\\CreateJar.bat "+parameter.getOdysseusPath()+" "+parameter.getTempDirectory()+"\\lib "+volumen[0]+":");
		
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()) );
						for ( String s; (s = in.readLine()) != null; )
							System.out.println( s );
						
						
			BufferedReader error = new BufferedReader(
								new InputStreamReader(p.getErrorStream()) );
									for ( String s; (s = error.readLine()) != null; )
										System.out.println( s );			
									
										
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
