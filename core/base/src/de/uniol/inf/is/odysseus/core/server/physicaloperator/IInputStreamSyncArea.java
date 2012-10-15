/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 *	This class can be used to generate time order over different input streams. 
 *  Additional Streams can be connected.
 *  
 *  @author Marco Grawunder
 *
 * @param <T> Elements that will be processed
 */
public interface IInputStreamSyncArea<T extends IStreamObject<?>> extends IClone {
	
	/**
	 * Adds a new element to the input area and determines if new elements can be generated
	 * Drops out of order elements.
	 * @param object the element top add
	 * @param port the input port of the element
	 */
	public void newElement(T object, int inPort);

	/**
	 * Can be called to state time progress 
	 * @param heartbeat all following elements have a new time stamp 
	 * @param inPort on this port
	 */
	public void newHeartbeat(PointInTime heartbeat, int inPort);

	/**
	 * States the end of processing and leads to sending all elements from 
	 * the input area to the next operator
	 */
	public void done();

	/**
	 * This method needs to be called before elements are send to the area
	 * @param sink
	 */
	public void init(IProcessInternal<T> sink);

	/** 
	 * This method can be used to add another port at runtime. Out out time elements
	 * will be removed and processing interrupted until all input streams are in sync again 
	 */
	void addInputPort(int port);	

	/**
	 * Removed the input port port from processing
	 * @param pos
	 */
	void removeInputPort(int port);
	
	public int size();

	@Override
	public IInputStreamSyncArea<T> clone();
	

	
}
