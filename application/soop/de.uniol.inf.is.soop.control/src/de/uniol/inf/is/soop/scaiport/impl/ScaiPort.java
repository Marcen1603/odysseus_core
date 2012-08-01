package de.uniol.inf.is.soop.scaiport.impl;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.www.ode.pmapi.ProcessManagementPortTypeProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.soop.webApp.webservice.DataWS;
import de.uniol.inf.is.soop.webApp.webservice.DataWSProxy;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ScaiPort  {
	private static final Logger LOG = LoggerFactory
			.getLogger(ScaiPort.class);
	public static final String SEPARATOR = ":";
	
	private DataWS port;
	//private Map<String, SourceSpec> sources = new HashMap<String, SourceSpec>();

	public ScaiPort() {
		SCAISensorPool.setAdapter(this);
		DataWSProxy proxy = new DataWSProxy();
		
		DataWS port = proxy.getDataWS();
		
	}

	
	public String getName() {
		return "SCAI";
	}

	
	protected void doDestroy() {
		//String domain = source.getConfiguration().get("domain").toString();
		//String name = source.getConfiguration().get("sensor").toString();
		//sources.remove(domain + SEPARATOR + name);

	}

	
	protected void doInit() {
		//String domain = source.getConfiguration().get("domain").toString();
		//String name = source.getConfiguration().get("sensor").toString();
		//sources.put(domain + SEPARATOR + name, source);
	}

	public void pushSensorData(String domain, String name,
		Map<String, Object> event, Calendar timestamp) {
		System.out.println(event.toString());
		try {
			port.pushData("foo", event.toString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}
}
