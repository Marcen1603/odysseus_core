package weasimulation;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimulationServer extends Thread {
	private ServerSocket socket;

	private Simulation simulation;

	private int port;

	public SimulationServer(int port, Simulation sim) throws IOException {
		this.port = port;
		this.socket = new ServerSocket(port);
		this.simulation = sim;
		sim.start();
	}

	public void run() {
		try {
			while (true) {
				Socket connection = socket.accept();
				this.simulation.addOutput(new OutputStreamWriter(connection
						.getOutputStream()));
				System.out.println("Connection from: " + 
						connection.getInetAddress().getHostName() + 
						"; to port: " + this.port);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.simulation.interrupt();
	}

}
