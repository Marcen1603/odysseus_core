package de.uniol.inf.is.odysseus.broker.movingobjects.generator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MovingObjectsGenerator extends Thread {	
	
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private long currentTime = 0;
	private VehicleGenerator vehicleGenerator = VehicleGenerator.getInstance();
	
	public MovingObjectsGenerator() throws IOException{
		PipedInputStream pIn = new PipedInputStream();
		PipedOutputStream pOut = new PipedOutputStream();
		pIn.connect(pOut);

		outputStream = new ObjectOutputStream(pOut);
		inputStream = new ObjectInputStream(pIn);
	}
	
	
	public ObjectInputStream getInputStream() {
		return inputStream;
	}
	
	
	@Override	
	public void run() {
		TupleContainer container = null;
		while(!interrupted()){
			
			container = getNextVehicleTupleContainer();
			try {
				outputStream.writeObject(container);
			
			outputStream.flush();
			super.sleep(1000);
			} catch (IOException e) {				
				e.printStackTrace();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
			
	public TupleContainer getNextVehicleTupleContainer(){
		Vehicle vehicle = vehicleGenerator.getNext();
		currentTime = System.currentTimeMillis();
		SDFAttributeList schema = MovingObjectsStreamType.getSchema(MovingObjectsStreamType.VEHICLE);
		RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(schema.getAttributeCount());
		tuple.setAttribute(0, currentTime);
		tuple.setAttribute(1, vehicle.getId());
		tuple.setAttribute(2, vehicle.getInterval());
		tuple.setAttribute(3, "VEHICLE");
		tuple.setAttribute(4, vehicle.getPositionX());
		tuple.setAttribute(5, vehicle.getPositionY());		
		TupleContainer container = new TupleContainer(tuple, MovingObjectsStreamType.VEHICLE);		
		return container;
	}
	

	
}
