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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public abstract class AbstractDataHandler<T> implements IDataHandler<T> {

	final private SDFSchema schema;
	
	// Default char set
	private Charset charset = Charset.forName("UTF-8");
	private CharsetDecoder decoder = charset.newDecoder();
	private CharsetEncoder encoder = charset.newEncoder();

	
	protected AbstractDataHandler(SDFSchema schema){
		this.schema = schema;
	}
	
	protected AbstractDataHandler(){
		this.schema = null;
	}
	
	@Override
	final public IDataHandler<T> createInstance(List<SDFDatatype> schema) {
		return getInstance(schema);
	}
	
	@Override
	final public IDataHandler<T> createInstance(SDFSchema schema) {
		IDataHandler<T> i = getInstance(schema);
		return i;
	}
	
	abstract protected IDataHandler<T> getInstance(SDFSchema schema); 

	protected IDataHandler<T> getInstance(List<SDFDatatype> schema){
		// Hint: Currently only needed for TupleDataHandler
		return null;
	}

	public void setCharset(String charsetString) {
		setCharset(Charset.forName(charsetString));
	}
	
	public void setCharset(Charset charset) {
		this.charset = charset;
		decoder = charset.newDecoder();
		encoder = charset.newEncoder();	
	}
	
	public Charset getCharset() {
		return charset;
	}
	
	public CharsetDecoder getDecoder() {
		return decoder;
	}
	
	public CharsetEncoder getEncoder() {
		return encoder;
	}
	
	@Override
	public T readData(InputStream inputStream) throws IOException {
		throw new UnsupportedOperationException("Sorry. Reading from input stream is currently not supported by this data handler");
	}
	
	@Override
	public T readData(Iterator<String> input) {
		throw new UnsupportedOperationException("Sorry. Reading from string input is currently not supported by this data handler");
	}
	
	@Override
	final public T readData(T input) {
		return input;
	}
	
	@Override
	public void writeData(StringBuilder string, Object data) {
	    string.append(data);
	}
		
    @Override
    public void writeData(List<String> output, Object data, WriteOptions options) {
        output.add(data.toString());
    }
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	@Override
	abstract public List<String> getSupportedDataTypes();
	
	// ------------------------------------------------------------------------------------------

	@Override
	public boolean isSemanticallyEqual(IDataHandler<?> other) {
		
		if(this.schema != other.getSchema()) {
			return false;
		} else if(!this.getSupportedDataTypes().containsAll(other.getSupportedDataTypes())) {
			return false;
		}
		// TODO: implement in child classes
		return false;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
