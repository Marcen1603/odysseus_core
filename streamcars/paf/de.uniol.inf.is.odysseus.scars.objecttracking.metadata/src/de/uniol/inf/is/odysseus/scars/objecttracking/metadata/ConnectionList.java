package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;

/**
 * ConnectionList is a special ArrayList which contains rated connections. It brings some functions
 * along to get all left objects for an right object or all right objects for an left object.
 *
 * @author Volker Janz
 *
 */
public class ConnectionList extends ArrayList<Connection> {

	private static final long serialVersionUID = 1L;

	/**
	 * Searches all right elements for a left element.
	 *
	 * @param leftelement The left element for which right elements should be searched.
	 * @return ArrayList of right elements - could contain 0..*
	 */
	public ArrayList<Object> getRightElementsForLeftElement(Object leftelement) {
		ArrayList<Object> tmplist = new ArrayList<Object>();
		for(int i = 0; i < this.size(); i++) {
			if(leftelement == this.get(i).getLeftPath()) {
				tmplist.add(this.get(i).getRightPath());
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
	public ArrayList<Object> getLeftElementsForRightElement(Object rightelement) {
		ArrayList<Object> tmplist = new ArrayList<Object>();
		for(int i = 0; i < this.size(); i++) {
			if(rightelement == this.get(i).getRightPath()) {
				tmplist.add(this.get(i).getLeftPath());
			}
		}
		return tmplist;
	}

	public ArrayList<int[]> getAllElements() {
		ArrayList<int[]> tmpList = new ArrayList<int[]>();
		int[] leftElement;
		int[] rightElement;
		for (int i = 0; i < this.size(); i++) {
			leftElement = this.get(i).getLeftPath();
			rightElement = this.get(i).getRightPath();
			if(!tmpList.contains(leftElement)) {
				tmpList.add(leftElement);
			}
			if(!tmpList.contains(rightElement)) {
				tmpList.add(rightElement);
			}
		}
		return tmpList;
	}

	/**
	 * Returns the rating for a specific pair
	 * @param leftPath leftPath
	 * @param rightPath rightPath
	 * @return The rating of the specific pair - returns 0 if it´s not found in connection list. So 0 is the default value for a new connection.
	 */
	public double getRatingForElementPair(int[] leftPath, int[] rightPath) {
		for(Connection con : this) {
			if(con.getLeftPath().equals(leftPath) && con.getRightPath().equals(rightPath)) {
				return con.getRating();
			}
		}
		return -1;
	}

}
