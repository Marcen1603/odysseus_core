package de.uniol.inf.is.odysseus.randomserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

					ObjectOutputStream oStream;

				

				
				
					oStream = new ObjectOutputStream(this.clientSocket
							.getOutputStream());

					while (this.clientSocket != null
							&& this.clientSocket.isConnected()) {
						try {					
							
							Object[] tuple = new Object[schema.getAttributes().size()];
					    	for(int i=0;i<tuple.length;i++){    		
					    		BasicAttribute attribute = schema.getAttributes().get(i);
					    		switch (attribute.getDataType()) {
								case Double:
									tuple[i] = Double.valueOf(RandomGenerator.randomDouble(Double.parseDouble(attribute.getMin()), Double.parseDouble(attribute.getMax())));
									oStream.writeDouble((Double) tuple[i]);
									break;
								case Integer:
									tuple[i] = Integer.valueOf(RandomGenerator.randomInt(Integer.parseInt(attribute.getMin()), Integer.parseInt(attribute.getMax())));
									oStream.writeInt((Integer) tuple[i]);
									break;
								case Long:
									tuple[i] = Long.valueOf(RandomGenerator.randomLong(Long.parseLong(attribute.getMin()), Long.parseLong(attribute.getMax())));
									oStream.writeLong((Long) tuple[i]);
									break;
								case Time:
									tuple[i] = Long.valueOf(System.currentTimeMillis());
									oStream.writeLong((Long) tuple[i]);
									break;
								case String:
									tuple[i] = RandomGenerator.randomString(Integer.parseInt(attribute.getMin()), Integer.parseInt(attribute.getMax()));
									oStream.writeChars((String) tuple[i]);
									break;
								default:
									break;
								}
					    	}
							for (int i = 0; i < tuple.length; i++) {								
								System.out.print(tuple[i] + "\t");
							}
							System.out.println();
							oStream.flush();
							sleep(1000);
						} catch (IOException ex) {												
							this.clientSocket = null;

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
