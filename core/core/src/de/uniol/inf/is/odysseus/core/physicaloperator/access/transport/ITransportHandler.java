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
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Map;

public interface ITransportHandler  {
	
	public void addListener(ITransportHandlerListener<?> listener);
	public void removeListener(ITransportHandlerListener<?> listener);
	
	public void open() throws UnknownHostException, IOException;
	public void close() throws IOException;
	
	public void send(byte[] message) throws IOException;
	
	public ITransportHandler createInstance(Map<String, String> options);
	
	public InputStream getInputStream();
		
	String getName();
}
