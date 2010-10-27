package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SDFSchemaElementSet<T extends SDFSchemaElement> extends SDFSchemaElement implements List<T>{ 

    /**
	 * 
	 */
	private static final long serialVersionUID = 3835214163915421257L;
	/**
	 * @uml.property  name="elements"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchemaElement"
	 */
    protected List<T> elements = new ArrayList<T>();

	public SDFSchemaElementSet(String URI) {
		super(URI);
	}

	public SDFSchemaElementSet() {
		super("TemporaryElement" + System.currentTimeMillis());
	}

	/**
     * @param attributes1
     */
    @SuppressWarnings("unchecked")
	public SDFSchemaElementSet(SDFSchemaElementSet newElements) {
        super("TemporaryElement" + System.currentTimeMillis());
        elements.addAll(newElements);
    }
	
	public boolean contains(SDFSchemaElement element){
		return this.elements.contains(element);
	}
	
	public int indexOf(SDFSchemaElement element){
		return elements.indexOf(element);
	}

	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("[");
		for (int i = 0; i < elements.size(); i++) {
			if (get(i)!=null){
				ret.append(get(i) + " ");
			}else{
				ret.append("<null> ");
			}
		}
		return ret.toString() + "]";
	}
	
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		for (SDFSchemaElement s: elements){
			xmlRetValue.append(indent);
			xmlRetValue.append("<SDFSchemaElement>");
			if (s != null){
				xmlRetValue.append(s.toString());
			}
			xmlRetValue.append("</SDFSchemaElement>\n");
		}
	}

	@Override
	public void add(int index, T element) {
		elements.add(index, element);
	}

	@Override
	public boolean add(T o) {
		return elements.add(o);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return elements.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return elements.addAll(index, c);
	}

	@Override
	public void clear() {
		elements.clear();
	}


	@Override
	public boolean contains(Object elem) {
		return elements.contains(elem);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}


	@Override
	public boolean equals(Object o) {
		return elements.equals(o);
	}

	@Override
	public T get(int index) {
		return elements.get(index);
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	@Override
	public int indexOf(Object elem) {
		return elements.indexOf(elem);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public int lastIndexOf(Object elem) {
		return elements.lastIndexOf(elem);
	}

	@Override
	public ListIterator<T> listIterator() {
		return elements.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public T remove(int index) {
		return elements.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		return elements.set(index, element);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}

	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}
	
}