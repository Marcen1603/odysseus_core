/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;

/**
 * ConnectionList is a special ArrayList which contains rated connections. It brings some functions
 * along to get all left objects for an right object or all right objects for an left object.
 *
 * @author Volker Janz
 *
 */
public class ConnectionList extends ArrayList<IConnection> {

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

	public Set<TupleIndexPath> getAllElements() {
		Set<TupleIndexPath> set = new HashSet<TupleIndexPath>();
		for( IConnection c : this ) {
			set.add(c.getLeftPath());
			set.add(c.getRightPath());
		}
		return set;
	}

	/**
	 * Returns the rating for a specific pair
	 * @param leftPath leftPath
	 * @param rightPath rightPath
	 * @return The rating of the specific pair - returns 0 if it�s not found in connection list. So 0 is the default value for a new connection.
	 */
	public double getRatingForElementPair(TupleIndexPath leftPath, TupleIndexPath rightPath) {
		for(IConnection con : this) {
			if( con.getLeftPath().equals(leftPath) && con.getRightPath().equals(rightPath)) {
				return con.getRating();
			}
		}
		return -1;
	}
}
