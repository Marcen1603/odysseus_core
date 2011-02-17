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
package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.List;

public class StreamCollector {

	private List< List<Object>> recievedObjects = new ArrayList<List<Object>>();
	
	public StreamCollector( int numOfPorts ) {
		for( int i = 0; i < numOfPorts; i++ )
			recievedObjects.add(new ArrayList<Object>());
	}
	
	public void recieve( Object punctuationOrTuple, int port ) {
		recievedObjects.get(port).add(punctuationOrTuple);
	}
	
	public boolean isReady() {
		for( int i = 0; i < getPortCount(); i++){
			if( recievedObjects.get(i).isEmpty())
				return false;
		}
		return true;
	}
	
	public List<Object> getNext() {
		List<Object> list = new ArrayList<Object>();
		if( !isReady() ) 
			return list; // leere Liste
		
		for( int i = 0; i < getPortCount(); i++ ) {
			list.add(recievedObjects.get(i).get(0));
			recievedObjects.get(i).remove(0);
		}
		
		return list;
	}
	
	public int getPortCount() {
		return recievedObjects.size();
	}
}
