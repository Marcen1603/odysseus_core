package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import de.uniol.inf.is.odysseus.core.Activator;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.model.osgi.Component;
import de.uniol.inf.is.odysseus.query.transformation.java.model.osgi.ObjectFactory;


public class JavaEmulateOSGIBindings {
	
	
	private List<String> neededImportList  = new ArrayList<String>();
	
	
	/*
	 * dynamic
	 */
	public String getCodeForOSGIBinds(String odysseusPath){
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
				File folder = new File(odysseusPath+"\\"+bundle+"\\OSGI-INF");
			
				List<String>  xmlFileList = getXMLFiles(folder);
				
				for(String xmlFile : xmlFileList){
					//registry file
				
						File file = new File(odysseusPath+"\\"+bundle+"\\OSGI-INF\\"+xmlFile);
						
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
			Class<?> stringClass;
			try {
				if(cp.getName().equals("MEP")){
					stringClass = loadClass(Activator.getBundleContext(), cp.getImplementation().getClazz(), "de.uniol.inf.is.odysseus.mep", null);
				}else{
					stringClass = Class.forName(cp.getImplementation().getClazz() );
				}
				//add class to importList
				neededImportList.add(cp.getImplementation().getClazz());
				code.append(stringClass.getSimpleName()+" " + stringClass.getSimpleName().toLowerCase()+" = new "+stringClass.getSimpleName()+"();");
				code.append("\n");
				
			} catch (ClassNotFoundException e) {
				//bundle is to imported 
				e.printStackTrace();
			}
		
		}
		
		//MEP Funktion
		Map<String, String> neededMEPFunctions = TransformationInformation.getInstance().getNeededMEPFunctions();
		
		for (Map.Entry<String, String> entry : neededMEPFunctions.entrySet())
		{
		
		    neededImportList.add(entry.getKey());
			
			code.append("\n");
			code.append("mep.registerFunction(new "+entry.getValue()+"());");
			code.append("\n");
		}

	
		code.append("\n");
		code.append("\n");
		
		
		
	
		//DataHandler
		Map<String, String> neededDataHandler = TransformationInformation.getInstance().getNeededDataHandler();
		
		for (Map.Entry<String, String> entry : neededDataHandler.entrySet())
		{
		
		    neededImportList.add(entry.getKey());
			code.append("\n");
			code.append("datahandlerregistry.registerDataHandler(new "+entry.getValue()+"());");
			code.append("\n");
		}
		
		//TransportHandler
		Map<String, String> neededTransportHandler = TransformationInformation.getInstance().getNeededTransportHandler();
		
		for (Map.Entry<String, String> entry : neededTransportHandler.entrySet())
		{
		    neededImportList.add(entry.getKey());
			code.append("\n");
			code.append("transporthandlerregistry.register(new "+entry.getValue()+"());");
			code.append("\n");
		}
	
		//ProtocolHandler
		Map<String, String> neededProtocolHandler = TransformationInformation.getInstance().getNeededProtocolHandler();
		
		for (Map.Entry<String, String> entry : neededProtocolHandler.entrySet())
		{
		    neededImportList.add(entry.getKey());
			code.append("\n");
			code.append("protocolhandlerregistry.register(new "+entry.getValue()+"());");
			code.append("\n");
		}
		
		
		
		/*
		//code for handler add
		for(Component cp : handlerList){
			Class<?> stringInterfaceClass;
			Class<?> implementationClass;
	
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
				
				neededImportList.add(implementationClass.getName());
				neededImportList.add(stringInterfaceClass.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
*/
		return code.toString();
		
	}
	
	public List<String> getXMLFiles(File folder) {
		List<String> xmlFileList = new ArrayList<String>();
	    for (File fileEntry : folder.listFiles()) {
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
	
	public List<String> getNeededImports(){
		return neededImportList;
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
