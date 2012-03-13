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
package de.uniol.inf.is.odysseus.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A lightweight adaption of RelationalTuple from Odysseus with no direct dependencies!
 * 
 * @author DGeesen
 * 
 */
public class DataTuple{
	

	protected List<Object> attributes;	
	protected int memSize = -1;
	
	public DataTuple(){
		this.attributes = new ArrayList<Object>(); 
	}
	
	public DataTuple(int size){
		this.attributes = new ArrayList<Object>(size);
	}
	
	
	public DataTuple(DataTuple copy) {				
		this.attributes = new ArrayList<Object>(copy.getAttributes());		
	}	

	public Object getAttribute(int pos) {
		return this.attributes.get(pos);
	}
		
	public void setAttribute(int pos, Object value) {
		this.attributes.set(pos, value);
	}	
	
	public List<Object> getAttributes() {
		return attributes;
	}
	
	public void addAttribute(Object value){
		this.attributes.add(value);
	}
	
	public void addInteger(Integer value){
		this.attributes.add(value);
	}
	
	public void addDouble(Double value){
		this.attributes.add(value);
	}
	
	public void addLong(Long value){
		this.attributes.add(value);
	}
	
	public void addString(String value){
		this.attributes.add(value);
	}
	
	public void addInteger(double value){
		Integer val = new Integer((int)value);
		this.attributes.add(val);
	}
	
	public void addDouble(double value){
		this.attributes.add(new Double(value));
	}
	
	public void addLong(double value){
		Double d = value;
		Long val = new Long(d.longValue());
		this.attributes.add(val);
	}
	
	public void addString(Object value){
		String s = (String) value;
		this.attributes.add(s);
	}
	

	public int memSize(boolean calcNew) {
		if (memSize == -1 || calcNew) {
			memSize = calcSize();
		}
		return memSize;
	}

	private static int calcBaseTypeSize(Object attObject) {
		if (attObject == null)
			return 0;
		if (attObject instanceof Integer)
			return Integer.SIZE / 8;
		if (attObject instanceof Double)
			return Double.SIZE / 8;
		if (attObject instanceof Long)
			return Long.SIZE / 8;
		if (attObject instanceof String)
			return ((String) attObject).length() * 2 // Unicode!
					+ Integer.SIZE / 8; // Für die Längeninformation										
		if (attObject instanceof DataTuple)
			return ((DataTuple) attObject).memSize(true);

		throw new IllegalArgumentException("Illegal Relation Attribute Type " + attObject);

	}

	private int calcSize() {
		int size = 0;
		for (Object attObject : attributes) {
			size = size + calcSize(attObject);
		}
		return size;
	}
	
	private int calcSize(Object attObject) {
		int size = 0;
		if (attObject instanceof Collection) {
			for (Object e : ((Collection<?>) attObject)) {
				size += calcSize(e);
			}

		} else {
			size += calcBaseTypeSize(attObject);
		}
		return size;
	}

	@Override
	public final int hashCode() {
		int ret = 0;
		for (Object o : this.attributes) {
			ret += o.hashCode();
		}
		return ret;
	}
	
	@Override
	public String toString() {
		String out="";
		String sep="";
		for(Object o : this.attributes){
			out=out+" "+sep+" "+o;
			sep = "|";
		}
		return out;
	}	
}
