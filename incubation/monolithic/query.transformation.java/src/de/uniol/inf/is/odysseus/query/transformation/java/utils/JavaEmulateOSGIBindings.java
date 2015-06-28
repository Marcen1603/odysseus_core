package de.uniol.inf.is.odysseus.query.transformation.java.utils;

public class JavaEmulateOSGIBindings {
	
	public String getCodeForDataHandlerRegistry(){
		
		StringBuilder code = new StringBuilder();
		
		code.append("\n");	
		code.append("\n");
		code.append("//DataHandler erstellen");
		code.append("\n");
		code.append("DataHandlerRegistry dataHandlerRegistry = new DataHandlerRegistry();");
		code.append("\n");
		code.append("IDataHandler integerHandler = new IntegerHandler();");
		code.append("\n");
		code.append("IDataHandler stringHandler = new StringHandler();");
		code.append("\n");
		code.append("IDataHandler tupleHandler = new TupleDataHandler();");
		code.append("\n");
		code.append("\n");
     
		code.append("dataHandlerRegistry.registerDataHandler(integerHandler);");
		code.append("\n");
		code.append("dataHandlerRegistry.registerDataHandler(stringHandler);");   
		code.append("\n");
		code.append("dataHandlerRegistry.registerDataHandler(tupleHandler);");
		code.append("\n");		
		code.append("\n");		
		code.append("\n");
    
        
		code.append("//ProtocolHandlerRegistry erstellen");
		code.append("\n");
		code.append("ProtocolHandlerRegistry protocolHandlerRegistry = new ProtocolHandlerRegistry();");
		code.append("\n");
		code.append("IProtocolHandler csvProtocolHandler = new SimpleCSVProtocolHandler();");
		code.append("\n");
		code.append("protocolHandlerRegistry.register(csvProtocolHandler);");
		code.append("\n");
		
	
		return code.toString();
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
