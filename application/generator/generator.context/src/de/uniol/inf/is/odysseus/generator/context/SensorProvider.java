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

package de.uniol.inf.is.odysseus.generator.context;

import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.DataType;
import de.uniol.inf.is.odysseus.generator.valuegenerator.PredifinedValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;

/**
 * 
 * @author Dennis Geesen Created at: 07.05.2012
 */
public class SensorProvider extends StreamClientHandler {

	@Override
	public void init() {
		// Time
		addGenerator(new IncreaseGenerator(new NoError(), 0, 2), DataType.LONG);
		// temp
		addGenerator(new PredifinedValueGenerator(23.1, 23.2, 23.3, 23.1, 23.4));

	}

	@Override
	public void close() {		

	}

	@Override
	public List<DataTuple> next() {
		pause(200);
		return buildDataTuple();
	}

	@Override
	public StreamClientHandler clone() {
		return new SensorProvider();
	}

}
