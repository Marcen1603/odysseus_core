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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.test.StatusCode;

/**
 * 
 * @author Timo Michelsen, Dennis Geesen, Jan Soeren Schwarz
 *
 * @param <T>
 */
public abstract class AbstractCompareSink<T extends IStreamObject<? extends ITimeInterval>> extends AbstractSink<T> {

	protected static final int MAX_OBJECTS = 10;

	Logger logger = LoggerFactory.getLogger(AbstractCompareSink.class);

	protected boolean tracing = false;
	protected String dataHandler = "TUPLE";
    protected List<Pair<String, String>> expectedOriginals = new ArrayList<>();
    protected DefaultTISweepArea<T> expected = new DefaultTISweepArea<>();
    protected List<T> inputdata = new ArrayList<>();
    protected List<ICompareSinkListener> listeners = new ArrayList<>();

	public AbstractCompareSink(List<Pair<String, String>> expected, String dataHandler) {
		this.expectedOriginals = new ArrayList<>(expected);
		this.dataHandler = dataHandler;
	}

	public AbstractCompareSink(AbstractCompareSink<T> s) {
		this.expectedOriginals = s.expectedOriginals;
		this.tracing = s.tracing;
		this.dataHandler = s.dataHandler;
		this.listeners = new ArrayList<>(s.listeners);
	}

	@Override
	protected void process_next(T object, int port) {
		synchronized (expected) {
			if(tracing){
				System.out.println("Process next: "+object);
			}
			inputdata.add(object);
			List<T> startSame = expected.extractEqualElementsStartingEquals(object,0.00001);
			if (startSame.size() == 0) {
				stopOperation(StatusCode.ERROR_NOT_EQUIVALENT);
				logger.debug(StatusCode.ERROR_NOT_EQUIVALENT.name() + ": Following object has no counterpart in expected values: ");
				logger.debug("\t" + object.toString());
				logger.debug("\tCurrently the following expected data remains (shows only last " + MAX_OBJECTS + " objects):");
				logger.debug(expected.getSweepAreaAsString("\t", MAX_OBJECTS, true));
				logger.debug("\tReceived the following data until now (shows only last " + MAX_OBJECTS + " objects):");
				for (int i = Math.min(MAX_OBJECTS, inputdata.size()) - 1; i >= 0; i--) {
					T t = inputdata.get(i);
					logger.debug("\t " + t);
				}

			} else {
				if (startSame.size() == 1) {
					T other = startSame.get(0);
					if (object.getMetadata().getEnd().equals(other.getMetadata().getEnd())) {
						// ok - exactly same metadata
						// System.out.println("FOUND EXACTLY MATCH");
					} else {
						logger.debug("Found a match with same starttime, but endtimestamp is different! Objects are:");
						logger.debug("\t" + "Processed: " + object.toString());
						logger.debug("\t" + "Expected:" + object.toString());
					}
				} else {
					logger.debug("There are more than one matching values. Just removed one from expected outputs!");
				}

			}
		}
	}

	public void addListener(ICompareSinkListener listener) {
		this.listeners.add(listener);
	}

	@Override
	protected void process_close() {

	}

	protected void stopOperation(StatusCode code) {
		stopOperation(false, code);
	}

	protected void stopOperation(boolean done, StatusCode code) {
		for (ICompareSinkListener listener : this.listeners) {
			listener.compareSinkProcessingDone(this, done, code);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	protected void process_done(int port) {
		synchronized (expected) {
			logger.debug("Received done...");
			if (expected.size() > 0) {
				stopOperation(true, StatusCode.ERROR_MISSING_DATA);
				logger.debug(StatusCode.ERROR_MISSING_DATA.name() + ": Some expected data was not part of the processing. Expected output still contains "+expected.size()+" objects. Last " + MAX_OBJECTS + " are:");
				logger.debug(expected.getSweepAreaAsString("\t", MAX_OBJECTS, true));

			} else {
				stopOperation(true, StatusCode.OK);
			}
		}
	}

	public boolean isTracing() {
		return tracing;
	}

	public void setTracing(boolean tracing) {
		logger.debug("set tracing to: "+tracing);
		this.tracing = tracing;
	}
}
