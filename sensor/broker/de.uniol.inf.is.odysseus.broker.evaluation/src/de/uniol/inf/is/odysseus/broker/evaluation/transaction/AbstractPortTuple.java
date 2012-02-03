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
package de.uniol.inf.is.odysseus.broker.evaluation.transaction;

/**
 * This class provides an abstract data structure to save several 
 * ports which are belong together.
 * 
 * @author Dennis Geesen
 */
public abstract class AbstractPortTuple {
	
	/** The ports. */
	int[] ports = new int[0];
	
	/**
	 * Instantiates a new tuple of ports.
	 *
	 * @param count the count of ports
	 */
	public AbstractPortTuple(int count){
		this.ports = new int[count];
	}
	
	/**
	 * Sets a new port for the given index
	 *
	 * @param number the index
	 * @param port the new port
	 */
	public void setPort(int number, int port){
		this.ports[number] = port;
	}
	
	/**
	 * Gets the port for a given index.
	 *
	 * @param number the index
	 * @return the port
	 */
	public int getPort(int number){
		return this.ports[number];
	}
}
