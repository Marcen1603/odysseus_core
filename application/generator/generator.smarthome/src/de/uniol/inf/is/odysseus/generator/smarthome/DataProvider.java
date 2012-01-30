/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.generator.smarthome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class DataProvider extends StreamClientHandler {
	
	private boolean hasNew = false;
	private String streamName;
	private DataReader reader;
	private ArrayList<DataTuple> buffer = new ArrayList<DataTuple>();

	public DataProvider(String streamName, DataReader reader) {
		this.streamName = streamName;
		this.reader = reader;
	}	

	public String getStreamName(){
		return this.streamName;
	}

	@Override
	public synchronized List<DataTuple> next() {
		List<DataTuple> tuples = new ArrayList<DataTuple>(this.buffer.size());
		if(hasNew){					
			tuples = new ArrayList<>(buffer);			
			this.buffer.clear();
			hasNew = false;
		}
		return tuples;
	}

	@Override
	public void init() {	
		this.reader.addListener(this);
	}

	@Override
	public void close() {		
		this.reader.removeListener(this);
	}	
	
	@Override
	public DataProvider clone() {
		return new DataProvider(streamName, reader);
	}

	public synchronized void addTuple(DataTuple tuple) {
		this.buffer.add(tuple);
		this.hasNew = true;
		
	}
	
}
