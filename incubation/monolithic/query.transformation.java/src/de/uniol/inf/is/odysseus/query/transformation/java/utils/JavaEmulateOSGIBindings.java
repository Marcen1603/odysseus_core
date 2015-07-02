package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import de.uniol.inf.is.odysseus.core.Activator;
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
		return getCodeForOSGIBinds();
		//return code.toString();
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
		/*
		 * TODO get all XML Files and bundles ?
		 */
		List<String> neededBundleList = new ArrayList<String>();
		neededBundleList.add("common\\core");
		neededBundleList.add("common\\mep");
		
		
		StringBuilder code =  new StringBuilder();

		List<Component> registryList = new ArrayList<Component>();
		List<Component> handlerList = new ArrayList<Component>();
		
		for(String bundle : neededBundleList){
				File folder = new File("F:\\Studium\\odysseus\\"+bundle+"\\OSGI-INF");
			
				List<String>  xmlFileList = getXMLFiles(folder);
				
				for(String xmlFile : xmlFileList){
					//registry file
				
						File file = new File("F:\\Studium\\odysseus\\"+bundle+"\\OSGI-INF\\"+xmlFile);
						
						JAXBContext jaxbContext;
						try {
							jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
							Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
							Component osgi = (Component) jaxbUnmarshaller.unmarshal(file);
							
							if(osgi.getReference()!= null){
								//registry
								registryList.add(osgi);
								
							}else{
								//handler
								handlerList.add(osgi);
							}
					
						
						
						} catch (JAXBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
				}
			
		}
		code.append("\n");
		code.append("\n");
		
		//First create code for registry
		for(Component cp : registryList){
			Class stringClass;
			try {
				if(cp.getName().equals("MEP")){
					stringClass = loadClass(Activator.getBundleContext(), cp.getImplementation().getClazz(), "de.uniol.inf.is.odysseus.mep", null);
				}else{
					stringClass = Class.forName(cp.getImplementation().getClazz() );
				}
				code.append(stringClass.getSimpleName()+" " + stringClass.getSimpleName().toLowerCase()+" = new "+stringClass.getSimpleName()+"();");
				code.append("\n");
				
			} catch (ClassNotFoundException e) {
				//bundle is to imported 
				e.printStackTrace();
			}
		
		}
		
		code.append("\n");
		code.append("\n");
		
		//code for handler add
		for(Component cp : handlerList){
			Class stringInterfaceClass;
			Class implementationClass;
	
			try {
				List<Component.Service.Provide> provides = cp.getService().getProvide();
				
				//dirty get first
				//TODO fix ?
				stringInterfaceClass = Class.forName(provides.get(0).getInterface());
				implementationClass = Class.forName(cp.getImplementation().getClazz());
				
				code.append(stringInterfaceClass.getSimpleName()+" " + implementationClass.getSimpleName().toLowerCase()+" = new "+implementationClass.getSimpleName()+"();");
				code.append("\n");
				
				Component registry = getRegistryForInterface(registryList, provides.get(0).getInterface());
				
				String bindString = registry.getReference().getBind();
				String registryVariable = Class.forName(registry.getImplementation().getClazz()).getSimpleName().toLowerCase();
				
				
				code.append(registryVariable+"."+bindString+"("+implementationClass.getSimpleName().toLowerCase()+");");
				code.append("\n");
				code.append("\n");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(code.toString());
		return code.toString();
		
	}
	
	public List<String> getXMLFiles(File folder) {
		List<String> xmlFileList = new ArrayList<String>();
	    for (File fileEntry : folder.listFiles()) {
	            System.out.println(fileEntry.getName());
	            xmlFileList.add(fileEntry.getName());
	    }
	    
	    return xmlFileList;
	}
	
	private Component getRegistryForInterface(List<Component> registryList,String stringInterface){
		for(Component cp : registryList){
			
			if(cp.getReference().getInterface().equals(stringInterface)){
				//registry found
				return cp;
			}
		}
		return null;
	}
	
	public static Class<?> loadClass(BundleContext context, String className, String bundleSymbolicName, Version bundleVersion) throws ClassNotFoundException {
		  for (Bundle bundle : context.getBundles()) {
		   if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(bundleSymbolicName)) {
			
		     return bundle.loadClass(className);
		  
		   }
		  }
		  throw new ClassNotFoundException("Could not load class " + className);
		 }
}
