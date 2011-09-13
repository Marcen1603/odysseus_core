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

package de.uniol.inf.is.odysseus.dsm.generators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * 
 * @author Daniel Weinberg
 * Created at: 07.09.2011
 */
public class Fridge extends StreamClientHandler{

	/*
	 * Durchschnittswerte Kühlschrank
	 * Verbrauch : 0,86 kWh
	 * Laufzeit: 24 Stunden
	 * Volt: 230 V
	 * Ampere: 0,156 A
	 * Watt = V * A = 35,83 W
	 */
	
	private double ampere = 0.156;
	private double volt = 230.0;

	@Override
	public void init() {

	}
	
	@Override
	public void close() {
		
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		
		tuple.addLong(SimulationClock.getInstance().getTime());
		tuple.addString("Fridge");
		tuple.addInteger(1);
		tuple.addDouble(volt);
		tuple.addDouble(ampere);

		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

}
