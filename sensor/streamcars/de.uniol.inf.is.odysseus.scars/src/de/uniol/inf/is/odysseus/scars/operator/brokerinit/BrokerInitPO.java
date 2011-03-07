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
package de.uniol.inf.is.odysseus.scars.operator.brokerinit;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class BrokerInitPO<R> extends AbstractPipe<R, R> {

	private int size = 1;
	private int counter = 0;
	private int punctuationCounter = 0;
	
	public BrokerInitPO() {
		
	}
	
	public BrokerInitPO( BrokerInitPO<R> po ) {
		setSize( po.getSize() );
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
//		if( punctuationCounter < size ) {
			printOutput("Send Punctuation: " + timestamp);
			sendPunctuation(timestamp);
//			punctuationCounter++;
//		}
	}

	@Override
	protected void process_next(R object, int port) {
		if( counter < size ) {
			printOutput("Send tuple");
			transfer(object);
			counter++;
		}
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new BrokerInitPO<R>(this);
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize( int size ) {
		if( size < 1 ) throw new IllegalArgumentException("size of BrokerInitPO must be posistive");
		this.size = size;
	}
	  
	  private void printOutput( String txt ) {
		  System.out.println("BROKER-INIT(" + hashCode() + "):" + txt);
	  }
}
