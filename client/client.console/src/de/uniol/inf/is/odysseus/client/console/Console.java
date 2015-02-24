package de.uniol.inf.is.odysseus.client.console;

import java.io.BufferedReader;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class Console implements IApplication{
	
	static private IExecutor executor;
	private static String command;
	private static String filename;
	private static String username;
	private static String password;
		
	
	// called by OSGi-DS
	public void unbindExecutor(IExecutor exec) {
		if (exec == executor) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exec) {
		synchronized (Console.class) {
			executor = exec;		
			Console.class.notifyAll();
		}
	}

	
	
	static private void runCommand() throws FileNotFoundException {
		if (executor != null && command != null){
			if (command.equals("exec")){
				ISession session = executor.login(username, password.getBytes());
				if (session != null){
					if (filename != null){
						FileInputStream inputStream = new FileInputStream(filename);
						String query = readFileLines(inputStream);
						executor.addQuery(query, "OdysseusScript", session, Context.empty());
					}
				}
			}
		}
		
	}
	
	
	private static String readFileLines(InputStream inputStream) {
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine = null;
		StringBuffer query = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				query.append(inputLine).append("\n");
			}
			
			in.close();
		} catch (IOException ignore) {
		}
		return query.toString();
	}

	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
		
		String[] args = (String[]) context.getArguments().get(
				"application.args");
		
		String host = "localhost";
		long port = 9669;
		String instance = "odysseus";
		
		synchronized(Console.class){
			while (executor == null){
				try {
					Console.class.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		String wsdlLocation = "http://"+host+":"+port+"/"+instance+"?wsdl";
		
		String serviceNamespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/";
		String service = "WebserviceServerService";
		boolean connected = ((IClientExecutor) executor).connect(wsdlLocation + ";" + serviceNamespace + ";" + service);
		
		if (!connected){
			System.err.println("Could not connect to "+wsdlLocation);
		}
		
		if (args.length == 0){
			System.out.println("Console mode currently not supported. Please use command line mode: ");
			System.out.println("Usage: <url> <username> <password> <command> <parameter>");
		}else if (args.length > 3){
			username = args[0];
			password = args[1];
			command = args[2];
			if (command.toLowerCase().equals("exec")){
				if (args.length != 2){
					System.out.println("Usage: exec filename");
				}else{
					filename = args[1];
					runCommand();
				}
			}else{
				System.err.println("Unknow command "+command);
			}
		}else{
			
		}
		return IApplicationContext.EXIT_ASYNC_RESULT;	
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
}
