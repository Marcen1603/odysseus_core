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

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractTransportHandler implements ITransportHandler {

	private List<ITransportHandlerListener<?>> transportHandlerListener = new ArrayList<ITransportHandlerListener<?>>();
	private int openCounter = 0;
	
	
	@Override
	public void addListener(ITransportHandlerListener<?> listener) {
		this.transportHandlerListener.add(listener);
	}

	@Override
	public void removeListener(ITransportHandlerListener<?> listener) {
		this.transportHandlerListener.remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	public void fireProcess(ByteBuffer message){
		for (ITransportHandlerListener<?> l: transportHandlerListener){
			((ITransportHandlerListener<ByteBuffer>)l).process(message);
		}
	}
	
	final synchronized public void open() throws UnknownHostException, IOException {
		if (openCounter == 0){
			process_open();
		}
		openCounter++;		
	}
	
	abstract public void process_open() throws UnknownHostException, IOException;

	@Override
	final synchronized public void close() throws IOException {
		openCounter--;
		if (openCounter == 0){
			process_close();
		}
	}
	
	abstract public void process_close() throws IOException;
	
}
