package de.uniol.inf.is.odysseus.query.transformation.java.utils;

public class JavaEmulateOSGIBindings {
	
	public String getCodeForDataHandlerRegistry(){
		
		StringBuilder dataHandlerRegistry = new StringBuilder();
		
		dataHandlerRegistry.append("DataHandlerRegistry dataHandlerRegistry = new DataHandlerRegistry();");
		dataHandlerRegistry.append("\n");
		dataHandlerRegistry.append("IDataHandler integerHandler = new IntegerHandler();");
		dataHandlerRegistry.append("\n");
		dataHandlerRegistry.append("IDataHandler stringHandler = new StringHandler();");
		dataHandlerRegistry.append("\n");
		dataHandlerRegistry.append("dataHandlerRegistry.registerDataHandler(integerHandler);");
		dataHandlerRegistry.append("\n");
		dataHandlerRegistry.append("dataHandlerRegistry.registerDataHandler(stringHandler);");
		dataHandlerRegistry.append("\n");
		
		
		return dataHandlerRegistry.toString();
	}


	
	/*
	 * java code
	 */
	
	/*
	public static String DataHandlerRegistry(){
		DataHandlerRegistry dataHandlerRegistry = new DataHandlerRegistry();
    	
        IDataHandler integerHandler = new IntegerHandler();
        IDataHandler stringHandler = new StringHandler();
        
        
        dataHandlerRegistry.registerDataHandler(integerHandler);
        dataHandlerRegistry.registerDataHandler(stringHandler);
	}
	*/

}
