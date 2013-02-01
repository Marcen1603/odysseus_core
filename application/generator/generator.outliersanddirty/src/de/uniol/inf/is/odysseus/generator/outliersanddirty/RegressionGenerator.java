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

package de.uniol.inf.is.odysseus.generator.outliersanddirty;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.AlternatingGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;

/**
 * 
 * @author Dennis Geesen
 * Created at: 30.03.2012
 */
public class RegressionGenerator extends StreamClientHandler{

	// CREATE STREAM clusters (timestamp STARTTIMESTAMP, valX DOUBLE, valY DOUBLE) CHANNEL localhost : 54321;
	
	private IValueGenerator timeGenerator;
	private IValueGenerator xGenerator;
	private IValueGenerator yGenerator;
	
	@Override
	public void init() {
		//Time
				timeGenerator = new IncreaseGenerator(new NoError(), 0, 1);
				timeGenerator.init();
								
				
				//xGenerator = new IncreaseGenerator(new NoError(), 0, 1);
				xGenerator = new AlternatingGenerator(new NoError(), 0, 1, 0, 10);
				xGenerator.init();

				yGenerator = new IncreaseGenerator(new NoError(), 0, 1);
				yGenerator.init();
				
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();		
		// number / time (long)
		tuple.addLong(timeGenerator.nextValue());
		// temp (double)
		tuple.addDouble(xGenerator.nextValue());
		// humidity (integer)
		tuple.addDouble(yGenerator.nextValue());
		
		tuple.addByte((byte)92);
		
		tuple.addShort((short)92);
		
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
		return new RegressionGenerator();
	}

}
