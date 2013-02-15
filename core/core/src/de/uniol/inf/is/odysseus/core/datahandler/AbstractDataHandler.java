/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public abstract class AbstractDataHandler<T> implements IDataHandler<T> {

	private boolean prototype = true;
	private SDFSchema schema;
	
	protected AbstractDataHandler(){
	}
	
	@Override
	public IDataHandler<T> createInstance(List<String> schema) {
		return getInstance(schema);
	}
	
	protected IDataHandler<T> getInstance(List<String> schema){
		// Hint: Currently only needed for TupleDataHandler
		return null;

	}

	@Override
	public IDataHandler<T> createInstance(SDFSchema schema) {
		IDataHandler<T> i = getInstance(schema);
		i.setPrototype(false);
		i.setSchema(schema);
		return i;
	}
	
	abstract protected IDataHandler<T> getInstance(SDFSchema schema); 

		
	@Override
	public T readData(String[] input) {
		if (input.length != 1) throw new IllegalArgumentException("Input-size must be one!");
		return readData(input[0]);
	}
	
	@Override
	public T readData(List<String> input) {
		if (input.size() != 1) throw new IllegalArgumentException("Input-size must be one!");
		return readData(input.get(0));
	}
	
	@Override
	public void writeData(StringBuilder string, Object data) {
	    string.append(data);
	}
	
    @Override
    public void writeData(List<String> output, Object data) {
        output.add(data.toString());
    }
	
	@Override
	public boolean isPrototype() {
		return prototype;
	}
	
	@Override
	public void setPrototype(boolean p) {
		this.prototype = p;
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}
	
	@Override
	abstract public List<String> getSupportedDataTypes();
	
}
