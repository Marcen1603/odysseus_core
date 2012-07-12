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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class CSVProtocolHandler<T> extends LineProtocolHandler<T> {

	private char delimiter;
	private boolean readFirstLine = true;
	private boolean firstLineSkipped = false;

	@Override
	public T getNext() throws IOException {
		if (!firstLineSkipped && !readFirstLine){
			reader.readLine();
			firstLineSkipped = true;
		}
		delay();
		List<String> ret = new LinkedList<String>();
		String line = reader.readLine();
		if (line != null) {
			StringBuffer elem = new StringBuffer();
			boolean overreadModus1 = false;
			boolean overreadModus2 = false;
			for (char c : line.toCharArray()) {
				if (c == '\"') {
					overreadModus1 = !overreadModus1;
					//elem.append(c);
				} else if (c == '\'') {
					overreadModus2 = !overreadModus2;
					//elem.append(c);
				} else {
					if (overreadModus1 || overreadModus2) {
						elem.append(c);
					} else {
						if (delimiter == c) {
							ret.add(elem.toString());
							elem = new StringBuffer();
						} else {
							elem.append(c);
						}
					}

				}
			}
			ret.add(elem.toString());
			T retValue = getDataHandler().readData(ret);
			return retValue;
		}
		return null;
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		CSVProtocolHandler<T> instance = new CSVProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.delimiter = options.get("delimiter").toCharArray()[0];
		if (options.get("readfirstline") != null){
			instance.readFirstLine = Boolean.parseBoolean(options.get("readfirstline"));
		}else{
			readFirstLine = true;
		}
		if (options.get("delay") != null) {
			instance.setDelay(Long.parseLong(options.get("delay")));
		}
		return instance;
	}

	@Override
	public String getName() {
		return "CSV";
	}

}
