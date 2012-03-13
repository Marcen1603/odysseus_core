/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
/**
 * The TransactionTS represents a waiting transaction, which has to be execute by the broker.
 * 
 * @author Dennis Geesen
 */
public class TransactionTS implements Comparable<TransactionTS> {

	/** The time. */
	private PointInTime time;
	
	/** The outgoing port. */
	private int outgoingPort;
	
	/**
	 * Instantiates a new transaction.
	 *
	 * @param outgoingPort the reading port for the transaction
	 * @param time the time of the request
	 */
	public TransactionTS(int outgoingPort, PointInTime time){
		this.time = time;
		this.outgoingPort = outgoingPort;
	}
	
	/**
	 * Compares the time to another transaction.
	 * This is similar to compareTo, but only considers the time.
	 * @see java.lang.Comparable#compareTo(java.lang.Object) 
	 *
	 * @param other the other transaction
	 * @return -1, 0 or 1 as this objects time is less than, equal or before than the other object
	 */
	public int compareToOnTime(TransactionTS other) {					
		return this.getPointInTime().compareTo(other.getPointInTime());
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TransactionTS other) {		
		if(this.getPointInTime().before(other.getPointInTime())){
			return -1;
		}
        if(this.getPointInTime().equals(other.getPointInTime())){
        	if(this.getOutgoingPort()==other.getOutgoingPort()){
        		return 0;
        	}
            if(this.getOutgoingPort()<other.getOutgoingPort()){
            	return -1;
            }
            return 1;
        }
        return 1;		
	}
	
	/**
	 * Checks if port and time of two transactions are equal.
	 *
	 * @param other the other transaction
	 * @return true, if is port and time are equal
	 */
	public boolean isPortAndTimeEqual(TransactionTS other){
		if(this.getOutgoingPort()==other.getOutgoingPort()){
			if(this.getPointInTime().equals(other.getPointInTime())){
				return true;
			}
            return false;
		}
        return false;
	}
	
	/**
	 * Gets the time of the transaction.
	 *
	 * @return the time
	 */
	public PointInTime getPointInTime(){
		return this.time;
	}
	
	/**
	 * Gets the reading port.
	 *
	 * @return the reading port of the transaction
	 */
	public int getOutgoingPort(){
		return this.outgoingPort;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {	
		return this.time+"("+getOutgoingPort()+")";
	}

}
