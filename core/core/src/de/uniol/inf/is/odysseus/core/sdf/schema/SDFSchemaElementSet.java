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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.core.sdf.SDFElement;

public class SDFSchemaElementSet<T> extends SDFElement implements Iterable<T>{ 

	private static final long serialVersionUID = 3835214163915421257L;
    protected List<T> elements = new ArrayList<T>();

	protected SDFSchemaElementSet(String URI) {
		super(URI);
	}

	public SDFSchemaElementSet() {
		super("TemporaryElement" + System.currentTimeMillis());
	}

	/**
     * @param attributes1
     */    
	public SDFSchemaElementSet(String uri, SDFSchemaElementSet<T> newElements) {
        super(uri);
        elements.addAll(newElements.elements);
    }
	
	public SDFSchemaElementSet(String uri, Collection<T> attributes1) {
		super(uri);
        elements.addAll(attributes1);
	}

	@Override
	public String toString() {
		return getURI()+" "+elements;
	}
	
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		for (T s: elements){
			xmlRetValue.append(indent);
			xmlRetValue.append("<SDFSchemaElement>");
			if (s != null){
				xmlRetValue.append(s.toString());
			}
			xmlRetValue.append("</SDFSchemaElement>\n");
		}
	}

	public boolean contains(Object elem) {
		return elements.contains(elem);
	}

	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}


	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SDFSchemaElementSet)){
			return false;
		}
		@SuppressWarnings("unchecked")
		SDFSchemaElementSet<T> otherSet = (SDFSchemaElementSet<T>) o;
		if (otherSet.size() != elements.size()){
			return false;
		}
		Iterator<T> other = otherSet.iterator();
		for (T e:elements){
			if (!e.equals(other.next())){
				return false;
			}
		}
		return true;
	}

	public T get(int index) {
		return elements.get(index);
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (T e:elements){
			hashCode+=e.hashCode();
		}
		return hashCode;
	}

	public int indexOf(Object elem) {
		return elements.indexOf(elem);
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public int lastIndexOf(Object elem) {
		return elements.lastIndexOf(elem);
	}

	public ListIterator<T> listIterator() {
		return elements.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return elements.listIterator(index);
	}

	public int size() {
		return elements.size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return elements.toArray();
	}

	public T[] toArray(T[] a) {
		return elements.toArray(a);
	}

	@Override
    public Iterator<T> iterator() {
		return elements.iterator();
	}
	
	@Override
    public SDFSchemaElementSet<T> clone() {
		return new SDFSchemaElementSet<T>(this.getURI(), this);
	}
	
}