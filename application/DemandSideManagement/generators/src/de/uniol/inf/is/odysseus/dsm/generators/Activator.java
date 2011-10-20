package de.uniol.inf.is.odysseus.dsm.generators;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		URL fileURL = bundleContext.getBundle().getEntry("cfg/singleHousehold.xml");
		InputStream inputStream = fileURL.openConnection().getInputStream();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(inputStream);
		ArrayList<StreamServer> serverBuffer = new ArrayList<StreamServer>();
		int speed = 10;
		int port = 54321;
		
		
		while(parser.hasNext()) {
		    int event = parser.next();
		    switch (event) {
		        case XMLStreamConstants.END_DOCUMENT:
		            parser.close();
		            break;
		        case XMLStreamConstants.START_ELEMENT:
		        	if (parser.getLocalName() == "general"){
		        		for( int i = 0; i < parser.getAttributeCount(); i++ ){
		        			if (parser.getAttributeLocalName(i) == "speed") {
		        				speed = Integer.parseInt(parser.getAttributeValue(i));
		        				SimulationClock.getInstance().setSpeed(speed);
				            } else if (parser.getAttributeLocalName(i) == "startport") {
				            	port = Integer.parseInt(parser.getAttributeValue(i));
				            }
		        		}
		    		} else if (parser.getLocalName() == "appliance"){
		    			ArrayList<String> buffer = new ArrayList<String>();
		        		for( int i = 0; i < parser.getAttributeCount(); i++ ){
				               buffer.add(parser.getAttributeValue(i));
			        	}
		        		serverBuffer.add(new StreamServer(port, new Appliance(buffer.get(0), Double.parseDouble(buffer.get(1)), Integer.parseInt(buffer.get(2)), Integer.parseInt(buffer.get(3)),
		        						Double.parseDouble(buffer.get(4)), Double.parseDouble(buffer.get(5)), speed, Integer.parseInt(buffer.get(6)), Integer.parseInt(buffer.get(7)), Integer.parseInt(buffer.get(8)),
		        						Integer.parseInt(buffer.get(9)), Double.parseDouble(buffer.get(10)), Integer.parseInt(buffer.get(11)))));
		        		port++;
		        	}
		        break;
		        default:
		            break;
		    }
		}
		
		for(int i = 0; i<serverBuffer.size(); i++){
			serverBuffer.get(i).start();
			Thread.sleep(100);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
