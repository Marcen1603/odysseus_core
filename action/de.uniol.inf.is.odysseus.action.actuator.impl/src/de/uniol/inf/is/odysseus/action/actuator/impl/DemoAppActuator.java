package de.uniol.inf.is.odysseus.action.actuator.impl;

import java.text.DateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.action.demoapp.AuctionMonitor;
import de.uniol.inf.is.odysseus.action.demoapp.AuctionMonitor.AuctionStatus;
import de.uniol.inf.is.odysseus.action.services.actuator.adapter.ActuatorAdapterSchema;


public class DemoAppActuator{
	
	@ActuatorAdapterSchema(show = true)
	public void newAuction(int id, String itemName, long timeStamp) {
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, "-", "-", DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.RED);
	}
	
	@ActuatorAdapterSchema(show = true)
	public void updateGreen(int id, String itemName, String personName, double price, long timeStamp){
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, ""+price, personName, DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.GREEN);
	}
	
	@ActuatorAdapterSchema(show = true)
	public void updateOrange(int id, String itemName, String personName, double price, long timeStamp){
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, ""+price, personName, DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.ORANGE);
	}

}
