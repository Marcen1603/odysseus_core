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
import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.RandomErrorModel;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.distribution.GaussianRandomGenerator;
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
public class WeatherStationGenerator extends StreamClientHandler {

	//CREATE STREAM weatherstation (timestamp STARTTIMESTAMP, temperature DOUBLE, humidity INTEGER, rain DOUBLE, pressure DOUBLE, location INTEGER) CHANNEL localhost : 54322;
	enum Attribute{
		Time, 
		Temperature,
		Humidity,
		Rain,
		Pressure,
		Location
	}
	
	
	long time = 0;
	double temp = 21.0;
	int humidity = 76;
	double rain = 0.1;
	double pressure = 1020.0;
	int location = 0;
	
	private Map<Attribute, IValueGenerator> generators = new HashMap<Attribute, IValueGenerator>();
	
	
	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// number / time (long)
		tuple.addLong(this.generators.get(Attribute.Time).nextValue());
		// temp (double)
		tuple.addDouble(this.generators.get(Attribute.Temperature).nextValue());
		// humidity (integer)
		tuple.addInteger(this.generators.get(Attribute.Humidity).nextValue());
		//rain (double)
		tuple.addDouble(this.generators.get(Attribute.Rain).nextValue());
		//pressure (double)
		tuple.addDouble(this.generators.get(Attribute.Pressure).nextValue());
		//location (integer)
		tuple.addInteger(this.generators.get(Attribute.Location).nextValue());
		
		//increase values
		time++;
		
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
		//Temperature
		IValueGenerator tempGenerator = new UniformDistributionGenerator(new ContinuousErrorModel(new DuplicateNoise(), 3), 18, 25);
		tempGenerator.init();
		this.generators.put(Attribute.Temperature, tempGenerator);
		//Humidity
		IValueGenerator humGenerator = new ConstantValueGenerator(new RandomErrorModel(new JitterNoise(5)), 1020);
		humGenerator.init();
		this.generators.put(Attribute.Humidity, humGenerator);
		//Rain
		IValueGenerator rainGenerator = new GaussianRandomGenerator(new BurstErrorModel(new JitterNoise(50), 10, 4), 50, 2);
		rainGenerator.init();
		this.generators.put(Attribute.Rain, rainGenerator);
		//Pressure
		IValueGenerator pressGenerator = new ConstantValueGenerator(new NoError(), 100);
		pressGenerator.init();
		this.generators.put(Attribute.Pressure, pressGenerator);
		//Location
		IValueGenerator locationGenerator = new AlternatingGenerator(new NoError(), 0, 2, 0, 20);
		locationGenerator.init();
		this.generators.put(Attribute.Location, locationGenerator);

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
