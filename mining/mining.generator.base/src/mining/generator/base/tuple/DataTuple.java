package mining.generator.base.tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A lightweight adaption of RelationalTuple
 * @author DGeesen
 *
 * @param <T> 
 */
public class DataTuple{
	

	protected List<Object> attributes;	
	protected int memSize = -1;
	
	public DataTuple(){
		this.attributes = new ArrayList<Object>(); 
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
	

	public int memSize(boolean calcNew) {
		if (memSize == -1 || calcNew) {
			memSize = calcSize();
		}
		return memSize;
	}

	private int calcBaseTypeSize(Object attObject) {
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
