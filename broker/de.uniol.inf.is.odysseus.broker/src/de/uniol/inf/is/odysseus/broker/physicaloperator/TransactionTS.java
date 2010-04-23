package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;

public class TransactionTS implements Comparable<TransactionTS> {

	private PointInTime time;
	private int outgoingPort;
	
	public TransactionTS(int outgoingPort, PointInTime time){
		this.time = time;
		this.outgoingPort = outgoingPort;
	}
	
	public int compareToOnTime(TransactionTS other) {					
		return this.getPointInTime().compareTo(other.getPointInTime());
	}

	@Override
	public int compareTo(TransactionTS other) {		
		if(this.getPointInTime().before(other.getPointInTime())){
			return -1;
		}else{
			if(this.getPointInTime().equals(other.getPointInTime())){
				if(this.getOutgoingPort()==other.getOutgoingPort()){
					return 0;
				}else{
					if(this.getOutgoingPort()<other.getOutgoingPort()){
						return -1;
					}else{
						return 1;
					}
				}
			}
			return 1;
		}		
	}
	
	public boolean isPortAndTimeEqual(TransactionTS other){
		if(this.getOutgoingPort()==other.getOutgoingPort()){
			if(this.getPointInTime().equals(other.getPointInTime())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public PointInTime getPointInTime(){
		return this.time;
	}
	
	public int getOutgoingPort(){
		return this.outgoingPort;		
	}
	
	@Override
	public String toString() {	
		return this.time+"("+getOutgoingPort()+")";
	}

}
