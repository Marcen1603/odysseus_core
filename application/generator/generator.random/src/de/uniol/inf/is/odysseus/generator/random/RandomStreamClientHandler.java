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

package de.uniol.inf.is.odysseus.generator.random;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class RandomStreamClientHandler extends StreamClientHandler {

	private static final int WAITING_TIME_MILLIS = 500;
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final int DEFAULT_MIN_VALUE = 0;
	private static final int DEFAULT_MAX_VALUE = 100;
	
	private final int minValue;
	private final int maxValue;
	
	private boolean isGenerating = true;
	
	public RandomStreamClientHandler() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}
	
	public RandomStreamClientHandler( int min, int max ) {
		if( min > max ) {
			minValue = max;
			maxValue = min;
		} else {
			minValue = min;
			maxValue = max;
		}
	}
	
	@Override
	public void init() {
	}

	@Override
	public void close() {
		isGenerating = false;
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		if( !isGenerating ) {
			// stops the StreamServer
			return null;
		}
		
		List<DataTuple> tuples = Lists.newArrayList();
		tuples.add(createRandomTuple(minValue, maxValue));
		
		waitSomeTime();
		
		return tuples;
	}
	
	public void stopGeneration() {
		isGenerating = false;
	}

	@Override
	public StreamClientHandler clone() {
		return new RandomStreamClientHandler(minValue, maxValue);
	}

	protected DataTuple createRandomTuple(int min, int max) {
		DataTuple t = new DataTuple();
		t.addLong(System.currentTimeMillis());
		t.addLong(createRandomValue(min, max));
		return t;
	}

	protected Long createRandomValue(int min, int max) {
		return (long)(RANDOM.nextDouble() * (max - min)) + min;
	}
	
	private static void waitSomeTime() {
		try {
			Thread.sleep(WAITING_TIME_MILLIS);
		} catch (InterruptedException ex) {}
	}
}
