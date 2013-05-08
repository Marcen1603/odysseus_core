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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class SimpleCSVProtocolHandler<T> extends LineProtocolHandler<T> {

	Logger LOG = LoggerFactory.getLogger(SimpleCSVProtocolHandler.class);

	private String delimiter;

	public SimpleCSVProtocolHandler() {
		super();
	}

	public SimpleCSVProtocolHandler(ITransportDirection direction,
			IAccessPattern access) {
		super(direction, access);
	}

	@Override
	protected void init(Map<String, String> options) {
		super.init(options);
		delimiter = options.get("delimiter");
	}

	@Override
	public T getNext() throws IOException {
		String line = super.getNextLine();
		if (line != null){
			return getDataHandler().readData(line.split(delimiter));
		}else{
			return null;
		}
	}

	@Override
	public void write(T object) throws IOException {
		List<String> output = new ArrayList<String>();
		getDataHandler().writeData(output, object);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < output.size(); i++) {
			if (i != 0) {
				sb.append(delimiter);
			}
			sb.append(output.get(i));
		}
		writer.write(sb.toString() + System.lineSeparator());
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		SimpleCSVProtocolHandler<T> instance = new SimpleCSVProtocolHandler<T>(
				direction, access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		instance.init(options);
		return instance;
	}
	
	@Override
	public String getName() {
		return "SimpleCSV";
	}

}
