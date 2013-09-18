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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class CSVProtocolHandler<T> extends AbstractCSVHandler<T> {
	
	public static final String NAME = "CSV";
	
	public CSVProtocolHandler() {
		super();
	}

	public CSVProtocolHandler(ITransportDirection direction,
			IAccessPattern access) {
		super(direction, access);
	}

	@Override
	protected T readLine(String line) {
		List<String> ret = new LinkedList<String>();
		StringBuffer elem = new StringBuffer();
		boolean overreadModus1 = false;
		boolean overreadModus2 = false;

		for (char c : line.toCharArray()) {

			if (c == textDelimiter) {
				overreadModus1 = !overreadModus1;
				// elem.append(c);
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
		if (trim){
			List<String> trimmed = new LinkedList<String>();
			for (String l: ret){
				trimmed.add(l.trim());
			}
			ret = trimmed;
		}
		T retValue = getDataHandler().readData(ret);
		return retValue;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		CSVProtocolHandler<T> instance = new CSVProtocolHandler<T>(direction,
				access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		instance.init(options);
		return instance;
	}

}
