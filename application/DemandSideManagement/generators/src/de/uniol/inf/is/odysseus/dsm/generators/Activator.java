/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
	 * 
	 * Laden der Konfigurationsdatei und Starten der Generatoren
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		//Einbinden der entsprechenden Konfigurationsdatei
		URL fileURL = bundleContext.getBundle().getEntry("cfg/cfg.xml");
		InputStream inputStream = fileURL.openConnection().getInputStream();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(inputStream);
		ArrayList<StreamServer> serverBuffer = new ArrayList<StreamServer>();
		int[][] priceModel = null;
		int speed = 0;
		int port = 0;
		int pricePort = 0;
		int j = 0;
		
		
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
		        						Double.parseDouble(buffer.get(4)), Double.parseDouble(buffer.get(5)), Integer.parseInt(buffer.get(6)), Integer.parseInt(buffer.get(7)), Integer.parseInt(buffer.get(8)),
		        						Integer.parseInt(buffer.get(9)), Integer.parseInt(buffer.get(10)), Integer.parseInt(buffer.get(11)), Double.parseDouble(buffer.get(12)))));
		        		port++;
		        		
		        	} else if (parser.getLocalName() == "priceModel"){
			        	for( int i = 0; i < parser.getAttributeCount(); i++ ){
			        		if (parser.getAttributeLocalName(i) == "port") {
			        			pricePort = Integer.parseInt(parser.getAttributeValue(i));
			        		} else if (parser.getAttributeLocalName(i) == "levels"){
			        			priceModel = new int[Integer.parseInt(parser.getAttributeValue(i))][3];
			        		}
			        	}
		    		} else if (parser.getLocalName() == "price"){
		    			for( int i = 0; i < parser.getAttributeCount(); i++ ){
		    				if (parser.getAttributeLocalName(i) == "start") {
		    					priceModel[j][i] = Integer.parseInt(parser.getAttributeValue(i));
		    				} else if (parser.getAttributeLocalName(i) == "end") {
		    					priceModel[j][i] = Integer.parseInt(parser.getAttributeValue(i));
		    				} else {
		    					priceModel[j][i] = Integer.parseInt(parser.getAttributeValue(i));
		    				}
		    			}
		    			j++;
		    		}
		        	break;
		        default:
		            break;
		    }
		}
		
		StreamServer server = new StreamServer(pricePort, new PriceProvider(priceModel));
		server.start();
		
		Thread.sleep(100);
		
		for(int i = 0; i < serverBuffer.size(); i++){
			serverBuffer.get(i).start();
			Thread.sleep(100);
		}
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
