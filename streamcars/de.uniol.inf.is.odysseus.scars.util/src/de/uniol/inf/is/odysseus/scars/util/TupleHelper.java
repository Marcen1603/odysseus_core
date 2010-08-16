package de.uniol.inf.is.odysseus.scars.util;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

public class TupleHelper {

	private MVRelationalTuple<?> tuple;
	
	public TupleHelper(MVRelationalTuple<?> tuple) {
		this.tuple = tuple;
	}
	
	public Object getObject( SchemaIndexPath path ) {
		return path.toTupleIndexPath(tuple).getTupleObject();
	}
	
	public Object getObject( List<Integer> path, boolean ignoreFirstIndex ) {
		int[] i = new int[path.size()];
		for( int p = 0; p < path.size(); p++ )
			i[p] = path.get(p);
		return getObject(i, ignoreFirstIndex);
	}
	
	public Object getObject(int[] path, boolean ignoreFirstIndex ) {
		Object actualAttribute = null;
		Object[] actualList = tuple.getAttributes();
		for( int i = ignoreFirstIndex ? 1 : 0; i < path.length; i++ ) {
			actualAttribute = actualList[path[i]];
			if( actualAttribute instanceof MVRelationalTuple<?>) {
				actualList = ((MVRelationalTuple<?>)actualAttribute).getAttributes();
			}
		}

		return actualAttribute;
	}

	public MVRelationalTuple<?> getTuple() {
		return tuple;
	}
	
	
}
