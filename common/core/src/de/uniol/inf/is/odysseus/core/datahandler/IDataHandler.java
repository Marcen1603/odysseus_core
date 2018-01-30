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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This is the base interface of all data handlers, a data handler is responsible for translation of different
 * representations of a SDFDatatype to and from internal Odysseus representations. 
 * 
 * It is important that each write-Method has a corresponding read method that inverts the processing (i.e. a call read(write(value)) must
 * return the value)
 * 
 * @author Marco Grawunder
 *
 * T is the Java-Class that is created from the data handler
 * @param <T>
 */

public interface IDataHandler<T> {

	/**
	 * Creates a new instance of the data handler
	 * if the data handler is complex (e.g. List, Tuple), the schema is use to
	 * describe the subtypes
	 * @param schema
	 * @return
	 */
	public IDataHandler<T> createInstance(SDFSchema schema);
	
	/**
	 * If set, it returns the set schema
	 * @return
	 */
	public SDFSchema getSchema();
	
	
	/**
	 * Creates a new instance with a set of data types (similar to schema)
	 * @param schema
	 * @return
	 */
	public IDataHandler<T> createInstance(List<SDFDatatype> schema);

	void setConversionOptions(ConversionOptions conversionOptions);
	
	ConversionOptions getConversionOptions();
	
	/**
	 * Read data from  Odysseus internal byte format
	 * @param buffer
	 * @return
	 */
	public T readData(ByteBuffer buffer);

	/**
	 * Write data in Odysseus internal byte format
	 * @param buffer
	 * @param data
	 */
	public void writeData(ByteBuffer buffer, Object data);

	/**
	 * Reads data from an input stream
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public T readData(InputStream inputStream) throws IOException;

	/**
	 * Reads data from a string representation, typically done with <T>.parse<T>() methods
	 * @param string
	 * @return
	 */
	public T readData(String string);
	
	/**
	 * Inverse function to write data to a string, typically done by toString method
	 * @param string
	 * @param data
	 */
	public void writeData(StringBuilder string, Object data);

	/**
	 * Read data from a list of input strings
	 * @param input
	 * @return
	 */
	public T readData(Iterator<String> input);
	
	/**
	 * Convert data to a list of strings (that must be readable be readData(List<String>)
	 * @param output
	 * @param data
	 */
	public void writeData(List<String> output, Object data, WriteOptions writeOptions);

	/**
	 * Directly read the element that should be created (and returns it directly without transforming)
	 * @param input
	 * @return
	 */
	public T readData(T input);

	
	public List<String> getSupportedDataTypes();

	int memSize(Object attribute);

	Class<?> createsType();
	
	public boolean isSemanticallyEqual(IDataHandler<?> other);
}
