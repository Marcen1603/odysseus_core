package de.uniol.inf.is.odysseus.action.ideaalwrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Client responsible for fetching values 
 * @author Simon Flandergan
 *
 */
public class SocketSensorClient extends Thread {
	private long interval;
	private Socket socket;	
	private List<StreamClient> clients;
	private String message;
	private SDFAttributeList schema;
	

	public SocketSensorClient(String ip, int port, long interval, String message, SDFAttributeList schema) throws UnknownHostException, IOException {
		this.interval = interval;
		this.socket = new Socket(ip, port);
		this.clients = new ArrayList<StreamClient>();
		this.message = message;
		this.schema = schema;
	}
	
	public void addClient(StreamClient streamClient) {
		synchronized (this.clients) {
			this.clients.add(streamClient);
		}
	}
	
	@Override
	public void run() {
		while (true){
			try {
				//send request to sensor
				PrintWriter outputStream = new PrintWriter(this.socket.getOutputStream(), true);
				outputStream.write(message);
				
				//receive result
				BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				String input = "";
				
				String inputLine = reader.readLine();
				while (inputLine != null){
					input = input.concat(inputLine);
					inputLine = reader.readLine();
				}
				
				RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(this.schema.size()+1);
				tuple.setAttribute(0, System.currentTimeMillis());
				//TODO datenformat => tupel entsprechend aufbauen
				
				//send tuple to clients
				for (StreamClient client : this.clients){
					client.writeObject(tuple);
				}
				
				try {
					//sleep for interval duration
					sleep(this.interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				this.closeSocket();
				break;
			}
		}
	}

	public void closeSocket() {
		if (this.socket != null && !this.socket.isClosed()){
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (StreamClient client : clients){
			client.closeSocket();
		}
		this.clients.clear();
	}
	
	public SDFAttributeList getSchema() {
		return schema;
	}

}
