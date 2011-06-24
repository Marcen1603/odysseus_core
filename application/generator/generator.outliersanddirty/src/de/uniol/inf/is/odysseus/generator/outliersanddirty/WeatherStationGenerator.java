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
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * 
 * @author Dennis Geesen
 * Created at: 24.06.2011
 */
public class WeatherStationGenerator extends StreamClientHandler {

	//CREATE STREAM weatherstation (timestamp STARTTIMESTAMP, temperature DOUBLE, humidity INTEGER, rain DOUBLE, pressure DOUBLE, location INTEGER) CHANNEL localhost : 54322;
	
	long time = 0;
	double temp = 21.0;
	int humidity = 76;
	double rain = 0.1;
	double pressure = 1020.0;
	int location = 0;
	
	
	
	
	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// number / time
		tuple.addAttribute(new Long(time));
		// temp
		tuple.addAttribute(new Double(temp));
		// humidity
		tuple.addAttribute(new Integer(humidity));
		//rain
		tuple.addAttribute(new Double(rain));
		//pressure
		tuple.addAttribute(new Double(pressure));
		//location
		tuple.addAttribute(new Integer(location));
		
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
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
