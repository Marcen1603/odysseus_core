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

	private static final int MAX_TUPLES = 10;

	Logger logger = LoggerFactory.getLogger(TICompareSink.class);
	private boolean tracing = false;
    private String dataHandler = "TUPLE";
	private List<Pair<String, String>> expectedOriginals = new ArrayList<>();
	private DefaultTISweepArea<Tuple<? extends ITimeInterval>> expected = new DefaultTISweepArea<>();
	private List<Tuple<? extends ITimeInterval>> inputdata = new ArrayList<>();
	// private DefaultTISweepArea<Tuple<? extends ITimeInterval>> input = new
	// DefaultTISweepArea<>();
	private List<ICompareSinkListener> listeners = new ArrayList<>();

	public TICompareSink(List<Pair<String, String>> expected, String dataHandler) {
		this.expectedOriginals = new ArrayList<>(expected);
		this.dataHandler = dataHandler;
	}

	public TICompareSink(TICompareSink s) {
		this.expectedOriginals = s.expectedOriginals;
		this.tracing = s.tracing;
		this.dataHandler = s.dataHandler;
		this.listeners = new ArrayList<>(s.listeners);
	}

	public void addListener(ICompareSinkListener listener) {
		this.listeners.add(listener);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		synchronized (expected) {
			this.expected.clear();
			this.inputdata.clear();
			@SuppressWarnings("unchecked")
			IDataHandler<Tuple<ITimeInterval>> dh = (IDataHandler<Tuple<ITimeInterval>>) DataHandlerRegistry.getDataHandler(this.dataHandler, getOutputSchema());
			Map<String, String> options = new HashMap<>();
			options.put(AbstractCSVHandler.DELIMITER, "|");
			SimpleCSVProtocolHandler<Tuple<ITimeInterval>> csvreader = new SimpleCSVProtocolHandler<Tuple<ITimeInterval>>(ITransportDirection.IN, IAccessPattern.PULL, dh);
			csvreader = (SimpleCSVProtocolHandler<Tuple<ITimeInterval>>) csvreader.createInstance(ITransportDirection.IN, IAccessPattern.PULL, options, dh);
			for (Pair<String, String> csv : expectedOriginals) {
				Tuple<ITimeInterval> tuple = csvreader.convertLine(csv.getE1());
				TimeInterval ti = TimeInterval.parseTimeInterval(csv.getE2());
				tuple.setMetadata(ti);
				this.expected.insert(tuple);
				if(tracing){
					System.out.println("load expected tuple: "+tuple);
				}
			}
			logger.debug("expected data loaded");
		}
	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> tuple, int port) {
		synchronized (expected) {
			if(tracing){
				System.out.println("Process next: "+tuple);
			}
			inputdata.add(tuple);
			List<Tuple<? extends ITimeInterval>> startSame = expected.extractEqualElementsStartingEquals(tuple,0.00001);
			if (startSame.size() == 0) {
				stopOperation(StatusCode.ERROR_NOT_EQUIVALENT);
				logger.debug(StatusCode.ERROR_NOT_EQUIVALENT.name() + ": Following tuple has no counterpart in expected values: ");
				logger.debug("\t" + tuple.toString());
				logger.debug("\tCurrently the following expected data remains (shows only last " + MAX_TUPLES + " tuples):");
				logger.debug(expected.getSweepAreaAsString("\t", MAX_TUPLES, true));
				logger.debug("\tReceived the following data until now (shows only last " + MAX_TUPLES + " tuples):");
				for (int i = Math.min(MAX_TUPLES, inputdata.size()) - 1; i >= 0; i--) {
					Tuple<? extends ITimeInterval> t = inputdata.get(i);
					logger.debug("\t " + t);
				}

			} else {
				if (startSame.size() == 1) {
					Tuple<? extends ITimeInterval> other = startSame.get(0);
					if (tuple.getMetadata().getEnd().equals(other.getMetadata().getEnd())) {
						// ok - exactly same metadata
						// System.out.println("FOUND EXACTLY MATCH");
					} else {
						logger.debug("Found a match with same starttime, but endtimestamp is different! Tuples are:");
						logger.debug("\t" + "Processed: " + tuple.toString());
						logger.debug("\t" + "Expected:" + tuple.toString());
					}
				} else {
					logger.debug("There are more than one matching values. Just removed one from expected outputs!");
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
		synchronized (expected) {
			logger.debug("Received done...");
			if (expected.size() > 0) {
				stopOperation(true, StatusCode.ERROR_MISSING_DATA);
				logger.debug(StatusCode.ERROR_MISSING_DATA.name() + ": Some expected data was not part of the processing. Expected output still contains the following tuples (last " + MAX_TUPLES + "):");
				logger.debug(expected.getSweepAreaAsString("\t", MAX_TUPLES, true));

			} else {
				stopOperation(true, StatusCode.OK);
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	@Override
	public TICompareSink clone() {
		return new TICompareSink(this);
	}

	public boolean isTracing() {
		return tracing;
	}

	public void setTracing(boolean tracing) {
		logger.debug("set tracing to: "+tracing);
		this.tracing = tracing;
	}

}
