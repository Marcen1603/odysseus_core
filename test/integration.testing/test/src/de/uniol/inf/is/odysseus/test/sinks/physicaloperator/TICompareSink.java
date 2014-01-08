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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractCSVHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.test.StatusCode;

/**
 * Sink, which compares calculated relational tuple with predefined tuple. Used
 * for tests only. Use callbacks to inform listener if one calculated tuple
 * differs or all calculated tuples are correct.
 * 
 * @author Timo Michelsen, Dennis Geesen
 * 
 */
public class TICompareSink extends AbstractSink<Tuple<? extends ITimeInterval>> {

	Logger logger = LoggerFactory.getLogger(TICompareSink.class);

	private List<Pair<String, String>> expectedOriginals = new ArrayList<>();
	private DefaultTISweepArea<Tuple<? extends ITimeInterval>> expected = new DefaultTISweepArea<>();
	// private DefaultTISweepArea<Tuple<? extends ITimeInterval>> input = new
	// DefaultTISweepArea<>();
	private List<ICompareSinkListener> listeners = new ArrayList<>();

	public TICompareSink(List<Pair<String, String>> expected) {
		this.expectedOriginals = new ArrayList<>(expected);
	}

	public TICompareSink(TICompareSink s) {
		this.expectedOriginals = s.expectedOriginals;
		this.listeners = new ArrayList<>(s.listeners);
	}

	public void addListener(ICompareSinkListener listener) {
		this.listeners.add(listener);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.expected.clear();
		@SuppressWarnings("unchecked")
		IDataHandler<Tuple<ITimeInterval>> dh = (IDataHandler<Tuple<ITimeInterval>>) DataHandlerRegistry.getDataHandler("TUPLE", getOutputSchema());
		Map<String, String> options = new HashMap<>();
		options.put(AbstractCSVHandler.DELIMITER, "|");
		SimpleCSVProtocolHandler<Tuple<ITimeInterval>> csvreader = new SimpleCSVProtocolHandler<Tuple<ITimeInterval>>(ITransportDirection.IN, IAccessPattern.PULL, dh);
		csvreader = (SimpleCSVProtocolHandler<Tuple<ITimeInterval>>) csvreader.createInstance(ITransportDirection.IN, IAccessPattern.PULL, options, dh);
		for (Pair<String, String> csv : expectedOriginals) {
			Tuple<ITimeInterval> tuple = csvreader.convertLine(csv.getE1());
			TimeInterval ti = TimeInterval.parseTimeInterval(csv.getE2());
			tuple.setMetadata(ti);
			this.expected.insert(tuple);
		}
	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> tuple, int port) {
		List<Tuple<? extends ITimeInterval>> startSame = expected.extractEqualElementsStartingEquals(tuple);
		if (startSame.size() == 0) {
			stopOperation(StatusCode.ERROR_NOT_EQUIVALENT);
		} else {
			if (startSame.size() == 1) {
				Tuple<? extends ITimeInterval> other = startSame.get(0);
				if (tuple.getMetadata().getEnd().equals(other.getMetadata().getEnd())) {
					// ok - exactly same metadata
//					System.out.println("FOUND EXACTLY MATCH");
				} else {
					System.out.println("different METADATA");
				}
			}

		}
	}

	@Override
	protected void process_close() {

	}

	private void stopOperation(StatusCode code) {
		stopOperation(false, code);
	}

	private void stopOperation(boolean done, StatusCode code) {
		for (ICompareSinkListener listener : this.listeners) {
			listener.compareSinkProcessingDone(this, done, code);
		}
	}

	@Override
	protected void process_done(int port) {
		if (expected.size() > 0) {
			stopOperation(true, StatusCode.ERROR_MISSING_DATA);
		} else {
			stopOperation(true, StatusCode.OK);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	@Override
	public TICompareSink clone() {
		return new TICompareSink(this);
	}

}
