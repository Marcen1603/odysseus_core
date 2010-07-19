package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;

/**
 * ConnectionList is a special ArrayList which contains rated connections. It brings some functions
 * along to get all left objects for an right object or all right objects for an left object.
 *
 * @author Volker Janz
 *
 * @param <L> Datatype of the left object
 * @param <R> Datatype of the right object
 * @param <W> Datatype of the rating - has to extend java.lang.Number (Double, Integer, ...)
 */
public class ConnectionList<L, R, W extends java.lang.Number> extends ArrayList<Connection<L, R, W>> {

	private static final long serialVersionUID = 1L;

	/**
	 * Searches all right elements for a left element.
	 *
	 * @param leftelement The left element for which right elements should be searched.
	 * @return ArrayList of right elements - could contain 0..*
	 */
	public ArrayList<R> getRightElementsForLeftElement(L leftelement) {
		ArrayList<R> tmplist = new ArrayList<R>();
		for(int i = 0; i < this.size(); i++) {
			if(leftelement == this.get(i).getLeft()) {
				tmplist.add(this.get(i).getRight());
			}
		}
		return tmplist;
	}

	/**
	 * Searches all left elements for a right element.
	 *
	 * @param rightelement The right element for which left elements should be searched.
	 * @return ArrayList of left elements - could contain 0..*
	 */
	public ArrayList<L> getLeftElementsForRightElement(R rightelement) {
		ArrayList<L> tmplist = new ArrayList<L>();
		for(int i = 0; i < this.size(); i++) {
			if(rightelement == this.get(i).getRight()) {
				tmplist.add(this.get(i).getLeft());
			}
		}
		return tmplist;
	}

}
