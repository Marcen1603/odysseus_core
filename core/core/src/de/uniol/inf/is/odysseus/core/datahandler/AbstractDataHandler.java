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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.util.List;


public abstract class AbstractDataHandler<T> implements IDataHandler<T> {

	boolean prototype = false;
	
	protected AbstractDataHandler(){
	}
	
	@Override
	public IDataHandler<T> getInstance(List<String> schema) {
		// Hint: Currently only needed for TupleDataHandler and ListDataHandler
		return null;
	}

		
	@Override
	public T readData(String[] input) {
		if (input.length != 1) throw new IllegalArgumentException("Input-size must be one!");
		return readData(input[0]);
	}
	
	@Override
	public void setPrototype(boolean b) {
		this.prototype = b;
	}
	
	@Override
	public boolean isPrototype() {
		return prototype;
	}
	
	@Override
	abstract public List<String> getSupportedDataTypes();
}
