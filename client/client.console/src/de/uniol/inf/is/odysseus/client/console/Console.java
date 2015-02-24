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

public class Console implements IApplication {

	static private IExecutor executor;
	private static ISession session;

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exec) {
		if (exec == executor) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exec) {
		executor = exec;
		synchronized (Console.class) {
			Console.class.notifyAll();
		}
	}

	static private void runExecuteFileCommand(String filename)
			throws FileNotFoundException {
		if (session != null) {
			if (filename != null) {
				FileInputStream inputStream = new FileInputStream(filename);
				String query = readFileLines(inputStream);
				executor.addQuery(query, "OdysseusScript", session,
						Context.empty());
			}
		} else {
			throw new RuntimeException("Not logged in!");
		}
	}

	private static String readFileLines(InputStream inputStream) {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));
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

		synchronized (Console.class) {
			while (executor == null) {
				try {
					Console.class.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (args.length == 0) {
			
			boolean stay = true;
		    InputStreamReader isr = new InputStreamReader(System.in);
		    BufferedReader br = new BufferedReader(isr);
			while (stay){
				System.out.print(">");
				String eingabe = br.readLine();
				if (eingabe.equalsIgnoreCase("exit")){
					stay = false;
					continue;
				}
				if (eingabe.toLowerCase().startsWith("connect")){
					String[] splitted = eingabe.split(" ");
					if (splitted.length == 1){
						connect(getDefaultConnection());
					}else if (splitted.length == 2){
						connect(splitted[1]);
					}else{
						System.err.println("Wrong number of params. Must be: connect <URL>");
					}
					continue;
				}
				if (eingabe.toLowerCase().startsWith("login")){
					String[] splitted = eingabe.split(" ");
					if (splitted.length == 3){
						login(splitted[1], splitted[2]);
					}else{
						System.err.println("Wrong number of params. Must be: login <user> <password>");
					}					
				}
				if (eingabe.toLowerCase().startsWith("exec")){
					if (session == null){
						System.err.println("Must be logged in!");
					}else{
						String[] splitted = eingabe.split(" ");
						if (splitted.length == 2){
							runExecuteFileCommand(splitted[1]);
						}else{
							System.err.println("Wrong number of params. Must be: exec <filename>");
						}
						continue;
					}
				}
			}
			
			
//			System.out
//					.println("Console mode currently not supported. Please use command line mode: ");
//			System.out
//					.println("Usage: [-url <url>] [-u <username>] [-p <password>] -c <command> <parameter>");

		} else {

			String wsdlLocation = null;
			String username = "System";
			String password = "manager";
			String command = "";
			String[] params = null;
			
			for (int i = 0; i < args.length; i++) {
				String a = args[i];
				if (a.equalsIgnoreCase("-url")) {
					wsdlLocation = args[++i];
				}else if (a.equalsIgnoreCase("-u")){
					username = args[++i];
				}else if (a.equalsIgnoreCase("-p")){
					password = args[++i];
				}else if (a.equalsIgnoreCase("-c")){
					command = args[++i];
					i++;
					params = new String[args.length - i];
					System.arraycopy(args, i, params, 0, args.length - i);
				}
			}

			if (wsdlLocation == null){
				connect(getDefaultConnection());
			}else{
				connect(wsdlLocation);
			}

			
			login(username, password);
			
			if (command.toLowerCase().startsWith("exec")){
				runExecuteFileCommand(params[0]);
			}else{
				throw new RuntimeException("Unknown command "+command);
			}
			
			
		}
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	private void login(String username, String password) {
		session = executor.login(username, password.getBytes());

		if (session == null){
			throw new RuntimeException("Login failed!");
		}
	}

	private String getDefaultConnection(){
		String host = "localhost";
		long port = 9669;
		String instance = "odysseus";

		String wsdlLocation = "http://" + host + ":" + port + "/"
				+ instance + "?wsdl";
		return wsdlLocation;
	}
	
	private void connect(String wsdlLocation) {


		String serviceNamespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/";
		String service = "WebserviceServerService";
		boolean connected = false;
		try {
			connected = ((IClientExecutor) executor).connect(wsdlLocation
					+ ";" + serviceNamespace + ";" + service);
		} catch (Exception e) {

		}
		if (!connected) {
			System.err.println("Could not connect to " + wsdlLocation);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
