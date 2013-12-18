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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.test.sinks.ICompareSinkListener;

/**
 * Sink, which compares calculated relational tuple with predefined tuple. Used
 * for tests only. Use callbacks to inform listener if one calculated tuple
 * differs or all calculated tuples are correct.
 * 
 * @author Timo Michelsen, Dennis Geesen
 * 
 */
public class TICompareSink extends AbstractSink<Tuple<ITimeInterval>> {

	Logger logger = LoggerFactory.getLogger(TICompareSink.class);

	final private ICompareSinkListener sinkListener;
	List<Tuple<ITimeInterval>> expectedOriginals = new ArrayList<>();
	DefaultTISweepArea<Tuple<ITimeInterval>> expected = new DefaultTISweepArea<>();
	DefaultTISweepArea<Tuple<ITimeInterval>> input = new DefaultTISweepArea<>();

	public TICompareSink(List<Tuple<ITimeInterval>> expectedOutput, ICompareSinkListener sinkListener) {
		this.expectedOriginals = new ArrayList<>(expectedOutput);
		this.sinkListener = sinkListener;
	}

	public TICompareSink(TICompareSink s) {
		this.sinkListener = s.sinkListener;
		this.expectedOriginals = s.expectedOriginals;
	}

	@Override
	protected void process_open() throws OpenFailedException {		
		this.expected.clear();
		this.expected.insertAll(expectedOriginals);		
	}
	
	@Override
	protected void process_next(Tuple<ITimeInterval> tuple, int port) {
		input.insert(tuple);		
	}
	
	@Override
	protected void process_close() {
		System.out.println("Check if sweepareas are equal!");
		if(expected.compareTo(input)==0){
			System.out.println("YES, ALL OK!!");
		}else{
			System.out.println("No, test failed!!");
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
