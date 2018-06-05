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

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class CSVProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractCSVHandler<T> {

	public static final String NAME = "CSV";

	public CSVProtocolHandler() {
		super();
	}

	public CSVProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	protected T readLine(String line, boolean readMeta) {
		List<String> ret = CSVParser.parseCSV(line, getConversionOptions().getTextSeperator(), getConversionOptions().getDelimiter(), trim);
        if ((this.getDataHandler().getSchema() != null) && (ret.size() < this.getDataHandler().getSchema().size())) {
            for (int i = ret.size(); i < this.getDataHandler().getSchema().size(); i++) {
                ret.add(null);
            }
        }
		T retValue = getDataHandler().readData(ret.iterator(), readMeta);
		return retValue;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		CSVProtocolHandler<T> instance = new CSVProtocolHandler<T>(direction,
				access, dataHandler, options);
		return instance;
	}

}
