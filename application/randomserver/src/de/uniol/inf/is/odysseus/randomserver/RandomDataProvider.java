package de.uniol.inf.is.odysseus.randomserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RandomDataProvider extends Thread {

	private int port;
	private Socket clientSocket;
	private boolean pause = false;
	private BasicSchema schema;

	RandomDataProvider(int port, BasicSchema schema) {
		this.port = port;
		this.schema = schema;
	}

	public synchronized void pause() {
		this.pause = true;
	}

	public synchronized void play() {
		this.pause = false;
	}

	public void run() {
		try {
			ServerSocket listenSocket = new ServerSocket(this.port);
			System.out.print("Server up and listening on port " + this.port);
			System.out.println(" and scheme is: "+schema.toString());
			while (true) {
				try {
					this.clientSocket = listenSocket.accept();
					System.out.println("New connection accepted from "
							+ clientSocket.getInetAddress() + ":"
							+ clientSocket.getPort());

					ObjectOutputStream oStream = new ObjectOutputStream(this.clientSocket
							.getOutputStream());

					while (this.clientSocket != null
							&& this.clientSocket.isConnected()) {
						try {					
							RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(schema.getAttributes().size());							
					    	for(int i=0;i<tuple.getAttributeCount();i++){    		
					    		BasicAttribute attribute = schema.getAttributes().get(i);
					    		switch (attribute.getDataType()) {
								case Double:
									tuple.addAttributeValue(i,Double.valueOf(RandomGenerator.randomDouble(Double.parseDouble(attribute.getMin()), Double.parseDouble(attribute.getMax()))));									
									break;
								case Integer:
									tuple.addAttributeValue(i, Integer.valueOf(RandomGenerator.randomInt(Integer.parseInt(attribute.getMin()), Integer.parseInt(attribute.getMax()))));									
									break;
								case Long:
									tuple.addAttributeValue(i, Long.valueOf(RandomGenerator.randomLong(Long.parseLong(attribute.getMin()), Long.parseLong(attribute.getMax()))));									
									break;
								case Time:
									tuple.addAttributeValue(i, Long.valueOf(System.currentTimeMillis()));									
									break;
								case String:
									tuple.addAttributeValue(i, RandomGenerator.randomString(Integer.parseInt(attribute.getMin()), Integer.parseInt(attribute.getMax())));									
									break;
								default:
									break;
								}
					    	}
					    	oStream.writeObject(tuple);					    	
					    	System.out.print(this.port+"\t | ");
							for (int i = 0; i < tuple.getAttributeCount(); i++) {								
								System.out.print(tuple.getAttribute(i).toString() + "\t");
							}
							System.out.println();
							oStream.flush();
							sleep(1000);
						} catch (IOException ex) {												
							this.clientSocket = null;
							ex.printStackTrace();
							System.err
									.println("Connection closed by client on port "
											+ this.port + ". Server is still alive...");
						}
						while (this.pause) {
							Thread.sleep(1000);
						}
					}

					System.out.println("Connection closed. Waiting for client...");

				} catch (IOException e) {
					System.err.println("Error while writing to stream...");
				} catch (InterruptedException e) {
					System.out.println("Server closed!");
					return;
				}
			}
		} catch (IOException e1) {
			System.err.println("Unable to start server, because port "+this.port+" is already in use.");
		}
	}
}
