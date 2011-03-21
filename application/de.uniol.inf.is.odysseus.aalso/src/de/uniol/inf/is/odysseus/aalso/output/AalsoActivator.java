package de.uniol.inf.is.odysseus.aalso.output;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.aalso.model.World;

public class AalsoActivator implements SimulatorOutputPrinter {

	private World world;
	private ArrayList<StreamServer> server = new ArrayList<StreamServer>();
	
	public AalsoActivator(World world) throws Exception {
		this.setWorld(world);
		this.getServer().add(new StreamServer(54001, AalsoDataProvider.class, world, "Bed"));
		for(StreamServer singleServer : this.getServer()){
			singleServer.start();
		}
	}
	
	@Override
	public void notifyIterationConcluded() {
		// Takes no further action in this case
	}

	public void cleanup() {
		for(StreamServer singleServer : this.getServer()){
			singleServer.close();
		}
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void setServer(ArrayList<StreamServer> server) {
		this.server = server;
	}

	public ArrayList<StreamServer> getServer() {
		return server;
	}
}
