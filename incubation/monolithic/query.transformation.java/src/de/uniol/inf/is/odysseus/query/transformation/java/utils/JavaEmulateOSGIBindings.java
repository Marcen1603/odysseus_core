package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.uniol.inf.is.odysseus.query.transformation.java.model.osgi.Component;
import de.uniol.inf.is.odysseus.query.transformation.java.model.osgi.ObjectFactory;

public class JavaEmulateOSGIBindings {
	
	/*
	 * TODO make it dynamic 
	 */
	public String getCodeForDataHandlerRegistry(){
		
		StringBuilder code = new StringBuilder();
		
		code.append("\n");	
		code.append("\n");
		code.append("//MEP registry erstellen");
		code.append("\n");
		code.append("MEP mepRegistry = MEP.getInstance();");
		code.append("mepRegistry.registerFunction(new GreaterThanOperator());");
		code.append("\n");	
		code.append("\n");
		
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
		
		//test dynamic
		//getCodeForOSGIBinds();
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
	
	
	/*
	 * dynamic
	 */

	public String getCodeForOSGIBinds(){
		StringBuilder code =  new StringBuilder();
		/*
		 * TODO get all XML Files and bundles ?
		 */
		List<String> neededBundleList = new ArrayList();
		neededBundleList.add("common\\core");
		
		
		List<Component> registryList = new ArrayList();
		
		
		for(String bundle : neededBundleList){
				File folder = new File("C:\\Studium\\Masterarbeit\\odysseus\\"+bundle+"\\OSGI-INF");
			
				List<String>  xmlFileList = getXMLFiles(folder);
				
				for(String xmlFile : xmlFileList){
					//registry file
					if(xmlFile.contains("Registry")){
						
						File file = new File("C:\\Studium\\Masterarbeit\\odysseus\\"+bundle+"\\OSGI-INF\\"+xmlFile);
						
						JAXBContext jaxbContext;
						try {
							jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
							Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
							
							Component osgi = (Component) jaxbUnmarshaller.unmarshal(file);
							registryList.add(osgi);
							Class stringClass = Class.forName(osgi.getImplementation().getClazz());
							
							
							code.append(stringClass.getSimpleName()+" " + stringClass.getSimpleName().toLowerCase()+" = new "+stringClass.getSimpleName()+"()");
							code.append("\n");
						
						} catch (JAXBException | ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
					//handler to reg	
						
					}
				}
			
		}
		
		System.out.println(code.toString());
		return "";
		
	}
	
	public List<String> getXMLFiles(File folder) {
		List<String> xmlFileList = new ArrayList();
	    for (File fileEntry : folder.listFiles()) {
	            System.out.println(fileEntry.getName());
	            xmlFileList.add(fileEntry.getName());
	    }
	    
	    return xmlFileList;
	}
}
