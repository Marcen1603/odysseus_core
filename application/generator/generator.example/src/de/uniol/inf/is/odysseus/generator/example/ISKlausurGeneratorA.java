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

package de.uniol.inf.is.odysseus.generator.example;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * 
 * @author Dennis Geesen
 * Created at: 05.09.2011
 */
public class ISKlausurGeneratorA  extends StreamClientHandler{

	private long[] time = {16, 33, 38, 51, 70};
	private int[] attribute = {8,9,5,8,4};
	private int pointer = 0;
	
	@Override
	public void init() {		
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// time
		tuple.addLong(time[pointer]);
		// a
		tuple.addInteger(attribute[pointer]);
		
		try {if(pointer<=(time.length-1)){
			Thread.sleep(1000);
		}else{
			Thread.sleep(10000);
		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pointer++;
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}
	
	@Override
	public StreamClientHandler clone() {
		return new ISKlausurGeneratorA();
	}

}
