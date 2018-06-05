/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.InputStream;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;


public interface ITransportHandlerListener<T extends IStreamObject<? extends IMetaAttribute>> {
	
	/**
	 * Is called when a new connection with the transport handler is established
	 * @param caller
	 */
	void onConnect(ITransportHandler caller);
	
	/**
	 * Is called when an existing connection to the transport handler is interrupted
	 * @param caller
	 */
	void onDisonnect(ITransportHandler caller);
	
	/**
	 * Implement this method to process the message
	 * @param message as ByteBuffer
	 */
	void process(long callerId, ByteBuffer message);
	
	/**
	 * Implement this method to process the message as InputStream
	 */
	void process(InputStream message);
	
	/**
	 * Implement this method to process the message
	 * @param message as String Array
	 */
	void process(String[] message);

	/**
	 * Implement this method to process the message
	 * @param message as String
	 */
	void process(String message);

	/**
	 * Implement this method to process the message
	 * @param message as T
	 */
	void process(T m, int port);

	
	/**
	 * Implement this method to process the message
	 * @param message as T
	 */
	void process(T m);

	/**
	 * With active source, use this method to state end of processing
	 */
	void propagateDone();
	
}
