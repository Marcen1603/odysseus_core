package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;


public class SchemaIndexPath {

	private List<SchemaIndex> indices;
	private boolean hasListInside = false;
	private SDFAttribute to;
	
	SchemaIndexPath( List<SchemaIndex> indices, SDFAttribute attributeTo ) {
		this.indices = indices;
		for( SchemaIndex i : indices ) {
			if( i.isList() ) {
				hasListInside = true;
				break;
			}
		}
		this.to = attributeTo;
	}
	
	public int getLength() {
		return indices.size();
	}
	
	public boolean hasList() {
		return hasListInside;
	}
	
	public SDFAttribute getAttribute() {
		return to;
	}
	
	public String getFullAttributeName() {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < indices.size(); i++ ) {
			sb.append(indices.get(i).getAttribute().getAttributeName());
			if( i < indices.size() - 1 ) 
				sb.append(":");
		}
		return sb.toString();
	}
	
	public List<SchemaIndex> getSchemaIndices() {
		return Collections.unmodifiableList(indices);
	}
	
	public int[] toArray() {
		int[] array = new int[indices.size()];
		for( int i = 0; i < indices.size(); i++ ) 
			array[i] = indices.get(i).toInt();
		return array;
	}
	
	public TupleIndexPath toTupleIndexPath(MVRelationalTuple<?> tuple) {
		List<TupleIndex> list = new ArrayList<TupleIndex>();
		Object parent = tuple;
		
		if( indices.size() == 1 && indices.get(0).isRoot()) {
			list.add(new TupleIndex(tuple, -1, getAttribute()));
			return new TupleIndexPath(list, this);
		}
		
		for( int i = 1; i < indices.size(); i++ ) {
			TupleIndex idx = new TupleIndex( ((MVRelationalTuple<?>)parent), indices.get(i).toInt(), indices.get(i).getAttribute());
			list.add(idx);
			
			if( parent instanceof MVRelationalTuple )
				parent = ((MVRelationalTuple<?>)parent).getAttribute(indices.get(i).toInt());
		}
		return new TupleIndexPath(list, this);
	}
	
	public SchemaIndex getSchemaIndex( int index ) {
		return indices.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( int i = 0; i < indices.size(); i++ ) {
			sb.append(indices.get(i));
			if( i < indices.size() - 1 ) 
				sb.append(", ");
		}
		sb.append("}");
			
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null ) return false;
		if( obj == this ) return true;
		if( !(obj instanceof SchemaIndexPath )) return false;
		
		SchemaIndexPath idx = (SchemaIndexPath)obj;
		
		return idx.indices.equals(this.indices) && idx.hasListInside == this.hasListInside && idx.to.equals(this.to);
	}
}
