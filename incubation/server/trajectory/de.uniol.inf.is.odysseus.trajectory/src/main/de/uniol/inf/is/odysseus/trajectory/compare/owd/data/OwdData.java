/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * An object which contains the necessary data structures for the <i>owd distance</i>.
 * Among others it contains inner classes for <tt>GridCells</tt> and <tt>GridCellList</tt>.
 * 
 * @author marcus
 *
 */
public class OwdData {
	
	/** the <tt>GridCellList</tt> */
	private final GridCellList gridCells;
	
	/**
	 * Creates an instance of <tt>OwdData</tt>.
	 * 
	 * @param gridCells the <tt>GridCellList<tt> of this <tt>OwdData</tt>
	 * @throws IllegalArgumentException if <tt>gridCells == null</tt>
	 */
	public OwdData(final GridCellList gridCells) {
		if(gridCells == null) {
			throw new IllegalArgumentException("gridCells is null");
		}
		this.gridCells = gridCells;
	}

	/**
	 * Returns the <tt>GridCellList<tt>.
	 * 
	 * @return the <tt>GridCellList<tt>.
	 */
	public GridCellList getGridCells() {
		return this.gridCells;
	}
	

	/**
	 * A <tt>GridCellList</tt> is a <tt>Iterable</tt> of <tt>GridCells</tt>.
	 * One can add a <tt>GridCell</tt> by passing their <tt>x</tt> and <tt>y</tt>
	 * value to the <tt>GridCellList</tt>. Removing is not allowed here.
	 * 
	 * @author marcus
	 *
	 */
	public static class GridCellList implements Iterable<GridCell> {
		
		/** the backing <tt>List</tt> */
		private final LinkedList<GridCell> internal = new LinkedList<>();
		
		/** the last inserted element */
		private GridCell tail;
		
		/**
		 * When invoking this method a new <tt>GridCell</tt> is internally
		 * created from the <tt>x</tt> and <tt>y</tt> value and added to this
		 * list. the previously added <tt>GridCell's</tt> <i>next</i> reference
		 * will be set to the newly created one and <i>previous</i> reference of
		 * the new created <tt>GridCell</tt> will be set to the previously added 
		 * one. 
		 * 
		 * <p>if the <i>x</i> and <i>y</i> value of the new <tt>GridCell</tt> is
		 * equal to the respective values of the previously added, the list will not
		 * change and <tt>false</tt> will ne returned. If the new <tt>GridCell</tt>
		 * is not adjacent to the previously added an <tt>IllegalArgumentException</tt>
		 * will be thrown.</p>
		 * 
		 * @param x the <i>x</i> value of the <tt>GridCell</tt> to be added
		 * @param y the <i>y</i> value of the <tt>GridCell</tt> to be added
		 * @return <tt>true</tt> if the new <tt>GridCell</tt> could be added, otherwise
		 *         <tt>false</tt>S
		 *         
		 * @throws IllegalArgumentException if the new <tt>GridCell</tt> is not 
		 *         adjacent to the previously added <tt>GridCell</tt>
		 */
		public boolean addGridCell(final int x, final int y) {
			final GridCell gridCell = new GridCell(x, y);
			if(this.tail != null) {
				final int distance = Math.abs(this.tail.getX() - gridCell.getX()) 
						+ Math.abs(this.tail.getY() - gridCell.getY());
				
				if(distance > 1) {
					throw new IllegalArgumentException("Cell not adjacent to last inserted cell");
				}
				
				if(distance == 0) {
					return false;
				}
						
				this.tail.next = gridCell;
				gridCell.previous = this.tail;
				this.tail = gridCell;
			} 
			this.tail = gridCell;
			return this.internal.add(gridCell);
		}
		
		/**
		 * Returns the first <tt>GridCell</tt> in this list.
		 * @return the first <tt>GridCell</tt> in this list
		 */
		public GridCell getFirst() {
			return this.internal.getFirst();
		}
		
		/**
		 * Returns the last <tt>GridCell</tt> in this list.
		 * @return the last <tt>GridCell</tt> in this list
		 */
		public GridCell getLast() {
			return this.internal.getLast();
		}
		
		/**
		 * Returns the number of <tt>GridCell</tt> in this list.
		 * @return the number of <tt>GridCell</tt> in this list
		 */
		public int size() {
			return this.internal.size();
		}

		@Override
		public Iterator<GridCell> iterator() {
			return this.internal.iterator();
		}

		@Override
		public String toString() {
			return this.internal.toString();
		}
	}

	/**
	 * An object representing a <i>cell</i> in an <i>owd grid</i>
	 * which is part of a trajectory.
	 * 
	 * <p>It basically has an <tt>x</tt> and an <tt>y</tt> value whose 
	 * indicate the cell's position in the grid and a
	 * reference to its <i>predecessor</i> and <i>successor</i></p>
	 * 
	 * @author marcus
	 *
	 */
	public static class GridCell implements Iterable<GridCell> {
		
		/** the <tt>x</tt> coordinate of this cell in the grid */
		private final int x;
		
		/** the <tt>y</tt> coordinate of this cell in the grid */
		private final int y;
		
		/** the predecessor of this cell */
		private GridCell previous;
		
		/** the successor of this cell */
		private GridCell next;
		
		/**
		 * Creates an <tt>GridCell</tt> from the passed <tt>x</tt> and 
		 * <tt>y</tt> value.
		 * 
		 * @param x the <tt>x</tt> coordinate of this cell in the grid
		 * @param y the <tt>y</tt> coordinate of this cell in the grid
		 */
		private GridCell(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Returns the <tt>x</tt> coordinate of this cell in the grid.
		 * 
		 * @return the <tt>x</tt> coordinate of this cell in the grid
		 */
		public int getX() {
			return this.x;
		}

		/**
		 * Returns the <tt>y</tt> coordinate of this cell in the grid.
		 * 
		 * @return the <tt>y</tt> coordinate of this cell in the grid
		 */
		public int getY() {
			return this.y;
		}

		/**
		 * Returns the predecessor of this cell.
		 * 
		 * @return the predecessor of this cell
		 */
		public GridCell getPrevious() {
			return this.previous;
		}

		/**
		 * Returns the successor of this cell.
		 * 
		 * @return the successor of this cell
		 */
		public GridCell getNext() {
			return this.next;
		}
		
		@Override
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}
		
		/**
		 * Checks whether the passed <tt>GridCell</tt> is adjacent to this <tt>GridCell</tt>.
		 * 
		 * @param other the other <tt>GridCell</tt> to check
		 * @return <tt>true</tt> if <tt>other</tt> is adjacent to this,
		 * 	       otherwise <tt>false</tt>
		 */
		public boolean isAdjacent(final GridCell other) {
			return (this.previous == other && other.next == this)
					|| (this.next == other && other.previous == this);
		}
		
		/**
		 * Computes the distance from this <tt>GridCell</tt> to another <tt>GridCell</tt>.
		 * 
		 * @param other the other <tt>GridCell</tt> which distance to this <tt>GridCell</tt>
		 *        shall be computed.
		 * @return the distance from this <tt>GridCell</tt> to <tt>other</tt>
		 */
		public double distance(final GridCell other) {
			return Math.sqrt(Math.pow(this.getX() - other.getX(), 2) 
					+ Math.pow(this.getY() - other.getY(), 2));
		}

		@Override
		public Iterator<GridCell> iterator() {
			return new Iterator<OwdData.GridCell>() {

				private GridCell current = GridCell.this;
				
				@Override
				public boolean hasNext() {
					return this.current != null;
				}

				@Override
				public GridCell next() {
					if(!this.hasNext()) {
						throw new NoSuchElementException();
					}
					final GridCell result = this.current;
					this.current = this.current.next;
					return result;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}
}
