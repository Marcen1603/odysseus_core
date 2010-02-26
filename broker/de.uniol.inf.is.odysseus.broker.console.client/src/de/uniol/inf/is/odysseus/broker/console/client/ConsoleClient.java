package de.uniol.inf.is.odysseus.broker.console.client;

import net.java.dev.jaxb.array.StringArray;
import de.uniol.inf.is.odysseus.broker.console.ConsoleServer;
import de.uniol.inf.is.odysseus.broker.console.ConsoleServerService;



public class ConsoleClient {
	
	public void run(){
		
		ConsoleServerService service = new ConsoleServerService();
		
		ConsoleServer server = service.getConsoleServerPort();		
		
		StringArray attributes = new StringArray();
		attributes.getItem().add("id");
		attributes.getItem().add("value");
		
		int port = server.registerView(attributes);
		
		StringArray values = new StringArray();
		values.getItem().add("12");
		values.getItem().add("1204398");
				
		server.sendTuple(port, values);
		
	}
	
}
