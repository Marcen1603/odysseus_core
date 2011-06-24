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
public class WindGenerator extends StreamClientHandler{

	//CREATE STREAM wind (timestamp LONG, bft INTEGER, wind_speed DOUBLE, wind_direction INTEGER, location INTEGER) CHANNEL localhost : 54321;

	long currentTime = 0;
	int currentBeafort = 0;
	double windSpeed = 0;
	int windDirection = 90;
	int location = 0;

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// number / time
		tuple.addAttribute(new Long(currentTime));
		// beaufort
		tuple.addAttribute(new Integer(currentBeafort));
		//wind speed
		tuple.addAttribute(new Double(windSpeed));
		//wind direction
		tuple.addAttribute(new Integer(windDirection));
		//location
		tuple.addAttribute(new Integer(location));
		
		//increase values
		currentTime++;
		
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

}
