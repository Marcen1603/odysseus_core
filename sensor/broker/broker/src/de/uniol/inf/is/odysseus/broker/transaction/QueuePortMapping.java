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
package de.uniol.inf.is.odysseus.broker.transaction;

/**
 * QueuePortMapping maps a writing port of a queue stream to a reading port of a data stream.
 * 
 * @author Dennis Geesen
 */
public class QueuePortMapping extends AbstractPortTuple{

	/**
	 * Instantiates a new queue port mapping.
	 *
	 * @param dataReadingPort the data reading port
	 * @param queueWritingPort the queue writing port
	 */
	public QueuePortMapping(int dataReadingPort, int queueWritingPort) {
		super(2);
		super.setPort(0, dataReadingPort);
		super.setPort(1, queueWritingPort);
	}
	
	/**
	 * Gets the data reading port.
	 *
	 * @return the data reading port
	 */
	public int getDataReadingPort(){
		return super.getPort(0);		
	}
	
	/**
	 * Gets the queue writing port.
	 *
	 * @return the queue writing port
	 */
	public int getQueueWritingPort(){
		return super.getPort(1);
	}
	

}
