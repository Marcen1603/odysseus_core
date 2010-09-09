package de.uniol.inf.is.odysseus.scars.util;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

/**
 * Temporäre Klasse. Unterstützt die Auswertung von Tupel
 * über SchemaIndexPaths.
 * 
 * @author Timo Michelsen
 *
 */
public class TupleHelper {

	private MVRelationalTuple<?> tuple;
	
	/**
	 * Erstellt eine neue TupleHelper-Instanz mit dem angegebenen Tupel.
	 * 
	 * @param tuple Tupel
	 */
	public TupleHelper(MVRelationalTuple<?> tuple) {
		this.tuple = tuple;
	}
	
	/**
	 * Liefert das Objekt im Tupel mit dem angegebenen <code>SchemaIndexPath</code>
	 * 
	 * @param path Pfad zu einem Tupel als Schemaindexpfad
	 * @return Das Objekt, welches sich an der angegebenen Stelle im Tupel befindet
	 */
	public Object getObject( SchemaIndexPath path ) {
		return path.toTupleIndexPath(tuple).getTupleObject();
	}
	
	/**
	 * Liefert das Objekt im Tupel mit dem angegebenen Integer-Pfad.
	 * 
	 * @param path Pfad zu einem Tupel als Schemaindexpfad
	 * @return Das Objekt, welches sich an der angegebenen Stelle im Tupel befindet
	 */
	public Object getObject( List<Integer> path ) {
		int[] i = new int[path.size()];
		for( int p = 0; p < path.size(); p++ )
			i[p] = path.get(p);
		return getObject(i);
	}
	
	/**
	 * Liefert das Objekt im Tupel mit dem angegebenen int-Pfad.
	 * 
	 * @param path Pfad zu einem Tupel als Schemaindexpfad
	 * @return Das Objekt, welches sich an der angegebenen Stelle im Tupel befindet
	 */
	public Object getObject(int[] path ) {
		Object actualAttribute = null;
		Object[] actualList = tuple.getAttributes();
		for( int i = 0; i < path.length; i++ ) {
			actualAttribute = actualList[path[i]];
			if( actualAttribute instanceof MVRelationalTuple<?>) {
				actualList = ((MVRelationalTuple<?>)actualAttribute).getAttributes();
			}
		}

		return actualAttribute;
	}

	/**
	 * Liefert das aktuell verwendete Tupel dieser TupleHelper-Instanz
	 * 
	 * @return Das verwendete Tuple
	 */
	public MVRelationalTuple<?> getTuple() {
		return tuple;
	}
	
	
}
