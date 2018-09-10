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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class SimpleCSVProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractCSVHandler<T> {

	Logger LOG = LoggerFactory.getLogger(SimpleCSVProtocolHandler.class);
	public static final String NAME = "SimpleCSV";
	
	public SimpleCSVProtocolHandler() {
		super();
	}

	public SimpleCSVProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	protected T readLine(String line, boolean readMeta) {
		List<String> ret = CSVParser.parseCSV(line, getConversionOptions().getDelimiter(), trim);
        if (ret.size() < this.getDataHandler().getSchema().size()) {
        	ret = ret.subList(0, ret.size());
        }
		return getDataHandler().readData(ret.iterator(), readMeta);
	}
		
	@Override
	public String getName() {
		return NAME;
	}
	
	public T convertLine(String line){
		return readLine(line);
	}

	public T convertLine(String line, boolean readMeta){
		return readLine(line, readMeta);
	}

	
	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		SimpleCSVProtocolHandler<T> instance = new SimpleCSVProtocolHandler<T>(
				direction, access, dataHandler, options);
		return instance;
	}

}
