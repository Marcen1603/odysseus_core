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
package de.uniol.inf.is.odysseus.test.sinks.physicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractCSVHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

/**
 * Sink, which compares calculated relational tuple with predefined tuple. Used
 * for tests only. Use callbacks to inform listener if one calculated tuple
 * differs or all calculated tuples are correct.
 * 
 * @author Timo Michelsen, Dennis Geesen
 * 
 */
public class TICompareSink<T extends IStreamObject<? extends ITimeInterval>> extends AbstractCompareSink<T> {

	Logger logger = LoggerFactory.getLogger(TICompareSink.class);

	public TICompareSink(List<Pair<String, String>> expected, String dataHandler) {
		super(expected, dataHandler);
	}

	public TICompareSink(TICompareSink<T> s) {
		super(s);
	}

	@Override
	public TICompareSink<T> clone() {
		return new TICompareSink<T>(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void process_open() throws OpenFailedException {
		synchronized (expected) {
			this.expected.clear();
			this.inputdata.clear();
			IDataHandler<T> dh = (IDataHandler<T>) DataHandlerRegistry.getDataHandler(this.dataHandler, getOutputSchema());
			Map<String, String> options = new HashMap<>();
			options.put(AbstractCSVHandler.DELIMITER, "|");
			SimpleCSVProtocolHandler<T> csvreader = new SimpleCSVProtocolHandler<T>(ITransportDirection.IN, IAccessPattern.PULL, dh);
			csvreader = (SimpleCSVProtocolHandler<T>) csvreader.createInstance(ITransportDirection.IN, IAccessPattern.PULL, options, dh);
			for (Pair<String, String> csv : expectedOriginals) {
				T tuple = (T) csvreader.convertLine(csv.getE1());
				TimeInterval ti = TimeInterval.parseTimeInterval(csv.getE2());
				((Tuple)tuple).setMetadata(ti);
				this.expected.insert(tuple);
				if(tracing){
					System.out.println("load expected tuple: "+tuple);
				}
			}
			logger.debug("expected data loaded");
		}
	}

}
