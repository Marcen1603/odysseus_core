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
package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;

public abstract class AbstractAtomicDataHandler implements IAtomicDataHandler {

	private ObjectInputStream stream;
	static protected List<String> types = new ArrayList<String>();		
	
	protected AbstractAtomicDataHandler(){
	}
	
	@Override
	public void setStream(ObjectInputStream stream) {
		this.stream = stream;
	}
	
	final public ObjectInputStream getStream() {
		return stream;
	}
	
	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}
}
