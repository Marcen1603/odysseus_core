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
package de.uniol.inf.is.odysseus.wrapper.jms.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportPattern;
/**
 * 
 * Generic transport handler for Normalized Message Router (ESB)
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class NMRTransportHandler extends AbstractTransportHandler{

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITransportHandler createInstance(ITransportPattern transportPattern, Map<String, String> options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process_open() throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process_close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
