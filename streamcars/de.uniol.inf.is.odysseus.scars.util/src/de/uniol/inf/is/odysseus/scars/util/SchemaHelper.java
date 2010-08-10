package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SchemaHelper {

	public static final String SOURCE_SEPARATOR = ".";
	public static final String ATTRIBUTE_SEPARATOR = ":";

	private static final String SOURCE_SPLIT_REGEX = "\\" + SOURCE_SEPARATOR;
	
	private SDFAttributeList schema;
	
	private Map<String, SchemaIndexPath> paths = new HashMap<String, SchemaIndexPath>();
	private Map<SDFAttribute, SchemaIndexPath> attributePaths = new HashMap<SDFAttribute, SchemaIndexPath>();
	
	private String sourceName = null;
	private SDFAttribute startTimestampAttribute = null;
	private SDFAttribute endTimestampAttribute = null;
	
	public SchemaHelper( SDFAttributeList schema ) {
		if( schema == null ) {
			throw new IllegalArgumentException("schema is null");
		}
		
		this.schema = schema;
		calculateAllPaths( schema, new ArrayList<SchemaIndex>(), null);
	}
	
	public SDFAttributeList getSchema() {
		return schema;
	}
	
	SchemaHelper( SchemaHelper other ) {
		this.schema = other.schema.clone();
		calculateAllPaths(schema, new ArrayList<SchemaIndex>(), null );
	}
	
	public SchemaIndexPath getSchemaIndexPath( String fullAttributeName ) {
		if( fullAttributeName.contains(SOURCE_SEPARATOR)) {
			String[] parts = fullAttributeName.split(SOURCE_SPLIT_REGEX);
			return paths.get(parts[1]);
		}
		SchemaIndexPath p = paths.get(fullAttributeName);
		if( p == null ) 
			throw new IllegalArgumentException("cannot find schemaindexpath for " + fullAttributeName);
		return paths.get(fullAttributeName);
	}
	
	public SchemaIndexPath getSchemaIndexPath( SDFAttribute attribute ) {
		return attributePaths.get(attribute);
	}
	
	public SDFAttribute getAttribute( String fullAttributeName ) {
		SchemaIndexPath p = getSchemaIndexPath(fullAttributeName);
		return p != null ? p.getAttribute() : null;
	}
	
	public Collection<String> getAttributeNames() {
		return Collections.unmodifiableCollection(paths.keySet());
	}
	
	public Collection<SchemaIndexPath> getSchemaIndexPaths() {
		return Collections.unmodifiableCollection(paths.values());
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public SDFAttribute getStartTimestampAttribute() {
		return startTimestampAttribute;
	}
	
	public SDFAttribute getEndTimestampAttribute() {
		return endTimestampAttribute;
	}
	
	private void calculateAllPaths(SDFAttributeList list, List<SchemaIndex> actualPath, String actualAttributeName) {
		for( int index = 0; index < list.getAttributeCount(); index++ ) {
			SDFAttribute attribute = list.getAttribute(index);
			if( sourceName == null ) 
				sourceName = attribute.getSourceName();
			String fullAttributeName = ""; 
			if( actualAttributeName != null ) 
				fullAttributeName = actualAttributeName + ATTRIBUTE_SEPARATOR + attribute.getAttributeName();
			else
				fullAttributeName = attribute.getAttributeName();
			
			// timestamp
			if( attribute.getDatatype().getURIWithoutQualName().equals("StartTimestamp")) {
				startTimestampAttribute = attribute;
			} else if( attribute.getDatatype().getURIWithoutQualName().equals("EndTimestamp")) {
				endTimestampAttribute = attribute;
			}
			
			actualPath.add( new SchemaIndex(index, attribute));
			paths.put(fullAttributeName, new SchemaIndexPath(copy(actualPath), attribute));
			attributePaths.put(attribute, new SchemaIndexPath(copy(actualPath), attribute));
			
			calculateAllPaths( attribute.getSubattributes(), actualPath, fullAttributeName);
			
			actualPath.remove(actualPath.size() - 1);
		}
	}
	
	private List<SchemaIndex> copy( List<SchemaIndex> base ) {
		List<SchemaIndex> newList = new ArrayList<SchemaIndex>();
		for( int i = 0; i < base.size(); i++ ) 
			newList.add( new SchemaIndex(base.get(i)));
		return newList;
	}
	
	public SchemaHelper clone() {
		return new SchemaHelper(this);
	}

}
