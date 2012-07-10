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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class SimpleCSVProtocolHandler<T> extends LineProtocolHandler<T> {

	private String delimiter;
	private boolean readFirstLine = true;
	private boolean firstLineSkipped = false;

	@Override
	public T getNext() throws IOException {
		if (!firstLineSkipped && !readFirstLine){
			reader.readLine();
			firstLineSkipped = true;
		}
		delay();
		return getDataHandler().readData(reader.readLine().split(delimiter));
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		SimpleCSVProtocolHandler<T> instance = new SimpleCSVProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.delimiter = options.get("delimiter");
		if (options.get("readFirstLine") != null){
			instance.readFirstLine = Boolean.parseBoolean(options.get("readfirstline"));
		}else{
			readFirstLine = true;
		}
		return instance;
	}

	@Override
	public String getName() {
		return "SimpleCSV";
	}
	
}
