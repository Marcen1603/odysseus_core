package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.FileLocator;
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
			
	
			try {
				Bundle neededBundel = org.osgi.framework.FrameworkUtil.getBundle(Class.forName(entry.getKey()));
				System.out.println(neededBundel.getSymbolicName());
	
				URL entryss = neededBundel.getEntry(".project");
				String path = FileLocator.toFileURL(entryss).getPath();
				
				System.out.println(path);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    neededImportList.add(entry.getKey());
			code.append("\n");
			code.append("protocolhandlerregistry.register(new "+entry.getValue()+"());");
			code.append("\n");
		}
		
		
		return code.toString();
		
	}
	
	public List<String> getXMLFiles(File folder) {
		List<String> xmlFileList = new ArrayList<String>();
	    for (File fileEntry : folder.listFiles()) {
	            xmlFileList.add(fileEntry.getName());
	    }
	    return xmlFileList;
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
