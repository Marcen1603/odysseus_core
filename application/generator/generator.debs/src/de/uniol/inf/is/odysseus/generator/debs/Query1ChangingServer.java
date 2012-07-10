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
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.generator.debs;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.switching.AlternatingDurationSwitchGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.switching.SwitchGenerator;

/**
 * 
 * @author Dennis Geesen Created at: 18.04.2012
 */
public class Query1ChangingServer extends StreamClientHandler {

	// CREATE STREAM gchSource (timestamp STARTTIMESTAMP, bm05 DOUBLE, bm06
	// DOUBLE, bm07 DOUBLE, bm08 DOUBLE, bm09 DOUBLE, bm10 DOUBLE)
	// CHANNEL localhost : 54321;

	private IValueGenerator timeGenerator;
	private IValueGenerator bm05;
	private IValueGenerator bm06;
	private IValueGenerator bm07;
	private IValueGenerator bm08;
	private IValueGenerator bm09;
	private IValueGenerator bm10;

	@Override
	public void init() {
		// Time
		timeGenerator = new IncreaseGenerator(new NoError(), 0, 1);
		timeGenerator.init();

		// bm05
		bm05 = new SwitchGenerator(new NoError(), 0, 1, 10, 4, 2);
		bm05.init();

		// bm08
		bm08 = new AlternatingDurationSwitchGenerator(new NoError(), 0, 1, 4, 10, 4, 10, 4);
		bm08.init();

		// bm06
		bm06 = new SwitchGenerator(new NoError(), 0, 1, 20, 4, 2);
		bm06.init();

		// bm09
		bm09 = new AlternatingDurationSwitchGenerator(new NoError(), 0, 1, 4, 10, 4, 10, 4);
		bm09.init();

		// bm07
		bm07 = new SwitchGenerator(new NoError(), 0, 1, 20, 4, 2);
		bm07.init();

		// bm10
		bm10 = new AlternatingDurationSwitchGenerator(new NoError(), 0, 1, 4, 10, 4, 10, 4);
		bm10.init();

	}

	@Override
	public void close() {

	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		tuple.addBoolean(toBoolean(bm05.nextValue()));
		tuple.addBoolean(toBoolean(bm06.nextValue()));
		tuple.addBoolean(toBoolean(bm07.nextValue()));
		tuple.addBoolean(toBoolean(bm08.nextValue()));
		tuple.addBoolean(toBoolean(bm09.nextValue()));
		tuple.addBoolean(toBoolean(bm10.nextValue()));

		tuple.addLong(timeGenerator.nextValue());

		//System.out.println(tuple);
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
		return new Query1ChangingServer();
	}

	private boolean toBoolean(double value) {
		if (value == 1.0) {
			return true;
		} else {
			return false;
		}
	}

}
