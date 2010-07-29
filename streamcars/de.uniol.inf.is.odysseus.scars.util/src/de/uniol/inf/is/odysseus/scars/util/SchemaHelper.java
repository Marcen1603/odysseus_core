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
	private String sourceName = null;
	
	public SchemaHelper( SDFAttributeList schema ) {
		if( schema == null ) {
			throw new IllegalArgumentException("schema is null");
		}
		
		this.schema = schema;
		
		// root
		SchemaIndex rootIndex = new SchemaIndex(-1, schema.get(0));
		List<SchemaIndex> list = new ArrayList<SchemaIndex>();
		list.add(rootIndex);
		paths.put(schema.get(0).getAttributeName(), new SchemaIndexPath(list, schema.get(0)));
		
		calculateAllPaths( schema.get(0).getSubattributes(), new ArrayList<SchemaIndex>(), null);
	}
	
	public SDFAttributeList getSchema() {
		return schema;
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
			
			actualPath.add( new SchemaIndex(index, attribute));
			paths.put(fullAttributeName, new SchemaIndexPath(copy(actualPath), attribute));
			
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

}
