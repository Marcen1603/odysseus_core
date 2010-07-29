package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class SchemaIndex {

	private int index;
	private SDFAttribute attribute;
	private boolean isList = false;
	
	public SchemaIndex( int index, SDFAttribute attribute ) {
		this.index = index;
		this.attribute = attribute;
		String dataType = attribute.getDatatype().getQualName();
		this.isList = dataType.equals("List") ? true : false;
	}
	
	public SchemaIndex( SchemaIndex index ) {
		this.index = index.index;
		this.attribute = index.attribute;
		this.isList = index.isList;
	}
	
	public int toInt() {
		return index;
	}
	
	public boolean isRoot() {
		return index < 0;
	}
	
	public SDFAttribute getAttribute() {
		return attribute;
	}
	
	public boolean isList() {
		return isList;
	}
	
	@Override
	public String toString() {
		return String.valueOf(index);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj == null ) return false;
		if( obj == this ) return true;
		if( !(obj instanceof SchemaIndex )) return false;
		
		SchemaIndex idx = (SchemaIndex)obj;
		
		return idx.isList == this.isList && idx.index == this.index && idx.attribute.equals(this.attribute);
	}
}
