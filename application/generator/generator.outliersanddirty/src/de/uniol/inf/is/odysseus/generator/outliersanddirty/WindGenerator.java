/** Copyright 2011 The Odysseus Team
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.BurstErrorModel;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.ContinuousErrorModel;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.NoError;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.PredifinedValueGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.distribution.UniformDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.evolve.AlternatingGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.evolve.IncreaseGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.noise.DuplicateNoise;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.noise.JitterNoise;

/**
 * 
 * @author Dennis Geesen
 * Created at: 24.06.2011
 */
public class WindGenerator extends StreamClientHandler{

	//CREATE STREAM wind (timestamp LONG, bft INTEGER, wind_speed DOUBLE, wind_direction INTEGER, location INTEGER) CHANNEL localhost : 54321;

	enum Attribute{
		Time, 
		Beaufort,
		Speed,
		Direction,		
		Location
	}
	
	private Map<Attribute, IValueGenerator> generators = new HashMap<Attribute, IValueGenerator>();

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// number / time (long)
		tuple.addLong(this.generators.get(Attribute.Time).nextValue());
		// bft (integer)
		tuple.addInteger(this.generators.get(Attribute.Beaufort).nextValue());
		// wind speed (double)
		tuple.addDouble(this.generators.get(Attribute.Speed).nextValue());
		// wind direction (integer)
		tuple.addInteger(this.generators.get(Attribute.Direction).nextValue());		
		//location (integer)
		tuple.addInteger(this.generators.get(Attribute.Location).nextValue());
			
		
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
		//Time
		IValueGenerator timeGenerator = new IncreaseGenerator(new NoError(), 0, 1);
		timeGenerator.init();
		this.generators.put(Attribute.Time, timeGenerator);		
		//Beaufort
		IValueGenerator bftGenerator = new UniformDistributionGenerator(new ContinuousErrorModel(new DuplicateNoise(), 3), 2, 3);
		bftGenerator.init();
		this.generators.put(Attribute.Beaufort, bftGenerator);
		//speed
		//IValueGenerator speedGenerator = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(5)), 18);
		IValueGenerator speedGenerator = new PredifinedValueGenerator(101,164,454,324,145,123,241,232,322);
		speedGenerator.init();
		this.generators.put(Attribute.Speed, speedGenerator);
		//Direction
		IValueGenerator directionGenerator = new ConstantValueGenerator(new BurstErrorModel(new JitterNoise(50), 10, 4), 182);
		directionGenerator.init();
		this.generators.put(Attribute.Direction, directionGenerator);		
		//Location
		IValueGenerator locationGenerator = new AlternatingGenerator(new NoError(), 0, 2, 0, 20);
		locationGenerator.init();
		this.generators.put(Attribute.Location, locationGenerator);
		
	}

	@Override
	public void close() {		
		
	}

}
