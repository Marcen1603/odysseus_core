package de.uniol.inf.is.odysseus.client.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import de.uniol.inf.is.odysseus.client.communication.rest.RestException;
import de.uniol.inf.is.odysseus.client.communication.rest.RestService;
import de.uniol.inf.is.odysseus.client.communication.socket.SocketReceiver;
import de.uniol.inf.is.odysseus.client.data.ConnectionHandler;
import de.uniol.inf.is.odysseus.client.data.DataHandler;
import de.uniol.inf.is.odysseus.client.model.SchemaEvent;
import de.uniol.inf.is.odysseus.rest.dto.response.AttributeInformation;
import de.uniol.inf.is.odysseus.rest.dto.response.SocketInfo;

/**
 * @author Tobias Brandt
 * @since 13.11.2016
 */
public class OdysseusCommunicationTest implements Observer {

	private List<SocketReceiver> receiverList;

	public static void main(String[] args) throws RestException, IOException {

		new Thread() {
			public void run() {
				try {
					OdysseusCommunicationTest test = new OdysseusCommunicationTest();
					test.startQuery();
					test.addObservers();
				} catch (RestException e) {
					e.printStackTrace();
				}
			}
		}.start();

		// Read line
		System.out.print("Write something to finish: ");
		readLine();
	}

	public OdysseusCommunicationTest() {
		receiverList = new ArrayList<>();
	}

	/**
	 * Here we register this class as the observer for all incoming data from
	 * Odysseus
	 */
	public void addObservers() {
		receiverList.stream()
				.forEach(socketReceiver -> DataHandler.getInstance().addObserverForConnection(socketReceiver, this));
	}

	/**
	 * Here we send a query to Odysseus.
	 *
	 * @throws RestException
	 */
	public void startQuery() throws RestException {
		String loginIp = "127.0.0.1";
		String username = "System";
		String password = "manager";

		System.out.println("Login to Odysseus ...");
		String token = RestService.login(loginIp, username, password, "");
		System.out.println("Login successfull. Token: " + token);

		String query = "#PARSER PQL \n #RUNQUERY \n input = TIMER({PERIOD = 1000, TIMEFROMSTART = true, SOURCE = 'Source'})";

		System.out.println("Install query:");
		System.out.println(query);
		Map<Integer, Map<String, Map<Integer, SocketInfo>>> connectionInfo = RestService.installAndConnectQuery(loginIp,
				token, query);

		receiveRestults(connectionInfo);
	}

	private void receiveRestults(Map<Integer, Map<String, Map<Integer, SocketInfo>>> connectionInfo) {
		System.out.println("Starting and connecting to query seems to be successfull.");
		System.out.println("Connection information: ");

		// Print the connection information

		for (int queryId : connectionInfo.keySet()) {
			for (String outerKey : connectionInfo.get(queryId).keySet()) {
				for (int innerKey : connectionInfo.get(queryId).get(outerKey).keySet()) {
					SocketInfo info = connectionInfo.get(queryId).get(outerKey).get(innerKey);
					if (info == null) {
						System.out.println("Warning: information for query id " + queryId + " operator name " + outerKey
								+ " - Port " + innerKey + " ist null!");
						continue;
					}
					System.out.println("Connection info for operator query id " + queryId + " operator name " + outerKey
							+ " - Port " + innerKey + ":: connection name: " + info.getConnectionName()
							+ ", description: " + info.getDescription() + ", ip: " + info.getIp() + ", port: "
							+ info.getPort());
					System.out.println("Schema: ");
					for (AttributeInformation attrInfo : info.getSchema()) {
						System.out.println(attrInfo.getName() + " - " + attrInfo.getDataType());
					}
				}
			}
		}

		// Add connection for every info we got
		for (int queryId : connectionInfo.keySet()) {
			for (String operatorName : connectionInfo.get(queryId).keySet()) {
				Map<Integer, SocketInfo> singleSocketInfos = connectionInfo.get(queryId).get(operatorName);
				for (int operatorOutputPort : singleSocketInfos.keySet()) {
					SocketInfo socketInfo = singleSocketInfos.get(operatorOutputPort);
					SocketReceiver receiver = new SocketReceiver(socketInfo);
					receiverList.add(receiver);
					ConnectionHandler.getInstance().addConnection(receiver);
				}
			}
		}

		System.out.println("Added socket receivers, waiting for data ...");
	}

	/**
	 * Here we receive the data from Odysseus
	 *
	 * @param o
	 * @param arg
	 */
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("-------------------");
		if (arg instanceof SchemaEvent) {
			SchemaEvent event = (SchemaEvent) arg;
			Map<String, String> attriutes = event.getAttributes();
			for (String key : attriutes.keySet()) {
				System.out.println(key + " : " + attriutes.get(key));
			}
		}
	}

	/**
	 * Read a line from the console
	 *
	 * @return
	 * @throws IOException
	 */
	private static String readLine() throws IOException {
		if (System.console() != null) {
			return System.console().readLine();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}
}