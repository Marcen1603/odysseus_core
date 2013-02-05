/********************************************************************************** 
  * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.generator.simple;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;

/**
 * @author Dennis Geesen
 *
 */
public class SimpleStreamClient extends StreamClientHandler {

	private IValueGenerator timeGenerator;
	private IValueGenerator alternatingGenerator;
	private IValueGenerator constantGenerator;
	
	@Override
	public void init() {
		timeGenerator = new IncreaseGenerator(new NoError(), 0, 1);
		timeGenerator.init();
						
		
		alternatingGenerator = new AlternatingGenerator(new NoError(), 0, 1, 0, 10);
		alternatingGenerator.init();

		constantGenerator = new ConstantValueGenerator(new NoError(), 100);
		constantGenerator.init();

	}

	@Override
	public void close() {

	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		DataTuple tuple = new DataTuple();		
		// number / time (long)
		tuple.addLong(timeGenerator.nextValue());
		// temp (double)
		tuple.addDouble(alternatingGenerator.nextValue());
		// humidity (double)
		tuple.addDouble(constantGenerator.nextValue());
		
	
		
		System.out.println(tuple);
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
	public StreamClientHandler clone() {
		return new SimpleStreamClient();
	}

}
