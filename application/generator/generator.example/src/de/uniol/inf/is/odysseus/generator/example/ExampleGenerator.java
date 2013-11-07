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

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 05.09.2011
 */
public class ExampleGenerator  extends AbstractDataGenerator{

	private long time = 0;
	private int intVal = 0;
	private String stringVal = "an example string";
	private boolean booleanVal = true;
	
	
	@Override
	public void process_init() {		
		
	}

	@Override
	public void close() {
		
	}

	// CREATE STREAM example (ts STARTTIMESTAMP, i INTEGER, s STRING, b BOOLEAN, bt BYTE, d DOUBLE, sh SHORT) CHANNEL localhost : 54321
	
	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		// time
		tuple.addLong(time++);
		// a
		tuple.addInteger(intVal);
		tuple.addString(stringVal);
		tuple.addBoolean(booleanVal);
		tuple.addByte(new Byte("1"));
		tuple.addDouble(123.456);
		tuple.addShort(new Short("1"));
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
	public ExampleGenerator newCleanInstance() {
		return new ExampleGenerator();
	}

}
