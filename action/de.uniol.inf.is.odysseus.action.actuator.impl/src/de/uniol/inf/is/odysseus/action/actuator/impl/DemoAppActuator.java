package de.uniol.inf.is.odysseus.action.actuator.impl;

import java.text.DateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.action.demoapp.AuctionMonitor;
import de.uniol.inf.is.odysseus.action.demoapp.AuctionMonitor.AuctionStatus;
import de.uniol.inf.is.odysseus.action.services.actuator.ActuatorAdapter;

public class DemoAppActuator extends ActuatorAdapter{
	
	public void newAuction(int id, String itemName, long timeStamp) {
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, "-", "-", DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.RED);
	}
	
	public void updateOrange(int id, String itemName, String personName, double price, long timeStamp){
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, ""+price, personName, DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.ORANGE);
	}
	
	public void updateGreen(int id, String itemName, String personName, double price, long timeStamp){
		AuctionMonitor.getInstance().updateData(
				new String[] {
						""+id,itemName, ""+price, personName, DateFormat.getInstance().format(new Date(timeStamp)) 
                }, AuctionStatus.GREEN);
	}

}
