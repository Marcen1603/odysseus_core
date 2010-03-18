package de.uniol.inf.is.odysseus.broker.movingobjects.generator;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class VehicleGenerator {
	
	private int pointer;
	private double movingOffset = 0d;
	private int interval = 0;
	private List<Vehicle> vehicles = new ArrayList<Vehicle>();
	
	public VehicleGenerator(){
		Random rand = new Random();
		for(int i=0;i<10;i++){
			double posX = rand.nextDouble();
			double posY = rand.nextDouble();
			vehicles.add(new Vehicle(i, interval, posX, posY));
		}
	}	
	
	public Vehicle getNext(){
		
		Vehicle current = this.vehicles.get(pointer);
		
		current.setInterval(interval);
		current.setPositionX(current.getPositionX()+movingOffset);
		current.setPositionY(current.getPositionY()+movingOffset);
		pointer++;
		if(pointer == 10){
			pointer=0;		
			movingOffset = movingOffset+1.0d;
			interval++;
		}
		return current;
	}
}
