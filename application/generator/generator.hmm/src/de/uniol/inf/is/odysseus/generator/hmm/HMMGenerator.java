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
package de.uniol.inf.is.odysseus.generator.hmm;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class HMMGenerator extends StreamClientHandler {

	private int number = 0;
	private int nextIndex = 0;

	private String[] observations = { "walk", "shop", "clean" };

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// number / time
		tuple.addAttribute(new Long(number));
		// observation
		tuple.addAttribute(observations[nextIndex]);

		number++;
		nextIndex++;
		if (nextIndex == observations.length) {
			nextIndex = 0;
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}
	

	@Override
	public void init() {

	}

	@Override
	public void close() {		

	}
	
	@Override
	public StreamClientHandler clone() {
		return new HMMGenerator();
	}

}
