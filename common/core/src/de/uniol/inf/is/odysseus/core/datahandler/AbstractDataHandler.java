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
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ICSVToString;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public abstract class AbstractDataHandler<T> implements IDataHandler<T> {

	final private SDFSchema schema;
	protected IMetaAttribute metaAttribute;
	boolean handleMetaData;
	private List<NullAwareTupleDataHandler> metaDataHandler;
	
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

		
	@Override
	public T readData(String[] input) {
		if (input.length != 1) throw new IllegalArgumentException("Input-size must be one!");
		return readData(input[0]);
	}
	
	@Override
	public T readData(InputStream inputStream) throws IOException {
		throw new UnsupportedOperationException("Sorry. Reading from input stream is currently not supported by this data handler");
	}
	
	@Override
	public T readData(List<String> input) {
		if (input.size() != 1) throw new IllegalArgumentException("Input-size must be one!");
		return readData(input.get(0));
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
    public void writeData(List<String> output, Object data) {
        output.add(data.toString());
    }
    
    @Override
    public void writeCSVData(StringBuilder string, Object data,
    		WriteOptions options) {
    	if (data instanceof ICSVToString){
    		string.append(((ICSVToString)data).csvToString(options));
    	}else{
    		// TODO: Find another solution ...
    	}    	
    }
    
    @Override
    @Deprecated
    public void writeCSVData(StringBuilder string, Object data,
    		char delimiter, Character textSeperator, DecimalFormat floatingFormatter,
    		DecimalFormat numberFormat, boolean writeMetadata) {
    	if (data instanceof ICSVToString){
    		string.append(((ICSVToString)data).csvToString(new WriteOptions(delimiter,textSeperator,floatingFormatter,numberFormat,writeMetadata)));
    	}else{
    		// TODO: Find another solution ...
    	}
    }
	
	@Override
	public void writeJSONData(StringBuilder string, Object data) {
		throw new IllegalArgumentException("Writing JSON data is not implemented in this DataHandler!");
	}
	
	@Override
	public byte[] writeBSONData(Object data) {
		throw new IllegalArgumentException("Writing BSON data is not implemented in this DataHandler!");
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	@Override
	abstract public List<String> getSupportedDataTypes();
	
	
	// ------------------------------------------------------------------------------------------
	// Meta data handling
	// ------------------------------------------------------------------------------------------
	
	@Override
	public void setHandleMetaData(boolean handleMetaData) {
		this.handleMetaData = handleMetaData;
	}
	
	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		this.metaAttribute = metaAttribute;
		for (SDFSchema schema: metaAttribute.getSchema()){
			this.metaDataHandler.add((NullAwareTupleDataHandler) new NullAwareTupleDataHandler().getInstance(schema));
		}
	};

	protected final IMetaAttribute readMetaData(InputStream inputStream) throws IOException{
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			res.add(dh.readData(inputStream));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected final IMetaAttribute readMetaData(String[] input){
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}
	
	protected final IMetaAttribute readMetaData(List<String> input){
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected final IMetaAttribute readMetaData(ByteBuffer input){
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected final void writeMetaData(List<String> output, IMetaAttribute metaAttribute){
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			dh.writeData(output,v.get(i++));
		}
	}	
	
	protected final void writeMetaData(StringBuilder output, IMetaAttribute metaAttribute){
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			dh.writeData(output,v.get(i++));
		}
	}
	
	
	protected final void writeMetaData(ByteBuffer output, IMetaAttribute metaAttribute){
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			dh.writeData(output,v.get(i++));
		}
	}
	
	protected final long getMetaDataMemSize(IMetaAttribute metaAttribute){
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		long size = 0;
		for (NullAwareTupleDataHandler dh: metaDataHandler){
			size+=dh.memSize(v.get(i++));
		}
		return size;
	}
	
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
